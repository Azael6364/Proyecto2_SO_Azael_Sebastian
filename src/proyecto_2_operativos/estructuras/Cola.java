/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.estructuras;

/**
 *
 * @author COMPUGAMER
 */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int size;

    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.size = 0;
    }

    // Metodo para agregar un elemento al final de la cola
    public void encolar(T data) {
        Nodo<T> nuevoNodo = new Nodo<>(data);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            finalCola.setNext(nuevoNodo);
        }
        finalCola = nuevoNodo;
        size++;
    }

    // Metodo para sacar y devolver el primer elemento de la cola
    public T desencolar() {
        if (estaVacia()) {
            return null; // O podrias lanzar una excepcion personalizada
        }
        T data = frente.getData();
        frente = frente.getNext();
        if (frente == null) {
            finalCola = null;
        }
        size--;
        return data;
    }

    // Metodo para ver el primer elemento sin sacarlo
    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getData();
    }

    // Metodo para verificar si la cola esta vacia
    public boolean estaVacia() {
        return frente == null;
    }

    // Metodo para obtener el tamano actual de la cola
    public int getSize() {
        return size;
    }
}