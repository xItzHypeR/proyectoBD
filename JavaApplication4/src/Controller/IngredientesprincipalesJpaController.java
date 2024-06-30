/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entities.Ingredientesprincipales;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Productos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author gpera
 */
public class IngredientesprincipalesJpaController implements Serializable {

    public IngredientesprincipalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public IngredientesprincipalesJpaController() {
        emf = Persistence.createEntityManagerFactory("MedicinaPU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingredientesprincipales ingredientesprincipales) throws PreexistingEntityException, Exception {
        if (ingredientesprincipales.getProductosList() == null) {
            ingredientesprincipales.setProductosList(new ArrayList<Productos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Productos> attachedProductosList = new ArrayList<Productos>();
            for (Productos productosListProductosToAttach : ingredientesprincipales.getProductosList()) {
                productosListProductosToAttach = em.getReference(productosListProductosToAttach.getClass(), productosListProductosToAttach.getIdProducto());
                attachedProductosList.add(productosListProductosToAttach);
            }
            ingredientesprincipales.setProductosList(attachedProductosList);
            em.persist(ingredientesprincipales);
            for (Productos productosListProductos : ingredientesprincipales.getProductosList()) {
                Ingredientesprincipales oldIdIngredientePrincipalOfProductosListProductos = productosListProductos.getIdIngredientePrincipal();
                productosListProductos.setIdIngredientePrincipal(ingredientesprincipales);
                productosListProductos = em.merge(productosListProductos);
                if (oldIdIngredientePrincipalOfProductosListProductos != null) {
                    oldIdIngredientePrincipalOfProductosListProductos.getProductosList().remove(productosListProductos);
                    oldIdIngredientePrincipalOfProductosListProductos = em.merge(oldIdIngredientePrincipalOfProductosListProductos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngredientesprincipales(ingredientesprincipales.getIdIngredientePrincipal()) != null) {
                throw new PreexistingEntityException("Ingredientesprincipales " + ingredientesprincipales + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingredientesprincipales ingredientesprincipales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredientesprincipales persistentIngredientesprincipales = em.find(Ingredientesprincipales.class, ingredientesprincipales.getIdIngredientePrincipal());
            List<Productos> productosListOld = persistentIngredientesprincipales.getProductosList();
            List<Productos> productosListNew = ingredientesprincipales.getProductosList();
            List<Productos> attachedProductosListNew = new ArrayList<Productos>();
            for (Productos productosListNewProductosToAttach : productosListNew) {
                productosListNewProductosToAttach = em.getReference(productosListNewProductosToAttach.getClass(), productosListNewProductosToAttach.getIdProducto());
                attachedProductosListNew.add(productosListNewProductosToAttach);
            }
            productosListNew = attachedProductosListNew;
            ingredientesprincipales.setProductosList(productosListNew);
            ingredientesprincipales = em.merge(ingredientesprincipales);
            for (Productos productosListOldProductos : productosListOld) {
                if (!productosListNew.contains(productosListOldProductos)) {
                    productosListOldProductos.setIdIngredientePrincipal(null);
                    productosListOldProductos = em.merge(productosListOldProductos);
                }
            }
            for (Productos productosListNewProductos : productosListNew) {
                if (!productosListOld.contains(productosListNewProductos)) {
                    Ingredientesprincipales oldIdIngredientePrincipalOfProductosListNewProductos = productosListNewProductos.getIdIngredientePrincipal();
                    productosListNewProductos.setIdIngredientePrincipal(ingredientesprincipales);
                    productosListNewProductos = em.merge(productosListNewProductos);
                    if (oldIdIngredientePrincipalOfProductosListNewProductos != null && !oldIdIngredientePrincipalOfProductosListNewProductos.equals(ingredientesprincipales)) {
                        oldIdIngredientePrincipalOfProductosListNewProductos.getProductosList().remove(productosListNewProductos);
                        oldIdIngredientePrincipalOfProductosListNewProductos = em.merge(oldIdIngredientePrincipalOfProductosListNewProductos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingredientesprincipales.getIdIngredientePrincipal();
                if (findIngredientesprincipales(id) == null) {
                    throw new NonexistentEntityException("The ingredientesprincipales with id " + id + " no longer exists.");
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
            Ingredientesprincipales ingredientesprincipales;
            try {
                ingredientesprincipales = em.getReference(Ingredientesprincipales.class, id);
                ingredientesprincipales.getIdIngredientePrincipal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingredientesprincipales with id " + id + " no longer exists.", enfe);
            }
            List<Productos> productosList = ingredientesprincipales.getProductosList();
            for (Productos productosListProductos : productosList) {
                productosListProductos.setIdIngredientePrincipal(null);
                productosListProductos = em.merge(productosListProductos);
            }
            em.remove(ingredientesprincipales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Integer> findIngredientesprincipalesEntities(Productos producto) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p.idIngredientePrincipal FROM Productos p WHERE p.idProducto = :idProducto");
            query.setParameter("idProducto", producto.getIdProducto());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ingredientesprincipales> findIngredientesprincipalesEntities(int maxResults, int firstResult) {
        return findIngredientesprincipalesEntities(false, maxResults, firstResult);
    }

    private List<Ingredientesprincipales> findIngredientesprincipalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingredientesprincipales.class));
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

    public Ingredientesprincipales findIngredientesprincipales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingredientesprincipales.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngredientesprincipalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingredientesprincipales> rt = cq.from(Ingredientesprincipales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
