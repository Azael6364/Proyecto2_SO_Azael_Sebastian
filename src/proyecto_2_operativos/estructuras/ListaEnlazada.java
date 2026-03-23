package proyecto_2_operativos.estructuras;

// Lista enlazada simple generica implementada manualmente
// No usa java.util.ArrayList ni ninguna coleccion del framework
public class ListaEnlazada<T> {
    private Nodo<T> head;
    private int size;

    public ListaEnlazada() {
        this.head = null;
        this.size = 0;
    }

    // Agrega un elemento al final de la lista
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

    // Retorna el elemento en el indice dado, o null si esta fuera de rango
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Nodo<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    // Elimina el elemento en el indice dado. Retorna true si tuvo exito
    public boolean remove(int index) {
        if (index < 0 || index >= size) return false;
        if (index == 0) {
            head = head.getNext();
        } else {
            Nodo<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            current.setNext(current.getNext().getNext());
        }
        size--;
        return true;
    }

    // Elimina la primera ocurrencia del objeto dado. Retorna true si lo encontro
    public boolean removeObject(T data) {
        if (head == null) return false;
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }
        Nodo<T> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData().equals(data)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return head == null; }

    // Devuelve el nodo cabeza (util para iteraciones externas)
    public Nodo<T> getHead() { return head; }
}
