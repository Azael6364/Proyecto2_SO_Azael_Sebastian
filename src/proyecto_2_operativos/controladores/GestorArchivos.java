package proyecto_2_operativos.controladores;

import proyecto_2_operativos.estructuras.Cola;
import proyecto_2_operativos.estructuras.ListaEnlazada;
import proyecto_2_operativos.modelos.*;

// Controlador principal del sistema de archivos simulado
// Gestiona el CRUD, la cola de procesos, los locks y el journal
public class GestorArchivos {

    private Directorio directorioRaiz;
    private DiscoVirtual disco;

    // Cola de procesos de E/S para el planificador
    private Cola<PCB> colaES;

    // Lista de locks activos: cada entrada es "nombreArchivo:tipoLock"
    // tipoLock puede ser "COMPARTIDO" (lectura) o "EXCLUSIVO" (escritura)
    private ListaEnlazada<String> locksActivos;

    // Journal de operaciones criticas
    private ListaEnlazada<EntradaJournal> journal;

    // Flag para simular un fallo antes del commit
    private boolean simularFallo;

    // Colores predefinidos para los archivos al cargar JSON
    private static final String[] COLORES = {
        "#E57373", "#81C784", "#64B5F6", "#FFD54F",
        "#BA68C8", "#4DB6AC", "#FF8A65", "#A1887F",
        "#90A4AE", "#F06292"
    };
    private int colorIndex = 0;

    public GestorArchivos(int capacidadDisco) {
        this.disco = new DiscoVirtual(capacidadDisco);
        this.directorioRaiz = new Directorio("Raiz", "Administrador", null);
        this.colaES = new Cola<>();
        this.locksActivos = new ListaEnlazada<>();
        this.journal = new ListaEnlazada<>();
        this.simularFallo = false;
    }

    // -----------------------------------------------------------------------
    // GESTION DE LOCKS
    // -----------------------------------------------------------------------

    // Intenta adquirir un lock compartido (lectura). Falla si hay lock exclusivo
    public boolean adquirirLockCompartido(String nombreArchivo) {
        for (int i = 0; i < locksActivos.getSize(); i++) {
            String lock = locksActivos.get(i);
            if (lock.startsWith(nombreArchivo + ":EXCLUSIVO")) return false;
        }
        locksActivos.add(nombreArchivo + ":COMPARTIDO");
        return true;
    }

    // Intenta adquirir un lock exclusivo (escritura). Falla si ya hay cualquier lock
    public boolean adquirirLockExclusivo(String nombreArchivo) {
        for (int i = 0; i < locksActivos.getSize(); i++) {
            if (locksActivos.get(i).startsWith(nombreArchivo + ":")) return false;
        }
        locksActivos.add(nombreArchivo + ":EXCLUSIVO");
        return true;
    }

    // Libera todos los locks del archivo indicado
    public void liberarLocks(String nombreArchivo) {
        // Recorremos hacia atras para poder eliminar sin afectar el indice
        for (int i = locksActivos.getSize() - 1; i >= 0; i--) {
            if (locksActivos.get(i).startsWith(nombreArchivo + ":")) {
                locksActivos.remove(i);
            }
        }
    }

    // -----------------------------------------------------------------------
    // COLA DE PROCESOS
    // -----------------------------------------------------------------------

    // Encola un proceso de E/S y lo deja en estado LISTO
    public void encolarProceso(PCB proceso) {
        proceso.setEstado(EstadoProceso.LISTO);
        colaES.encolar(proceso);
    }

    // Retorna la cola de E/S para que el planificador la use
    public Cola<PCB> getColaES() { return colaES; }

    // -----------------------------------------------------------------------
    // JOURNAL
    // -----------------------------------------------------------------------

    public void setSimularFallo(boolean valor) { this.simularFallo = valor; }

    // Agrega una entrada PENDIENTE al journal antes de ejecutar una operacion critica
    private EntradaJournal registrarJournal(OperacionCRUD op, String nombre, int bloques) {
        EntradaJournal entrada = new EntradaJournal(op, nombre, bloques);
        journal.add(entrada);
        return entrada;
    }

    // Marca una entrada del journal como CONFIRMADA (commit)
    private void commitJournal(EntradaJournal entrada) {
        entrada.setEstado(EntradaJournal.EstadoJournal.CONFIRMADO);
    }

    // Revisa el journal al arrancar y deshace operaciones PENDIENTES (recovery)
    public void recuperarDesdeJournal() {
        for (int i = 0; i < journal.getSize(); i++) {
            EntradaJournal e = journal.get(i);
            if (e.getEstado() == EntradaJournal.EstadoJournal.PENDIENTE) {
                // Deshacemos la operacion incompleta
                if (e.getOperacion() == OperacionCRUD.CREAR && e.getPrimerBloqueAsignado() != -1) {
                    disco.liberarBloques(e.getPrimerBloqueAsignado());
                    // Eliminamos el archivo del directorio si ya fue insertado
                    eliminarArchivoDirRecursivo(directorioRaiz, e.getNombreArchivo());
                }
                e.setEstado(EntradaJournal.EstadoJournal.DESHECHO);
            }
        }
    }

