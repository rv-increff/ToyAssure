package assure.dao;

import assure.pojo.ProductPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
@Repository
public abstract class AbstractDao {
	
	@PersistenceContext
	protected EntityManager em;

	public <T> void add(T pojoObject){
		em.persist(pojoObject);
	}
	public <T> List<T> select(Class<T> pojoClass, Integer pageNumber, Integer pageSize){
		CriteriaQuery<T> cr = cr(pojoClass);
		Root<T> root = cr.from(pojoClass);
		cr.select(root);

		TypedQuery<T> query =  em.createQuery(cr);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);

		List<T> results = query.getResultList();
		return results;
	}
	public <T> T selectById(Class<T> pojoClass,Long id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cr = cr(pojoClass);
		Root<T> root = cr.from(pojoClass);
		cr.select(root).where(cb.equal(root.get("id"), id));

		TypedQuery<T> query =  em.createQuery(cr);
		return query.getSingleResult();

	}
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected EntityManager em() {
		return em;
	}

	protected <T> CriteriaQuery cr(Class<T> pojoClass){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(pojoClass);
		return cr;
	}

	protected <T> TypedQuery<T> select(Class<T> pojoClass, List<Pair> whereList){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cr = cr(pojoClass);
		Root<ProductPojo> root = cr.from(pojoClass);
		cr  = cr.select(root);
		for( Pair where : whereList){
			String fieldName = where.getKey().toString();
			Object filedValue = where.getValue();
			cr.where(cb.equal(root.get(fieldName), filedValue));
		}
		TypedQuery<T> query =  em.createQuery(cr);
		return query;
	}


}
