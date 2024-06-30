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
@Table(name = "produccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produccion.findAll", query = "SELECT p FROM Produccion p")
    , @NamedQuery(name = "Produccion.findByIdProduccion", query = "SELECT p FROM Produccion p WHERE p.idProduccion = :idProduccion")
    , @NamedQuery(name = "Produccion.findByIdProducto", query = "SELECT p FROM Produccion p WHERE p.idProducto = :idProducto")
    , @NamedQuery(name = "Produccion.findByFechaProduccion", query = "SELECT p FROM Produccion p WHERE p.fechaProduccion = :fechaProduccion")
    , @NamedQuery(name = "Produccion.findByCantidadProducida", query = "SELECT p FROM Produccion p WHERE p.cantidadProducida = :cantidadProducida")})
public class Produccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProduccion")
    private Integer idProduccion;
    @Column(name = "idProducto")
    private Integer idProducto;
    @Column(name = "fechaProduccion")
    @Temporal(TemporalType.DATE)
    private Date fechaProduccion;
    @Column(name = "cantidadProducida")
    private Integer cantidadProducida;
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    @ManyToOne
    private Empleados idEmpleado;

    public Produccion() {
    }

    public Produccion(Integer idProduccion) {
        this.idProduccion = idProduccion;
    }

    public Integer getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Integer idProduccion) {
        this.idProduccion = idProduccion;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(Date string) {
        this.fechaProduccion = string;
    }

    public Integer getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(Integer cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    public Empleados getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleados idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduccion != null ? idProduccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produccion)) {
            return false;
        }
        Produccion other = (Produccion) object;
        if ((this.idProduccion == null && other.idProduccion != null) || (this.idProduccion != null && !this.idProduccion.equals(other.idProduccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Produccion[ idProduccion=" + idProduccion + " ]";
    }
    
}
