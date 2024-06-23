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
import Entities.Ingredientes;
import java.util.ArrayList;
import java.util.List;
import Entities.Compras;
import Entities.Proveedores;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ProveedoresJpaController implements Serializable {

    public ProveedoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedores proveedores) throws PreexistingEntityException, Exception {
        if (proveedores.getIngredientesList() == null) {
            proveedores.setIngredientesList(new ArrayList<Ingredientes>());
        }
        if (proveedores.getComprasList() == null) {
            proveedores.setComprasList(new ArrayList<Compras>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ingredientes> attachedIngredientesList = new ArrayList<Ingredientes>();
            for (Ingredientes ingredientesListIngredientesToAttach : proveedores.getIngredientesList()) {
                ingredientesListIngredientesToAttach = em.getReference(ingredientesListIngredientesToAttach.getClass(), ingredientesListIngredientesToAttach.getIdIngrediente());
                attachedIngredientesList.add(ingredientesListIngredientesToAttach);
            }
            proveedores.setIngredientesList(attachedIngredientesList);
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for (Compras comprasListComprasToAttach : proveedores.getComprasList()) {
                comprasListComprasToAttach = em.getReference(comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getIdCompra());
                attachedComprasList.add(comprasListComprasToAttach);
            }
            proveedores.setComprasList(attachedComprasList);
            em.persist(proveedores);
            for (Ingredientes ingredientesListIngredientes : proveedores.getIngredientesList()) {
                Proveedores oldIdProveedorOfIngredientesListIngredientes = ingredientesListIngredientes.getIdProveedor();
                ingredientesListIngredientes.setIdProveedor(proveedores);
                ingredientesListIngredientes = em.merge(ingredientesListIngredientes);
                if (oldIdProveedorOfIngredientesListIngredientes != null) {
                    oldIdProveedorOfIngredientesListIngredientes.getIngredientesList().remove(ingredientesListIngredientes);
                    oldIdProveedorOfIngredientesListIngredientes = em.merge(oldIdProveedorOfIngredientesListIngredientes);
                }
            }
            for (Compras comprasListCompras : proveedores.getComprasList()) {
                Proveedores oldIdProveedorOfComprasListCompras = comprasListCompras.getIdProveedor();
                comprasListCompras.setIdProveedor(proveedores);
                comprasListCompras = em.merge(comprasListCompras);
                if (oldIdProveedorOfComprasListCompras != null) {
                    oldIdProveedorOfComprasListCompras.getComprasList().remove(comprasListCompras);
                    oldIdProveedorOfComprasListCompras = em.merge(oldIdProveedorOfComprasListCompras);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedores(proveedores.getIdProveedor()) != null) {
                throw new PreexistingEntityException("Proveedores " + proveedores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedores proveedores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores persistentProveedores = em.find(Proveedores.class, proveedores.getIdProveedor());
            List<Ingredientes> ingredientesListOld = persistentProveedores.getIngredientesList();
            List<Ingredientes> ingredientesListNew = proveedores.getIngredientesList();
            List<Compras> comprasListOld = persistentProveedores.getComprasList();
            List<Compras> comprasListNew = proveedores.getComprasList();
            List<Ingredientes> attachedIngredientesListNew = new ArrayList<Ingredientes>();
            for (Ingredientes ingredientesListNewIngredientesToAttach : ingredientesListNew) {
                ingredientesListNewIngredientesToAttach = em.getReference(ingredientesListNewIngredientesToAttach.getClass(), ingredientesListNewIngredientesToAttach.getIdIngrediente());
                attachedIngredientesListNew.add(ingredientesListNewIngredientesToAttach);
            }
            ingredientesListNew = attachedIngredientesListNew;
            proveedores.setIngredientesList(ingredientesListNew);
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for (Compras comprasListNewComprasToAttach : comprasListNew) {
                comprasListNewComprasToAttach = em.getReference(comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getIdCompra());
                attachedComprasListNew.add(comprasListNewComprasToAttach);
            }
            comprasListNew = attachedComprasListNew;
            proveedores.setComprasList(comprasListNew);
            proveedores = em.merge(proveedores);
            for (Ingredientes ingredientesListOldIngredientes : ingredientesListOld) {
                if (!ingredientesListNew.contains(ingredientesListOldIngredientes)) {
                    ingredientesListOldIngredientes.setIdProveedor(null);
                    ingredientesListOldIngredientes = em.merge(ingredientesListOldIngredientes);
                }
            }
            for (Ingredientes ingredientesListNewIngredientes : ingredientesListNew) {
                if (!ingredientesListOld.contains(ingredientesListNewIngredientes)) {
                    Proveedores oldIdProveedorOfIngredientesListNewIngredientes = ingredientesListNewIngredientes.getIdProveedor();
                    ingredientesListNewIngredientes.setIdProveedor(proveedores);
                    ingredientesListNewIngredientes = em.merge(ingredientesListNewIngredientes);
                    if (oldIdProveedorOfIngredientesListNewIngredientes != null && !oldIdProveedorOfIngredientesListNewIngredientes.equals(proveedores)) {
                        oldIdProveedorOfIngredientesListNewIngredientes.getIngredientesList().remove(ingredientesListNewIngredientes);
                        oldIdProveedorOfIngredientesListNewIngredientes = em.merge(oldIdProveedorOfIngredientesListNewIngredientes);
                    }
                }
            }
            for (Compras comprasListOldCompras : comprasListOld) {
                if (!comprasListNew.contains(comprasListOldCompras)) {
                    comprasListOldCompras.setIdProveedor(null);
                    comprasListOldCompras = em.merge(comprasListOldCompras);
                }
            }
            for (Compras comprasListNewCompras : comprasListNew) {
                if (!comprasListOld.contains(comprasListNewCompras)) {
                    Proveedores oldIdProveedorOfComprasListNewCompras = comprasListNewCompras.getIdProveedor();
                    comprasListNewCompras.setIdProveedor(proveedores);
                    comprasListNewCompras = em.merge(comprasListNewCompras);
                    if (oldIdProveedorOfComprasListNewCompras != null && !oldIdProveedorOfComprasListNewCompras.equals(proveedores)) {
                        oldIdProveedorOfComprasListNewCompras.getComprasList().remove(comprasListNewCompras);
                        oldIdProveedorOfComprasListNewCompras = em.merge(oldIdProveedorOfComprasListNewCompras);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedores.getIdProveedor();
                if (findProveedores(id) == null) {
                    throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.");
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
            Proveedores proveedores;
            try {
                proveedores = em.getReference(Proveedores.class, id);
                proveedores.getIdProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.", enfe);
            }
            List<Ingredientes> ingredientesList = proveedores.getIngredientesList();
            for (Ingredientes ingredientesListIngredientes : ingredientesList) {
                ingredientesListIngredientes.setIdProveedor(null);
                ingredientesListIngredientes = em.merge(ingredientesListIngredientes);
            }
            List<Compras> comprasList = proveedores.getComprasList();
            for (Compras comprasListCompras : comprasList) {
                comprasListCompras.setIdProveedor(null);
                comprasListCompras = em.merge(comprasListCompras);
            }
            em.remove(proveedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedores> findProveedoresEntities() {
        return findProveedoresEntities(true, -1, -1);
    }

    public List<Proveedores> findProveedoresEntities(int maxResults, int firstResult) {
        return findProveedoresEntities(false, maxResults, firstResult);
    }

    private List<Proveedores> findProveedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedores.class));
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

    public Proveedores findProveedores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedores.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedores> rt = cq.from(Proveedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
