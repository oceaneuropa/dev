package co.edu.uniandes.nosqljpa.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public class Persistencer<T, PK> {

	private static final Logger LOG = Logger.getLogger(Persistencer.class.getName());
	protected Class<T> entityClass;
	protected EntityManager entityManager;

	public Persistencer() {
		this.entityManager = JPAConnection.CONNECTION.getEntityManager();
	}

	public T add(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entity;
	}

	public T update(T entity) {
		try {
			entityManager.merge(entity);
		} catch (Exception e) {
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entity;
	}

	public T find(PK id) {
		T entity;
		try {
			entity = entityManager.find(entityClass, id);
		} catch (NoResultException | NonUniqueResultException e) {
			entity = null;
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entity;
	}

	public List<T> all() {
		List<T> entities;
		String queryString = "Select c FROM " + entityClass.getSimpleName() + " c";
		Query query = entityManager.createQuery(queryString);
		try {
			entities = query.getResultList();
		} catch (NoResultException | NonUniqueResultException e) {
			entities = null;
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entities;
	}

	public Boolean delete(PK id) {
		try {
			T entity = entityManager.find(entityClass, id);
			entityManager.getTransaction().begin();
			this.entityManager.remove(entity);
			entityManager.getTransaction().commit();
			return true;
		} catch (RuntimeException e) {
			LOG.log(Level.WARNING, e.getMessage());
			return false;
		}
	}

	public T findCode(String code) {
		T entity;
		String queryString = "Select c FROM " + entityClass.getSimpleName() + " c where c.code = :code1";
		Query query = entityManager.createQuery(queryString).setParameter("code1", code);
		try {
			entity = (T) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			entity = null;
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entity;
	}

	public List<T> findBySensorId(String id) {
		List<T> entities;
		String queryString = "Select c FROM " + entityClass.getSimpleName() + " c where c.idSensor = :id1";
		Query query = entityManager.createQuery(queryString).setParameter("id1", id);
		try {
			entities = query.getResultList();
		} catch (NoResultException | NonUniqueResultException e) {
			entities = null;
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entities;
	}

	public List<T> findByRoomId(String id) {
		List<T> entities;
		String queryString = "Select c FROM " + entityClass.getSimpleName() + " c where c.roomID = :id1";
		Query query = entityManager.createQuery(queryString).setParameter("id1", id);
		try {
			entities = query.getResultList();
		} catch (NoResultException | NonUniqueResultException e) {
			entities = null;
			LOG.log(Level.WARNING, e.getMessage());
		}
		return entities;
	}

}
