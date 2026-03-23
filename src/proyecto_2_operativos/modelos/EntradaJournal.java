package proyecto_2_operativos.modelos;

// Representa una entrada en el journal del sistema de archivos
// Se usa para garantizar consistencia ante fallos (inspirado en NTFS journaling)
public class EntradaJournal {

    // Estados posibles de una entrada en el journal
    public enum EstadoJournal {
        PENDIENTE,
        CONFIRMADO,
        DESHECHO
    }

    private int id;
    private static int contadorId = 1;

    private OperacionCRUD operacion;
    private String nombreArchivo;
    private int tamanoBloques;     // Usado solo si la operacion es CREAR
    private int primerBloqueAsignado; // Guardado para poder deshacer el CREAR
    private EstadoJournal estado;
    private long timestamp;

    public EntradaJournal(OperacionCRUD operacion, String nombreArchivo, int tamanoBloques) {
        this.id = contadorId++;
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.tamanoBloques = tamanoBloques;
        this.primerBloqueAsignado = -1;
        this.estado = EstadoJournal.PENDIENTE;
        this.timestamp = System.currentTimeMillis();
    }

    public static void resetContador() { contadorId = 1; }

    public int getId() { return id; }

    public OperacionCRUD getOperacion() { return operacion; }

    public String getNombreArchivo() { return nombreArchivo; }

    public int getTamanoBloques() { return tamanoBloques; }

    public int getPrimerBloqueAsignado() { return primerBloqueAsignado; }
    public void setPrimerBloqueAsignado(int bloque) { this.primerBloqueAsignado = bloque; }

    public EstadoJournal getEstado() { return estado; }
    public void setEstado(EstadoJournal estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "[J" + id + "] " + operacion + " \"" + nombreArchivo + "\" -> " + estado;
    }
}
