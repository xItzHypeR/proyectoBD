/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
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
@Table(name = "productoingredientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productoingredientes.findAll", query = "SELECT p FROM Productoingredientes p")
    , @NamedQuery(name = "Productoingredientes.findByIdProductoIngrediente", query = "SELECT p FROM Productoingredientes p WHERE p.idProductoIngrediente = :idProductoIngrediente")
    , @NamedQuery(name = "Productoingredientes.findByCantidad", query = "SELECT p FROM Productoingredientes p WHERE p.cantidad = :cantidad")})
public class Productoingredientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProductoIngrediente")
    private Integer idProductoIngrediente;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "idIngrediente", referencedColumnName = "idIngrediente")
    @ManyToOne
    private Ingredientes idIngrediente;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne
    private Productos idProducto;

    public Productoingredientes() {
    }

    public Productoingredientes(Integer idProductoIngrediente) {
        this.idProductoIngrediente = idProductoIngrediente;
    }

    public Integer getIdProductoIngrediente() {
        return idProductoIngrediente;
    }

    public void setIdProductoIngrediente(Integer idProductoIngrediente) {
        this.idProductoIngrediente = idProductoIngrediente;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Ingredientes getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Ingredientes idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductoIngrediente != null ? idProductoIngrediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productoingredientes)) {
            return false;
        }
        Productoingredientes other = (Productoingredientes) object;
        if ((this.idProductoIngrediente == null && other.idProductoIngrediente != null) || (this.idProductoIngrediente != null && !this.idProductoIngrediente.equals(other.idProductoIngrediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Productoingredientes[ idProductoIngrediente=" + idProductoIngrediente + " ]";
    }
    
}
