/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import Controller.EmpleadosJpaController;
import Entities.Empleados;

import Controller.ProduccionJpaController;
import Entities.Produccion;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author gpera
 */
public class PanelEmpleados extends javax.swing.JPanel {

    EmpleadosJpaController ctrempleados = new EmpleadosJpaController();
    Empleados empleados = new Empleados();

    ProduccionJpaController ctrproduccion = new ProduccionJpaController();
    Produccion produccion = new Produccion();

    /**
     * Creates new form PanelEmpleados
     */
    public PanelEmpleados() {
        initComponents();
        // showBarChart();
        rellenarTabla();
        agregarMouseListenerTabla();
    }

    public void agregarMouseListenerTabla() {
        jtEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jtEmpleados.getSelectedRow();
                    if (row != -1) {
                        TFIdEmpleado.setText(jtEmpleados.getValueAt(row, 0).toString());
                        TFNombreEmpleado.setText(jtEmpleados.getValueAt(row, 1).toString());
                        TFApellidoEmpleado.setText(jtEmpleados.getValueAt(row, 2).toString());
                        TFCargo.setText(jtEmpleados.getValueAt(row, 3).toString());
                        TFSalario.setText(jtEmpleados.getValueAt(row, 4).toString());
                        TFTelefono.setText(jtEmpleados.getValueAt(row, 5).toString());
                        TFEmail.setText(jtEmpleados.getValueAt(row, 6).toString());
                        TFCantidadP.setText(jtEmpleados.getValueAt(row, 7).toString());

                        BTGuardar.setEnabled(false); // Deshabilitar botón Guardar
                    }
                }
            }
        });
    }

    public void rellenarTabla() {
        String columna[] = { "ID", "Nombre", "Apellido", "Cargo", "Salario", "Telefono", "Email",
                "Cantidad Producida" };
        DefaultTableModel modelo = new DefaultTableModel(columna, 0);
        Object[] obj = new Object[8];
        List<Empleados> ls;
        try {
            ls = ctrempleados.findEmpleadosEntities();
            for (Empleados empleado : ls) {
                obj[0] = empleado.getIdEmpleado();
                obj[1] = empleado.getNombreEmpleado();
                obj[2] = empleado.getApellidoEmpleado();
                obj[3] = empleado.getCargo();
                obj[4] = empleado.getSalario();
                obj[5] = empleado.getTelefono();
                obj[6] = empleado.getEmail();
                int cantidadProducida = obtenerCantidadProducida(empleado);
                obj[7] = cantidadProducida;
                modelo.addRow(obj);

                jtEmpleados.setAutoCreateRowSorter(true);
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
                jtEmpleados.setRowSorter(sorter);

                jtEmpleados.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent mouseEvent) {
                        JTable table = (JTable) mouseEvent.getSource();
                        Point point = mouseEvent.getPoint();

                        if (mouseEvent.getClickCount() == 2) {
                            TFIdEmpleado.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 0).toString());
                            TFNombreEmpleado
                                    .setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 1).toString());
                            TFApellidoEmpleado
                                    .setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 2).toString());
                            TFCargo.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 3).toString());
                            TFSalario.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 4).toString());
                            TFTelefono.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 5).toString());
                            TFEmail.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 6).toString());
                            TFCantidadP.setText(jtEmpleados.getValueAt(jtEmpleados.getSelectedRow(), 7).toString());

                        }

                        ActivarEditar();
                    }
                });
            }
            jtEmpleados.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Error");
        }
    }

    public void ActivarEditar() {
        TFNombreEmpleado.setEnabled(true);
        TFApellidoEmpleado.setEnabled(true);
        TFCargo.setEnabled(true);
        TFSalario.setEnabled(true);
        TFTelefono.setEnabled(true);
        TFEmail.setEnabled(true);
        TFCantidadP.setEnabled(true);

        BTGuardar.setEnabled(true);
        BTEditar.setEnabled(true);
        BTNuevo.setEnabled(false);
    }

    private int obtenerCantidadProducida(Empleados empleado) {
        int cantidadTotal = 0;
        try {
            List<Integer> producciones = ctrproduccion.findCantidadProducidaByEmpleado(empleado);
            for (Integer produccion : producciones) {
                if (produccion != null) {
                    cantidadTotal += produccion;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + " Error al obtener la cantidad producida");
        }
        return cantidadTotal;
    }

    public void showHistogram() {

        double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
                12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
                49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
                93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
                77, 44, 58, 91, 10, 67, 57, 19, 88, 84
        };

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 20);

        JFreeChart chart = ChartFactory.createHistogram("JFreeChart Histogram",
                "Data",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        /*
         * panel.setPreferredSize(Histograma.getPreferredSize());
         * 
         * Histograma.setLayout(new BorderLayout());
         * Histograma.add(panel, BorderLayout.NORTH);
         * 
         * 
         */
        repaint();
    }

    public void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(200, "Produccion", "Enero");
        dataset.setValue(150, "Produccion", "Febrero");
        dataset.setValue(18, "Produccion", "Marzo");
        dataset.setValue(100, "Produccion", "Abril");
        dataset.setValue(80, "Produccion", "Mayo");
        dataset.setValue(250, "Produccion", "Junio");

        JFreeChart chart = ChartFactory.createBarChart("Grafico de empleados", "Mensual", "Produccion",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setRangeGridlinePaint(Color.BLUE);
        categoryPlot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color clr3 = new Color(204, 0, 51);
        renderer.setSeriesPaint(0, clr3);

        /*
         * ChartPanel barpChartPanel = new ChartPanel(chart);
         * 
         * Histograma.removeAll();
         * Histograma.add(barpChartPanel, BorderLayout.CENTER);
         * Histograma.validate();
         */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtEmpleados = new javax.swing.JTable();
        BTEditar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TFNombreEmpleado = new javax.swing.JTextField();
        TFApellidoEmpleado = new javax.swing.JTextField();
        TFIdEmpleado = new javax.swing.JTextField();
        TFSalario = new javax.swing.JTextField();
        TFTelefono = new javax.swing.JTextField();
        TFEmail = new javax.swing.JTextField();
        TFCantidadP = new javax.swing.JTextField();
        TFCargo = new javax.swing.JTextField();
        BTNuevo = new javax.swing.JButton();
        BTGuardar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        TFIdProducto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        TFFechaP = new javax.swing.JTextField();

        setMaximumSize(new java.awt.Dimension(678, 600));
        setMinimumSize(new java.awt.Dimension(678, 600));
        setPreferredSize(new java.awt.Dimension(678, 600));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setMaximumSize(new java.awt.Dimension(678, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(678, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(678, 800));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Este es el panel empleados");

        jtEmpleados.setBackground(new java.awt.Color(204, 204, 204));
        jtEmpleados.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jtEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        jtEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtEmpleados.setToolTipText("");
        jtEmpleados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtEmpleados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtEmpleados.setMaximumSize(new java.awt.Dimension(60, 64));
        jtEmpleados.setRowHeight(30);
        jScrollPane1.setViewportView(jtEmpleados);

        BTEditar.setFont(new java.awt.Font("Cascadia Code", 1, 20)); // NOI18N
        BTEditar.setText("Editar");
        BTEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTEditarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Nombre empleado");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Apellido empleado");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("ID Empleado");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Salario");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Telefono");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Cargo");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Email");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Cantidad producida");

        TFNombreEmpleado.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFApellidoEmpleado.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFIdEmpleado.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        TFIdEmpleado.setEnabled(false);

        TFSalario.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFTelefono.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFEmail.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFCantidadP.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        TFCargo.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        BTNuevo.setFont(new java.awt.Font("Cascadia Code", 1, 20)); // NOI18N
        BTNuevo.setText("Nuevo");
        BTNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNuevoActionPerformed(evt);
            }
        });

        BTGuardar.setFont(new java.awt.Font("Cascadia Code", 1, 20)); // NOI18N
        BTGuardar.setText("Guardar");
        BTGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGuardarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("ID Producto");

        TFIdProducto.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Fecha Produccion");

        TFFechaP.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(89, 89, 89)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6))))
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(TFCantidadP, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BTGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BTEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(BTNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(TFIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(TFFechaP, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(17, 17, 17))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(TFIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(TFNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(TFIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(TFFechaP, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(TFCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(TFSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(TFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BTNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(BTEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(TFCantidadP, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(BTGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void BloquearControles() {
        TFNombreEmpleado.setEnabled(false);
        TFApellidoEmpleado.setEnabled(false);
        TFCargo.setEnabled(false);
        TFSalario.setEnabled(false);
        TFTelefono.setEnabled(false);
        TFEmail.setEnabled(false);
        TFCantidadP.setEnabled(false);

        BTGuardar.setEnabled(false);
        BTEditar.setEnabled(false);
        BTNuevo.setEnabled(true);
    }

    private void BTEditarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Validación para TFIdEmpleado
            if (TFIdEmpleado.getText().trim().isEmpty() || !TFIdEmpleado.getText().matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "ID de empleado inválido.");
                return;
            }
            
            // Validación para TFSalario
            if (TFSalario.getText().trim().isEmpty() || !TFSalario.getText().matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(null, "Salario inválido.");
                return;
            }
            
            
            // Validación para TFCantidadP
            if (TFCantidadP.getText().trim().isEmpty() || !TFCantidadP.getText().matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Cantidad producida inválida.");
                return;
            }
            
            // Validación para TFIdProducto
            if (TFIdProducto.getText().trim().isEmpty() || !TFIdProducto.getText().matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "ID de producto inválido.");
                return;
            }
            empleados.setIdEmpleado(Integer.parseInt(TFIdEmpleado.getText()));
            empleados.setNombreEmpleado(TFNombreEmpleado.getText());
            empleados.setApellidoEmpleado(TFApellidoEmpleado.getText());
            empleados.setCargo(TFCargo.getText());
            empleados.setSalario(Double.parseDouble(TFSalario.getText()));
            
            empleados.setTelefono(TFTelefono.getText());
            empleados.setEmail(TFEmail.getText());
            produccion.setCantidadProducida(Integer.parseInt(TFCantidadP.getText()));
            produccion.setIdProducto(Integer.parseInt(TFIdProducto.getText()));
            produccion.setIdEmpleado(empleados);

            updateEmpleado(empleados);
            updateProduccion(produccion);

            BloquearControles();
            JOptionPane.showMessageDialog(null, "Editado Correctamente");
            rellenarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + " Error");
        }
    }

    public void updateEmpleado(Empleados empleado) {
        String sql = "UPDATE empleados SET nombreEmpleado=?, apellidoEmpleado=?, cargo=?, salario=?, telefono=?, email=? WHERE idEmpleado=?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medicinanatural", "root", "rootsito");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, empleado.getNombreEmpleado());
            pstmt.setString(2, empleado.getApellidoEmpleado());
            pstmt.setString(3, empleado.getCargo());
            pstmt.setDouble(4, empleado.getSalario());
            pstmt.setString(5, empleado.getTelefono());
            pstmt.setString(6, empleado.getEmail());
            pstmt.setInt(7, empleado.getIdEmpleado());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateProduccion(Produccion produccion) {
        String sql = "UPDATE produccion SET cantidadProducida=?, idProducto=? WHERE idEmpleado=?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medicinanatural", "root", "rootsito");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, produccion.getCantidadProducida());
            pstmt.setInt(2, produccion.getIdProducto());
            pstmt.setInt(3, produccion.getIdEmpleado().getIdEmpleado());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void BTNuevoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_BTNuevoActionPerformed
        // vaciar todos los textfields
        TFIdEmpleado.setText("");
        TFNombreEmpleado.setText("");
        TFApellidoEmpleado.setText("");
        TFCargo.setText("");
        TFSalario.setText("");
        TFTelefono.setText("");
        TFEmail.setText("");
        TFCantidadP.setText("");
        TFIdProducto.setText("");

        // habilitar los textfields
        TFNombreEmpleado.setEnabled(true);
        TFApellidoEmpleado.setEnabled(true);
        TFCargo.setEnabled(true);
        TFSalario.setEnabled(true);
        TFTelefono.setEnabled(true);
        TFEmail.setEnabled(true);
        TFCantidadP.setEnabled(true);

        // habilitar los botones
        BTGuardar.setEnabled(true);
        BTEditar.setEnabled(true);
        BTNuevo.setEnabled(false);

    }// GEN-LAST:event_BTNuevoActionPerformed

    private void BTGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            empleados.setNombreEmpleado(TFNombreEmpleado.getText());
            empleados.setApellidoEmpleado(TFApellidoEmpleado.getText());
            empleados.setCargo(TFCargo.getText());
            empleados.setSalario(Double.parseDouble(TFSalario.getText()));
            empleados.setTelefono(TFTelefono.getText());
            empleados.setEmail(TFEmail.getText());
            produccion.setCantidadProducida(Integer.parseInt(TFCantidadP.getText()));
            produccion.setIdProducto(Integer.parseInt(TFIdProducto.getText()));
            produccion.setIdEmpleado(empleados);

            // Convertir la fecha de String a Date
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaProduccion = formatoFecha.parse(TFFechaP.getText());
            produccion.setFechaProduccion(fechaProduccion);

            ctrempleados.create(empleados);
            ctrproduccion.create(produccion);

            BloquearControles();
            JOptionPane.showMessageDialog(null, "Guardado Correctamente");
            rellenarTabla(); // Refresh the table after saving
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + " Error");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTEditar;
    private javax.swing.JButton BTGuardar;
    private javax.swing.JButton BTNuevo;
    private javax.swing.JTextField TFApellidoEmpleado;
    private javax.swing.JTextField TFCantidadP;
    private javax.swing.JTextField TFCargo;
    private javax.swing.JTextField TFEmail;
    private javax.swing.JTextField TFFechaP;
    private javax.swing.JTextField TFIdEmpleado;
    private javax.swing.JTextField TFIdProducto;
    private javax.swing.JTextField TFNombreEmpleado;
    private javax.swing.JTextField TFSalario;
    private javax.swing.JTextField TFTelefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtEmpleados;
    // End of variables declaration//GEN-END:variables
}
