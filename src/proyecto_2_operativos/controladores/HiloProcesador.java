/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.controladores;

import proyecto_2_operativos.modelos.PCB;
import proyecto_2_operativos.modelos.EstadoProceso;
import proyecto_2_operativos.gui.VentanaPrincipal;
import proyecto_2_operativos.estructuras.Cola;
import proyecto_2_operativos.modelos.OperacionCRUD;

/**
 *
 * @author COMPUGAMER
 */
// Hilo en segundo plano que simula el procesamiento del sistema
public class HiloProcesador extends Thread {
    
    private VentanaPrincipal ventana;
    private GestorArchivos gestor;
    private boolean corriendo;

    public HiloProcesador(VentanaPrincipal ventana, GestorArchivos gestor) {
        this.ventana = ventana;
        this.gestor = gestor;
        this.corriendo = true;
    }

    @Override
    public void run() {
        while (corriendo) {
            try {
                // Revisamos si hay procesos en la cola
                if (!gestor.getColaES().estaVacia()) {
                    
                    // 1. Sacamos el proceso de la cola
                    PCB procesoActual = gestor.getColaES().desencolar();
                    
                    // 2. Lo pasamos a EJECUTANDO y actualizamos la interfaz
                    procesoActual.setEstado(EstadoProceso.EJECUTANDO);
                    javax.swing.SwingUtilities.invokeLater(() -> ventana.actualizarTodoThreadSafe());
                    
                    // 3. Simulamos un retardo de E/S (2 segundos) para que se vea en pantalla
                    Thread.sleep(2000); 
                    
                    // 4. Ejecutamos la operacion real segun el PCB
                    ejecutarOperacionReal(procesoActual);
                    
                    // 5. Lo pasamos a TERMINADO y actualizamos
                    procesoActual.setEstado(EstadoProceso.TERMINADO);
                    javax.swing.SwingUtilities.invokeLater(() -> ventana.actualizarTodoThreadSafe());
                    
                } else {
                    // Si no hay procesos, el hilo descansa medio segundo antes de volver a mirar
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Hilo procesador interrumpido.");
            }
        }
    }

    // Metodo que traduce el PCB a la orden real en el Gestor
    private void ejecutarOperacionReal(PCB p) {
        switch (p.getOperacion()) {
            case CREAR:
                // Usamos un color aleatorio o el del gestor
                gestor.crearArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo(), 
                                    p.getTamanoBloques(), p.getUsuario(), gestor.siguienteColor());
                break;
            case ELIMINAR:
                gestor.eliminarArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo());
                break;
            // Aqui se pueden agregar LEER y ACTUALIZAR
        }
    }
}