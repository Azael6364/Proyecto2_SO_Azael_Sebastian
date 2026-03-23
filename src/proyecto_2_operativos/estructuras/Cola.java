package proyecto_2_operativos.estructuras;

// Cola generica implementada manualmente con nodos enlazados
// No usa Queue, Stack, ArrayList ni ninguna coleccion de java.util
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int size;

    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.size = 0;
    }

    // Agrega un elemento al final de la cola
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

    // Saca y devuelve el primer elemento de la cola
    public T desencolar() {
        if (estaVacia()) return null;
        T data = frente.getData();
        frente = frente.getNext();
        if (frente == null) finalCola = null;
        size--;
        return data;
    }

    // Devuelve el primer elemento sin sacarlo
    public T verFrente() {
        if (estaVacia()) return null;
        return frente.getData();
    }

    public boolean estaVacia() { return frente == null; }

    public int getSize() { return size; }

    // Devuelve el nodo frontal (util para recorrer la cola sin modificarla)
    public Nodo<T> getFrente() { return frente; }
}
