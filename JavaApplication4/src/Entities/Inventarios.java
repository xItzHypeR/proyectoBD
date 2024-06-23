/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gpera
 */
@Entity
@Table(name = "inventarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventarios.findAll", query = "SELECT i FROM Inventarios i")
    , @NamedQuery(name = "Inventarios.findByIdInventario", query = "SELECT i FROM Inventarios i WHERE i.idInventario = :idInventario")
    , @NamedQuery(name = "Inventarios.findByCantidadDisponible", query = "SELECT i FROM Inventarios i WHERE i.cantidadDisponible = :cantidadDisponible")
    , @NamedQuery(name = "Inventarios.findByFechaActualizacion", query = "SELECT i FROM Inventarios i WHERE i.fechaActualizacion = :fechaActualizacion")})
public class Inventarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idInventario")
    private Integer idInventario;
    @Column(name = "cantidadDisponible")
    private Integer cantidadDisponible;
    @Column(name = "fechaActualizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne
    private Productos idProducto;

    public Inventarios() {
    }

    public Inventarios(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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
        hash += (idInventario != null ? idInventario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventarios)) {
            return false;
        }
        Inventarios other = (Inventarios) object;
        if ((this.idInventario == null && other.idInventario != null) || (this.idInventario != null && !this.idInventario.equals(other.idInventario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Inventarios[ idInventario=" + idInventario + " ]";
    }
    
}
