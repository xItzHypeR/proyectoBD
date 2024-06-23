/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Pedidos;
import java.util.ArrayList;
import java.util.List;
import Entities.Ventas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getPedidosList() == null) {
            cliente.setPedidosList(new ArrayList<Pedidos>());
        }
        if (cliente.getVentasList() == null) {
            cliente.setVentasList(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pedidos> attachedPedidosList = new ArrayList<Pedidos>();
            for (Pedidos pedidosListPedidosToAttach : cliente.getPedidosList()) {
                pedidosListPedidosToAttach = em.getReference(pedidosListPedidosToAttach.getClass(), pedidosListPedidosToAttach.getIdPedidos());
                attachedPedidosList.add(pedidosListPedidosToAttach);
            }
            cliente.setPedidosList(attachedPedidosList);
            List<Ventas> attachedVentasList = new ArrayList<Ventas>();
            for (Ventas ventasListVentasToAttach : cliente.getVentasList()) {
                ventasListVentasToAttach = em.getReference(ventasListVentasToAttach.getClass(), ventasListVentasToAttach.getIdVenta());
                attachedVentasList.add(ventasListVentasToAttach);
            }
            cliente.setVentasList(attachedVentasList);
            em.persist(cliente);
            for (Pedidos pedidosListPedidos : cliente.getPedidosList()) {
                Cliente oldIdClienteOfPedidosListPedidos = pedidosListPedidos.getIdCliente();
                pedidosListPedidos.setIdCliente(cliente);
                pedidosListPedidos = em.merge(pedidosListPedidos);
                if (oldIdClienteOfPedidosListPedidos != null) {
                    oldIdClienteOfPedidosListPedidos.getPedidosList().remove(pedidosListPedidos);
                    oldIdClienteOfPedidosListPedidos = em.merge(oldIdClienteOfPedidosListPedidos);
                }
            }
            for (Ventas ventasListVentas : cliente.getVentasList()) {
                Cliente oldIdClienteOfVentasListVentas = ventasListVentas.getIdCliente();
                ventasListVentas.setIdCliente(cliente);
                ventasListVentas = em.merge(ventasListVentas);
                if (oldIdClienteOfVentasListVentas != null) {
                    oldIdClienteOfVentasListVentas.getVentasList().remove(ventasListVentas);
                    oldIdClienteOfVentasListVentas = em.merge(oldIdClienteOfVentasListVentas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getIdCliente()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Pedidos> pedidosListOld = persistentCliente.getPedidosList();
            List<Pedidos> pedidosListNew = cliente.getPedidosList();
            List<Ventas> ventasListOld = persistentCliente.getVentasList();
            List<Ventas> ventasListNew = cliente.getVentasList();
            List<Pedidos> attachedPedidosListNew = new ArrayList<Pedidos>();
            for (Pedidos pedidosListNewPedidosToAttach : pedidosListNew) {
                pedidosListNewPedidosToAttach = em.getReference(pedidosListNewPedidosToAttach.getClass(), pedidosListNewPedidosToAttach.getIdPedidos());
                attachedPedidosListNew.add(pedidosListNewPedidosToAttach);
            }
            pedidosListNew = attachedPedidosListNew;
            cliente.setPedidosList(pedidosListNew);
            List<Ventas> attachedVentasListNew = new ArrayList<Ventas>();
            for (Ventas ventasListNewVentasToAttach : ventasListNew) {
                ventasListNewVentasToAttach = em.getReference(ventasListNewVentasToAttach.getClass(), ventasListNewVentasToAttach.getIdVenta());
                attachedVentasListNew.add(ventasListNewVentasToAttach);
            }
            ventasListNew = attachedVentasListNew;
            cliente.setVentasList(ventasListNew);
            cliente = em.merge(cliente);
            for (Pedidos pedidosListOldPedidos : pedidosListOld) {
                if (!pedidosListNew.contains(pedidosListOldPedidos)) {
                    pedidosListOldPedidos.setIdCliente(null);
                    pedidosListOldPedidos = em.merge(pedidosListOldPedidos);
                }
            }
            for (Pedidos pedidosListNewPedidos : pedidosListNew) {
                if (!pedidosListOld.contains(pedidosListNewPedidos)) {
                    Cliente oldIdClienteOfPedidosListNewPedidos = pedidosListNewPedidos.getIdCliente();
                    pedidosListNewPedidos.setIdCliente(cliente);
                    pedidosListNewPedidos = em.merge(pedidosListNewPedidos);
                    if (oldIdClienteOfPedidosListNewPedidos != null && !oldIdClienteOfPedidosListNewPedidos.equals(cliente)) {
                        oldIdClienteOfPedidosListNewPedidos.getPedidosList().remove(pedidosListNewPedidos);
                        oldIdClienteOfPedidosListNewPedidos = em.merge(oldIdClienteOfPedidosListNewPedidos);
                    }
                }
            }
            for (Ventas ventasListOldVentas : ventasListOld) {
                if (!ventasListNew.contains(ventasListOldVentas)) {
                    ventasListOldVentas.setIdCliente(null);
                    ventasListOldVentas = em.merge(ventasListOldVentas);
                }
            }
            for (Ventas ventasListNewVentas : ventasListNew) {
                if (!ventasListOld.contains(ventasListNewVentas)) {
                    Cliente oldIdClienteOfVentasListNewVentas = ventasListNewVentas.getIdCliente();
                    ventasListNewVentas.setIdCliente(cliente);
                    ventasListNewVentas = em.merge(ventasListNewVentas);
                    if (oldIdClienteOfVentasListNewVentas != null && !oldIdClienteOfVentasListNewVentas.equals(cliente)) {
                        oldIdClienteOfVentasListNewVentas.getVentasList().remove(ventasListNewVentas);
                        oldIdClienteOfVentasListNewVentas = em.merge(oldIdClienteOfVentasListNewVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<Pedidos> pedidosList = cliente.getPedidosList();
            for (Pedidos pedidosListPedidos : pedidosList) {
                pedidosListPedidos.setIdCliente(null);
                pedidosListPedidos = em.merge(pedidosListPedidos);
            }
            List<Ventas> ventasList = cliente.getVentasList();
            for (Ventas ventasListVentas : ventasList) {
                ventasListVentas.setIdCliente(null);
                ventasListVentas = em.merge(ventasListVentas);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
