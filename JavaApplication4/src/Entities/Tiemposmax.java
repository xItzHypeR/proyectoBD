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
@Table(name = "tiemposmax")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiemposmax.findAll", query = "SELECT t FROM Tiemposmax t")
    , @NamedQuery(name = "Tiemposmax.findByIdTiempoMax", query = "SELECT t FROM Tiemposmax t WHERE t.idTiempoMax = :idTiempoMax")
    , @NamedQuery(name = "Tiemposmax.findByTiempoMax", query = "SELECT t FROM Tiemposmax t WHERE t.tiempoMax = :tiempoMax")})
public class Tiemposmax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTiempoMax")
    private Integer idTiempoMax;
    @Column(name = "tiempoMax")
    @Temporal(TemporalType.TIME)
    private Date tiempoMax;
    @OneToMany(mappedBy = "idTiempoMax")
    private List<Tiempos> tiemposList;

    public Tiemposmax() {
    }

    public Tiemposmax(Integer idTiempoMax) {
        this.idTiempoMax = idTiempoMax;
    }

    public Integer getIdTiempoMax() {
        return idTiempoMax;
    }

    public void setIdTiempoMax(Integer idTiempoMax) {
        this.idTiempoMax = idTiempoMax;
    }

    public Date getTiempoMax() {
        return tiempoMax;
    }

    public void setTiempoMax(Date tiempoMax) {
        this.tiempoMax = tiempoMax;
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
        hash += (idTiempoMax != null ? idTiempoMax.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiemposmax)) {
            return false;
        }
        Tiemposmax other = (Tiemposmax) object;
        if ((this.idTiempoMax == null && other.idTiempoMax != null) || (this.idTiempoMax != null && !this.idTiempoMax.equals(other.idTiempoMax))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tiemposmax[ idTiempoMax=" + idTiempoMax + " ]";
    }
    
}
