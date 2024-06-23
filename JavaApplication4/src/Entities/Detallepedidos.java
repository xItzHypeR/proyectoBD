/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gpera
 */
@Entity
@Table(name = "detallepedidos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallepedidos.findAll", query = "SELECT d FROM Detallepedidos d")
    , @NamedQuery(name = "Detallepedidos.findByIdDetallePedidos", query = "SELECT d FROM Detallepedidos d WHERE d.idDetallePedidos = :idDetallePedidos")
    , @NamedQuery(name = "Detallepedidos.findByCantidad", query = "SELECT d FROM Detallepedidos d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "Detallepedidos.findByPrecioUnitario", query = "SELECT d FROM Detallepedidos d WHERE d.precioUnitario = :precioUnitario")
    , @NamedQuery(name = "Detallepedidos.findBySubtotal", query = "SELECT d FROM Detallepedidos d WHERE d.subtotal = :subtotal")})
public class Detallepedidos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDetallePedidos")
    private Integer idDetallePedidos;
    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precioUnitario")
    private Float precioUnitario;
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @JoinColumn(name = "idPedido", referencedColumnName = "idPedidos")
    @ManyToOne
    private Pedidos idPedido;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne
    private Productos idProducto;
    @OneToMany(mappedBy = "idDetallePedidos")
    private List<Facturas> facturasList;

    public Detallepedidos() {
    }

    public Detallepedidos(Integer idDetallePedidos) {
        this.idDetallePedidos = idDetallePedidos;
    }

    public Integer getIdDetallePedidos() {
        return idDetallePedidos;
    }

    public void setIdDetallePedidos(Integer idDetallePedidos) {
        this.idDetallePedidos = idDetallePedidos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Pedidos getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedidos idPedido) {
        this.idPedido = idPedido;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetallePedidos != null ? idDetallePedidos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallepedidos)) {
            return false;
        }
        Detallepedidos other = (Detallepedidos) object;
        if ((this.idDetallePedidos == null && other.idDetallePedidos != null) || (this.idDetallePedidos != null && !this.idDetallePedidos.equals(other.idDetallePedidos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detallepedidos[ idDetallePedidos=" + idDetallePedidos + " ]";
    }
    
}
