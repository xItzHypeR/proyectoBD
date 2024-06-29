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
    }

    public void rellenarTabla() {
        String columna[] = {"ID", "Nombre", "Apellido", "Cargo", "Salario", "Telefono", "Email", "Cantidad Producida"};
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
            }
            jtEmpleados.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Error");
        }
    }

    private int obtenerCantidadProducida(Empleados empleado) {
        int cantidadTotal = 0;
        try {
            List<Produccion> producciones = ctrproduccion.findProduccionByEmpleado(empleado);
            for (Produccion produccion : producciones) {
                cantidadTotal += produccion.getCantidadProducida();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Error al obtener la cantidad producida");
        }
        return cantidadTotal;
    }

    public void showHistogram() {

        double[] values = {95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
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
                false
        );
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(Histograma.getPreferredSize());

        Histograma.setLayout(new BorderLayout());
        Histograma.add(panel, BorderLayout.NORTH);

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

        ChartPanel barpChartPanel = new ChartPanel(chart);
        Histograma.removeAll();
        Histograma.add(barpChartPanel, BorderLayout.CENTER);
        Histograma.validate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtEmpleados = new javax.swing.JTable();
        Histograma = new javax.swing.JPanel();

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
        jtEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        jtEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtEmpleados.setToolTipText("");
        jtEmpleados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtEmpleados.setMaximumSize(new java.awt.Dimension(60, 64));
        jScrollPane1.setViewportView(jtEmpleados);

        Histograma.setMaximumSize(new java.awt.Dimension(100, 100));
        Histograma.setMinimumSize(new java.awt.Dimension(100, 100));
        Histograma.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout HistogramaLayout = new javax.swing.GroupLayout(Histograma);
        Histograma.setLayout(HistogramaLayout);
        HistogramaLayout.setHorizontalGroup(
            HistogramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        HistogramaLayout.setVerticalGroup(
            HistogramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(Histograma, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(346, 346, 346)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(Histograma, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1026, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 782, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Histograma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtEmpleados;
    // End of variables declaration//GEN-END:variables
}
