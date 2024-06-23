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
import Entities.Proveedores;
import Entities.Productoingredientes;
import java.util.ArrayList;
import java.util.List;
import Entities.Detallecompra;
import Entities.Ingredientes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class IngredientesJpaController implements Serializable {

    public IngredientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingredientes ingredientes) throws PreexistingEntityException, Exception {
        if (ingredientes.getProductoingredientesList() == null) {
            ingredientes.setProductoingredientesList(new ArrayList<Productoingredientes>());
        }
        if (ingredientes.getDetallecompraList() == null) {
            ingredientes.setDetallecompraList(new ArrayList<Detallecompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores idProveedor = ingredientes.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                ingredientes.setIdProveedor(idProveedor);
            }
            List<Productoingredientes> attachedProductoingredientesList = new ArrayList<Productoingredientes>();
            for (Productoingredientes productoingredientesListProductoingredientesToAttach : ingredientes.getProductoingredientesList()) {
                productoingredientesListProductoingredientesToAttach = em.getReference(productoingredientesListProductoingredientesToAttach.getClass(), productoingredientesListProductoingredientesToAttach.getIdProductoIngrediente());
                attachedProductoingredientesList.add(productoingredientesListProductoingredientesToAttach);
            }
            ingredientes.setProductoingredientesList(attachedProductoingredientesList);
            List<Detallecompra> attachedDetallecompraList = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListDetallecompraToAttach : ingredientes.getDetallecompraList()) {
                detallecompraListDetallecompraToAttach = em.getReference(detallecompraListDetallecompraToAttach.getClass(), detallecompraListDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraList.add(detallecompraListDetallecompraToAttach);
            }
            ingredientes.setDetallecompraList(attachedDetallecompraList);
            em.persist(ingredientes);
            if (idProveedor != null) {
                idProveedor.getIngredientesList().add(ingredientes);
                idProveedor = em.merge(idProveedor);
            }
            for (Productoingredientes productoingredientesListProductoingredientes : ingredientes.getProductoingredientesList()) {
                Ingredientes oldIdIngredienteOfProductoingredientesListProductoingredientes = productoingredientesListProductoingredientes.getIdIngrediente();
                productoingredientesListProductoingredientes.setIdIngrediente(ingredientes);
                productoingredientesListProductoingredientes = em.merge(productoingredientesListProductoingredientes);
                if (oldIdIngredienteOfProductoingredientesListProductoingredientes != null) {
                    oldIdIngredienteOfProductoingredientesListProductoingredientes.getProductoingredientesList().remove(productoingredientesListProductoingredientes);
                    oldIdIngredienteOfProductoingredientesListProductoingredientes = em.merge(oldIdIngredienteOfProductoingredientesListProductoingredientes);
                }
            }
            for (Detallecompra detallecompraListDetallecompra : ingredientes.getDetallecompraList()) {
                Ingredientes oldIdIngredienteOfDetallecompraListDetallecompra = detallecompraListDetallecompra.getIdIngrediente();
                detallecompraListDetallecompra.setIdIngrediente(ingredientes);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
                if (oldIdIngredienteOfDetallecompraListDetallecompra != null) {
                    oldIdIngredienteOfDetallecompraListDetallecompra.getDetallecompraList().remove(detallecompraListDetallecompra);
                    oldIdIngredienteOfDetallecompraListDetallecompra = em.merge(oldIdIngredienteOfDetallecompraListDetallecompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngredientes(ingredientes.getIdIngrediente()) != null) {
                throw new PreexistingEntityException("Ingredientes " + ingredientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingredientes ingredientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredientes persistentIngredientes = em.find(Ingredientes.class, ingredientes.getIdIngrediente());
            Proveedores idProveedorOld = persistentIngredientes.getIdProveedor();
            Proveedores idProveedorNew = ingredientes.getIdProveedor();
            List<Productoingredientes> productoingredientesListOld = persistentIngredientes.getProductoingredientesList();
            List<Productoingredientes> productoingredientesListNew = ingredientes.getProductoingredientesList();
            List<Detallecompra> detallecompraListOld = persistentIngredientes.getDetallecompraList();
            List<Detallecompra> detallecompraListNew = ingredientes.getDetallecompraList();
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                ingredientes.setIdProveedor(idProveedorNew);
            }
            List<Productoingredientes> attachedProductoingredientesListNew = new ArrayList<Productoingredientes>();
            for (Productoingredientes productoingredientesListNewProductoingredientesToAttach : productoingredientesListNew) {
                productoingredientesListNewProductoingredientesToAttach = em.getReference(productoingredientesListNewProductoingredientesToAttach.getClass(), productoingredientesListNewProductoingredientesToAttach.getIdProductoIngrediente());
                attachedProductoingredientesListNew.add(productoingredientesListNewProductoingredientesToAttach);
            }
            productoingredientesListNew = attachedProductoingredientesListNew;
            ingredientes.setProductoingredientesList(productoingredientesListNew);
            List<Detallecompra> attachedDetallecompraListNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListNewDetallecompraToAttach : detallecompraListNew) {
                detallecompraListNewDetallecompraToAttach = em.getReference(detallecompraListNewDetallecompraToAttach.getClass(), detallecompraListNewDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraListNew.add(detallecompraListNewDetallecompraToAttach);
            }
            detallecompraListNew = attachedDetallecompraListNew;
            ingredientes.setDetallecompraList(detallecompraListNew);
            ingredientes = em.merge(ingredientes);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getIngredientesList().remove(ingredientes);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getIngredientesList().add(ingredientes);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Productoingredientes productoingredientesListOldProductoingredientes : productoingredientesListOld) {
                if (!productoingredientesListNew.contains(productoingredientesListOldProductoingredientes)) {
                    productoingredientesListOldProductoingredientes.setIdIngrediente(null);
                    productoingredientesListOldProductoingredientes = em.merge(productoingredientesListOldProductoingredientes);
                }
            }
            for (Productoingredientes productoingredientesListNewProductoingredientes : productoingredientesListNew) {
                if (!productoingredientesListOld.contains(productoingredientesListNewProductoingredientes)) {
                    Ingredientes oldIdIngredienteOfProductoingredientesListNewProductoingredientes = productoingredientesListNewProductoingredientes.getIdIngrediente();
                    productoingredientesListNewProductoingredientes.setIdIngrediente(ingredientes);
                    productoingredientesListNewProductoingredientes = em.merge(productoingredientesListNewProductoingredientes);
                    if (oldIdIngredienteOfProductoingredientesListNewProductoingredientes != null && !oldIdIngredienteOfProductoingredientesListNewProductoingredientes.equals(ingredientes)) {
                        oldIdIngredienteOfProductoingredientesListNewProductoingredientes.getProductoingredientesList().remove(productoingredientesListNewProductoingredientes);
                        oldIdIngredienteOfProductoingredientesListNewProductoingredientes = em.merge(oldIdIngredienteOfProductoingredientesListNewProductoingredientes);
                    }
                }
            }
            for (Detallecompra detallecompraListOldDetallecompra : detallecompraListOld) {
                if (!detallecompraListNew.contains(detallecompraListOldDetallecompra)) {
                    detallecompraListOldDetallecompra.setIdIngrediente(null);
                    detallecompraListOldDetallecompra = em.merge(detallecompraListOldDetallecompra);
                }
            }
            for (Detallecompra detallecompraListNewDetallecompra : detallecompraListNew) {
                if (!detallecompraListOld.contains(detallecompraListNewDetallecompra)) {
                    Ingredientes oldIdIngredienteOfDetallecompraListNewDetallecompra = detallecompraListNewDetallecompra.getIdIngrediente();
                    detallecompraListNewDetallecompra.setIdIngrediente(ingredientes);
                    detallecompraListNewDetallecompra = em.merge(detallecompraListNewDetallecompra);
                    if (oldIdIngredienteOfDetallecompraListNewDetallecompra != null && !oldIdIngredienteOfDetallecompraListNewDetallecompra.equals(ingredientes)) {
                        oldIdIngredienteOfDetallecompraListNewDetallecompra.getDetallecompraList().remove(detallecompraListNewDetallecompra);
                        oldIdIngredienteOfDetallecompraListNewDetallecompra = em.merge(oldIdIngredienteOfDetallecompraListNewDetallecompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingredientes.getIdIngrediente();
                if (findIngredientes(id) == null) {
                    throw new NonexistentEntityException("The ingredientes with id " + id + " no longer exists.");
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
            Ingredientes ingredientes;
            try {
                ingredientes = em.getReference(Ingredientes.class, id);
                ingredientes.getIdIngrediente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingredientes with id " + id + " no longer exists.", enfe);
            }
            Proveedores idProveedor = ingredientes.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getIngredientesList().remove(ingredientes);
                idProveedor = em.merge(idProveedor);
            }
            List<Productoingredientes> productoingredientesList = ingredientes.getProductoingredientesList();
            for (Productoingredientes productoingredientesListProductoingredientes : productoingredientesList) {
                productoingredientesListProductoingredientes.setIdIngrediente(null);
                productoingredientesListProductoingredientes = em.merge(productoingredientesListProductoingredientes);
            }
            List<Detallecompra> detallecompraList = ingredientes.getDetallecompraList();
            for (Detallecompra detallecompraListDetallecompra : detallecompraList) {
                detallecompraListDetallecompra.setIdIngrediente(null);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
            }
            em.remove(ingredientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingredientes> findIngredientesEntities() {
        return findIngredientesEntities(true, -1, -1);
    }

    public List<Ingredientes> findIngredientesEntities(int maxResults, int firstResult) {
        return findIngredientesEntities(false, maxResults, firstResult);
    }

    private List<Ingredientes> findIngredientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingredientes.class));
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

    public Ingredientes findIngredientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingredientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngredientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingredientes> rt = cq.from(Ingredientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
