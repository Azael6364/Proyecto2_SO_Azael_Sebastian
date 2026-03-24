package proyecto_2_operativos.controladores;

import proyecto_2_operativos.estructuras.Cola;
import proyecto_2_operativos.estructuras.ListaEnlazada;
import proyecto_2_operativos.modelos.PCB;
import proyecto_2_operativos.modelos.EstadoProceso;

// Planificador de disco que implementa FIFO, SSTF, SCAN y C-SCAN
// Recibe una cola de PCBs y determina el orden de atencion segun la politica activa
public class PlanificadorDisco {

    public enum Politica { FIFO, SSTF, SCAN, CSCAN }

    // Resultado de una ejecucion de planificacion
    public static class ResultadoPlanificacion {
        public ListaEnlazada<Integer> ordenEjecucion; // Posiciones del cabezal en orden
        public ListaEnlazada<String> nombresArchivos;  // Nombres de los archivos en ese orden
        public int desplazamientoTotal;

        public ResultadoPlanificacion() {
            ordenEjecucion = new ListaEnlazada<>();
            nombresArchivos = new ListaEnlazada<>();
            desplazamientoTotal = 0;
        }
    }

    // Ejecuta el planificador sobre la cola de procesos segun la politica elegida
    // Direccion inicial del cabezal para SCAN/C-SCAN: true = hacia arriba (mayor)
    public static ResultadoPlanificacion ejecutar(Cola<PCB> cola, int cabezalInicial,
                                                   Politica politica, boolean direccionArriba) {
        // Copiamos los procesos de la cola a una lista propia para no destruirla
        ListaEnlazada<PCB> pendientes = new ListaEnlazada<>();
        Cola<PCB> temporal = new Cola<>();

        while (!cola.estaVacia()) {
            PCB p = cola.desencolar();
            pendientes.add(p);
            temporal.encolar(p);
        }
        // Restauramos la cola original
        while (!temporal.estaVacia()) cola.encolar(temporal.desencolar());

        ResultadoPlanificacion resultado = new ResultadoPlanificacion();
        int cabezal = cabezalInicial;

        switch (politica) {
            case FIFO:
                ejecutarFIFO(pendientes, cabezal, resultado);
                break;
            case SSTF:
                ejecutarSSTF(pendientes, cabezal, resultado);
                break;
            case SCAN:
                ejecutarSCAN(pendientes, cabezal, resultado, direccionArriba, false);
                break;
            case CSCAN:
                ejecutarSCAN(pendientes, cabezal, resultado, direccionArriba, true);
                break;
        }
        return resultado;
    }

    // FIFO: atiende en el orden en que llegaron a la cola
    private static void ejecutarFIFO(ListaEnlazada<PCB> pendientes, int cabezal,
                                      ResultadoPlanificacion resultado) {
        int pos = cabezal;
        for (int i = 0; i < pendientes.getSize(); i++) {
            PCB p = pendientes.get(i);
            resultado.ordenEjecucion.add(p.getPosicionCabezal());
            resultado.nombresArchivos.add(p.getNombreArchivo());
            resultado.desplazamientoTotal += Math.abs(p.getPosicionCabezal() - pos);
            pos = p.getPosicionCabezal();
        }
    }

    // SSTF: atiende siempre la solicitud mas cercana al cabezal actual
    private static void ejecutarSSTF(ListaEnlazada<PCB> pendientes, int cabezal,
                                      ResultadoPlanificacion resultado) {
        // Usamos una lista auxiliar para marcar cuales ya se atendieron
        boolean[] atendido = new boolean[pendientes.getSize()];
        int pos = cabezal;

        for (int iter = 0; iter < pendientes.getSize(); iter++) {
            int menorDistancia = Integer.MAX_VALUE;
            int indiceMasCercano = -1;

            for (int i = 0; i < pendientes.getSize(); i++) {
                if (!atendido[i]) {
                    int dist = Math.abs(pendientes.get(i).getPosicionCabezal() - pos);
                    if (dist < menorDistancia) {
                        menorDistancia = dist;
                        indiceMasCercano = i;
                    }
                }
            }

            if (indiceMasCercano != -1) {
                PCB p = pendientes.get(indiceMasCercano);
                resultado.ordenEjecucion.add(p.getPosicionCabezal());
                resultado.nombresArchivos.add(p.getNombreArchivo());
                resultado.desplazamientoTotal += menorDistancia;
                pos = p.getPosicionCabezal();
                atendido[indiceMasCercano] = true;
            }
        }
    }

