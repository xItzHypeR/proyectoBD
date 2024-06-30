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
import Entities.Ingredientesprincipales;
import Entities.Detallepedidos;
import java.util.ArrayList;
import java.util.List;
import Entities.Productoingredientes;
import Entities.Detalleventa;
import Entities.Empleados;
import Entities.Inventarios;
import Entities.Productos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author gpera
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public ProductosJpaController() {
        emf = Persistence.createEntityManagerFactory("MedicinaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) throws PreexistingEntityException, Exception {
        if (productos.getDetallepedidosList() == null) {
            productos.setDetallepedidosList(new ArrayList<Detallepedidos>());
        }
        if (productos.getProductoingredientesList() == null) {
            productos.setProductoingredientesList(new ArrayList<Productoingredientes>());
        }
        if (productos.getDetalleventaList() == null) {
            productos.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        if (productos.getInventariosList() == null) {
            productos.setInventariosList(new ArrayList<Inventarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredientesprincipales idIngredientePrincipal = productos.getIdIngredientePrincipal();
            if (idIngredientePrincipal != null) {
                idIngredientePrincipal = em.getReference(idIngredientePrincipal.getClass(),
                        idIngredientePrincipal.getIdIngredientePrincipal());
                productos.setIdIngredientePrincipal(idIngredientePrincipal);
            }
            List<Detallepedidos> attachedDetallepedidosList = new ArrayList<Detallepedidos>();
            for (Detallepedidos detallepedidosListDetallepedidosToAttach : productos.getDetallepedidosList()) {
                detallepedidosListDetallepedidosToAttach = em.getReference(
                        detallepedidosListDetallepedidosToAttach.getClass(),
                        detallepedidosListDetallepedidosToAttach.getIdDetallePedidos());
                attachedDetallepedidosList.add(detallepedidosListDetallepedidosToAttach);
            }
            productos.setDetallepedidosList(attachedDetallepedidosList);
            List<Productoingredientes> attachedProductoingredientesList = new ArrayList<Productoingredientes>();
            for (Productoingredientes productoingredientesListProductoingredientesToAttach : productos
                    .getProductoingredientesList()) {
                productoingredientesListProductoingredientesToAttach = em.getReference(
                        productoingredientesListProductoingredientesToAttach.getClass(),
                        productoingredientesListProductoingredientesToAttach.getIdProductoIngrediente());
                attachedProductoingredientesList.add(productoingredientesListProductoingredientesToAttach);
            }
            productos.setProductoingredientesList(attachedProductoingredientesList);
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : productos.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(),
                        detalleventaListDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            productos.setDetalleventaList(attachedDetalleventaList);
            List<Inventarios> attachedInventariosList = new ArrayList<Inventarios>();
            for (Inventarios inventariosListInventariosToAttach : productos.getInventariosList()) {
                inventariosListInventariosToAttach = em.getReference(inventariosListInventariosToAttach.getClass(),
                        inventariosListInventariosToAttach.getIdInventario());
                attachedInventariosList.add(inventariosListInventariosToAttach);
            }
            productos.setInventariosList(attachedInventariosList);
            em.persist(productos);
            if (idIngredientePrincipal != null) {
                idIngredientePrincipal.getProductosList().add(productos);
                idIngredientePrincipal = em.merge(idIngredientePrincipal);
            }
            for (Detallepedidos detallepedidosListDetallepedidos : productos.getDetallepedidosList()) {
                Productos oldIdProductoOfDetallepedidosListDetallepedidos = detallepedidosListDetallepedidos
                        .getIdProducto();
                detallepedidosListDetallepedidos.setIdProducto(productos);
                detallepedidosListDetallepedidos = em.merge(detallepedidosListDetallepedidos);
                if (oldIdProductoOfDetallepedidosListDetallepedidos != null) {
                    oldIdProductoOfDetallepedidosListDetallepedidos.getDetallepedidosList()
                            .remove(detallepedidosListDetallepedidos);
                    oldIdProductoOfDetallepedidosListDetallepedidos = em
                            .merge(oldIdProductoOfDetallepedidosListDetallepedidos);
                }
            }
            for (Productoingredientes productoingredientesListProductoingredientes : productos
                    .getProductoingredientesList()) {
                Productos oldIdProductoOfProductoingredientesListProductoingredientes = productoingredientesListProductoingredientes
                        .getIdProducto();
                productoingredientesListProductoingredientes.setIdProducto(productos);
                productoingredientesListProductoingredientes = em.merge(productoingredientesListProductoingredientes);
                if (oldIdProductoOfProductoingredientesListProductoingredientes != null) {
                    oldIdProductoOfProductoingredientesListProductoingredientes.getProductoingredientesList()
                            .remove(productoingredientesListProductoingredientes);
                    oldIdProductoOfProductoingredientesListProductoingredientes = em
                            .merge(oldIdProductoOfProductoingredientesListProductoingredientes);
                }
            }
            for (Detalleventa detalleventaListDetalleventa : productos.getDetalleventaList()) {
                Productos oldIdProductoOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getIdProducto();
                detalleventaListDetalleventa.setIdProducto(productos);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldIdProductoOfDetalleventaListDetalleventa != null) {
                    oldIdProductoOfDetalleventaListDetalleventa.getDetalleventaList()
                            .remove(detalleventaListDetalleventa);
                    oldIdProductoOfDetalleventaListDetalleventa = em.merge(oldIdProductoOfDetalleventaListDetalleventa);
                }
            }
            for (Inventarios inventariosListInventarios : productos.getInventariosList()) {
                Productos oldIdProductoOfInventariosListInventarios = inventariosListInventarios.getIdProducto();
                inventariosListInventarios.setIdProducto(productos);
                inventariosListInventarios = em.merge(inventariosListInventarios);
                if (oldIdProductoOfInventariosListInventarios != null) {
                    oldIdProductoOfInventariosListInventarios.getInventariosList().remove(inventariosListInventarios);
                    oldIdProductoOfInventariosListInventarios = em.merge(oldIdProductoOfInventariosListInventarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductos(productos.getIdProducto()) != null) {
                throw new PreexistingEntityException("Productos " + productos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getIdProducto());
            Ingredientesprincipales idIngredientePrincipalOld = persistentProductos.getIdIngredientePrincipal();
            Ingredientesprincipales idIngredientePrincipalNew = productos.getIdIngredientePrincipal();
            List<Detallepedidos> detallepedidosListOld = persistentProductos.getDetallepedidosList();
            List<Detallepedidos> detallepedidosListNew = productos.getDetallepedidosList();
            List<Productoingredientes> productoingredientesListOld = persistentProductos.getProductoingredientesList();
            List<Productoingredientes> productoingredientesListNew = productos.getProductoingredientesList();
            List<Detalleventa> detalleventaListOld = persistentProductos.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = productos.getDetalleventaList();
            List<Inventarios> inventariosListOld = persistentProductos.getInventariosList();
            List<Inventarios> inventariosListNew = productos.getInventariosList();
            if (idIngredientePrincipalNew != null) {
                idIngredientePrincipalNew = em.getReference(idIngredientePrincipalNew.getClass(),
                        idIngredientePrincipalNew.getIdIngredientePrincipal());
                productos.setIdIngredientePrincipal(idIngredientePrincipalNew);
            }
            List<Detallepedidos> attachedDetallepedidosListNew = new ArrayList<Detallepedidos>();
            for (Detallepedidos detallepedidosListNewDetallepedidosToAttach : detallepedidosListNew) {
                detallepedidosListNewDetallepedidosToAttach = em.getReference(
                        detallepedidosListNewDetallepedidosToAttach.getClass(),
                        detallepedidosListNewDetallepedidosToAttach.getIdDetallePedidos());
                attachedDetallepedidosListNew.add(detallepedidosListNewDetallepedidosToAttach);
            }
            detallepedidosListNew = attachedDetallepedidosListNew;
            productos.setDetallepedidosList(detallepedidosListNew);
            List<Productoingredientes> attachedProductoingredientesListNew = new ArrayList<Productoingredientes>();
            for (Productoingredientes productoingredientesListNewProductoingredientesToAttach : productoingredientesListNew) {
                productoingredientesListNewProductoingredientesToAttach = em.getReference(
                        productoingredientesListNewProductoingredientesToAttach.getClass(),
                        productoingredientesListNewProductoingredientesToAttach.getIdProductoIngrediente());
                attachedProductoingredientesListNew.add(productoingredientesListNewProductoingredientesToAttach);
            }
            productoingredientesListNew = attachedProductoingredientesListNew;
            productos.setProductoingredientesList(productoingredientesListNew);
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(
                        detalleventaListNewDetalleventaToAttach.getClass(),
                        detalleventaListNewDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            productos.setDetalleventaList(detalleventaListNew);
            List<Inventarios> attachedInventariosListNew = new ArrayList<Inventarios>();
            for (Inventarios inventariosListNewInventariosToAttach : inventariosListNew) {
                inventariosListNewInventariosToAttach = em.getReference(
                        inventariosListNewInventariosToAttach.getClass(),
                        inventariosListNewInventariosToAttach.getIdInventario());
                attachedInventariosListNew.add(inventariosListNewInventariosToAttach);
            }
            inventariosListNew = attachedInventariosListNew;
            productos.setInventariosList(inventariosListNew);
            productos = em.merge(productos);
            if (idIngredientePrincipalOld != null && !idIngredientePrincipalOld.equals(idIngredientePrincipalNew)) {
                idIngredientePrincipalOld.getProductosList().remove(productos);
                idIngredientePrincipalOld = em.merge(idIngredientePrincipalOld);
            }
            if (idIngredientePrincipalNew != null && !idIngredientePrincipalNew.equals(idIngredientePrincipalOld)) {
                idIngredientePrincipalNew.getProductosList().add(productos);
                idIngredientePrincipalNew = em.merge(idIngredientePrincipalNew);
            }
            for (Detallepedidos detallepedidosListOldDetallepedidos : detallepedidosListOld) {
                if (!detallepedidosListNew.contains(detallepedidosListOldDetallepedidos)) {
                    detallepedidosListOldDetallepedidos.setIdProducto(null);
                    detallepedidosListOldDetallepedidos = em.merge(detallepedidosListOldDetallepedidos);
                }
            }
            for (Detallepedidos detallepedidosListNewDetallepedidos : detallepedidosListNew) {
                if (!detallepedidosListOld.contains(detallepedidosListNewDetallepedidos)) {
                    Productos oldIdProductoOfDetallepedidosListNewDetallepedidos = detallepedidosListNewDetallepedidos
                            .getIdProducto();
                    detallepedidosListNewDetallepedidos.setIdProducto(productos);
                    detallepedidosListNewDetallepedidos = em.merge(detallepedidosListNewDetallepedidos);
                    if (oldIdProductoOfDetallepedidosListNewDetallepedidos != null
                            && !oldIdProductoOfDetallepedidosListNewDetallepedidos.equals(productos)) {
                        oldIdProductoOfDetallepedidosListNewDetallepedidos.getDetallepedidosList()
                                .remove(detallepedidosListNewDetallepedidos);
                        oldIdProductoOfDetallepedidosListNewDetallepedidos = em
                                .merge(oldIdProductoOfDetallepedidosListNewDetallepedidos);
                    }
                }
            }
            for (Productoingredientes productoingredientesListOldProductoingredientes : productoingredientesListOld) {
                if (!productoingredientesListNew.contains(productoingredientesListOldProductoingredientes)) {
                    productoingredientesListOldProductoingredientes.setIdProducto(null);
                    productoingredientesListOldProductoingredientes = em
                            .merge(productoingredientesListOldProductoingredientes);
                }
            }
            for (Productoingredientes productoingredientesListNewProductoingredientes : productoingredientesListNew) {
                if (!productoingredientesListOld.contains(productoingredientesListNewProductoingredientes)) {
                    Productos oldIdProductoOfProductoingredientesListNewProductoingredientes = productoingredientesListNewProductoingredientes
                            .getIdProducto();
                    productoingredientesListNewProductoingredientes.setIdProducto(productos);
                    productoingredientesListNewProductoingredientes = em
                            .merge(productoingredientesListNewProductoingredientes);
                    if (oldIdProductoOfProductoingredientesListNewProductoingredientes != null
                            && !oldIdProductoOfProductoingredientesListNewProductoingredientes.equals(productos)) {
                        oldIdProductoOfProductoingredientesListNewProductoingredientes.getProductoingredientesList()
                                .remove(productoingredientesListNewProductoingredientes);
                        oldIdProductoOfProductoingredientesListNewProductoingredientes = em
                                .merge(oldIdProductoOfProductoingredientesListNewProductoingredientes);
                    }
                }
            }
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    detalleventaListOldDetalleventa.setIdProducto(null);
                    detalleventaListOldDetalleventa = em.merge(detalleventaListOldDetalleventa);
                }
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Productos oldIdProductoOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa
                            .getIdProducto();
                    detalleventaListNewDetalleventa.setIdProducto(productos);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldIdProductoOfDetalleventaListNewDetalleventa != null
                            && !oldIdProductoOfDetalleventaListNewDetalleventa.equals(productos)) {
                        oldIdProductoOfDetalleventaListNewDetalleventa.getDetalleventaList()
                                .remove(detalleventaListNewDetalleventa);
                        oldIdProductoOfDetalleventaListNewDetalleventa = em
                                .merge(oldIdProductoOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            for (Inventarios inventariosListOldInventarios : inventariosListOld) {
                if (!inventariosListNew.contains(inventariosListOldInventarios)) {
                    inventariosListOldInventarios.setIdProducto(null);
                    inventariosListOldInventarios = em.merge(inventariosListOldInventarios);
                }
            }
            for (Inventarios inventariosListNewInventarios : inventariosListNew) {
                if (!inventariosListOld.contains(inventariosListNewInventarios)) {
                    Productos oldIdProductoOfInventariosListNewInventarios = inventariosListNewInventarios
                            .getIdProducto();
                    inventariosListNewInventarios.setIdProducto(productos);
                    inventariosListNewInventarios = em.merge(inventariosListNewInventarios);
                    if (oldIdProductoOfInventariosListNewInventarios != null
                            && !oldIdProductoOfInventariosListNewInventarios.equals(productos)) {
                        oldIdProductoOfInventariosListNewInventarios.getInventariosList()
                                .remove(inventariosListNewInventarios);
                        oldIdProductoOfInventariosListNewInventarios = em
                                .merge(oldIdProductoOfInventariosListNewInventarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productos.getIdProducto();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
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
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            Ingredientesprincipales idIngredientePrincipal = productos.getIdIngredientePrincipal();
            if (idIngredientePrincipal != null) {
                idIngredientePrincipal.getProductosList().remove(productos);
                idIngredientePrincipal = em.merge(idIngredientePrincipal);
            }
            List<Detallepedidos> detallepedidosList = productos.getDetallepedidosList();
            for (Detallepedidos detallepedidosListDetallepedidos : detallepedidosList) {
                detallepedidosListDetallepedidos.setIdProducto(null);
                detallepedidosListDetallepedidos = em.merge(detallepedidosListDetallepedidos);
            }
            List<Productoingredientes> productoingredientesList = productos.getProductoingredientesList();
            for (Productoingredientes productoingredientesListProductoingredientes : productoingredientesList) {
                productoingredientesListProductoingredientes.setIdProducto(null);
                productoingredientesListProductoingredientes = em.merge(productoingredientesListProductoingredientes);
            }
            List<Detalleventa> detalleventaList = productos.getDetalleventaList();
            for (Detalleventa detalleventaListDetalleventa : detalleventaList) {
                detalleventaListDetalleventa.setIdProducto(null);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
            }
            List<Inventarios> inventariosList = productos.getInventariosList();
            for (Inventarios inventariosListInventarios : inventariosList) {
                inventariosListInventarios.setIdProducto(null);
                inventariosListInventarios = em.merge(inventariosListInventarios);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Integer> findProductosByIngredientePrincipal(Productos producto) {
        EntityManager em = getEntityManager();
        try {
            Query query = em
                    .createQuery("SELECT p.idIngredientePrincipal FROM Productos p WHERE p.idProducto = :idProducto");
            query.setParameter("idProducto", producto.getIdProducto());
            return query.getResultList();

        } finally {
            em.close();
        }

    }

    public List<Productos> findProductosByProducto(Productos producto) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p.idProducto FROM Productos p WHERE p.idProducto = :idProducto");
            query.setParameter("idProducto", producto.getIdProducto());
            return query.getResultList();
        } finally {
            em.close();
        }
        
    }
}
