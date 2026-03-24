package proyecto_2_operativos.gui;

import proyecto_2_operativos.controladores.GestorArchivos;
import proyecto_2_operativos.controladores.PlanificadorDisco;
import proyecto_2_operativos.controladores.PlanificadorDisco.Politica;
import proyecto_2_operativos.controladores.PlanificadorDisco.ResultadoPlanificacion;
import proyecto_2_operativos.estructuras.ListaEnlazada;
import proyecto_2_operativos.modelos.*;
import proyecto_2_operativos.controladores.HiloProcesador;
import proyecto_2_operativos.modelos.OperacionCRUD;

/**
 * Ventana principal del simulador de sistema de archivos concurrente
 * Mantiene el diseno original del .form y conecta toda la logica nueva
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    // Array de etiquetas para visualizar los bloques del disco (10x10 = 100 bloques)
    private javax.swing.JLabel[] bloquesVisuales = new javax.swing.JLabel[100];

    // Controlador principal del sistema de archivos
    private GestorArchivos gestor;

    // Modelos para las listas de journal y cola de procesos
    private javax.swing.DefaultListModel<String> modeloJournal;
    private javax.swing.DefaultListModel<String> modeloCola;

    // Modelo para la tabla de archivos
    private javax.swing.table.DefaultTableModel modeloTabla;

    // Directorio actualmente activo (donde se crean los archivos)
    private Directorio directorioActual;

    // Posicion actual del cabezal del disco para el planificador
    private int posicionCabezal = 0;

    // Modo actual: true = Administrador, false = Usuario
    private boolean modoAdmin = true;

    /**
     * Constructor: inicializa el gestor, la GUI y el disco visual
     */
    public VentanaPrincipal() {
        initComponents();
        gestor = new GestorArchivos(100);
        directorioActual = gestor.getDirectorioRaiz();
        

        // Inicializamos los modelos de las listas
        modeloJournal = new javax.swing.DefaultListModel<>();
        modeloCola    = new javax.swing.DefaultListModel<>();
        jList1.setModel(modeloJournal);
        jList2.setModel(modeloCola);

        // Inicializamos el modelo de la tabla con columnas correctas
        modeloTabla = new javax.swing.table.DefaultTableModel(
            new String[]{"Archivo", "Dueño", "Bloques", "Primer Bloque", "Color"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        jTable1.setModel(modeloTabla);

        // Renderer para pintar la columna Color con el color real del archivo
        jTable1.getColumnModel().getColumn(4).setCellRenderer(
            (table, value, isSelected, hasFocus, row, col) -> {
                javax.swing.JLabel lbl = new javax.swing.JLabel(
                    value == null ? "" : value.toString());
                lbl.setOpaque(true);
                try { lbl.setBackground(java.awt.Color.decode(value.toString())); }
                catch (Exception ex) { lbl.setBackground(java.awt.Color.GRAY); }
                return lbl;
            }
        );

        // Conectamos los botones que el .form dejo sin listener
        jButton2.addActionListener(e -> accionLeer());
        jButton3.addActionListener(e -> accionActualizar());
        jButton4.addActionListener(e -> accionEliminar());

        // Listener del arbol: cambia el directorio activo al seleccionar
        jTree1.addTreeSelectionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode nodo =
                (javax.swing.tree.DefaultMutableTreeNode)
                jTree1.getLastSelectedPathComponent();
            if (nodo != null && nodo.getUserObject() instanceof Directorio) {
                directorioActual = (Directorio) nodo.getUserObject();
            }
        });
        HiloProcesador procesador = new HiloProcesador(this, gestor);
        procesador.start();
        inicializarDiscoVisual();
        actualizarTodo();
    }

    /**
     * Metodo generado por el Form Editor - NO modificar
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelDiscoVirtual = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(30, 35, 45));
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));

        jPanel1.setBackground(new java.awt.Color(30, 35, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(40, 45, 55));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 80));

        jButton1.setText("Crear");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Leer");

        jButton3.setText("Actualizar");

        jButton4.setText("Eliminar");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Administrador", "Usuario", " ", " "}));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"FIFO", "SSTF", "SCAN", "C-SCAN"}));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(647, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setBackground(new java.awt.Color(40, 45, 55));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel3.setPreferredSize(new java.awt.Dimension(280, 0));

        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setBackground(new java.awt.Color(40, 45, 55));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{{null, null, null}, {null, null, null},
                           {null, null, null}, {null, null, null}},
            new String[]{"Archivo", "Bloques", "Primer Bloque"}
        ));
        jScrollPane2.setViewportView(jTable1);

        panelDiscoVirtual.setBackground(new java.awt.Color(40, 45, 55));

        javax.swing.GroupLayout panelDiscoVirtualLayout =
            new javax.swing.GroupLayout(panelDiscoVirtual);
        panelDiscoVirtual.setLayout(panelDiscoVirtualLayout);
        panelDiscoVirtualLayout.setHorizontalGroup(
            panelDiscoVirtualLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelDiscoVirtualLayout.setVerticalGroup(
            panelDiscoVirtualLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
            .addComponent(panelDiscoVirtual, javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                    269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelDiscoVirtual, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(40, 45, 55));
        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel5.setPreferredSize(new java.awt.Dimension(280, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cola de Procesos");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Journal");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52))
                    .addComponent(jScrollPane4)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                    30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE,
                    167, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                    30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE,
                    254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>

    // -----------------------------------------------------------------------
    // LISTENERS DE LOS COMPONENTES DEL FORM
    // -----------------------------------------------------------------------

    // Listener del comboBox de modo (Administrador / Usuario)
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        modoAdmin = jComboBox1.getSelectedIndex() == 0;
        // Bloqueamos los botones de escritura en modo Usuario
        jButton1.setEnabled(modoAdmin);
        jButton3.setEnabled(modoAdmin);
        jButton4.setEnabled(modoAdmin);
    }

    // Listener del boton Crear
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        accionCrear();
    }

    // -----------------------------------------------------------------------
    // ACCIONES CRUD
    // -----------------------------------------------------------------------

    // Muestra un dialogo para crear un archivo y lo agrega al sistema
    private void accionCrear() {
        javax.swing.JTextField txtNombre  = new javax.swing.JTextField();
        javax.swing.JTextField txtBloques = new javax.swing.JTextField();
        javax.swing.JTextField txtDueno   = new javax.swing.JTextField("Administrador");

        Object[] campos = {
            "Nombre del archivo:", txtNombre,
            "Tamano en bloques (1-" + gestor.getDisco().getBloquesLibres() + "):", txtBloques,
            "Dueño:", txtDueno
        };

        int opcion = javax.swing.JOptionPane.showConfirmDialog(this, campos,
            "Crear Archivo", javax.swing.JOptionPane.OK_CANCEL_OPTION,
            javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (opcion != javax.swing.JOptionPane.OK_OPTION) return;

        String nombre = txtNombre.getText().trim();
        String dueno  = txtDueno.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacio.");
            return;
        }

        int bloques;
        try {
            bloques = Integer.parseInt(txtBloques.getText().trim());
            if (bloques <= 0 || bloques > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un numero valido de bloques (1-100).");
            return;
        }

        String color = gestor.siguienteColor();

        // Creamos el PCB para registrar el proceso en la cola
        PCB proceso = new PCB(OperacionCRUD.CREAR, nombre, dueno,
            gestor.getDisco().primerBloqueLibre(), bloques);
        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestor.encolarProceso(proceso);

        int resultado = gestor.crearArchivo(directorioActual, nombre, bloques, dueno, color);
        proceso.setEstado(EstadoProceso.TERMINADO);

        switch (resultado) {
            case 0:
                posicionCabezal = proceso.getPosicionCabezal();
                mostrarInfo("Archivo \"" + nombre + "\" creado con " + bloques + " bloques.");
                break;
            case -1:
                mostrarError("No hay espacio suficiente en el disco.");
                break;
            case -2:
                proceso.setEstado(EstadoProceso.BLOQUEADO);
                mostrarError("El archivo esta bloqueado por otro proceso.");
                break;
            case -3:
                mostrarError("FALLO SIMULADO: operacion interrumpida antes del commit.\n"
                    + "Use 'Recuperar Journal' para restaurar la consistencia.");
                break;
        }
        actualizarTodo();
    }

    // Muestra un dialogo para leer un archivo del directorio actual
    private void accionLeer() {
        String nombre = javax.swing.JOptionPane.showInputDialog(this,
            "Nombre del archivo a leer:", "Leer Archivo",
            javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (nombre == null || nombre.trim().isEmpty()) return;
        nombre = nombre.trim();

        // Creamos el PCB para registrar la operacion de lectura
        Archivo arch = directorioActual.buscarArchivo(nombre);
        PCB proceso = new PCB(OperacionCRUD.LEER, nombre,
            modoAdmin ? "Administrador" : "Usuario",
            arch != null ? arch.getPrimerBloque() : 0);
        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestor.encolarProceso(proceso);

        Archivo encontrado = gestor.leerArchivo(directorioActual, nombre);
        proceso.setEstado(EstadoProceso.TERMINADO);

        if (encontrado != null) {
            posicionCabezal = encontrado.getPrimerBloque();
            mostrarInfo("Archivo encontrado:\n"
                + "  Nombre:       " + encontrado.getNombre() + "\n"
                + "  Dueño:        " + encontrado.getDueno() + "\n"
                + "  Bloques:      " + encontrado.getTamanoBloques() + "\n"
                + "  Primer bloque: " + encontrado.getPrimerBloque());
        } else {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            mostrarError("Archivo no encontrado o bloqueado por otro proceso.");
        }
        actualizarTodo();
    }

    // Muestra un dialogo para renombrar un archivo (solo administrador)
    private void accionActualizar() {
        javax.swing.JTextField txtActual = new javax.swing.JTextField();
        javax.swing.JTextField txtNuevo  = new javax.swing.JTextField();
        Object[] campos = {"Nombre actual:", txtActual, "Nuevo nombre:", txtNuevo};

        int opcion = javax.swing.JOptionPane.showConfirmDialog(this, campos,
            "Renombrar Archivo", javax.swing.JOptionPane.OK_CANCEL_OPTION,
            javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (opcion != javax.swing.JOptionPane.OK_OPTION) return;

        String actual = txtActual.getText().trim();
        String nuevo  = txtNuevo.getText().trim();

        if (actual.isEmpty() || nuevo.isEmpty()) {
            mostrarError("Los campos no pueden estar vacios.");
            return;
        }

        PCB proceso = new PCB(OperacionCRUD.ACTUALIZAR, actual, "Administrador", 0);
        proceso.setNuevoNombre(nuevo);
        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestor.encolarProceso(proceso);

        int resultado = gestor.actualizarNombreArchivo(directorioActual, actual, nuevo);
        proceso.setEstado(EstadoProceso.TERMINADO);

        if (resultado == 0) {
            mostrarInfo("Archivo renombrado a \"" + nuevo + "\".");
        } else if (resultado == -1) {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            mostrarError("Archivo \"" + actual + "\" no encontrado.");
        } else {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            mostrarError("El archivo esta bloqueado por otro proceso.");
        }
        actualizarTodo();
    }

    // Muestra un dialogo para eliminar un archivo del directorio actual
    private void accionEliminar() {
        String nombre = javax.swing.JOptionPane.showInputDialog(this,
            "Nombre del archivo a eliminar:", "Eliminar Archivo",
            javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (nombre == null || nombre.trim().isEmpty()) return;
        nombre = nombre.trim();

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Seguro que desea eliminar \"" + nombre + "\"?",
            "Confirmar eliminacion", javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        PCB proceso = new PCB(OperacionCRUD.ELIMINAR, nombre, "Administrador", 0);
        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestor.encolarProceso(proceso);

        int resultado = gestor.eliminarArchivo(directorioActual, nombre);
        proceso.setEstado(EstadoProceso.TERMINADO);

        if (resultado == 0) {
            mostrarInfo("Archivo \"" + nombre + "\" eliminado. Bloques liberados.");
        } else if (resultado == -1) {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            mostrarError("Archivo no encontrado.");
        } else {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            mostrarError("El archivo esta bloqueado por otro proceso.");
        }
        actualizarTodo();
    }

    // -----------------------------------------------------------------------
    // INICIALIZACION Y ACTUALIZACION DEL DISCO VISUAL
    // -----------------------------------------------------------------------

    // Inicializa la cuadricula de 10x10 bloques en el panel del disco
    private void inicializarDiscoVisual() {
        panelDiscoVirtual.setLayout(new java.awt.GridLayout(10, 10, 2, 2));

        for (int i = 0; i < 100; i++) {
            bloquesVisuales[i] = new javax.swing.JLabel(
                String.format("%02d", i), javax.swing.SwingConstants.CENTER);
            bloquesVisuales[i].setOpaque(true);
            bloquesVisuales[i].setBackground(new java.awt.Color(60, 65, 75));
            bloquesVisuales[i].setForeground(new java.awt.Color(220, 220, 220));
            bloquesVisuales[i].setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(30, 35, 45)));
            panelDiscoVirtual.add(bloquesVisuales[i]);
        }
        panelDiscoVirtual.revalidate();
        panelDiscoVirtual.repaint();
    }

    // Pinta los bloques segun el estado actual del disco virtual
    public void actualizarDiscoVisual() {
        proyecto_2_operativos.modelos.Bloque[] bloquesDelDisco =
            gestor.getDisco().getBloques();

        for (int i = 0; i < 100; i++) {
            if (bloquesDelDisco[i].isOcupado()) {
                try {
                    bloquesVisuales[i].setBackground(
                        java.awt.Color.decode(bloquesDelDisco[i].getColorHex()));
                } catch (Exception ex) {
                    bloquesVisuales[i].setBackground(java.awt.Color.GRAY);
                }
                bloquesVisuales[i].setForeground(java.awt.Color.BLACK);
                bloquesVisuales[i].setToolTipText(bloquesDelDisco[i].getNombreArchivo());
            } else {
                bloquesVisuales[i].setBackground(new java.awt.Color(60, 65, 75));
                bloquesVisuales[i].setForeground(new java.awt.Color(220, 220, 220));
                bloquesVisuales[i].setToolTipText("Libre");
            }
        }

        // Marcamos la posicion actual del cabezal con borde amarillo
        if (posicionCabezal >= 0 && posicionCabezal < 100) {
            bloquesVisuales[posicionCabezal].setBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(255, 220, 0), 2));
        }
        panelDiscoVirtual.repaint();
    }

    // -----------------------------------------------------------------------
    // ACTUALIZACION DE TODOS LOS COMPONENTES
    // -----------------------------------------------------------------------

    // Actualiza todos los componentes visuales de una sola vez
    private void actualizarTodo() {
        actualizarArbol();
        actualizarTabla();
        actualizarDiscoVisual();
        actualizarJournal();
        actualizarCola();
    }

    // Reconstruye el JTree con toda la jerarquia de directorios y archivos
    public void actualizarArbol() {
        javax.swing.tree.DefaultMutableTreeNode nodoRaiz =
            construirNodoArbol(gestor.getDirectorioRaiz());
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(nodoRaiz));
        for (int i = 0; i < jTree1.getRowCount(); i++) jTree1.expandRow(i);
    }

    // Construye recursivamente el arbol de nodos para el JTree
    private javax.swing.tree.DefaultMutableTreeNode construirNodoArbol(Directorio dir) {
        javax.swing.tree.DefaultMutableTreeNode nodo =
            new javax.swing.tree.DefaultMutableTreeNode(dir);

        // Primero agregamos los subdirectorios
        for (int i = 0; i < dir.getSubdirectorios().getSize(); i++) {
            nodo.add(construirNodoArbol(dir.getSubdirectorios().get(i)));
        }
        // Luego los archivos como hojas
        for (int i = 0; i < dir.getArchivos().getSize(); i++) {
            Archivo a = dir.getArchivos().get(i);
            String texto = a.getNombre() + " (" + a.getTamanoBloques()
                + " bloq, dueño: " + a.getDueno() + ")";
            nodo.add(new javax.swing.tree.DefaultMutableTreeNode(texto));
        }
        return nodo;
    }

    // Actualiza el JTable con todos los archivos del sistema
    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        llenarTablaRecursivo(gestor.getDirectorioRaiz());
    }

    // Recorre recursivamente los directorios para llenar la tabla
    private void llenarTablaRecursivo(Directorio dir) {
        ListaEnlazada<Archivo> archivos = dir.getArchivos();
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo a = archivos.get(i);
            modeloTabla.addRow(new Object[]{
                a.getNombre(), a.getDueno(),
                a.getTamanoBloques(), a.getPrimerBloque(), a.getColorHex()
            });
        }
        for (int i = 0; i < dir.getSubdirectorios().getSize(); i++) {
            llenarTablaRecursivo(dir.getSubdirectorios().get(i));
        }
    }

    // Actualiza la lista del journal con el estado actual de las transacciones
    private void actualizarJournal() {
        modeloJournal.clear();
        ListaEnlazada<EntradaJournal> journal = gestor.getJournal();
        for (int i = 0; i < journal.getSize(); i++) {
            EntradaJournal e = journal.get(i);
            String icono =
                e.getEstado() == EntradaJournal.EstadoJournal.CONFIRMADO ? "[OK]   " :
                e.getEstado() == EntradaJournal.EstadoJournal.DESHECHO   ? "[UNDO] " :
                                                                            "[PEND] ";
            modeloJournal.addElement(icono + e.toString());
        }
    }

    // Actualiza la lista de la cola de procesos de E/S
    private void actualizarCola() {
        modeloCola.clear();
        proyecto_2_operativos.estructuras.Nodo<PCB> nodo =
            gestor.getColaES().getFrente();
        while (nodo != null) {
            modeloCola.addElement(nodo.getData().toString());
            nodo = nodo.getNext();
        }
    }

    // -----------------------------------------------------------------------
    // PLANIFICADOR DE DISCO (usando jComboBox2 para la politica)
    // -----------------------------------------------------------------------

    // Ejecuta el planificador con la politica seleccionada en jComboBox2
    public void ejecutarPlanificador(int cabezalInicial) {
        if (gestor.getColaES().estaVacia()) {
            mostrarInfo("La cola de procesos esta vacia. Cree archivos primero.");
            return;
        }

        String politicaStr = (String) jComboBox2.getSelectedItem();
        Politica politica;
        switch (politicaStr) {
            case "SSTF":   politica = Politica.SSTF;  break;
            case "SCAN":   politica = Politica.SCAN;  break;
            case "C-SCAN": politica = Politica.CSCAN; break;
            default:       politica = Politica.FIFO;  break;
        }

        ResultadoPlanificacion resultado = PlanificadorDisco.ejecutar(
            gestor.getColaES(), cabezalInicial, politica, true);

        StringBuilder sb = new StringBuilder();
        sb.append("Politica: ").append(politicaStr).append("\n");
        sb.append("Cabezal inicial: ").append(cabezalInicial).append("\n");
        sb.append("Desplazamiento total: ")
          .append(resultado.desplazamientoTotal).append(" pistas\n\n");
        sb.append("Orden de atencion:\n");

        for (int i = 0; i < resultado.ordenEjecucion.getSize(); i++) {
            sb.append("  ").append(i + 1).append(". Bloque ")
              .append(resultado.ordenEjecucion.get(i))
              .append("  [").append(resultado.nombresArchivos.get(i)).append("]\n");
        }

        // Animamos el cabezal en el disco visual
        animarCabezal(cabezalInicial, resultado.ordenEjecucion);

        javax.swing.JTextArea area = new javax.swing.JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(420, 280));

        javax.swing.JOptionPane.showMessageDialog(this, scroll,
            "Resultado - " + politicaStr, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    // Mueve el cabezal visualmente por las posiciones del planificador
    private void animarCabezal(int inicio,
            ListaEnlazada<Integer> posiciones) {
        // Reiniciamos todos los bordes
        for (int i = 0; i < 100; i++) {
            bloquesVisuales[i].setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(30, 35, 45)));
        }

        final int[] indice    = {-1};
        final int[] posActual = {inicio};

        javax.swing.Timer timer = new javax.swing.Timer(300, null);
        timer.addActionListener(e -> {
            // Quitamos el borde del bloque anterior
            if (posActual[0] >= 0 && posActual[0] < 100) {
                bloquesVisuales[posActual[0]].setBorder(
                    javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(30, 35, 45)));
            }
            indice[0]++;
            if (indice[0] >= posiciones.getSize()) {
                timer.stop();
                posicionCabezal = posActual[0];
                return;
            }
            posActual[0] = posiciones.get(indice[0]);
            if (posActual[0] >= 0 && posActual[0] < 100) {
                bloquesVisuales[posActual[0]].setBorder(
                    javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(255, 220, 0), 3));
                panelDiscoVirtual.repaint();
            }
        });
        timer.start();
    }

    // -----------------------------------------------------------------------
    // UTILIDADES
    // -----------------------------------------------------------------------

    private void mostrarInfo(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje,
            "Informacion", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje,
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    // Devuelve el gestor para uso externo si se necesita
    public GestorArchivos getGestor() { return gestor; }

    // -----------------------------------------------------------------------
    // MAIN
    // -----------------------------------------------------------------------
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException |
                 javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
    
    public void actualizarTodoThreadSafe() {
    actualizarArbol();
    actualizarTabla();
    actualizarDiscoVisual();
    actualizarCola();
    actualizarJournal();
}

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    private javax.swing.JPanel panelDiscoVirtual;
    // End of variables declaration
}
