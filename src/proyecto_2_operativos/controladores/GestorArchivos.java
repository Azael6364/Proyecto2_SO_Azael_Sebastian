/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.controladores;

import proyecto_2_operativos.modelos.Archivo;
import proyecto_2_operativos.modelos.Directorio;
import proyecto_2_operativos.modelos.DiscoVirtual;

/**
 *
 * @author COMPUGAMER
 */


public class GestorArchivos {
    private Directorio directorioRaiz;
    private DiscoVirtual disco;
    
    // Constructor
    public GestorArchivos(int capacidadDisco) {
        // Inicializamos el disco con la capacidad que le pasemos
        this.disco = new DiscoVirtual(capacidadDisco);
        
        // Creamos la carpeta raiz del sistema
        this.directorioRaiz = new Directorio("Raiz", "Administrador", null);
    }
    
    // Metodo para CREAR un archivo
    // Retorna true si se creo exitosamente, false si no hay espacio
    public boolean crearArchivo(Directorio dirActual, String nombre, int tamanoBloques, String dueno, String colorHex) {
        // 1. Intentamos asignar los bloques en el disco virtual
        int primerBloque = disco.asignarBloques(nombre, tamanoBloques, colorHex);
        
        if (primerBloque != -1) {
            // 2. Si hubo espacio, creamos el objeto Archivo
            Archivo nuevoArchivo = new Archivo(nombre, tamanoBloques, dueno, colorHex);
            nuevoArchivo.setPrimerBloque(primerBloque);
            
            // 3. Lo agregamos a la lista enlazada del directorio actual
            dirActual.agregarArchivo(nuevoArchivo);
            return true;
        } else {
            // No hay espacio suficiente en el disco
            return false;
        }
    }
    
    // Metodo para CREAR un subdirectorio
    public void crearDirectorio(Directorio dirActual, String nombre, String dueno) {
        Directorio nuevoDir = new Directorio(nombre, dueno, dirActual);
        dirActual.agregarSubdirectorio(nuevoDir);
    }
    
    // Getters
    public Directorio getDirectorioRaiz() { return directorioRaiz; }
    public DiscoVirtual getDisco() { return disco; }
}