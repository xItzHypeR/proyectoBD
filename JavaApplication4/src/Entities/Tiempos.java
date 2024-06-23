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
@Table(name = "tiempos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiempos.findAll", query = "SELECT t FROM Tiempos t")
    , @NamedQuery(name = "Tiempos.findByIdTiempos", query = "SELECT t FROM Tiempos t WHERE t.idTiempos = :idTiempos")
    , @NamedQuery(name = "Tiempos.findByTiempoCronometrado", query = "SELECT t FROM Tiempos t WHERE t.tiempoCronometrado = :tiempoCronometrado")})
public class Tiempos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTiempos")
    private Integer idTiempos;
    @Column(name = "tiempoCronometrado")
    @Temporal(TemporalType.TIME)
    private Date tiempoCronometrado;
    @OneToMany(mappedBy = "idTiempos")
    private List<Tiempoactividades> tiempoactividadesList;
    @JoinColumn(name = "idTiempoMax", referencedColumnName = "idTiempoMax")
    @ManyToOne
    private Tiemposmax idTiempoMax;
    @JoinColumn(name = "idTiempoMin", referencedColumnName = "idTiempoMin")
    @ManyToOne
    private Tiemposmin idTiempoMin;

    public Tiempos() {
    }

    public Tiempos(Integer idTiempos) {
        this.idTiempos = idTiempos;
    }

    public Integer getIdTiempos() {
        return idTiempos;
    }

    public void setIdTiempos(Integer idTiempos) {
        this.idTiempos = idTiempos;
    }

    public Date getTiempoCronometrado() {
        return tiempoCronometrado;
    }

    public void setTiempoCronometrado(Date tiempoCronometrado) {
        this.tiempoCronometrado = tiempoCronometrado;
    }

    @XmlTransient
    public List<Tiempoactividades> getTiempoactividadesList() {
        return tiempoactividadesList;
    }

    public void setTiempoactividadesList(List<Tiempoactividades> tiempoactividadesList) {
        this.tiempoactividadesList = tiempoactividadesList;
    }

    public Tiemposmax getIdTiempoMax() {
        return idTiempoMax;
    }

    public void setIdTiempoMax(Tiemposmax idTiempoMax) {
        this.idTiempoMax = idTiempoMax;
    }

    public Tiemposmin getIdTiempoMin() {
        return idTiempoMin;
    }

    public void setIdTiempoMin(Tiemposmin idTiempoMin) {
        this.idTiempoMin = idTiempoMin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiempos != null ? idTiempos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiempos)) {
            return false;
        }
        Tiempos other = (Tiempos) object;
        if ((this.idTiempos == null && other.idTiempos != null) || (this.idTiempos != null && !this.idTiempos.equals(other.idTiempos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tiempos[ idTiempos=" + idTiempos + " ]";
    }
    
}
