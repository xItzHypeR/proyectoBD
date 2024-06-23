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
@Table(name = "ingredientesprincipales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingredientesprincipales.findAll", query = "SELECT i FROM Ingredientesprincipales i")
    , @NamedQuery(name = "Ingredientesprincipales.findByIdIngredientePrincipal", query = "SELECT i FROM Ingredientesprincipales i WHERE i.idIngredientePrincipal = :idIngredientePrincipal")
    , @NamedQuery(name = "Ingredientesprincipales.findByIngredientePrincipal", query = "SELECT i FROM Ingredientesprincipales i WHERE i.ingredientePrincipal = :ingredientePrincipal")})
public class Ingredientesprincipales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idIngredientePrincipal")
    private Integer idIngredientePrincipal;
    @Column(name = "ingredientePrincipal")
    private String ingredientePrincipal;
    @OneToMany(mappedBy = "idIngredientePrincipal")
    private List<Productos> productosList;

    public Ingredientesprincipales() {
    }

    public Ingredientesprincipales(Integer idIngredientePrincipal) {
        this.idIngredientePrincipal = idIngredientePrincipal;
    }

    public Integer getIdIngredientePrincipal() {
        return idIngredientePrincipal;
    }

    public void setIdIngredientePrincipal(Integer idIngredientePrincipal) {
        this.idIngredientePrincipal = idIngredientePrincipal;
    }

    public String getIngredientePrincipal() {
        return ingredientePrincipal;
    }

    public void setIngredientePrincipal(String ingredientePrincipal) {
        this.ingredientePrincipal = ingredientePrincipal;
    }

    @XmlTransient
    public List<Productos> getProductosList() {
        return productosList;
    }

    public void setProductosList(List<Productos> productosList) {
        this.productosList = productosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIngredientePrincipal != null ? idIngredientePrincipal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingredientesprincipales)) {
            return false;
        }
        Ingredientesprincipales other = (Ingredientesprincipales) object;
        if ((this.idIngredientePrincipal == null && other.idIngredientePrincipal != null) || (this.idIngredientePrincipal != null && !this.idIngredientePrincipal.equals(other.idIngredientePrincipal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Ingredientesprincipales[ idIngredientePrincipal=" + idIngredientePrincipal + " ]";
    }
    
}
