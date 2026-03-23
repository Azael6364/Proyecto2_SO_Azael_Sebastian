/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.modelos;

/**
 *
 * @author COMPUGAMER
 */
public class Archivo {
    private String nombre;
    private int tamanoBloques;
    private String dueno;
    private String colorHex; // Para representarlo visualmente en el SD
    private int primerBloque; // Referencia al primer bloque de la asignacion encadenada

    // Constructor
    public Archivo(String nombre, int tamanoBloques, String dueno, String colorHex) {
        this.nombre = nombre;
        this.tamanoBloques = tamanoBloques;
        this.dueno = dueno;
        this.colorHex = colorHex;
        this.primerBloque = -1; // -1 indicara que aun no tiene bloques asignados
    }

    // Getters y Setters basicos
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getTamanoBloques() { return tamanoBloques; }
    
    public String getDueno() { return dueno; }
    
    public String getColorHex() { return colorHex; }
    
    public int getPrimerBloque() { return primerBloque; }
    public void setPrimerBloque(int primerBloque) { this.primerBloque = primerBloque; }
}