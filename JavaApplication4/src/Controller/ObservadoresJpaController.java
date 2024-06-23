/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Observadores;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Tiempoactividades;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ObservadoresJpaController implements Serializable {

    public ObservadoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Observadores observadores) throws PreexistingEntityException, Exception {
        if (observadores.getTiempoactividadesList() == null) {
            observadores.setTiempoactividadesList(new ArrayList<Tiempoactividades>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tiempoactividades> attachedTiempoactividadesList = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListTiempoactividadesToAttach : observadores.getTiempoactividadesList()) {
                tiempoactividadesListTiempoactividadesToAttach = em.getReference(tiempoactividadesListTiempoactividadesToAttach.getClass(), tiempoactividadesListTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesList.add(tiempoactividadesListTiempoactividadesToAttach);
            }
            observadores.setTiempoactividadesList(attachedTiempoactividadesList);
            em.persist(observadores);
            for (Tiempoactividades tiempoactividadesListTiempoactividades : observadores.getTiempoactividadesList()) {
                Observadores oldIdObservadorOfTiempoactividadesListTiempoactividades = tiempoactividadesListTiempoactividades.getIdObservador();
                tiempoactividadesListTiempoactividades.setIdObservador(observadores);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
                if (oldIdObservadorOfTiempoactividadesListTiempoactividades != null) {
                    oldIdObservadorOfTiempoactividadesListTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListTiempoactividades);
                    oldIdObservadorOfTiempoactividadesListTiempoactividades = em.merge(oldIdObservadorOfTiempoactividadesListTiempoactividades);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObservadores(observadores.getIdObservador()) != null) {
                throw new PreexistingEntityException("Observadores " + observadores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Observadores observadores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Observadores persistentObservadores = em.find(Observadores.class, observadores.getIdObservador());
            List<Tiempoactividades> tiempoactividadesListOld = persistentObservadores.getTiempoactividadesList();
            List<Tiempoactividades> tiempoactividadesListNew = observadores.getTiempoactividadesList();
            List<Tiempoactividades> attachedTiempoactividadesListNew = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListNewTiempoactividadesToAttach : tiempoactividadesListNew) {
                tiempoactividadesListNewTiempoactividadesToAttach = em.getReference(tiempoactividadesListNewTiempoactividadesToAttach.getClass(), tiempoactividadesListNewTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesListNew.add(tiempoactividadesListNewTiempoactividadesToAttach);
            }
            tiempoactividadesListNew = attachedTiempoactividadesListNew;
            observadores.setTiempoactividadesList(tiempoactividadesListNew);
            observadores = em.merge(observadores);
            for (Tiempoactividades tiempoactividadesListOldTiempoactividades : tiempoactividadesListOld) {
                if (!tiempoactividadesListNew.contains(tiempoactividadesListOldTiempoactividades)) {
                    tiempoactividadesListOldTiempoactividades.setIdObservador(null);
                    tiempoactividadesListOldTiempoactividades = em.merge(tiempoactividadesListOldTiempoactividades);
                }
            }
            for (Tiempoactividades tiempoactividadesListNewTiempoactividades : tiempoactividadesListNew) {
                if (!tiempoactividadesListOld.contains(tiempoactividadesListNewTiempoactividades)) {
                    Observadores oldIdObservadorOfTiempoactividadesListNewTiempoactividades = tiempoactividadesListNewTiempoactividades.getIdObservador();
                    tiempoactividadesListNewTiempoactividades.setIdObservador(observadores);
                    tiempoactividadesListNewTiempoactividades = em.merge(tiempoactividadesListNewTiempoactividades);
                    if (oldIdObservadorOfTiempoactividadesListNewTiempoactividades != null && !oldIdObservadorOfTiempoactividadesListNewTiempoactividades.equals(observadores)) {
                        oldIdObservadorOfTiempoactividadesListNewTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListNewTiempoactividades);
                        oldIdObservadorOfTiempoactividadesListNewTiempoactividades = em.merge(oldIdObservadorOfTiempoactividadesListNewTiempoactividades);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = observadores.getIdObservador();
                if (findObservadores(id) == null) {
                    throw new NonexistentEntityException("The observadores with id " + id + " no longer exists.");
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
            Observadores observadores;
            try {
                observadores = em.getReference(Observadores.class, id);
                observadores.getIdObservador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The observadores with id " + id + " no longer exists.", enfe);
            }
            List<Tiempoactividades> tiempoactividadesList = observadores.getTiempoactividadesList();
            for (Tiempoactividades tiempoactividadesListTiempoactividades : tiempoactividadesList) {
                tiempoactividadesListTiempoactividades.setIdObservador(null);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
            }
            em.remove(observadores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Observadores> findObservadoresEntities() {
        return findObservadoresEntities(true, -1, -1);
    }

    public List<Observadores> findObservadoresEntities(int maxResults, int firstResult) {
        return findObservadoresEntities(false, maxResults, firstResult);
    }

    private List<Observadores> findObservadoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Observadores.class));
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

    public Observadores findObservadores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Observadores.class, id);
        } finally {
            em.close();
        }
    }

    public int getObservadoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Observadores> rt = cq.from(Observadores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
