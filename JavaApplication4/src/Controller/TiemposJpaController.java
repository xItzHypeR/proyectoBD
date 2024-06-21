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
import Entities.Tiemposmax;
import Entities.Tiemposmin;
import Entities.Tiempoactividades;
import Entities.Tiempos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class TiemposJpaController implements Serializable {

    public TiemposJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiempos tiempos) throws PreexistingEntityException, Exception {
        if (tiempos.getTiempoactividadesList() == null) {
            tiempos.setTiempoactividadesList(new ArrayList<Tiempoactividades>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiemposmax idTiempoMax = tiempos.getIdTiempoMax();
            if (idTiempoMax != null) {
                idTiempoMax = em.getReference(idTiempoMax.getClass(), idTiempoMax.getIdTiempoMax());
                tiempos.setIdTiempoMax(idTiempoMax);
            }
            Tiemposmin idTiempoMin = tiempos.getIdTiempoMin();
            if (idTiempoMin != null) {
                idTiempoMin = em.getReference(idTiempoMin.getClass(), idTiempoMin.getIdTiempoMin());
                tiempos.setIdTiempoMin(idTiempoMin);
            }
            List<Tiempoactividades> attachedTiempoactividadesList = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListTiempoactividadesToAttach : tiempos.getTiempoactividadesList()) {
                tiempoactividadesListTiempoactividadesToAttach = em.getReference(tiempoactividadesListTiempoactividadesToAttach.getClass(), tiempoactividadesListTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesList.add(tiempoactividadesListTiempoactividadesToAttach);
            }
            tiempos.setTiempoactividadesList(attachedTiempoactividadesList);
            em.persist(tiempos);
            if (idTiempoMax != null) {
                idTiempoMax.getTiemposList().add(tiempos);
                idTiempoMax = em.merge(idTiempoMax);
            }
            if (idTiempoMin != null) {
                idTiempoMin.getTiemposList().add(tiempos);
                idTiempoMin = em.merge(idTiempoMin);
            }
            for (Tiempoactividades tiempoactividadesListTiempoactividades : tiempos.getTiempoactividadesList()) {
                Tiempos oldIdTiemposOfTiempoactividadesListTiempoactividades = tiempoactividadesListTiempoactividades.getIdTiempos();
                tiempoactividadesListTiempoactividades.setIdTiempos(tiempos);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
                if (oldIdTiemposOfTiempoactividadesListTiempoactividades != null) {
                    oldIdTiemposOfTiempoactividadesListTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListTiempoactividades);
                    oldIdTiemposOfTiempoactividadesListTiempoactividades = em.merge(oldIdTiemposOfTiempoactividadesListTiempoactividades);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiempos(tiempos.getIdTiempos()) != null) {
                throw new PreexistingEntityException("Tiempos " + tiempos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiempos tiempos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempos persistentTiempos = em.find(Tiempos.class, tiempos.getIdTiempos());
            Tiemposmax idTiempoMaxOld = persistentTiempos.getIdTiempoMax();
            Tiemposmax idTiempoMaxNew = tiempos.getIdTiempoMax();
            Tiemposmin idTiempoMinOld = persistentTiempos.getIdTiempoMin();
            Tiemposmin idTiempoMinNew = tiempos.getIdTiempoMin();
            List<Tiempoactividades> tiempoactividadesListOld = persistentTiempos.getTiempoactividadesList();
            List<Tiempoactividades> tiempoactividadesListNew = tiempos.getTiempoactividadesList();
            if (idTiempoMaxNew != null) {
                idTiempoMaxNew = em.getReference(idTiempoMaxNew.getClass(), idTiempoMaxNew.getIdTiempoMax());
                tiempos.setIdTiempoMax(idTiempoMaxNew);
            }
            if (idTiempoMinNew != null) {
                idTiempoMinNew = em.getReference(idTiempoMinNew.getClass(), idTiempoMinNew.getIdTiempoMin());
                tiempos.setIdTiempoMin(idTiempoMinNew);
            }
            List<Tiempoactividades> attachedTiempoactividadesListNew = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListNewTiempoactividadesToAttach : tiempoactividadesListNew) {
                tiempoactividadesListNewTiempoactividadesToAttach = em.getReference(tiempoactividadesListNewTiempoactividadesToAttach.getClass(), tiempoactividadesListNewTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesListNew.add(tiempoactividadesListNewTiempoactividadesToAttach);
            }
            tiempoactividadesListNew = attachedTiempoactividadesListNew;
            tiempos.setTiempoactividadesList(tiempoactividadesListNew);
            tiempos = em.merge(tiempos);
            if (idTiempoMaxOld != null && !idTiempoMaxOld.equals(idTiempoMaxNew)) {
                idTiempoMaxOld.getTiemposList().remove(tiempos);
                idTiempoMaxOld = em.merge(idTiempoMaxOld);
            }
            if (idTiempoMaxNew != null && !idTiempoMaxNew.equals(idTiempoMaxOld)) {
                idTiempoMaxNew.getTiemposList().add(tiempos);
                idTiempoMaxNew = em.merge(idTiempoMaxNew);
            }
            if (idTiempoMinOld != null && !idTiempoMinOld.equals(idTiempoMinNew)) {
                idTiempoMinOld.getTiemposList().remove(tiempos);
                idTiempoMinOld = em.merge(idTiempoMinOld);
            }
            if (idTiempoMinNew != null && !idTiempoMinNew.equals(idTiempoMinOld)) {
                idTiempoMinNew.getTiemposList().add(tiempos);
                idTiempoMinNew = em.merge(idTiempoMinNew);
            }
            for (Tiempoactividades tiempoactividadesListOldTiempoactividades : tiempoactividadesListOld) {
                if (!tiempoactividadesListNew.contains(tiempoactividadesListOldTiempoactividades)) {
                    tiempoactividadesListOldTiempoactividades.setIdTiempos(null);
                    tiempoactividadesListOldTiempoactividades = em.merge(tiempoactividadesListOldTiempoactividades);
                }
            }
            for (Tiempoactividades tiempoactividadesListNewTiempoactividades : tiempoactividadesListNew) {
                if (!tiempoactividadesListOld.contains(tiempoactividadesListNewTiempoactividades)) {
                    Tiempos oldIdTiemposOfTiempoactividadesListNewTiempoactividades = tiempoactividadesListNewTiempoactividades.getIdTiempos();
                    tiempoactividadesListNewTiempoactividades.setIdTiempos(tiempos);
                    tiempoactividadesListNewTiempoactividades = em.merge(tiempoactividadesListNewTiempoactividades);
                    if (oldIdTiemposOfTiempoactividadesListNewTiempoactividades != null && !oldIdTiemposOfTiempoactividadesListNewTiempoactividades.equals(tiempos)) {
                        oldIdTiemposOfTiempoactividadesListNewTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListNewTiempoactividades);
                        oldIdTiemposOfTiempoactividadesListNewTiempoactividades = em.merge(oldIdTiemposOfTiempoactividadesListNewTiempoactividades);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiempos.getIdTiempos();
                if (findTiempos(id) == null) {
                    throw new NonexistentEntityException("The tiempos with id " + id + " no longer exists.");
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
            Tiempos tiempos;
            try {
                tiempos = em.getReference(Tiempos.class, id);
                tiempos.getIdTiempos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiempos with id " + id + " no longer exists.", enfe);
            }
            Tiemposmax idTiempoMax = tiempos.getIdTiempoMax();
            if (idTiempoMax != null) {
                idTiempoMax.getTiemposList().remove(tiempos);
                idTiempoMax = em.merge(idTiempoMax);
            }
            Tiemposmin idTiempoMin = tiempos.getIdTiempoMin();
            if (idTiempoMin != null) {
                idTiempoMin.getTiemposList().remove(tiempos);
                idTiempoMin = em.merge(idTiempoMin);
            }
            List<Tiempoactividades> tiempoactividadesList = tiempos.getTiempoactividadesList();
            for (Tiempoactividades tiempoactividadesListTiempoactividades : tiempoactividadesList) {
                tiempoactividadesListTiempoactividades.setIdTiempos(null);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
            }
            em.remove(tiempos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiempos> findTiemposEntities() {
        return findTiemposEntities(true, -1, -1);
    }

    public List<Tiempos> findTiemposEntities(int maxResults, int firstResult) {
        return findTiemposEntities(false, maxResults, firstResult);
    }

    private List<Tiempos> findTiemposEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiempos.class));
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

    public Tiempos findTiempos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiempos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiemposCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiempos> rt = cq.from(Tiempos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