    // Busca y elimina un archivo por nombre en todo el arbol de directorios
    private void eliminarArchivoDirRecursivo(Directorio dir, String nombre) {
        Archivo encontrado = dir.buscarArchivo(nombre);
        if (encontrado != null) {
            dir.eliminarArchivo(encontrado);
            return;
        }
        for (int i = 0; i < dir.getSubdirectorios().getSize(); i++) {
            eliminarArchivoDirRecursivo(dir.getSubdirectorios().get(i), nombre);
        }
    }

    // -----------------------------------------------------------------------
    // CRUD DE ARCHIVOS
    // -----------------------------------------------------------------------

    // CREAR archivo con journaling y lock exclusivo
    // Retorna: 0=exito, -1=sin espacio, -2=lock no disponible, -3=fallo simulado
    public int crearArchivo(Directorio dirActual, String nombre, int tamanoBloques,
                             String dueno, String colorHex) {
        // Journaling: registrar PENDIENTE antes de operar
        EntradaJournal entrada = registrarJournal(OperacionCRUD.CREAR, nombre, tamanoBloques);

        // Adquirir lock exclusivo para escritura
        if (!adquirirLockExclusivo(nombre)) return -2;

        // Asignar bloques en el disco
        int primerBloque = disco.asignarBloques(nombre, tamanoBloques, colorHex);
        if (primerBloque == -1) {
            liberarLocks(nombre);
            journal.removeObject(entrada);
            return -1;
        }

        // Guardamos en el journal por si hay fallo antes del commit
        entrada.setPrimerBloqueAsignado(primerBloque);

        // Punto de fallo simulado: el sistema "cae" antes del commit
        if (simularFallo) {
            simularFallo = false; // Solo falla una vez
            liberarLocks(nombre);
            // La entrada queda PENDIENTE en el journal para que recovery la deshaga
            return -3;
        }

        // Operacion exitosa: creamos el objeto y lo agregamos al directorio
        Archivo nuevoArchivo = new Archivo(nombre, tamanoBloques, dueno, colorHex);
        nuevoArchivo.setPrimerBloque(primerBloque);
        dirActual.agregarArchivo(nuevoArchivo);

        // Commit: marcamos la operacion como completada
        commitJournal(entrada);
        liberarLocks(nombre);
        return 0;
    }

    // LEER archivo: solo requiere lock compartido
    // Retorna el archivo si se puede leer, null si esta bloqueado
    public Archivo leerArchivo(Directorio dir, String nombre) {
        Archivo archivo = dir.buscarArchivo(nombre);
        if (archivo == null) return null;

        if (!adquirirLockCompartido(nombre)) return null; // Hay escritura en curso

        // Liberamos inmediatamente: la lectura es atomica en esta simulacion
        liberarLocks(nombre);
        return archivo;
    }

    // ACTUALIZAR nombre de un archivo (solo administrador)
    // Retorna: 0=exito, -1=no encontrado, -2=lock no disponible
    public int actualizarNombreArchivo(Directorio dir, String nombreActual, String nuevoNombre) {
        Archivo archivo = dir.buscarArchivo(nombreActual);
        if (archivo == null) return -1;

        if (!adquirirLockExclusivo(nombreActual)) return -2;

        // Actualizamos tambien el nombre en los bloques del disco
        Bloque[] bloques = disco.getBloques();
        int actual = archivo.getPrimerBloque();
        while (actual != -1) {
            bloques[actual].setNombreArchivo(nuevoNombre);
            actual = bloques[actual].getSiguienteBloque();
        }

        archivo.setNombre(nuevoNombre);
        liberarLocks(nombreActual);
        return 0;
    }

    // ELIMINAR archivo con journaling
    // Retorna: 0=exito, -1=no encontrado, -2=lock no disponible
    public int eliminarArchivo(Directorio dir, String nombre) {
        Archivo archivo = dir.buscarArchivo(nombre);
        if (archivo == null) return -1;

        EntradaJournal entrada = registrarJournal(OperacionCRUD.ELIMINAR, nombre, 0);

        if (!adquirirLockExclusivo(nombre)) {
            journal.removeObject(entrada);
            return -2;
        }

        disco.liberarBloques(archivo.getPrimerBloque());
        dir.eliminarArchivo(archivo);

        commitJournal(entrada);
        liberarLocks(nombre);
        return 0;
    }

    // ELIMINAR directorio y todo su contenido recursivamente
    public void eliminarDirectorio(Directorio dir) {
        // Primero eliminamos todos los archivos del directorio
        while (!dir.getArchivos().isEmpty()) {
            Archivo a = dir.getArchivos().get(0);
            disco.liberarBloques(a.getPrimerBloque());
            dir.eliminarArchivo(a);
        }
        // Luego eliminamos recursivamente cada subdirectorio
        for (int i = 0; i < dir.getSubdirectorios().getSize(); i++) {
            eliminarDirectorio(dir.getSubdirectorios().get(i));
        }
    }

