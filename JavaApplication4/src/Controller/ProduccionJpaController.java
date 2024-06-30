/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Empleados;
import Entities.Produccion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author gpera
 */
public class ProduccionJpaController implements Serializable {

    public ProduccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public ProduccionJpaController() {
        emf = Persistence.createEntityManagerFactory("MedicinaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produccion produccion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(produccion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduccion(produccion.getIdProduccion()) != null) {
                throw new PreexistingEntityException("Produccion " + produccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produccion produccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            produccion = em.merge(produccion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produccion.getIdProduccion();
                if (findProduccion(id) == null) {
                    throw new NonexistentEntityException("The produccion with id " + id + " no longer exists.");
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
            Produccion produccion;
            try {
                produccion = em.getReference(Produccion.class, id);
                produccion.getIdProduccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produccion with id " + id + " no longer exists.", enfe);
            }
            em.remove(produccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produccion> findProduccionEntities() {
        return findProduccionEntities(true, -1, -1);
    }

    public List<Produccion> findProduccionByEmpleado(Empleados empleado) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Produccion p WHERE p.idEmpleado = :empleado");
            query.setParameter("empleado", empleado);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Integer> findCantidadProducidaByEmpleado(Empleados empleado) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p.cantidadProducida FROM Produccion p WHERE p.idEmpleado.idEmpleado = :idEmpleado");
            query.setParameter("idEmpleado", empleado.getIdEmpleado());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produccion> findProduccionEntities(int maxResults, int firstResult) {
        return findProduccionEntities(false, maxResults, firstResult);
    }

    private List<Produccion> findProduccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produccion.class));
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

    public Produccion findProduccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getProduccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produccion> rt = cq.from(Produccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
