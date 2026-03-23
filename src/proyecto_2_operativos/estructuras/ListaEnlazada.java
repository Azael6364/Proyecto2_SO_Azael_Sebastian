/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_2_operativos.estructuras;

/**
 *
 * @author COMPUGAMER
 */
public class ListaEnlazada<T> {
    private Nodo<T> head;
    private int size;

    public ListaEnlazada() {
        this.head = null;
        this.size = 0;
    }

    // Método para agregar al final
    public void add(T data) {
        Nodo<T> newNode = new Nodo<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Nodo<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    // Método para obtener el tamaño
    public int getSize() {
        return size;
    }
    
    // Metodo para obtener un elemento por su indice
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Nodo<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    // Método para verificar si está vacía
    public boolean isEmpty() {
        return head == null;
    }
    
}