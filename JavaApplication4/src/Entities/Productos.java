/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gpera
 */
@Entity
@Table(name = "productos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p")
    , @NamedQuery(name = "Productos.findByIdProducto", query = "SELECT p FROM Productos p WHERE p.idProducto = :idProducto")
    , @NamedQuery(name = "Productos.findByNombreProducto", query = "SELECT p FROM Productos p WHERE p.nombreProducto = :nombreProducto")
    , @NamedQuery(name = "Productos.findByTipoProducto", query = "SELECT p FROM Productos p WHERE p.tipoProducto = :tipoProducto")
    , @NamedQuery(name = "Productos.findByFechaProduccion", query = "SELECT p FROM Productos p WHERE p.fechaProduccion = :fechaProduccion")
    , @NamedQuery(name = "Productos.findByFechaExpiracion", query = "SELECT p FROM Productos p WHERE p.fechaExpiracion = :fechaExpiracion")
    , @NamedQuery(name = "Productos.findByPrecio", query = "SELECT p FROM Productos p WHERE p.precio = :precio")})
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProducto")
    private Integer idProducto;
    @Column(name = "nombreProducto")
    private String nombreProducto;
    @Column(name = "tipoProducto")
    private String tipoProducto;
    @Column(name = "fechaProduccion")
    @Temporal(TemporalType.DATE)
    private Date fechaProduccion;
    @Column(name = "fechaExpiracion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpiracion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio")
    private Float precio;
    @OneToMany(mappedBy = "idProducto")
    private List<Detallepedidos> detallepedidosList;
    @OneToMany(mappedBy = "idProducto")
    private List<Productoingredientes> productoingredientesList;
    @OneToMany(mappedBy = "idProducto")
    private List<Detalleventa> detalleventaList;
    @OneToMany(mappedBy = "idProducto")
    private List<Inventarios> inventariosList;
    @JoinColumn(name = "idIngredientePrincipal", referencedColumnName = "idIngredientePrincipal")
    @ManyToOne
    private Ingredientesprincipales idIngredientePrincipal;

    public Productos() {
    }

    public Productos(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    @XmlTransient
    public List<Detallepedidos> getDetallepedidosList() {
        return detallepedidosList;
    }

    public void setDetallepedidosList(List<Detallepedidos> detallepedidosList) {
        this.detallepedidosList = detallepedidosList;
    }

    @XmlTransient
    public List<Productoingredientes> getProductoingredientesList() {
        return productoingredientesList;
    }

    public void setProductoingredientesList(List<Productoingredientes> productoingredientesList) {
        this.productoingredientesList = productoingredientesList;
    }

    @XmlTransient
    public List<Detalleventa> getDetalleventaList() {
        return detalleventaList;
    }

    public void setDetalleventaList(List<Detalleventa> detalleventaList) {
        this.detalleventaList = detalleventaList;
    }

    @XmlTransient
    public List<Inventarios> getInventariosList() {
        return inventariosList;
    }

    public void setInventariosList(List<Inventarios> inventariosList) {
        this.inventariosList = inventariosList;
    }

    public Ingredientesprincipales getIdIngredientePrincipal() {
        return idIngredientePrincipal;
    }

    public void setIdIngredientePrincipal(Ingredientesprincipales idIngredientePrincipal) {
        this.idIngredientePrincipal = idIngredientePrincipal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Productos[ idProducto=" + idProducto + " ]";
    }
    
}
