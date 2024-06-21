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
@Table(name = "tiempoactividades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiempoactividades.findAll", query = "SELECT t FROM Tiempoactividades t")
    , @NamedQuery(name = "Tiempoactividades.findByIdTiempoActividad", query = "SELECT t FROM Tiempoactividades t WHERE t.idTiempoActividad = :idTiempoActividad")})
public class Tiempoactividades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTiempoActividad")
    private Integer idTiempoActividad;
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    @ManyToOne
    private Empleados idEmpleado;
    @JoinColumn(name = "idObservador", referencedColumnName = "idObservador")
    @ManyToOne
    private Observadores idObservador;
    @JoinColumn(name = "idTiempos", referencedColumnName = "idTiempos")
    @ManyToOne
    private Tiempos idTiempos;

    public Tiempoactividades() {
    }

    public Tiempoactividades(Integer idTiempoActividad) {
        this.idTiempoActividad = idTiempoActividad;
    }

    public Integer getIdTiempoActividad() {
        return idTiempoActividad;
    }

    public void setIdTiempoActividad(Integer idTiempoActividad) {
        this.idTiempoActividad = idTiempoActividad;
    }

    public Empleados getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleados idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Observadores getIdObservador() {
        return idObservador;
    }

    public void setIdObservador(Observadores idObservador) {
        this.idObservador = idObservador;
    }

    public Tiempos getIdTiempos() {
        return idTiempos;
    }

    public void setIdTiempos(Tiempos idTiempos) {
        this.idTiempos = idTiempos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiempoActividad != null ? idTiempoActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiempoactividades)) {
            return false;
        }
        Tiempoactividades other = (Tiempoactividades) object;
        if ((this.idTiempoActividad == null && other.idTiempoActividad != null) || (this.idTiempoActividad != null && !this.idTiempoActividad.equals(other.idTiempoActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tiempoactividades[ idTiempoActividad=" + idTiempoActividad + " ]";
    }
    
}
