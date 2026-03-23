package proyecto_2_operativos.estructuras;

// Nodo generico usado por la lista enlazada y la cola
public class Nodo<T> {
    private T data;
    private Nodo<T> next;

    public Nodo(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public Nodo<T> getNext() { return next; }
    public void setNext(Nodo<T> next) { this.next = next; }
}