    // SCAN y C-SCAN: el cabezal barre en una direccion atendiendo solicitudes
    // Si esCscan=true, al llegar al extremo vuelve al inicio sin atender en el regreso
    private static void ejecutarSCAN(ListaEnlazada<PCB> pendientes, int cabezal,
                                      ResultadoPlanificacion resultado,
                                      boolean direccionArriba, boolean esCscan) {
        // Separamos las solicitudes en dos grupos respecto al cabezal
        ListaEnlazada<Integer> posiciones = new ListaEnlazada<>();
        ListaEnlazada<String> nombres = new ListaEnlazada<>();

        for (int i = 0; i < pendientes.getSize(); i++) {
            posiciones.add(pendientes.get(i).getPosicionCabezal());
            nombres.add(pendientes.get(i).getNombreArchivo());
        }

        // Ordenamos por burbuja para facilitar el barrido
        ordenarParalelo(posiciones, nombres);

        int pos = cabezal;

        if (direccionArriba) {
            // Primero atendemos los que estan por encima del cabezal (>= cabezal)
            for (int i = 0; i < posiciones.getSize(); i++) {
                if (posiciones.get(i) >= cabezal) {
                    resultado.ordenEjecucion.add(posiciones.get(i));
                    resultado.nombresArchivos.add(nombres.get(i));
                    resultado.desplazamientoTotal += Math.abs(posiciones.get(i) - pos);
                    pos = posiciones.get(i);
                }
            }
            if (esCscan) {
                // C-SCAN: regresa al inicio (posicion 0) sin atender en el camino
                resultado.desplazamientoTotal += pos; // Costo de ir al inicio
                pos = 0;
                // Luego atiende los que estaban por debajo del cabezal original
                for (int i = 0; i < posiciones.getSize(); i++) {
                    if (posiciones.get(i) < cabezal) {
                        resultado.ordenEjecucion.add(posiciones.get(i));
                        resultado.nombresArchivos.add(nombres.get(i));
                        resultado.desplazamientoTotal += Math.abs(posiciones.get(i) - pos);
                        pos = posiciones.get(i);
                    }
                }
            } else {
                // SCAN: regresa atendiendo los que estaban debajo (en orden descendente)
                for (int i = posiciones.getSize() - 1; i >= 0; i--) {
                    if (posiciones.get(i) < cabezal) {
                        resultado.ordenEjecucion.add(posiciones.get(i));
                        resultado.nombresArchivos.add(nombres.get(i));
                        resultado.desplazamientoTotal += Math.abs(posiciones.get(i) - pos);
                        pos = posiciones.get(i);
                    }
                }
            }
        } else {
            // Direccion hacia abajo: primero los menores al cabezal
            for (int i = posiciones.getSize() - 1; i >= 0; i--) {
                if (posiciones.get(i) <= cabezal) {
                    resultado.ordenEjecucion.add(posiciones.get(i));
                    resultado.nombresArchivos.add(nombres.get(i));
                    resultado.desplazamientoTotal += Math.abs(posiciones.get(i) - pos);
                    pos = posiciones.get(i);
                }
            }
            if (!esCscan) {
                for (int i = 0; i < posiciones.getSize(); i++) {
                    if (posiciones.get(i) > cabezal) {
                        resultado.ordenEjecucion.add(posiciones.get(i));
                        resultado.nombresArchivos.add(nombres.get(i));
                        resultado.desplazamientoTotal += Math.abs(posiciones.get(i) - pos);
                        pos = posiciones.get(i);
                    }
                }
            }
        }
    }

    // Ordenamiento burbuja sobre dos listas en paralelo (posicion y nombre)
    private static void ordenarParalelo(ListaEnlazada<Integer> posiciones,
                                         ListaEnlazada<String> nombres) {
        int n = posiciones.getSize();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (posiciones.get(j) > posiciones.get(j + 1)) {
                    // Intercambiamos posiciones
                    int tmpPos = posiciones.get(j);
                    String tmpNom = nombres.get(j);
                    // Como ListaEnlazada no tiene set(), usamos remove + insert
                    // Usamos un truco: reconstruimos los valores via los nodos
                    intercambiarEnLista(posiciones, j, j + 1);
                    intercambiarEnListaStr(nombres, j, j + 1);
                }
            }
        }
    }

    // Intercambia dos elementos en una lista de enteros usando nodos
    private static void intercambiarEnLista(ListaEnlazada<Integer> lista, int i, int j) {
        proyecto_2_operativos.estructuras.Nodo<Integer> ni = lista.getHead();
        proyecto_2_operativos.estructuras.Nodo<Integer> nj = lista.getHead();
        for (int k = 0; k < i; k++) ni = ni.getNext();
        for (int k = 0; k < j; k++) nj = nj.getNext();
        int tmp = ni.getData();
        ni.setData(nj.getData());
        nj.setData(tmp);
    }

    // Intercambia dos elementos en una lista de Strings usando nodos
    private static void intercambiarEnListaStr(ListaEnlazada<String> lista, int i, int j) {
        proyecto_2_operativos.estructuras.Nodo<String> ni = lista.getHead();
        proyecto_2_operativos.estructuras.Nodo<String> nj = lista.getHead();
        for (int k = 0; k < i; k++) ni = ni.getNext();
        for (int k = 0; k < j; k++) nj = nj.getNext();
        String tmp = ni.getData();
        ni.setData(nj.getData());
        nj.setData(tmp);
    }
}