    // CREAR directorio
    public void crearDirectorio(Directorio dirActual, String nombre, String dueno) {
        Directorio nuevoDir = new Directorio(nombre, dueno, dirActual);
        dirActual.agregarSubdirectorio(nuevoDir);
    }

    // -----------------------------------------------------------------------
    // CARGA DESDE JSON (estructura minima sin libreria externa)
    // -----------------------------------------------------------------------

    // Carga archivos del sistema desde un JSON simplificado
    // Formato esperado segun el enunciado: system_files con bloques y nombre
    public String cargarDesdeJSON(String jsonTexto) {
        StringBuilder log = new StringBuilder();
        try {
            // Extraemos la seccion "system_files"
            int idx = jsonTexto.indexOf("\"system_files\"");
            if (idx == -1) return "JSON invalido: falta 'system_files'";

            int inicio = jsonTexto.indexOf('{', idx + 14);
            int fin = jsonTexto.lastIndexOf('}', jsonTexto.lastIndexOf('}') - 1);
            String sysFiles = jsonTexto.substring(inicio + 1, fin);

            // Buscamos cada entrada con patron: "pos": { "name": "...", "blocks": N }
            int pos = 0;
            while (pos < sysFiles.length()) {
                int posInicio = sysFiles.indexOf('"', pos);
                if (posInicio == -1) break;
                int posFin = sysFiles.indexOf('"', posInicio + 1);
                String posStr = sysFiles.substring(posInicio + 1, posFin);

                int bloquePos;
                try { bloquePos = Integer.parseInt(posStr); }
                catch (NumberFormatException e) { pos = posFin + 1; continue; }

                int nameIdx = sysFiles.indexOf("\"name\"", posFin);
                int nameStart = sysFiles.indexOf('"', nameIdx + 6) + 1;
                int nameEnd = sysFiles.indexOf('"', nameStart);
                String nombre = sysFiles.substring(nameStart, nameEnd);

                int blocksIdx = sysFiles.indexOf("\"blocks\"", nameEnd);
                int blocksStart = sysFiles.indexOf(':', blocksIdx) + 1;
                int blocksEnd = sysFiles.indexOf('\n', blocksStart);
                if (blocksEnd == -1) blocksEnd = sysFiles.indexOf('}', blocksStart);
                String blocksStr = sysFiles.substring(blocksStart, blocksEnd).trim()
                        .replace(",", "").replace("}", "").trim();
                int cantBloques = Integer.parseInt(blocksStr);

                // Asignamos el color de la paleta y creamos el archivo en la raiz
                String color = COLORES[colorIndex % COLORES.length];
                colorIndex++;

                // Intentamos asignar bloques comenzando desde la posicion indicada
                // Para respetar la posicion exacta del JSON, asignamos el bloque manualmente
                DiscoVirtual d = disco;
                Bloque[] bs = d.getBloques();

                // Si el bloque de inicio esta libre, lo usamos como punto de partida
                if (bloquePos < d.getCapacidadTotal() && !bs[bloquePos].isOcupado()) {
                    int primerBloque = d.asignarBloques(nombre, cantBloques, color);
                    if (primerBloque != -1) {
                        Archivo arch = new Archivo(nombre, cantBloques, "Sistema", color);
                        arch.setPrimerBloque(primerBloque);
                        directorioRaiz.agregarArchivo(arch);
                        log.append("Cargado: ").append(nombre).append("\n");
                    } else {
                        log.append("Sin espacio para: ").append(nombre).append("\n");
                    }
                } else {
                    // El bloque indicado ya esta ocupado; asignamos donde haya espacio
                    int primerBloque = d.asignarBloques(nombre, cantBloques, color);
                    if (primerBloque != -1) {
                        Archivo arch = new Archivo(nombre, cantBloques, "Sistema", color);
                        arch.setPrimerBloque(primerBloque);
                        directorioRaiz.agregarArchivo(arch);
                        log.append("Cargado (reubicado): ").append(nombre).append("\n");
                    } else {
                        log.append("Sin espacio para: ").append(nombre).append("\n");
                    }
                }

                // Avanzamos al siguiente elemento del JSON
                pos = sysFiles.indexOf('}', nameEnd) + 1;
            }
        } catch (Exception ex) {
            return "Error al parsear JSON: " + ex.getMessage();
        }
        return log.toString();
    }

    // -----------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------

    public Directorio getDirectorioRaiz() { return directorioRaiz; }
    public DiscoVirtual getDisco() { return disco; }
    public ListaEnlazada<EntradaJournal> getJournal() { return journal; }
    public ListaEnlazada<String> getLocksActivos() { return locksActivos; }

    // Genera el siguiente color de la paleta para archivos nuevos
    public String siguienteColor() {
        return COLORES[colorIndex++ % COLORES.length];
    }
}
