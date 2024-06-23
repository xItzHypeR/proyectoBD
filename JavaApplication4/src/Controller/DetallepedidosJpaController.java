/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Detallepedidos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Pedidos;
import Entities.Productos;
import Entities.Facturas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class DetallepedidosJpaController implements Serializable {

    public DetallepedidosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallepedidos detallepedidos) throws PreexistingEntityException, Exception {
        if (detallepedidos.getFacturasList() == null) {
            detallepedidos.setFacturasList(new ArrayList<Facturas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidos idPedido = detallepedidos.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedidos());
                detallepedidos.setIdPedido(idPedido);
            }
            Productos idProducto = detallepedidos.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                detallepedidos.setIdProducto(idProducto);
            }
            List<Facturas> attachedFacturasList = new ArrayList<Facturas>();
            for (Facturas facturasListFacturasToAttach : detallepedidos.getFacturasList()) {
                facturasListFacturasToAttach = em.getReference(facturasListFacturasToAttach.getClass(), facturasListFacturasToAttach.getIdFactura());
                attachedFacturasList.add(facturasListFacturasToAttach);
            }
            detallepedidos.setFacturasList(attachedFacturasList);
            em.persist(detallepedidos);
            if (idPedido != null) {
                idPedido.getDetallepedidosList().add(detallepedidos);
                idPedido = em.merge(idPedido);
            }
            if (idProducto != null) {
                idProducto.getDetallepedidosList().add(detallepedidos);
                idProducto = em.merge(idProducto);
            }
            for (Facturas facturasListFacturas : detallepedidos.getFacturasList()) {
                Detallepedidos oldIdDetallePedidosOfFacturasListFacturas = facturasListFacturas.getIdDetallePedidos();
                facturasListFacturas.setIdDetallePedidos(detallepedidos);
                facturasListFacturas = em.merge(facturasListFacturas);
                if (oldIdDetallePedidosOfFacturasListFacturas != null) {
                    oldIdDetallePedidosOfFacturasListFacturas.getFacturasList().remove(facturasListFacturas);
                    oldIdDetallePedidosOfFacturasListFacturas = em.merge(oldIdDetallePedidosOfFacturasListFacturas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetallepedidos(detallepedidos.getIdDetallePedidos()) != null) {
                throw new PreexistingEntityException("Detallepedidos " + detallepedidos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallepedidos detallepedidos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallepedidos persistentDetallepedidos = em.find(Detallepedidos.class, detallepedidos.getIdDetallePedidos());
            Pedidos idPedidoOld = persistentDetallepedidos.getIdPedido();
            Pedidos idPedidoNew = detallepedidos.getIdPedido();
            Productos idProductoOld = persistentDetallepedidos.getIdProducto();
            Productos idProductoNew = detallepedidos.getIdProducto();
            List<Facturas> facturasListOld = persistentDetallepedidos.getFacturasList();
            List<Facturas> facturasListNew = detallepedidos.getFacturasList();
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedidos());
                detallepedidos.setIdPedido(idPedidoNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                detallepedidos.setIdProducto(idProductoNew);
            }
            List<Facturas> attachedFacturasListNew = new ArrayList<Facturas>();
            for (Facturas facturasListNewFacturasToAttach : facturasListNew) {
                facturasListNewFacturasToAttach = em.getReference(facturasListNewFacturasToAttach.getClass(), facturasListNewFacturasToAttach.getIdFactura());
                attachedFacturasListNew.add(facturasListNewFacturasToAttach);
            }
            facturasListNew = attachedFacturasListNew;
            detallepedidos.setFacturasList(facturasListNew);
            detallepedidos = em.merge(detallepedidos);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getDetallepedidosList().remove(detallepedidos);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getDetallepedidosList().add(detallepedidos);
                idPedidoNew = em.merge(idPedidoNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getDetallepedidosList().remove(detallepedidos);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getDetallepedidosList().add(detallepedidos);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Facturas facturasListOldFacturas : facturasListOld) {
                if (!facturasListNew.contains(facturasListOldFacturas)) {
                    facturasListOldFacturas.setIdDetallePedidos(null);
                    facturasListOldFacturas = em.merge(facturasListOldFacturas);
                }
            }
            for (Facturas facturasListNewFacturas : facturasListNew) {
                if (!facturasListOld.contains(facturasListNewFacturas)) {
                    Detallepedidos oldIdDetallePedidosOfFacturasListNewFacturas = facturasListNewFacturas.getIdDetallePedidos();
                    facturasListNewFacturas.setIdDetallePedidos(detallepedidos);
                    facturasListNewFacturas = em.merge(facturasListNewFacturas);
                    if (oldIdDetallePedidosOfFacturasListNewFacturas != null && !oldIdDetallePedidosOfFacturasListNewFacturas.equals(detallepedidos)) {
                        oldIdDetallePedidosOfFacturasListNewFacturas.getFacturasList().remove(facturasListNewFacturas);
                        oldIdDetallePedidosOfFacturasListNewFacturas = em.merge(oldIdDetallePedidosOfFacturasListNewFacturas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallepedidos.getIdDetallePedidos();
                if (findDetallepedidos(id) == null) {
                    throw new NonexistentEntityException("The detallepedidos with id " + id + " no longer exists.");
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
            Detallepedidos detallepedidos;
            try {
                detallepedidos = em.getReference(Detallepedidos.class, id);
                detallepedidos.getIdDetallePedidos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallepedidos with id " + id + " no longer exists.", enfe);
            }
            Pedidos idPedido = detallepedidos.getIdPedido();
            if (idPedido != null) {
                idPedido.getDetallepedidosList().remove(detallepedidos);
                idPedido = em.merge(idPedido);
            }
            Productos idProducto = detallepedidos.getIdProducto();
            if (idProducto != null) {
                idProducto.getDetallepedidosList().remove(detallepedidos);
                idProducto = em.merge(idProducto);
            }
            List<Facturas> facturasList = detallepedidos.getFacturasList();
            for (Facturas facturasListFacturas : facturasList) {
                facturasListFacturas.setIdDetallePedidos(null);
                facturasListFacturas = em.merge(facturasListFacturas);
            }
            em.remove(detallepedidos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallepedidos> findDetallepedidosEntities() {
        return findDetallepedidosEntities(true, -1, -1);
    }

    public List<Detallepedidos> findDetallepedidosEntities(int maxResults, int firstResult) {
        return findDetallepedidosEntities(false, maxResults, firstResult);
    }

    private List<Detallepedidos> findDetallepedidosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallepedidos.class));
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

    public Detallepedidos findDetallepedidos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallepedidos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallepedidosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallepedidos> rt = cq.from(Detallepedidos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
