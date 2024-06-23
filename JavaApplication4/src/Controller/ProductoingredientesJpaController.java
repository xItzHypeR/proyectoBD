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
import Entities.Productoingredientes;
import Entities.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class ProductoingredientesJpaController implements Serializable {

    public ProductoingredientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productoingredientes productoingredientes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredientes idIngrediente = productoingredientes.getIdIngrediente();
            if (idIngrediente != null) {
                idIngrediente = em.getReference(idIngrediente.getClass(), idIngrediente.getIdIngrediente());
                productoingredientes.setIdIngrediente(idIngrediente);
            }
            Productos idProducto = productoingredientes.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                productoingredientes.setIdProducto(idProducto);
            }
            em.persist(productoingredientes);
            if (idIngrediente != null) {
                idIngrediente.getProductoingredientesList().add(productoingredientes);
                idIngrediente = em.merge(idIngrediente);
            }
            if (idProducto != null) {
                idProducto.getProductoingredientesList().add(productoingredientes);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductoingredientes(productoingredientes.getIdProductoIngrediente()) != null) {
                throw new PreexistingEntityException("Productoingredientes " + productoingredientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productoingredientes productoingredientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productoingredientes persistentProductoingredientes = em.find(Productoingredientes.class, productoingredientes.getIdProductoIngrediente());
            Ingredientes idIngredienteOld = persistentProductoingredientes.getIdIngrediente();
            Ingredientes idIngredienteNew = productoingredientes.getIdIngrediente();
            Productos idProductoOld = persistentProductoingredientes.getIdProducto();
            Productos idProductoNew = productoingredientes.getIdProducto();
            if (idIngredienteNew != null) {
                idIngredienteNew = em.getReference(idIngredienteNew.getClass(), idIngredienteNew.getIdIngrediente());
                productoingredientes.setIdIngrediente(idIngredienteNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                productoingredientes.setIdProducto(idProductoNew);
            }
            productoingredientes = em.merge(productoingredientes);
            if (idIngredienteOld != null && !idIngredienteOld.equals(idIngredienteNew)) {
                idIngredienteOld.getProductoingredientesList().remove(productoingredientes);
                idIngredienteOld = em.merge(idIngredienteOld);
            }
            if (idIngredienteNew != null && !idIngredienteNew.equals(idIngredienteOld)) {
                idIngredienteNew.getProductoingredientesList().add(productoingredientes);
                idIngredienteNew = em.merge(idIngredienteNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getProductoingredientesList().remove(productoingredientes);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getProductoingredientesList().add(productoingredientes);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productoingredientes.getIdProductoIngrediente();
                if (findProductoingredientes(id) == null) {
                    throw new NonexistentEntityException("The productoingredientes with id " + id + " no longer exists.");
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
            Productoingredientes productoingredientes;
            try {
                productoingredientes = em.getReference(Productoingredientes.class, id);
                productoingredientes.getIdProductoIngrediente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoingredientes with id " + id + " no longer exists.", enfe);
            }
            Ingredientes idIngrediente = productoingredientes.getIdIngrediente();
            if (idIngrediente != null) {
                idIngrediente.getProductoingredientesList().remove(productoingredientes);
                idIngrediente = em.merge(idIngrediente);
            }
            Productos idProducto = productoingredientes.getIdProducto();
            if (idProducto != null) {
                idProducto.getProductoingredientesList().remove(productoingredientes);
                idProducto = em.merge(idProducto);
            }
            em.remove(productoingredientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productoingredientes> findProductoingredientesEntities() {
        return findProductoingredientesEntities(true, -1, -1);
    }

    public List<Productoingredientes> findProductoingredientesEntities(int maxResults, int firstResult) {
        return findProductoingredientesEntities(false, maxResults, firstResult);
    }

    private List<Productoingredientes> findProductoingredientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productoingredientes.class));
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

    public Productoingredientes findProductoingredientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productoingredientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoingredientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productoingredientes> rt = cq.from(Productoingredientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
