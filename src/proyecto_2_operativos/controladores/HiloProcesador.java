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
                    Thread.sleep(4000); 
                    
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
        int resultado;
        switch (p.getOperacion()) {
            case CREAR:
                resultado = gestor.crearArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo(), 
                                    p.getTamanoBloques(), p.getUsuario(), gestor.siguienteColor());
                if(resultado == -1) mostrarMensaje("No hay espacio suficiente para " + p.getNombreArchivo(), true);
                else if(resultado == -2) mostrarMensaje("El archivo " + p.getNombreArchivo() + " esta bloqueado.", true);
                break;
                
            case ELIMINAR:
                resultado = gestor.eliminarArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo());
                if(resultado == -1) mostrarMensaje("No se encontro el archivo " + p.getNombreArchivo(), true);
                break;
                
            case ACTUALIZAR:
                resultado = gestor.actualizarNombreArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo(), p.getNuevoNombre());
                if(resultado == -1) mostrarMensaje("No se encontro el archivo " + p.getNombreArchivo(), true);
                break;
                
            case LEER:
                proyecto_2_operativos.modelos.Archivo arch = gestor.leerArchivo(gestor.getDirectorioRaiz(), p.getNombreArchivo());
                if (arch != null) {
                    mostrarMensaje("Lectura Exitosa:\nArchivo: " + arch.getNombre() + "\nBloques: " + arch.getTamanoBloques() + "\nDueño: " + arch.getDueno(), false);
                } else {
                    mostrarMensaje("Archivo no encontrado o bloqueado por otro proceso.", true);
                }
                break;
        }
    }

    // Metodo seguro para mostrar pop-ups desde un hilo en segundo plano sin romper Java Swing
    private void mostrarMensaje(String msg, boolean esError) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (esError) {
                javax.swing.JOptionPane.showMessageDialog(ventana, msg, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(ventana, msg, "Informacion", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}