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
import Entities.Tiempos;
import Entities.Tiemposmin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class TiemposminJpaController implements Serializable {

    public TiemposminJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiemposmin tiemposmin) throws PreexistingEntityException, Exception {
        if (tiemposmin.getTiemposList() == null) {
            tiemposmin.setTiemposList(new ArrayList<Tiempos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tiempos> attachedTiemposList = new ArrayList<Tiempos>();
            for (Tiempos tiemposListTiemposToAttach : tiemposmin.getTiemposList()) {
                tiemposListTiemposToAttach = em.getReference(tiemposListTiemposToAttach.getClass(), tiemposListTiemposToAttach.getIdTiempos());
                attachedTiemposList.add(tiemposListTiemposToAttach);
            }
            tiemposmin.setTiemposList(attachedTiemposList);
            em.persist(tiemposmin);
            for (Tiempos tiemposListTiempos : tiemposmin.getTiemposList()) {
                Tiemposmin oldIdTiempoMinOfTiemposListTiempos = tiemposListTiempos.getIdTiempoMin();
                tiemposListTiempos.setIdTiempoMin(tiemposmin);
                tiemposListTiempos = em.merge(tiemposListTiempos);
                if (oldIdTiempoMinOfTiemposListTiempos != null) {
                    oldIdTiempoMinOfTiemposListTiempos.getTiemposList().remove(tiemposListTiempos);
                    oldIdTiempoMinOfTiemposListTiempos = em.merge(oldIdTiempoMinOfTiemposListTiempos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiemposmin(tiemposmin.getIdTiempoMin()) != null) {
                throw new PreexistingEntityException("Tiemposmin " + tiemposmin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiemposmin tiemposmin) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiemposmin persistentTiemposmin = em.find(Tiemposmin.class, tiemposmin.getIdTiempoMin());
            List<Tiempos> tiemposListOld = persistentTiemposmin.getTiemposList();
            List<Tiempos> tiemposListNew = tiemposmin.getTiemposList();
            List<Tiempos> attachedTiemposListNew = new ArrayList<Tiempos>();
            for (Tiempos tiemposListNewTiemposToAttach : tiemposListNew) {
                tiemposListNewTiemposToAttach = em.getReference(tiemposListNewTiemposToAttach.getClass(), tiemposListNewTiemposToAttach.getIdTiempos());
                attachedTiemposListNew.add(tiemposListNewTiemposToAttach);
            }
            tiemposListNew = attachedTiemposListNew;
            tiemposmin.setTiemposList(tiemposListNew);
            tiemposmin = em.merge(tiemposmin);
            for (Tiempos tiemposListOldTiempos : tiemposListOld) {
                if (!tiemposListNew.contains(tiemposListOldTiempos)) {
                    tiemposListOldTiempos.setIdTiempoMin(null);
                    tiemposListOldTiempos = em.merge(tiemposListOldTiempos);
                }
            }
            for (Tiempos tiemposListNewTiempos : tiemposListNew) {
                if (!tiemposListOld.contains(tiemposListNewTiempos)) {
                    Tiemposmin oldIdTiempoMinOfTiemposListNewTiempos = tiemposListNewTiempos.getIdTiempoMin();
                    tiemposListNewTiempos.setIdTiempoMin(tiemposmin);
                    tiemposListNewTiempos = em.merge(tiemposListNewTiempos);
                    if (oldIdTiempoMinOfTiemposListNewTiempos != null && !oldIdTiempoMinOfTiemposListNewTiempos.equals(tiemposmin)) {
                        oldIdTiempoMinOfTiemposListNewTiempos.getTiemposList().remove(tiemposListNewTiempos);
                        oldIdTiempoMinOfTiemposListNewTiempos = em.merge(oldIdTiempoMinOfTiemposListNewTiempos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiemposmin.getIdTiempoMin();
                if (findTiemposmin(id) == null) {
                    throw new NonexistentEntityException("The tiemposmin with id " + id + " no longer exists.");
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
            Tiemposmin tiemposmin;
            try {
                tiemposmin = em.getReference(Tiemposmin.class, id);
                tiemposmin.getIdTiempoMin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiemposmin with id " + id + " no longer exists.", enfe);
            }
            List<Tiempos> tiemposList = tiemposmin.getTiemposList();
            for (Tiempos tiemposListTiempos : tiemposList) {
                tiemposListTiempos.setIdTiempoMin(null);
                tiemposListTiempos = em.merge(tiemposListTiempos);
            }
            em.remove(tiemposmin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiemposmin> findTiemposminEntities() {
        return findTiemposminEntities(true, -1, -1);
    }

    public List<Tiemposmin> findTiemposminEntities(int maxResults, int firstResult) {
        return findTiemposminEntities(false, maxResults, firstResult);
    }

    private List<Tiemposmin> findTiemposminEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiemposmin.class));
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

    public Tiemposmin findTiemposmin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiemposmin.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiemposminCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiemposmin> rt = cq.from(Tiemposmin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
