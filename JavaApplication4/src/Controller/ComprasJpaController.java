/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Compras;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Proveedores;
import Entities.Detallecompra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ComprasJpaController implements Serializable {

    public ComprasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compras compras) throws PreexistingEntityException, Exception {
        if (compras.getDetallecompraList() == null) {
            compras.setDetallecompraList(new ArrayList<Detallecompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores idProveedor = compras.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                compras.setIdProveedor(idProveedor);
            }
            List<Detallecompra> attachedDetallecompraList = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListDetallecompraToAttach : compras.getDetallecompraList()) {
                detallecompraListDetallecompraToAttach = em.getReference(detallecompraListDetallecompraToAttach.getClass(), detallecompraListDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraList.add(detallecompraListDetallecompraToAttach);
            }
            compras.setDetallecompraList(attachedDetallecompraList);
            em.persist(compras);
            if (idProveedor != null) {
                idProveedor.getComprasList().add(compras);
                idProveedor = em.merge(idProveedor);
            }
            for (Detallecompra detallecompraListDetallecompra : compras.getDetallecompraList()) {
                Compras oldIdCompraOfDetallecompraListDetallecompra = detallecompraListDetallecompra.getIdCompra();
                detallecompraListDetallecompra.setIdCompra(compras);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
                if (oldIdCompraOfDetallecompraListDetallecompra != null) {
                    oldIdCompraOfDetallecompraListDetallecompra.getDetallecompraList().remove(detallecompraListDetallecompra);
                    oldIdCompraOfDetallecompraListDetallecompra = em.merge(oldIdCompraOfDetallecompraListDetallecompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompras(compras.getIdCompra()) != null) {
                throw new PreexistingEntityException("Compras " + compras + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compras compras) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compras persistentCompras = em.find(Compras.class, compras.getIdCompra());
            Proveedores idProveedorOld = persistentCompras.getIdProveedor();
            Proveedores idProveedorNew = compras.getIdProveedor();
            List<Detallecompra> detallecompraListOld = persistentCompras.getDetallecompraList();
            List<Detallecompra> detallecompraListNew = compras.getDetallecompraList();
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                compras.setIdProveedor(idProveedorNew);
            }
            List<Detallecompra> attachedDetallecompraListNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListNewDetallecompraToAttach : detallecompraListNew) {
                detallecompraListNewDetallecompraToAttach = em.getReference(detallecompraListNewDetallecompraToAttach.getClass(), detallecompraListNewDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraListNew.add(detallecompraListNewDetallecompraToAttach);
            }
            detallecompraListNew = attachedDetallecompraListNew;
            compras.setDetallecompraList(detallecompraListNew);
            compras = em.merge(compras);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getComprasList().remove(compras);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getComprasList().add(compras);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Detallecompra detallecompraListOldDetallecompra : detallecompraListOld) {
                if (!detallecompraListNew.contains(detallecompraListOldDetallecompra)) {
                    detallecompraListOldDetallecompra.setIdCompra(null);
                    detallecompraListOldDetallecompra = em.merge(detallecompraListOldDetallecompra);
                }
            }
            for (Detallecompra detallecompraListNewDetallecompra : detallecompraListNew) {
                if (!detallecompraListOld.contains(detallecompraListNewDetallecompra)) {
                    Compras oldIdCompraOfDetallecompraListNewDetallecompra = detallecompraListNewDetallecompra.getIdCompra();
                    detallecompraListNewDetallecompra.setIdCompra(compras);
                    detallecompraListNewDetallecompra = em.merge(detallecompraListNewDetallecompra);
                    if (oldIdCompraOfDetallecompraListNewDetallecompra != null && !oldIdCompraOfDetallecompraListNewDetallecompra.equals(compras)) {
                        oldIdCompraOfDetallecompraListNewDetallecompra.getDetallecompraList().remove(detallecompraListNewDetallecompra);
                        oldIdCompraOfDetallecompraListNewDetallecompra = em.merge(oldIdCompraOfDetallecompraListNewDetallecompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compras.getIdCompra();
                if (findCompras(id) == null) {
                    throw new NonexistentEntityException("The compras with id " + id + " no longer exists.");
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
            Compras compras;
            try {
                compras = em.getReference(Compras.class, id);
                compras.getIdCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compras with id " + id + " no longer exists.", enfe);
            }
            Proveedores idProveedor = compras.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getComprasList().remove(compras);
                idProveedor = em.merge(idProveedor);
            }
            List<Detallecompra> detallecompraList = compras.getDetallecompraList();
            for (Detallecompra detallecompraListDetallecompra : detallecompraList) {
                detallecompraListDetallecompra.setIdCompra(null);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
            }
            em.remove(compras);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compras> findComprasEntities() {
        return findComprasEntities(true, -1, -1);
    }

    public List<Compras> findComprasEntities(int maxResults, int firstResult) {
        return findComprasEntities(false, maxResults, firstResult);
    }

    private List<Compras> findComprasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compras.class));
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

    public Compras findCompras(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compras.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compras> rt = cq.from(Compras.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
