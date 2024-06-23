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
import Entities.Tiemposmax;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class TiemposmaxJpaController implements Serializable {

    public TiemposmaxJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiemposmax tiemposmax) throws PreexistingEntityException, Exception {
        if (tiemposmax.getTiemposList() == null) {
            tiemposmax.setTiemposList(new ArrayList<Tiempos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tiempos> attachedTiemposList = new ArrayList<Tiempos>();
            for (Tiempos tiemposListTiemposToAttach : tiemposmax.getTiemposList()) {
                tiemposListTiemposToAttach = em.getReference(tiemposListTiemposToAttach.getClass(), tiemposListTiemposToAttach.getIdTiempos());
                attachedTiemposList.add(tiemposListTiemposToAttach);
            }
            tiemposmax.setTiemposList(attachedTiemposList);
            em.persist(tiemposmax);
            for (Tiempos tiemposListTiempos : tiemposmax.getTiemposList()) {
                Tiemposmax oldIdTiempoMaxOfTiemposListTiempos = tiemposListTiempos.getIdTiempoMax();
                tiemposListTiempos.setIdTiempoMax(tiemposmax);
                tiemposListTiempos = em.merge(tiemposListTiempos);
                if (oldIdTiempoMaxOfTiemposListTiempos != null) {
                    oldIdTiempoMaxOfTiemposListTiempos.getTiemposList().remove(tiemposListTiempos);
                    oldIdTiempoMaxOfTiemposListTiempos = em.merge(oldIdTiempoMaxOfTiemposListTiempos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiemposmax(tiemposmax.getIdTiempoMax()) != null) {
                throw new PreexistingEntityException("Tiemposmax " + tiemposmax + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiemposmax tiemposmax) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiemposmax persistentTiemposmax = em.find(Tiemposmax.class, tiemposmax.getIdTiempoMax());
            List<Tiempos> tiemposListOld = persistentTiemposmax.getTiemposList();
            List<Tiempos> tiemposListNew = tiemposmax.getTiemposList();
            List<Tiempos> attachedTiemposListNew = new ArrayList<Tiempos>();
            for (Tiempos tiemposListNewTiemposToAttach : tiemposListNew) {
                tiemposListNewTiemposToAttach = em.getReference(tiemposListNewTiemposToAttach.getClass(), tiemposListNewTiemposToAttach.getIdTiempos());
                attachedTiemposListNew.add(tiemposListNewTiemposToAttach);
            }
            tiemposListNew = attachedTiemposListNew;
            tiemposmax.setTiemposList(tiemposListNew);
            tiemposmax = em.merge(tiemposmax);
            for (Tiempos tiemposListOldTiempos : tiemposListOld) {
                if (!tiemposListNew.contains(tiemposListOldTiempos)) {
                    tiemposListOldTiempos.setIdTiempoMax(null);
                    tiemposListOldTiempos = em.merge(tiemposListOldTiempos);
                }
            }
            for (Tiempos tiemposListNewTiempos : tiemposListNew) {
                if (!tiemposListOld.contains(tiemposListNewTiempos)) {
                    Tiemposmax oldIdTiempoMaxOfTiemposListNewTiempos = tiemposListNewTiempos.getIdTiempoMax();
                    tiemposListNewTiempos.setIdTiempoMax(tiemposmax);
                    tiemposListNewTiempos = em.merge(tiemposListNewTiempos);
                    if (oldIdTiempoMaxOfTiemposListNewTiempos != null && !oldIdTiempoMaxOfTiemposListNewTiempos.equals(tiemposmax)) {
                        oldIdTiempoMaxOfTiemposListNewTiempos.getTiemposList().remove(tiemposListNewTiempos);
                        oldIdTiempoMaxOfTiemposListNewTiempos = em.merge(oldIdTiempoMaxOfTiemposListNewTiempos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiemposmax.getIdTiempoMax();
                if (findTiemposmax(id) == null) {
                    throw new NonexistentEntityException("The tiemposmax with id " + id + " no longer exists.");
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
            Tiemposmax tiemposmax;
            try {
                tiemposmax = em.getReference(Tiemposmax.class, id);
                tiemposmax.getIdTiempoMax();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiemposmax with id " + id + " no longer exists.", enfe);
            }
            List<Tiempos> tiemposList = tiemposmax.getTiemposList();
            for (Tiempos tiemposListTiempos : tiemposList) {
                tiemposListTiempos.setIdTiempoMax(null);
                tiemposListTiempos = em.merge(tiemposListTiempos);
            }
            em.remove(tiemposmax);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiemposmax> findTiemposmaxEntities() {
        return findTiemposmaxEntities(true, -1, -1);
    }

    public List<Tiemposmax> findTiemposmaxEntities(int maxResults, int firstResult) {
        return findTiemposmaxEntities(false, maxResults, firstResult);
    }

    private List<Tiemposmax> findTiemposmaxEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiemposmax.class));
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

    public Tiemposmax findTiemposmax(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiemposmax.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiemposmaxCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiemposmax> rt = cq.from(Tiemposmax.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
