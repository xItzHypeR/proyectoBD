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
import Entities.Observadores;
import Entities.Tiempoactividades;
import Entities.Tiempos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class TiempoactividadesJpaController implements Serializable {

    public TiempoactividadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiempoactividades tiempoactividades) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Observadores idObservador = tiempoactividades.getIdObservador();
            if (idObservador != null) {
                idObservador = em.getReference(idObservador.getClass(), idObservador.getIdObservador());
                tiempoactividades.setIdObservador(idObservador);
            }
            Tiempos idTiempos = tiempoactividades.getIdTiempos();
            if (idTiempos != null) {
                idTiempos = em.getReference(idTiempos.getClass(), idTiempos.getIdTiempos());
                tiempoactividades.setIdTiempos(idTiempos);
            }
            em.persist(tiempoactividades);
            if (idObservador != null) {
                idObservador.getTiempoactividadesList().add(tiempoactividades);
                idObservador = em.merge(idObservador);
            }
            if (idTiempos != null) {
                idTiempos.getTiempoactividadesList().add(tiempoactividades);
                idTiempos = em.merge(idTiempos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiempoactividades(tiempoactividades.getIdTiempoActividad()) != null) {
                throw new PreexistingEntityException("Tiempoactividades " + tiempoactividades + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiempoactividades tiempoactividades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempoactividades persistentTiempoactividades = em.find(Tiempoactividades.class, tiempoactividades.getIdTiempoActividad());
            Observadores idObservadorOld = persistentTiempoactividades.getIdObservador();
            Observadores idObservadorNew = tiempoactividades.getIdObservador();
            Tiempos idTiemposOld = persistentTiempoactividades.getIdTiempos();
            Tiempos idTiemposNew = tiempoactividades.getIdTiempos();
            if (idObservadorNew != null) {
                idObservadorNew = em.getReference(idObservadorNew.getClass(), idObservadorNew.getIdObservador());
                tiempoactividades.setIdObservador(idObservadorNew);
            }
            if (idTiemposNew != null) {
                idTiemposNew = em.getReference(idTiemposNew.getClass(), idTiemposNew.getIdTiempos());
                tiempoactividades.setIdTiempos(idTiemposNew);
            }
            tiempoactividades = em.merge(tiempoactividades);
            if (idObservadorOld != null && !idObservadorOld.equals(idObservadorNew)) {
                idObservadorOld.getTiempoactividadesList().remove(tiempoactividades);
                idObservadorOld = em.merge(idObservadorOld);
            }
            if (idObservadorNew != null && !idObservadorNew.equals(idObservadorOld)) {
                idObservadorNew.getTiempoactividadesList().add(tiempoactividades);
                idObservadorNew = em.merge(idObservadorNew);
            }
            if (idTiemposOld != null && !idTiemposOld.equals(idTiemposNew)) {
                idTiemposOld.getTiempoactividadesList().remove(tiempoactividades);
                idTiemposOld = em.merge(idTiemposOld);
            }
            if (idTiemposNew != null && !idTiemposNew.equals(idTiemposOld)) {
                idTiemposNew.getTiempoactividadesList().add(tiempoactividades);
                idTiemposNew = em.merge(idTiemposNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiempoactividades.getIdTiempoActividad();
                if (findTiempoactividades(id) == null) {
                    throw new NonexistentEntityException("The tiempoactividades with id " + id + " no longer exists.");
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
            Tiempoactividades tiempoactividades;
            try {
                tiempoactividades = em.getReference(Tiempoactividades.class, id);
                tiempoactividades.getIdTiempoActividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiempoactividades with id " + id + " no longer exists.", enfe);
            }
            Observadores idObservador = tiempoactividades.getIdObservador();
            if (idObservador != null) {
                idObservador.getTiempoactividadesList().remove(tiempoactividades);
                idObservador = em.merge(idObservador);
            }
            Tiempos idTiempos = tiempoactividades.getIdTiempos();
            if (idTiempos != null) {
                idTiempos.getTiempoactividadesList().remove(tiempoactividades);
                idTiempos = em.merge(idTiempos);
            }
            em.remove(tiempoactividades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiempoactividades> findTiempoactividadesEntities() {
        return findTiempoactividadesEntities(true, -1, -1);
    }

    public List<Tiempoactividades> findTiempoactividadesEntities(int maxResults, int firstResult) {
        return findTiempoactividadesEntities(false, maxResults, firstResult);
    }

    private List<Tiempoactividades> findTiempoactividadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiempoactividades.class));
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

    public Tiempoactividades findTiempoactividades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiempoactividades.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiempoactividadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiempoactividades> rt = cq.from(Tiempoactividades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
