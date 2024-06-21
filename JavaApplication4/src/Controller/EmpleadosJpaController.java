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
import Entities.Departamentos;
import Entities.Empleados;
import java.util.ArrayList;
import java.util.List;
import Entities.Tiempoactividades;
import Entities.Produccion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) throws PreexistingEntityException, Exception {
        if (empleados.getDepartamentosList() == null) {
            empleados.setDepartamentosList(new ArrayList<Departamentos>());
        }
        if (empleados.getTiempoactividadesList() == null) {
            empleados.setTiempoactividadesList(new ArrayList<Tiempoactividades>());
        }
        if (empleados.getProduccionList() == null) {
            empleados.setProduccionList(new ArrayList<Produccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Departamentos> attachedDepartamentosList = new ArrayList<Departamentos>();
            for (Departamentos departamentosListDepartamentosToAttach : empleados.getDepartamentosList()) {
                departamentosListDepartamentosToAttach = em.getReference(departamentosListDepartamentosToAttach.getClass(), departamentosListDepartamentosToAttach.getIdDepartamento());
                attachedDepartamentosList.add(departamentosListDepartamentosToAttach);
            }
            empleados.setDepartamentosList(attachedDepartamentosList);
            List<Tiempoactividades> attachedTiempoactividadesList = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListTiempoactividadesToAttach : empleados.getTiempoactividadesList()) {
                tiempoactividadesListTiempoactividadesToAttach = em.getReference(tiempoactividadesListTiempoactividadesToAttach.getClass(), tiempoactividadesListTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesList.add(tiempoactividadesListTiempoactividadesToAttach);
            }
            empleados.setTiempoactividadesList(attachedTiempoactividadesList);
            List<Produccion> attachedProduccionList = new ArrayList<Produccion>();
            for (Produccion produccionListProduccionToAttach : empleados.getProduccionList()) {
                produccionListProduccionToAttach = em.getReference(produccionListProduccionToAttach.getClass(), produccionListProduccionToAttach.getIdProduccion());
                attachedProduccionList.add(produccionListProduccionToAttach);
            }
            empleados.setProduccionList(attachedProduccionList);
            em.persist(empleados);
            for (Departamentos departamentosListDepartamentos : empleados.getDepartamentosList()) {
                departamentosListDepartamentos.getEmpleadosList().add(empleados);
                departamentosListDepartamentos = em.merge(departamentosListDepartamentos);
            }
            for (Tiempoactividades tiempoactividadesListTiempoactividades : empleados.getTiempoactividadesList()) {
                Empleados oldIdEmpleadoOfTiempoactividadesListTiempoactividades = tiempoactividadesListTiempoactividades.getIdEmpleado();
                tiempoactividadesListTiempoactividades.setIdEmpleado(empleados);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
                if (oldIdEmpleadoOfTiempoactividadesListTiempoactividades != null) {
                    oldIdEmpleadoOfTiempoactividadesListTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListTiempoactividades);
                    oldIdEmpleadoOfTiempoactividadesListTiempoactividades = em.merge(oldIdEmpleadoOfTiempoactividadesListTiempoactividades);
                }
            }
            for (Produccion produccionListProduccion : empleados.getProduccionList()) {
                Empleados oldIdEmpleadoOfProduccionListProduccion = produccionListProduccion.getIdEmpleado();
                produccionListProduccion.setIdEmpleado(empleados);
                produccionListProduccion = em.merge(produccionListProduccion);
                if (oldIdEmpleadoOfProduccionListProduccion != null) {
                    oldIdEmpleadoOfProduccionListProduccion.getProduccionList().remove(produccionListProduccion);
                    oldIdEmpleadoOfProduccionListProduccion = em.merge(oldIdEmpleadoOfProduccionListProduccion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleados(empleados.getIdEmpleado()) != null) {
                throw new PreexistingEntityException("Empleados " + empleados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleados empleados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getIdEmpleado());
            List<Departamentos> departamentosListOld = persistentEmpleados.getDepartamentosList();
            List<Departamentos> departamentosListNew = empleados.getDepartamentosList();
            List<Tiempoactividades> tiempoactividadesListOld = persistentEmpleados.getTiempoactividadesList();
            List<Tiempoactividades> tiempoactividadesListNew = empleados.getTiempoactividadesList();
            List<Produccion> produccionListOld = persistentEmpleados.getProduccionList();
            List<Produccion> produccionListNew = empleados.getProduccionList();
            List<Departamentos> attachedDepartamentosListNew = new ArrayList<Departamentos>();
            for (Departamentos departamentosListNewDepartamentosToAttach : departamentosListNew) {
                departamentosListNewDepartamentosToAttach = em.getReference(departamentosListNewDepartamentosToAttach.getClass(), departamentosListNewDepartamentosToAttach.getIdDepartamento());
                attachedDepartamentosListNew.add(departamentosListNewDepartamentosToAttach);
            }
            departamentosListNew = attachedDepartamentosListNew;
            empleados.setDepartamentosList(departamentosListNew);
            List<Tiempoactividades> attachedTiempoactividadesListNew = new ArrayList<Tiempoactividades>();
            for (Tiempoactividades tiempoactividadesListNewTiempoactividadesToAttach : tiempoactividadesListNew) {
                tiempoactividadesListNewTiempoactividadesToAttach = em.getReference(tiempoactividadesListNewTiempoactividadesToAttach.getClass(), tiempoactividadesListNewTiempoactividadesToAttach.getIdTiempoActividad());
                attachedTiempoactividadesListNew.add(tiempoactividadesListNewTiempoactividadesToAttach);
            }
            tiempoactividadesListNew = attachedTiempoactividadesListNew;
            empleados.setTiempoactividadesList(tiempoactividadesListNew);
            List<Produccion> attachedProduccionListNew = new ArrayList<Produccion>();
            for (Produccion produccionListNewProduccionToAttach : produccionListNew) {
                produccionListNewProduccionToAttach = em.getReference(produccionListNewProduccionToAttach.getClass(), produccionListNewProduccionToAttach.getIdProduccion());
                attachedProduccionListNew.add(produccionListNewProduccionToAttach);
            }
            produccionListNew = attachedProduccionListNew;
            empleados.setProduccionList(produccionListNew);
            empleados = em.merge(empleados);
            for (Departamentos departamentosListOldDepartamentos : departamentosListOld) {
                if (!departamentosListNew.contains(departamentosListOldDepartamentos)) {
                    departamentosListOldDepartamentos.getEmpleadosList().remove(empleados);
                    departamentosListOldDepartamentos = em.merge(departamentosListOldDepartamentos);
                }
            }
            for (Departamentos departamentosListNewDepartamentos : departamentosListNew) {
                if (!departamentosListOld.contains(departamentosListNewDepartamentos)) {
                    departamentosListNewDepartamentos.getEmpleadosList().add(empleados);
                    departamentosListNewDepartamentos = em.merge(departamentosListNewDepartamentos);
                }
            }
            for (Tiempoactividades tiempoactividadesListOldTiempoactividades : tiempoactividadesListOld) {
                if (!tiempoactividadesListNew.contains(tiempoactividadesListOldTiempoactividades)) {
                    tiempoactividadesListOldTiempoactividades.setIdEmpleado(null);
                    tiempoactividadesListOldTiempoactividades = em.merge(tiempoactividadesListOldTiempoactividades);
                }
            }
            for (Tiempoactividades tiempoactividadesListNewTiempoactividades : tiempoactividadesListNew) {
                if (!tiempoactividadesListOld.contains(tiempoactividadesListNewTiempoactividades)) {
                    Empleados oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades = tiempoactividadesListNewTiempoactividades.getIdEmpleado();
                    tiempoactividadesListNewTiempoactividades.setIdEmpleado(empleados);
                    tiempoactividadesListNewTiempoactividades = em.merge(tiempoactividadesListNewTiempoactividades);
                    if (oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades != null && !oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades.equals(empleados)) {
                        oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades.getTiempoactividadesList().remove(tiempoactividadesListNewTiempoactividades);
                        oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades = em.merge(oldIdEmpleadoOfTiempoactividadesListNewTiempoactividades);
                    }
                }
            }
            for (Produccion produccionListOldProduccion : produccionListOld) {
                if (!produccionListNew.contains(produccionListOldProduccion)) {
                    produccionListOldProduccion.setIdEmpleado(null);
                    produccionListOldProduccion = em.merge(produccionListOldProduccion);
                }
            }
            for (Produccion produccionListNewProduccion : produccionListNew) {
                if (!produccionListOld.contains(produccionListNewProduccion)) {
                    Empleados oldIdEmpleadoOfProduccionListNewProduccion = produccionListNewProduccion.getIdEmpleado();
                    produccionListNewProduccion.setIdEmpleado(empleados);
                    produccionListNewProduccion = em.merge(produccionListNewProduccion);
                    if (oldIdEmpleadoOfProduccionListNewProduccion != null && !oldIdEmpleadoOfProduccionListNewProduccion.equals(empleados)) {
                        oldIdEmpleadoOfProduccionListNewProduccion.getProduccionList().remove(produccionListNewProduccion);
                        oldIdEmpleadoOfProduccionListNewProduccion = em.merge(oldIdEmpleadoOfProduccionListNewProduccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleados.getIdEmpleado();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
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
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            List<Departamentos> departamentosList = empleados.getDepartamentosList();
            for (Departamentos departamentosListDepartamentos : departamentosList) {
                departamentosListDepartamentos.getEmpleadosList().remove(empleados);
                departamentosListDepartamentos = em.merge(departamentosListDepartamentos);
            }
            List<Tiempoactividades> tiempoactividadesList = empleados.getTiempoactividadesList();
            for (Tiempoactividades tiempoactividadesListTiempoactividades : tiempoactividadesList) {
                tiempoactividadesListTiempoactividades.setIdEmpleado(null);
                tiempoactividadesListTiempoactividades = em.merge(tiempoactividadesListTiempoactividades);
            }
            List<Produccion> produccionList = empleados.getProduccionList();
            for (Produccion produccionListProduccion : produccionList) {
                produccionListProduccion.setIdEmpleado(null);
                produccionListProduccion = em.merge(produccionListProduccion);
            }
            em.remove(empleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
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

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
