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
@Table(name = "pedidos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedidos.findAll", query = "SELECT p FROM Pedidos p")
    , @NamedQuery(name = "Pedidos.findByIdPedidos", query = "SELECT p FROM Pedidos p WHERE p.idPedidos = :idPedidos")
    , @NamedQuery(name = "Pedidos.findByDireccion", query = "SELECT p FROM Pedidos p WHERE p.direccion = :direccion")
    , @NamedQuery(name = "Pedidos.findByNombreCliente", query = "SELECT p FROM Pedidos p WHERE p.nombreCliente = :nombreCliente")
    , @NamedQuery(name = "Pedidos.findByApellidoCliente", query = "SELECT p FROM Pedidos p WHERE p.apellidoCliente = :apellidoCliente")
    , @NamedQuery(name = "Pedidos.findByFechaDeEntrega", query = "SELECT p FROM Pedidos p WHERE p.fechaDeEntrega = :fechaDeEntrega")
    , @NamedQuery(name = "Pedidos.findByFechaPedido", query = "SELECT p FROM Pedidos p WHERE p.fechaPedido = :fechaPedido")
    , @NamedQuery(name = "Pedidos.findByIdDetallePedidos", query = "SELECT p FROM Pedidos p WHERE p.idDetallePedidos = :idDetallePedidos")})
public class Pedidos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPedidos")
    private Integer idPedidos;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "nombreCliente")
    private String nombreCliente;
    @Column(name = "apellidoCliente")
    private String apellidoCliente;
    @Column(name = "fechaDeEntrega")
    @Temporal(TemporalType.DATE)
    private Date fechaDeEntrega;
    @Column(name = "fechaPedido")
    @Temporal(TemporalType.DATE)
    private Date fechaPedido;
    @Column(name = "idDetallePedidos")
    private Integer idDetallePedidos;
    @OneToMany(mappedBy = "idPedido")
    private List<Detallepedidos> detallepedidosList;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne
    private Cliente idCliente;

    public Pedidos() {
    }

    public Pedidos(Integer idPedidos) {
        this.idPedidos = idPedidos;
    }

    public Integer getIdPedidos() {
        return idPedidos;
    }

    public void setIdPedidos(Integer idPedidos) {
        this.idPedidos = idPedidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public Date getFechaDeEntrega() {
        return fechaDeEntrega;
    }

    public void setFechaDeEntrega(Date fechaDeEntrega) {
        this.fechaDeEntrega = fechaDeEntrega;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Integer getIdDetallePedidos() {
        return idDetallePedidos;
    }

    public void setIdDetallePedidos(Integer idDetallePedidos) {
        this.idDetallePedidos = idDetallePedidos;
    }

    @XmlTransient
    public List<Detallepedidos> getDetallepedidosList() {
        return detallepedidosList;
    }

    public void setDetallepedidosList(List<Detallepedidos> detallepedidosList) {
        this.detallepedidosList = detallepedidosList;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedidos != null ? idPedidos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidos)) {
            return false;
        }
        Pedidos other = (Pedidos) object;
        if ((this.idPedidos == null && other.idPedidos != null) || (this.idPedidos != null && !this.idPedidos.equals(other.idPedidos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Pedidos[ idPedidos=" + idPedidos + " ]";
    }
    
}
