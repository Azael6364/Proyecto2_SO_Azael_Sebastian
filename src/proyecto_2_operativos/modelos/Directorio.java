/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.modelos;

/**
 *
 * @author COMPUGAMER
 */
import proyecto_2_operativos.estructuras.ListaEnlazada;

public class Directorio {
    private String nombre;
    private String dueno;
    private Directorio padre; // Referencia al directorio superior
    private ListaEnlazada<Directorio> subdirectorios;
    private ListaEnlazada<Archivo> archivos;

    // Constructor
    public Directorio(String nombre, String dueno, Directorio padre) {
        this.nombre = nombre;
        this.dueno = dueno;
        this.padre = padre;
        this.subdirectorios = new ListaEnlazada<>();
        this.archivos = new ListaEnlazada<>();
    }

    // Metodo para agregar un archivo a este directorio
    public void agregarArchivo(Archivo archivo) {
        archivos.add(archivo);
    }

    // Metodo para agregar un subdirectorio
    public void agregarSubdirectorio(Directorio directorio) {
        subdirectorios.add(directorio);
    }

    // Getters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDueno() { return dueno; }
    
    public Directorio getPadre() { return padre; }
    
    public ListaEnlazada<Directorio> getSubdirectorios() { return subdirectorios; }
    public ListaEnlazada<Archivo> getArchivos() { return archivos; }
}