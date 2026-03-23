package proyecto_2_operativos.modelos;

// Simula el disco fisico del sistema de archivos
// Usa asignacion encadenada: cada archivo es una lista enlazada de bloques
public class DiscoVirtual {
    private Bloque[] bloques;
    private int capacidadTotal;
    private int bloquesLibres;

    public DiscoVirtual(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
        this.bloquesLibres = capacidadTotal;
        this.bloques = new Bloque[capacidadTotal];
        for (int i = 0; i < capacidadTotal; i++) {
            bloques[i] = new Bloque(i);
        }
    }

    // Asigna bloques para un archivo usando asignacion encadenada
    // Retorna el indice del primer bloque, o -1 si no hay espacio suficiente
    public int asignarBloques(String nombreArchivo, int cantidad, String colorHex) {
        if (cantidad > bloquesLibres) return -1;

        int primerBloque = -1;
        int bloqueAnterior = -1;
        int asignados = 0;

        for (int i = 0; i < capacidadTotal && asignados < cantidad; i++) {
            if (!bloques[i].isOcupado()) {
                bloques[i].setOcupado(true);
                bloques[i].setNombreArchivo(nombreArchivo);
                bloques[i].setColorHex(colorHex);

                if (primerBloque == -1) primerBloque = i;
                if (bloqueAnterior != -1) bloques[bloqueAnterior].setSiguienteBloque(i);

                bloqueAnterior = i;
                asignados++;
                bloquesLibres--;
            }
        }
        return primerBloque;
    }

    // Libera todos los bloques encadenados a partir del indice dado
    public void liberarBloques(int indicePrimerBloque) {
        int actual = indicePrimerBloque;
        while (actual != -1) {
            int siguiente = bloques[actual].getSiguienteBloque();
            bloques[actual].liberar();
            bloquesLibres++;
            actual = siguiente;
        }
    }

    // Retorna el numero del primer bloque libre encontrado, o -1 si no hay ninguno
    public int primerBloqueLibre() {
        for (int i = 0; i < capacidadTotal; i++) {
            if (!bloques[i].isOcupado()) return i;
        }
        return -1;
    }

    public Bloque[] getBloques() { return bloques; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getBloquesLibres() { return bloquesLibres; }
    public int getBloquesOcupados() { return capacidadTotal - bloquesLibres; }
}
