/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
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
import Controller.ProduccionJpaController;
import Entities.Empleados;
import Entities.Produccion;

import Entities.Productos;
import Controller.ProductosJpaController;

import javax.swing.JOptionPane;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

/**
 *
 * @author gpera
 */
public class PanelGraficos extends javax.swing.JPanel {

    EmpleadosJpaController ctrempleados = new EmpleadosJpaController();
    Empleados empleados = new Empleados();

    ProduccionJpaController ctrproduccion = new ProduccionJpaController();
    Produccion produccion = new Produccion();
    
    ProductosJpaController ctrproductos = new ProductosJpaController();
    Productos productos = new Productos();
    

    /**
     * Creates new form PanelEmpleados
     */
    public PanelGraficos() {
        initComponents();

        showHistogramEmpleados();
        showBarChartEmpleados();

        showHistogramInventario();
        showBarChartInventario();

    }

    public void showHistogramEmpleados() {

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
        panel.setPreferredSize(HistogramaEmpleados.getPreferredSize());

        HistogramaEmpleados.setLayout(new BorderLayout());
        HistogramaEmpleados.add(panel, BorderLayout.NORTH);

        repaint();

    }

    public void showBarChartEmpleados() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    List<Empleados> empleadosList = ctrempleados.findEmpleadosEntities();
    
    // Ordenar empleados por cantidad producida en orden descendente
    empleadosList.sort((e1, e2) -> Integer.compare(obtenerCantidadProducida(e2), obtenerCantidadProducida(e1)));

    // Crear el dataset de las barras
    for (Empleados empleado : empleadosList) {
        int cantidadProducida = obtenerCantidadProducida(empleado);
        dataset.setValue(cantidadProducida, "Producción", empleado.getNombreEmpleado());
    }

    JFreeChart chart = ChartFactory.createBarChart(
            "Gráfico de Producción por Empleado", "Empleado", "Producción",
            dataset, PlotOrientation.VERTICAL, false, true, false);

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setRangeGridlinePaint(Color.BLUE);
    plot.setBackgroundPaint(Color.WHITE);
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    Color clr3 = new Color(204, 0, 51);
    renderer.setSeriesPaint(0, clr3);

    // Crear el dataset de la línea de acumulación
    double totalProduccion = empleadosList.stream().mapToInt(this::obtenerCantidadProducida).sum();
    double acumulado = 0.0;
    DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();

    for (Empleados empleado : empleadosList) {
        int cantidadProducida = obtenerCantidadProducida(empleado);
        acumulado += cantidadProducida;
        double porcentajeAcumulado = (acumulado / totalProduccion) * 100.0;
        lineDataset.addValue(porcentajeAcumulado, "Acumulado", empleado.getNombreEmpleado());
    }

    // Añadir el dataset de la línea al gráfico
    plot.setDataset(1, lineDataset);
    plot.mapDatasetToRangeAxis(1, 0);

    // Crear y personalizar el renderizador de la línea
    LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
    plot.setRenderer(1, lineRenderer);

    // Establecer el orden de renderización de los datasets
    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

    // Mostrar el gráfico en el panel
    ChartPanel barChartPanel = new ChartPanel(chart);
    HistogramaEmpleados.removeAll();
    HistogramaEmpleados.add(barChartPanel, BorderLayout.CENTER);
    HistogramaEmpleados.validate();
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


    public void showHistogramInventario() {

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
        panel.setPreferredSize(HistogramaInventario.getPreferredSize());

        HistogramaInventario.setLayout(new BorderLayout());
        HistogramaInventario.add(panel, BorderLayout.NORTH);

        repaint();

    }

    public void showBarChartInventario() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    List<Productos> productosList = ctrproductos.findProductosEntities();

    // Ordenar productos por precio en orden descendente
    productosList.sort((p1, p2) -> Double.compare(obtenerPrecioProductos(p2), obtenerPrecioProductos(p1)));

    // Crear el dataset de las barras
    for (Productos producto : productosList) {
        double precioProducto = obtenerPrecioProductos(producto);
        dataset.setValue(precioProducto, "Productos", producto.getNombreProducto());
    }

    JFreeChart chart = ChartFactory.createBarChart(
            "Gráfico de precios de productos", "Productos", "Precio",
            dataset, PlotOrientation.VERTICAL, false, true, false);

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setRangeGridlinePaint(Color.BLUE);
    plot.setBackgroundPaint(Color.WHITE);
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    Color clr3 = new Color(204, 0, 51);
    renderer.setSeriesPaint(0, clr3);

    // Crear el dataset de la línea de acumulación
    double totalPrecio = productosList.stream().mapToDouble(this::obtenerPrecioProductos).sum();
    double acumulado = 0.0;
    DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();

    for (Productos producto : productosList) {
        double precioProducto = obtenerPrecioProductos(producto);
        acumulado += precioProducto;
        double porcentajeAcumulado = (acumulado / totalPrecio) * 100.0;
        lineDataset.addValue(porcentajeAcumulado, "Acumulado", producto.getNombreProducto());
    }

    // Añadir el dataset de la línea al gráfico
    plot.setDataset(1, lineDataset);
    plot.mapDatasetToRangeAxis(1, 0);

    // Crear y personalizar el renderizador de la línea
    LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
    plot.setRenderer(1, lineRenderer);

    // Establecer el orden de renderización de los datasets
    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

    // Mostrar el gráfico en el panel
    ChartPanel barChartPanel = new ChartPanel(chart);
    HistogramaInventario.removeAll();
    HistogramaInventario.add(barChartPanel, BorderLayout.CENTER);
    HistogramaInventario.validate();
}


    private int obtenerPrecioProductos(Productos producto) {
        int precioTotal = 0;
        try {
            precioTotal += producto.getPrecio();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Error al obtener el precio de los productos");
        }
        return precioTotal;
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
        HistogramaEmpleados = new javax.swing.JPanel();
        HistogramaInventario = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setMaximumSize(new java.awt.Dimension(678, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(678, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(678, 800));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Este es el panel graficos");

        HistogramaEmpleados.setMaximumSize(new java.awt.Dimension(100, 100));
        HistogramaEmpleados.setMinimumSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout HistogramaEmpleadosLayout = new javax.swing.GroupLayout(HistogramaEmpleados);
        HistogramaEmpleados.setLayout(HistogramaEmpleadosLayout);
        HistogramaEmpleadosLayout.setHorizontalGroup(
            HistogramaEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1188, Short.MAX_VALUE)
        );
        HistogramaEmpleadosLayout.setVerticalGroup(
            HistogramaEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );

        HistogramaInventario.setMaximumSize(new java.awt.Dimension(100, 100));
        HistogramaInventario.setMinimumSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout HistogramaInventarioLayout = new javax.swing.GroupLayout(HistogramaInventario);
        HistogramaInventario.setLayout(HistogramaInventarioLayout);
        HistogramaInventarioLayout.setHorizontalGroup(
            HistogramaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        HistogramaInventarioLayout.setVerticalGroup(
            HistogramaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 363, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(297, 297, 297)
                .addComponent(jLabel1)
                .addContainerGap(601, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HistogramaEmpleados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HistogramaInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HistogramaEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(HistogramaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HistogramaEmpleados;
    private javax.swing.JPanel HistogramaInventario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
