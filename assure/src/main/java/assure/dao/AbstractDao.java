package assure.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;

    Class<T> clazz;

    public AbstractDao() {
        this.clazz = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public <T> T add(T pojoObject) {
        em.persist(pojoObject);
        return pojoObject;
    }

    public List<T> select(Integer pageNumber, Integer pageSize) {
        CriteriaQuery<T> criteriaQuery = criteriaQuery();
        Root<T> root = criteriaQuery.from(this.clazz);
        criteriaQuery.select(root);
        TypedQuery<T> query = em.createQuery(criteriaQuery);
        //TODO add sorting by id  (sortBY,sortType(asc,desc), pageNumber,pageSize)
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public List<T> select() {
        CriteriaQuery<T> criteriaQuery = criteriaQuery();
        Root<T> root = criteriaQuery.from(this.clazz);
        criteriaQuery.select(root);
        TypedQuery<T> query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    public void update() {
    }

    protected T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected CriteriaQuery criteriaQuery() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(this.clazz);
        return criteriaQuery;
    }

}
