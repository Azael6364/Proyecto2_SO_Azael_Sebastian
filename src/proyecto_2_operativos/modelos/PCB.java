package proyecto_2_operativos.modelos;

// Bloque de control de proceso (PCB)
// Contiene toda la informacion necesaria para gestionar un proceso en el sistema
public class PCB {
    private static int contadorPID = 1; // Generador de IDs unicos

    private int pid;
    private EstadoProceso estado;
    private OperacionCRUD operacion;
    private String nombreArchivo;
    private String usuario;          // "Administrador" o "Usuario"
    private int tamanoBloques;       // Solo relevante si la operacion es CREAR
    private int posicionCabezal;     // Posicion del bloque objetivo en el disco (para el planificador)
    private String nuevoNombre;      // Solo relevante si la operacion es ACTUALIZAR

    // Constructor general
    public PCB(OperacionCRUD operacion, String nombreArchivo, String usuario, int posicionCabezal) {
        this.pid = contadorPID++;
        this.estado = EstadoProceso.NUEVO;
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.usuario = usuario;
        this.posicionCabezal = posicionCabezal;
        this.tamanoBloques = 0;
        this.nuevoNombre = null;
    }

    // Constructor sobrecargado para operacion CREAR (necesita tamano en bloques)
    public PCB(OperacionCRUD operacion, String nombreArchivo, String usuario, int posicionCabezal, int tamanoBloques) {
        this(operacion, nombreArchivo, usuario, posicionCabezal);
        this.tamanoBloques = tamanoBloques;
    }

    // Reinicia el contador (util al cargar un nuevo estado desde JSON)
    public static void resetContador() { contadorPID = 1; }

    public int getPid() { return pid; }

    public EstadoProceso getEstado() { return estado; }
    public void setEstado(EstadoProceso estado) { this.estado = estado; }

    public OperacionCRUD getOperacion() { return operacion; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getUsuario() { return usuario; }

    public int getTamanoBloques() { return tamanoBloques; }

    public int getPosicionCabezal() { return posicionCabezal; }
    public void setPosicionCabezal(int pos) { this.posicionCabezal = pos; }

    public String getNuevoNombre() { return nuevoNombre; }
    public void setNuevoNombre(String nuevoNombre) { this.nuevoNombre = nuevoNombre; }

    @Override
    public String toString() {
        return "PID:" + pid + " | " + operacion + " | " + nombreArchivo + " | " + estado;
    }
}
