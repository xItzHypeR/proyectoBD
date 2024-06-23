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
@Table(name = "tiemposmin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiemposmin.findAll", query = "SELECT t FROM Tiemposmin t")
    , @NamedQuery(name = "Tiemposmin.findByIdTiempoMin", query = "SELECT t FROM Tiemposmin t WHERE t.idTiempoMin = :idTiempoMin")
    , @NamedQuery(name = "Tiemposmin.findByTiempoMin", query = "SELECT t FROM Tiemposmin t WHERE t.tiempoMin = :tiempoMin")})
public class Tiemposmin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTiempoMin")
    private Integer idTiempoMin;
    @Column(name = "tiempoMin")
    @Temporal(TemporalType.TIME)
    private Date tiempoMin;
    @OneToMany(mappedBy = "idTiempoMin")
    private List<Tiempos> tiemposList;

    public Tiemposmin() {
    }

    public Tiemposmin(Integer idTiempoMin) {
        this.idTiempoMin = idTiempoMin;
    }

    public Integer getIdTiempoMin() {
        return idTiempoMin;
    }

    public void setIdTiempoMin(Integer idTiempoMin) {
        this.idTiempoMin = idTiempoMin;
    }

    public Date getTiempoMin() {
        return tiempoMin;
    }

    public void setTiempoMin(Date tiempoMin) {
        this.tiempoMin = tiempoMin;
    }

    @XmlTransient
    public List<Tiempos> getTiemposList() {
        return tiemposList;
    }

    public void setTiemposList(List<Tiempos> tiemposList) {
        this.tiemposList = tiemposList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiempoMin != null ? idTiempoMin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiemposmin)) {
            return false;
        }
        Tiemposmin other = (Tiemposmin) object;
        if ((this.idTiempoMin == null && other.idTiempoMin != null) || (this.idTiempoMin != null && !this.idTiempoMin.equals(other.idTiempoMin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tiemposmin[ idTiempoMin=" + idTiempoMin + " ]";
    }
    
}
