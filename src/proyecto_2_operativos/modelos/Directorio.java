package proyecto_2_operativos.modelos;

import proyecto_2_operativos.estructuras.ListaEnlazada;

// Representa un directorio en la jerarquia del sistema de archivos
public class Directorio {
    private String nombre;
    private String dueno;
    private Directorio padre;
    private ListaEnlazada<Directorio> subdirectorios;
    private ListaEnlazada<Archivo> archivos;

    public Directorio(String nombre, String dueno, Directorio padre) {
        this.nombre = nombre;
        this.dueno = dueno;
        this.padre = padre;
        this.subdirectorios = new ListaEnlazada<>();
        this.archivos = new ListaEnlazada<>();
    }

    public void agregarArchivo(Archivo archivo) {
        archivos.add(archivo);
    }

    public void agregarSubdirectorio(Directorio directorio) {
        subdirectorios.add(directorio);
    }

    // Busca y retorna un archivo por nombre. Retorna null si no existe
    public Archivo buscarArchivo(String nombre) {
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo a = archivos.get(i);
            if (a.getNombre().equals(nombre)) return a;
        }
        return null;
    }

    // Elimina un archivo de la lista de este directorio. Retorna true si lo encontro
    public boolean eliminarArchivo(Archivo archivo) {
        return archivos.removeObject(archivo);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDueno() { return dueno; }

    public Directorio getPadre() { return padre; }

    public ListaEnlazada<Directorio> getSubdirectorios() { return subdirectorios; }
    public ListaEnlazada<Archivo> getArchivos() { return archivos; }

    @Override
    public String toString() { return nombre; }
}
