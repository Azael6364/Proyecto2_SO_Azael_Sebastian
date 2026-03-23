/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.modelos;

/**
 *
 * @author COMPUGAMER
 */
public class Bloque {
    private int id;
    private boolean ocupado;
    private int siguienteBloque; // Para la asignacion encadenada. -1 significa fin o libre.
    private String colorHex; // Para pintarlo en la GUI despues
    private String nombreArchivo; // Para saber a quien pertenece en la tabla

    public Bloque(int id) {
        this.id = id;
        this.ocupado = false;
        this.siguienteBloque = -1;
        this.colorHex = "#FFFFFF"; // Blanco por defecto
        this.nombreArchivo = "";
    }

    // Getters y Setters
    public int getId() { return id; }
    
    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
    
    public int getSiguienteBloque() { return siguienteBloque; }
    public void setSiguienteBloque(int siguienteBloque) { this.siguienteBloque = siguienteBloque; }
    
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    // Metodo para limpiar el bloque cuando se elimina un archivo
    public void liberar() {
        this.ocupado = false;
        this.siguienteBloque = -1;
        this.colorHex = "#FFFFFF";
        this.nombreArchivo = "";
    }
}