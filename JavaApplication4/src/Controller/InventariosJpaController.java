/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Inventarios;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class InventariosJpaController implements Serializable {

    public InventariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inventarios inventarios) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos idProducto = inventarios.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                inventarios.setIdProducto(idProducto);
            }
            em.persist(inventarios);
            if (idProducto != null) {
                idProducto.getInventariosList().add(inventarios);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInventarios(inventarios.getIdInventario()) != null) {
                throw new PreexistingEntityException("Inventarios " + inventarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inventarios inventarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventarios persistentInventarios = em.find(Inventarios.class, inventarios.getIdInventario());
            Productos idProductoOld = persistentInventarios.getIdProducto();
            Productos idProductoNew = inventarios.getIdProducto();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                inventarios.setIdProducto(idProductoNew);
            }
            inventarios = em.merge(inventarios);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getInventariosList().remove(inventarios);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getInventariosList().add(inventarios);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarios.getIdInventario();
                if (findInventarios(id) == null) {
                    throw new NonexistentEntityException("The inventarios with id " + id + " no longer exists.");
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
            Inventarios inventarios;
            try {
                inventarios = em.getReference(Inventarios.class, id);
                inventarios.getIdInventario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarios with id " + id + " no longer exists.", enfe);
            }
            Productos idProducto = inventarios.getIdProducto();
            if (idProducto != null) {
                idProducto.getInventariosList().remove(inventarios);
                idProducto = em.merge(idProducto);
            }
            em.remove(inventarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inventarios> findInventariosEntities() {
        return findInventariosEntities(true, -1, -1);
    }

    public List<Inventarios> findInventariosEntities(int maxResults, int firstResult) {
        return findInventariosEntities(false, maxResults, firstResult);
    }

    private List<Inventarios> findInventariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventarios.class));
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

    public Inventarios findInventarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inventarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventarios> rt = cq.from(Inventarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
