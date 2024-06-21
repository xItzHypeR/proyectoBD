/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gpera
 */
@Entity
@Table(name = "detallecompra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d")
    , @NamedQuery(name = "Detallecompra.findByIdDetalleCompra", query = "SELECT d FROM Detallecompra d WHERE d.idDetalleCompra = :idDetalleCompra")
    , @NamedQuery(name = "Detallecompra.findBySubtotal", query = "SELECT d FROM Detallecompra d WHERE d.subtotal = :subtotal")
    , @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "Detallecompra.findByPrecioUnitario", query = "SELECT d FROM Detallecompra d WHERE d.precioUnitario = :precioUnitario")})
public class Detallecompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDetalleCompra")
    private Integer idDetalleCompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "precioUnitario")
    private BigDecimal precioUnitario;
    @JoinColumn(name = "idCompra", referencedColumnName = "idCompra")
    @ManyToOne
    private Compras idCompra;
    @JoinColumn(name = "idIngrediente", referencedColumnName = "idIngrediente")
    @ManyToOne
    private Ingredientes idIngrediente;

    public Detallecompra() {
    }

    public Detallecompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Compras getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Compras idCompra) {
        this.idCompra = idCompra;
    }

    public Ingredientes getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Ingredientes idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleCompra != null ? idDetalleCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallecompra)) {
            return false;
        }
        Detallecompra other = (Detallecompra) object;
        if ((this.idDetalleCompra == null && other.idDetalleCompra != null) || (this.idDetalleCompra != null && !this.idDetalleCompra.equals(other.idDetalleCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detallecompra[ idDetalleCompra=" + idDetalleCompra + " ]";
    }
    
}
