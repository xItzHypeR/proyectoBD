/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
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
@Table(name = "ingredientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingredientes.findAll", query = "SELECT i FROM Ingredientes i")
    , @NamedQuery(name = "Ingredientes.findByIdIngrediente", query = "SELECT i FROM Ingredientes i WHERE i.idIngrediente = :idIngrediente")
    , @NamedQuery(name = "Ingredientes.findByIngrediente", query = "SELECT i FROM Ingredientes i WHERE i.ingrediente = :ingrediente")
    , @NamedQuery(name = "Ingredientes.findByCantidadDisponible", query = "SELECT i FROM Ingredientes i WHERE i.cantidadDisponible = :cantidadDisponible")
    , @NamedQuery(name = "Ingredientes.findByCosto", query = "SELECT i FROM Ingredientes i WHERE i.costo = :costo")})
public class Ingredientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idIngrediente")
    private Integer idIngrediente;
    @Column(name = "ingrediente")
    private String ingrediente;
    @Column(name = "cantidadDisponible")
    private Integer cantidadDisponible;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo")
    private Float costo;
    @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor")
    @ManyToOne
    private Proveedores idProveedor;
    @OneToMany(mappedBy = "idIngrediente")
    private List<Productoingredientes> productoingredientesList;
    @OneToMany(mappedBy = "idIngrediente")
    private List<Detallecompra> detallecompraList;

    public Ingredientes() {
    }

    public Ingredientes(Integer idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public Integer getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Integer idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Proveedores getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedores idProveedor) {
        this.idProveedor = idProveedor;
    }

    @XmlTransient
    public List<Productoingredientes> getProductoingredientesList() {
        return productoingredientesList;
    }

    public void setProductoingredientesList(List<Productoingredientes> productoingredientesList) {
        this.productoingredientesList = productoingredientesList;
    }

    @XmlTransient
    public List<Detallecompra> getDetallecompraList() {
        return detallecompraList;
    }

    public void setDetallecompraList(List<Detallecompra> detallecompraList) {
        this.detallecompraList = detallecompraList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIngrediente != null ? idIngrediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingredientes)) {
            return false;
        }
        Ingredientes other = (Ingredientes) object;
        if ((this.idIngrediente == null && other.idIngrediente != null) || (this.idIngrediente != null && !this.idIngrediente.equals(other.idIngrediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Ingredientes[ idIngrediente=" + idIngrediente + " ]";
    }
    
}
