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
import Entities.Compras;
import Entities.Detallecompra;
import Entities.Ingredientes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class DetallecompraJpaController implements Serializable {

    public DetallecompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecompra detallecompra) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compras idCompra = detallecompra.getIdCompra();
            if (idCompra != null) {
                idCompra = em.getReference(idCompra.getClass(), idCompra.getIdCompra());
                detallecompra.setIdCompra(idCompra);
            }
            Ingredientes idIngrediente = detallecompra.getIdIngrediente();
            if (idIngrediente != null) {
                idIngrediente = em.getReference(idIngrediente.getClass(), idIngrediente.getIdIngrediente());
                detallecompra.setIdIngrediente(idIngrediente);
            }
            em.persist(detallecompra);
            if (idCompra != null) {
                idCompra.getDetallecompraList().add(detallecompra);
                idCompra = em.merge(idCompra);
            }
            if (idIngrediente != null) {
                idIngrediente.getDetallecompraList().add(detallecompra);
                idIngrediente = em.merge(idIngrediente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetallecompra(detallecompra.getIdDetalleCompra()) != null) {
                throw new PreexistingEntityException("Detallecompra " + detallecompra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallecompra detallecompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallecompra persistentDetallecompra = em.find(Detallecompra.class, detallecompra.getIdDetalleCompra());
            Compras idCompraOld = persistentDetallecompra.getIdCompra();
            Compras idCompraNew = detallecompra.getIdCompra();
            Ingredientes idIngredienteOld = persistentDetallecompra.getIdIngrediente();
            Ingredientes idIngredienteNew = detallecompra.getIdIngrediente();
            if (idCompraNew != null) {
                idCompraNew = em.getReference(idCompraNew.getClass(), idCompraNew.getIdCompra());
                detallecompra.setIdCompra(idCompraNew);
            }
            if (idIngredienteNew != null) {
                idIngredienteNew = em.getReference(idIngredienteNew.getClass(), idIngredienteNew.getIdIngrediente());
                detallecompra.setIdIngrediente(idIngredienteNew);
            }
            detallecompra = em.merge(detallecompra);
            if (idCompraOld != null && !idCompraOld.equals(idCompraNew)) {
                idCompraOld.getDetallecompraList().remove(detallecompra);
                idCompraOld = em.merge(idCompraOld);
            }
            if (idCompraNew != null && !idCompraNew.equals(idCompraOld)) {
                idCompraNew.getDetallecompraList().add(detallecompra);
                idCompraNew = em.merge(idCompraNew);
            }
            if (idIngredienteOld != null && !idIngredienteOld.equals(idIngredienteNew)) {
                idIngredienteOld.getDetallecompraList().remove(detallecompra);
                idIngredienteOld = em.merge(idIngredienteOld);
            }
            if (idIngredienteNew != null && !idIngredienteNew.equals(idIngredienteOld)) {
                idIngredienteNew.getDetallecompraList().add(detallecompra);
                idIngredienteNew = em.merge(idIngredienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallecompra.getIdDetalleCompra();
                if (findDetallecompra(id) == null) {
                    throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.");
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
            Detallecompra detallecompra;
            try {
                detallecompra = em.getReference(Detallecompra.class, id);
                detallecompra.getIdDetalleCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.", enfe);
            }
            Compras idCompra = detallecompra.getIdCompra();
            if (idCompra != null) {
                idCompra.getDetallecompraList().remove(detallecompra);
                idCompra = em.merge(idCompra);
            }
            Ingredientes idIngrediente = detallecompra.getIdIngrediente();
            if (idIngrediente != null) {
                idIngrediente.getDetallecompraList().remove(detallecompra);
                idIngrediente = em.merge(idIngrediente);
            }
            em.remove(detallecompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallecompra> findDetallecompraEntities() {
        return findDetallecompraEntities(true, -1, -1);
    }

    public List<Detallecompra> findDetallecompraEntities(int maxResults, int firstResult) {
        return findDetallecompraEntities(false, maxResults, firstResult);
    }

    private List<Detallecompra> findDetallecompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecompra.class));
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

    public Detallecompra findDetallecompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecompra> rt = cq.from(Detallecompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
