/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Detallepedidos;
import Entities.Facturas;
import Entities.Ventas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class FacturasJpaController implements Serializable {

    public FacturasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturas facturas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallepedidos idDetallePedidos = facturas.getIdDetallePedidos();
            if (idDetallePedidos != null) {
                idDetallePedidos = em.getReference(idDetallePedidos.getClass(), idDetallePedidos.getIdDetallePedidos());
                facturas.setIdDetallePedidos(idDetallePedidos);
            }
            Ventas idVenta = facturas.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getIdVenta());
                facturas.setIdVenta(idVenta);
            }
            em.persist(facturas);
            if (idDetallePedidos != null) {
                idDetallePedidos.getFacturasList().add(facturas);
                idDetallePedidos = em.merge(idDetallePedidos);
            }
            if (idVenta != null) {
                idVenta.getFacturasList().add(facturas);
                idVenta = em.merge(idVenta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFacturas(facturas.getIdFactura()) != null) {
                throw new PreexistingEntityException("Facturas " + facturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facturas facturas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas persistentFacturas = em.find(Facturas.class, facturas.getIdFactura());
            Detallepedidos idDetallePedidosOld = persistentFacturas.getIdDetallePedidos();
            Detallepedidos idDetallePedidosNew = facturas.getIdDetallePedidos();
            Ventas idVentaOld = persistentFacturas.getIdVenta();
            Ventas idVentaNew = facturas.getIdVenta();
            if (idDetallePedidosNew != null) {
                idDetallePedidosNew = em.getReference(idDetallePedidosNew.getClass(), idDetallePedidosNew.getIdDetallePedidos());
                facturas.setIdDetallePedidos(idDetallePedidosNew);
            }
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getIdVenta());
                facturas.setIdVenta(idVentaNew);
            }
            facturas = em.merge(facturas);
            if (idDetallePedidosOld != null && !idDetallePedidosOld.equals(idDetallePedidosNew)) {
                idDetallePedidosOld.getFacturasList().remove(facturas);
                idDetallePedidosOld = em.merge(idDetallePedidosOld);
            }
            if (idDetallePedidosNew != null && !idDetallePedidosNew.equals(idDetallePedidosOld)) {
                idDetallePedidosNew.getFacturasList().add(facturas);
                idDetallePedidosNew = em.merge(idDetallePedidosNew);
            }
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getFacturasList().remove(facturas);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getFacturasList().add(facturas);
                idVentaNew = em.merge(idVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturas.getIdFactura();
                if (findFacturas(id) == null) {
                    throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.");
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
            Facturas facturas;
            try {
                facturas = em.getReference(Facturas.class, id);
                facturas.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.", enfe);
            }
            Detallepedidos idDetallePedidos = facturas.getIdDetallePedidos();
            if (idDetallePedidos != null) {
                idDetallePedidos.getFacturasList().remove(facturas);
                idDetallePedidos = em.merge(idDetallePedidos);
            }
            Ventas idVenta = facturas.getIdVenta();
            if (idVenta != null) {
                idVenta.getFacturasList().remove(facturas);
                idVenta = em.merge(idVenta);
            }
            em.remove(facturas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facturas> findFacturasEntities() {
        return findFacturasEntities(true, -1, -1);
    }

    public List<Facturas> findFacturasEntities(int maxResults, int firstResult) {
        return findFacturasEntities(false, maxResults, firstResult);
    }

    private List<Facturas> findFacturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturas.class));
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

    public Facturas findFacturas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturas.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturas> rt = cq.from(Facturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
