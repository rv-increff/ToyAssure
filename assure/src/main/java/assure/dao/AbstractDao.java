package assure.dao;

import assure.pojo.ProductPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
@Repository
public abstract class AbstractDao <T> {
	
	@PersistenceContext
	protected EntityManager em;

	Class<T> clazz;
	public AbstractDao(){
		this.clazz = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public <T> T add(T pojoObject){
		em.persist(pojoObject);
		return pojoObject;
	}
	public <T> List<T> select(Integer pageNumber, Integer pageSize){
		CriteriaQuery<T> cr = cr();
		Root<T> root = (Root<T>) cr.from(this.clazz);
		cr.select(root);

		TypedQuery<T> query =  em.createQuery(cr);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);
		List<T> results = query.getResultList();
		return results;
	}
//	public <T> TypedQuery<T> selectIn( List<?> inList, String columnName){
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<T> cr = cr();
//		Root<T> root = (Root<T>) cr.from(this.clazz);
//		cr  = cr.select(root).where(root.get(columnName).in(inList));
//		TypedQuery<T> query =  em.createQuery(cr);
//		return query;
//	}

	public void update(){}

	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected <T> CriteriaQuery cr(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cr = (CriteriaQuery<T>) cb.createQuery(this.clazz);
		return cr;
	}

}
