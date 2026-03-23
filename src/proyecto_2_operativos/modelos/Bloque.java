package proyecto_2_operativos.modelos;

// Representa un bloque fisico del disco simulado
public class Bloque {
    private int id;
    private boolean ocupado;
    private int siguienteBloque; // Puntero al siguiente bloque en la cadena. -1 = fin o libre
    private String colorHex;     // Color del archivo propietario
    private String nombreArchivo;

    public Bloque(int id) {
        this.id = id;
        this.ocupado = false;
        this.siguienteBloque = -1;
        this.colorHex = "#3C4148"; // Gris oscuro = libre
        this.nombreArchivo = "";
    }

    public int getId() { return id; }

    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    public int getSiguienteBloque() { return siguienteBloque; }
    public void setSiguienteBloque(int siguienteBloque) { this.siguienteBloque = siguienteBloque; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    // Restablece el bloque a su estado libre original
    public void liberar() {
        this.ocupado = false;
        this.siguienteBloque = -1;
        this.colorHex = "#3C4148";
        this.nombreArchivo = "";
    }
}
