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
import Entities.Cliente;
import Entities.Detallepedidos;
import Entities.Pedidos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gpera
 */
public class PedidosJpaController implements Serializable {

    public PedidosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedidos pedidos) throws PreexistingEntityException, Exception {
        if (pedidos.getDetallepedidosList() == null) {
            pedidos.setDetallepedidosList(new ArrayList<Detallepedidos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = pedidos.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                pedidos.setIdCliente(idCliente);
            }
            List<Detallepedidos> attachedDetallepedidosList = new ArrayList<Detallepedidos>();
            for (Detallepedidos detallepedidosListDetallepedidosToAttach : pedidos.getDetallepedidosList()) {
                detallepedidosListDetallepedidosToAttach = em.getReference(detallepedidosListDetallepedidosToAttach.getClass(), detallepedidosListDetallepedidosToAttach.getIdDetallePedidos());
                attachedDetallepedidosList.add(detallepedidosListDetallepedidosToAttach);
            }
            pedidos.setDetallepedidosList(attachedDetallepedidosList);
            em.persist(pedidos);
            if (idCliente != null) {
                idCliente.getPedidosList().add(pedidos);
                idCliente = em.merge(idCliente);
            }
            for (Detallepedidos detallepedidosListDetallepedidos : pedidos.getDetallepedidosList()) {
                Pedidos oldIdPedidoOfDetallepedidosListDetallepedidos = detallepedidosListDetallepedidos.getIdPedido();
                detallepedidosListDetallepedidos.setIdPedido(pedidos);
                detallepedidosListDetallepedidos = em.merge(detallepedidosListDetallepedidos);
                if (oldIdPedidoOfDetallepedidosListDetallepedidos != null) {
                    oldIdPedidoOfDetallepedidosListDetallepedidos.getDetallepedidosList().remove(detallepedidosListDetallepedidos);
                    oldIdPedidoOfDetallepedidosListDetallepedidos = em.merge(oldIdPedidoOfDetallepedidosListDetallepedidos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPedidos(pedidos.getIdPedidos()) != null) {
                throw new PreexistingEntityException("Pedidos " + pedidos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedidos pedidos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidos persistentPedidos = em.find(Pedidos.class, pedidos.getIdPedidos());
            Cliente idClienteOld = persistentPedidos.getIdCliente();
            Cliente idClienteNew = pedidos.getIdCliente();
            List<Detallepedidos> detallepedidosListOld = persistentPedidos.getDetallepedidosList();
            List<Detallepedidos> detallepedidosListNew = pedidos.getDetallepedidosList();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                pedidos.setIdCliente(idClienteNew);
            }
            List<Detallepedidos> attachedDetallepedidosListNew = new ArrayList<Detallepedidos>();
            for (Detallepedidos detallepedidosListNewDetallepedidosToAttach : detallepedidosListNew) {
                detallepedidosListNewDetallepedidosToAttach = em.getReference(detallepedidosListNewDetallepedidosToAttach.getClass(), detallepedidosListNewDetallepedidosToAttach.getIdDetallePedidos());
                attachedDetallepedidosListNew.add(detallepedidosListNewDetallepedidosToAttach);
            }
            detallepedidosListNew = attachedDetallepedidosListNew;
            pedidos.setDetallepedidosList(detallepedidosListNew);
            pedidos = em.merge(pedidos);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getPedidosList().remove(pedidos);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getPedidosList().add(pedidos);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Detallepedidos detallepedidosListOldDetallepedidos : detallepedidosListOld) {
                if (!detallepedidosListNew.contains(detallepedidosListOldDetallepedidos)) {
                    detallepedidosListOldDetallepedidos.setIdPedido(null);
                    detallepedidosListOldDetallepedidos = em.merge(detallepedidosListOldDetallepedidos);
                }
            }
            for (Detallepedidos detallepedidosListNewDetallepedidos : detallepedidosListNew) {
                if (!detallepedidosListOld.contains(detallepedidosListNewDetallepedidos)) {
                    Pedidos oldIdPedidoOfDetallepedidosListNewDetallepedidos = detallepedidosListNewDetallepedidos.getIdPedido();
                    detallepedidosListNewDetallepedidos.setIdPedido(pedidos);
                    detallepedidosListNewDetallepedidos = em.merge(detallepedidosListNewDetallepedidos);
                    if (oldIdPedidoOfDetallepedidosListNewDetallepedidos != null && !oldIdPedidoOfDetallepedidosListNewDetallepedidos.equals(pedidos)) {
                        oldIdPedidoOfDetallepedidosListNewDetallepedidos.getDetallepedidosList().remove(detallepedidosListNewDetallepedidos);
                        oldIdPedidoOfDetallepedidosListNewDetallepedidos = em.merge(oldIdPedidoOfDetallepedidosListNewDetallepedidos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidos.getIdPedidos();
                if (findPedidos(id) == null) {
                    throw new NonexistentEntityException("The pedidos with id " + id + " no longer exists.");
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
            Pedidos pedidos;
            try {
                pedidos = em.getReference(Pedidos.class, id);
                pedidos.getIdPedidos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidos with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = pedidos.getIdCliente();
            if (idCliente != null) {
                idCliente.getPedidosList().remove(pedidos);
                idCliente = em.merge(idCliente);
            }
            List<Detallepedidos> detallepedidosList = pedidos.getDetallepedidosList();
            for (Detallepedidos detallepedidosListDetallepedidos : detallepedidosList) {
                detallepedidosListDetallepedidos.setIdPedido(null);
                detallepedidosListDetallepedidos = em.merge(detallepedidosListDetallepedidos);
            }
            em.remove(pedidos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedidos> findPedidosEntities() {
        return findPedidosEntities(true, -1, -1);
    }

    public List<Pedidos> findPedidosEntities(int maxResults, int firstResult) {
        return findPedidosEntities(false, maxResults, firstResult);
    }

    private List<Pedidos> findPedidosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedidos.class));
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

    public Pedidos findPedidos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedidos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedidos> rt = cq.from(Pedidos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
