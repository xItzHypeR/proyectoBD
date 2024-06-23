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
@Table(name = "observadores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observadores.findAll", query = "SELECT o FROM Observadores o")
    , @NamedQuery(name = "Observadores.findByIdObservador", query = "SELECT o FROM Observadores o WHERE o.idObservador = :idObservador")
    , @NamedQuery(name = "Observadores.findByNombreObservador", query = "SELECT o FROM Observadores o WHERE o.nombreObservador = :nombreObservador")})
public class Observadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idObservador")
    private Integer idObservador;
    @Column(name = "nombreObservador")
    private String nombreObservador;
    @OneToMany(mappedBy = "idObservador")
    private List<Tiempoactividades> tiempoactividadesList;

    public Observadores() {
    }

    public Observadores(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public Integer getIdObservador() {
        return idObservador;
    }

    public void setIdObservador(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public String getNombreObservador() {
        return nombreObservador;
    }

    public void setNombreObservador(String nombreObservador) {
        this.nombreObservador = nombreObservador;
    }

    @XmlTransient
    public List<Tiempoactividades> getTiempoactividadesList() {
        return tiempoactividadesList;
    }

    public void setTiempoactividadesList(List<Tiempoactividades> tiempoactividadesList) {
        this.tiempoactividadesList = tiempoactividadesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObservador != null ? idObservador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observadores)) {
            return false;
        }
        Observadores other = (Observadores) object;
        if ((this.idObservador == null && other.idObservador != null) || (this.idObservador != null && !this.idObservador.equals(other.idObservador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Observadores[ idObservador=" + idObservador + " ]";
    }
    
}
