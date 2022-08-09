package assure.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDao {
	
	@PersistenceContext
	protected EntityManager em;

	public <T> void add(T pojoObject){
		em.persist(pojoObject);
	}
	public <T> List<T> select(Class<T> pojoClass, Integer pageNumber, Integer pageSize){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(pojoClass);
		Root<T> root = cr.from(pojoClass);
		cr.select(root);

		TypedQuery<T> query =  em.createQuery(cr);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);

		List<T> results = query.getResultList();
		return results;
	}
	public <T> T selectById(Class<T> pojo,Long id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(pojo);
		Root<T> root = cr.from(pojo);
		cr.select(root).where(cb.equal(root.get("id"), id));

		TypedQuery<T> query =  em.createQuery(cr);
		return getSingle(query);

	}
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected EntityManager em() {
		return em;
	}

}
