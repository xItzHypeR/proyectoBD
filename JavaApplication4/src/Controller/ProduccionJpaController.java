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
import Entities.Empleados;
import Entities.Produccion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ProduccionJpaController implements Serializable {

    public ProduccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produccion produccion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados idEmpleado = produccion.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                produccion.setIdEmpleado(idEmpleado);
            }
            em.persist(produccion);
            if (idEmpleado != null) {
                idEmpleado.getProduccionList().add(produccion);
                idEmpleado = em.merge(idEmpleado);
            }
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
            Produccion persistentProduccion = em.find(Produccion.class, produccion.getIdProduccion());
            Empleados idEmpleadoOld = persistentProduccion.getIdEmpleado();
            Empleados idEmpleadoNew = produccion.getIdEmpleado();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                produccion.setIdEmpleado(idEmpleadoNew);
            }
            produccion = em.merge(produccion);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getProduccionList().remove(produccion);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getProduccionList().add(produccion);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
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
            Empleados idEmpleado = produccion.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getProduccionList().remove(produccion);
                idEmpleado = em.merge(idEmpleado);
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
