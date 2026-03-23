/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.modelos;

/**
 *
 * @author COMPUGAMER
 */
public class PCB {
    private static int contadorPID = 1; // Para autogenerar IDs unicos
    
    private int pid;
    private EstadoProceso estado;
    private OperacionCRUD operacion;
    private String nombreArchivo;
    private String usuario; // Para validar si es Administrador o Usuario normal
    
    // Datos utiles para el planificador de disco y operaciones
    private int tamanoBloques; // Solo util si la operacion es CREAR
    private int posicionCabezal; // La posicion o bloque objetivo para la E/S
    
    // Constructor general
    public PCB(OperacionCRUD operacion, String nombreArchivo, String usuario, int posicionCabezal) {
        this.pid = contadorPID++;
        this.estado = EstadoProceso.NUEVO; // Todo proceso nace en estado NUEVO
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.usuario = usuario;
        this.posicionCabezal = posicionCabezal;
        this.tamanoBloques = 0; 
    }
    
    // Constructor sobrecargado para la operacion CREAR (necesita tamano)
    public PCB(OperacionCRUD operacion, String nombreArchivo, String usuario, int posicionCabezal, int tamanoBloques) {
        this(operacion, nombreArchivo, usuario, posicionCabezal);
        this.tamanoBloques = tamanoBloques;
    }

    // Getters y Setters
    public int getPid() { return pid; }
    
    public EstadoProceso getEstado() { return estado; }
    public void setEstado(EstadoProceso estado) { this.estado = estado; }
    
    public OperacionCRUD getOperacion() { return operacion; }
    
    public String getNombreArchivo() { return nombreArchivo; }
    
    public String getUsuario() { return usuario; }
    
    public int getTamanoBloques() { return tamanoBloques; }
    
    public int getPosicionCabezal() { return posicionCabezal; }
    public void setPosicionCabezal(int posicionCabezal) { this.posicionCabezal = posicionCabezal; }
    
    // Metodo util para imprimir en consola o en logs
    @Override
    public String toString() {
        return "PCB PID: " + pid + " | Op: " + operacion + " | Archivo: " + nombreArchivo + " | Estado: " + estado;
    }
}