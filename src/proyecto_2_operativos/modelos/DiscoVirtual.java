/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.modelos;

/**
 *
 * @author COMPUGAMER
 */
public class DiscoVirtual {
    private Bloque[] bloques;
    private int capacidadTotal;
    private int bloquesLibres;

    public DiscoVirtual(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
        this.bloquesLibres = capacidadTotal;
        this.bloques = new Bloque[capacidadTotal];
        
        // Inicializamos todos los bloques
        for (int i = 0; i < capacidadTotal; i++) {
            bloques[i] = new Bloque(i);
        }
    }

    // Metodo para asignar bloques a un archivo usando asignacion encadenada
    // Retorna el indice del primer bloque asignado, o -1 si no hay espacio
    public int asignarBloques(String nombreArchivo, int cantidadNecesaria, String colorHex) {
        if (cantidadNecesaria > bloquesLibres) {
            return -1; // No hay espacio suficiente
        }

        int primerBloque = -1;
        int bloqueAnterior = -1;
        int bloquesAsignados = 0;

        for (int i = 0; i < capacidadTotal && bloquesAsignados < cantidadNecesaria; i++) {
            if (!bloques[i].isOcupado()) {
                // Ocupamos el bloque
                bloques[i].setOcupado(true);
                bloques[i].setNombreArchivo(nombreArchivo);
                bloques[i].setColorHex(colorHex);

                if (primerBloque == -1) {
                    primerBloque = i; // Guardamos el inicio de la cadena
                }

                if (bloqueAnterior != -1) {
                    // El bloque anterior ahora apunta a este nuevo bloque
                    bloques[bloqueAnterior].setSiguienteBloque(i);
                }

                bloqueAnterior = i;
                bloquesAsignados++;
                bloquesLibres--;
            }
        }
        
        return primerBloque;
    }

    // Metodo para liberar los bloques encadenados de un archivo
    public void liberarBloques(int indicePrimerBloque) {
        int actual = indicePrimerBloque;
        while (actual != -1) {
            int siguiente = bloques[actual].getSiguienteBloque();
            bloques[actual].liberar();
            bloquesLibres++;
            actual = siguiente;
        }
    }

    // Getters
    public Bloque[] getBloques() { return bloques; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getBloquesLibres() { return bloquesLibres; }
}