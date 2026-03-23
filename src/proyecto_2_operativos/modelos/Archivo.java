package proyecto_2_operativos.modelos;

// Representa un archivo dentro del sistema de archivos simulado
public class Archivo {
    private String nombre;
    private int tamanoBloques;
    private String dueno;
    private String colorHex;      // Color unico para diferenciarlo visualmente en el disco
    private int primerBloque;     // Indice del primer bloque en la asignacion encadenada

    public Archivo(String nombre, int tamanoBloques, String dueno, String colorHex) {
        this.nombre = nombre;
        this.tamanoBloques = tamanoBloques;
        this.dueno = dueno;
        this.colorHex = colorHex;
        this.primerBloque = -1; // -1 indica que aun no tiene bloques asignados
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getTamanoBloques() { return tamanoBloques; }

    public String getDueno() { return dueno; }

    public String getColorHex() { return colorHex; }

    public int getPrimerBloque() { return primerBloque; }
    public void setPrimerBloque(int primerBloque) { this.primerBloque = primerBloque; }
}
