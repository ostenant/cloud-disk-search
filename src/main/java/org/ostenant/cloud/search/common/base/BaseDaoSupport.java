package org.ostenant.cloud.search.common.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * 实现BaseDAO基本方法
 * 
 * @author Administrator
 * 
 * @param <T>
 */
@SuppressWarnings("all")
public abstract class BaseDaoSupport<T> implements BaseDao<T> {

	private SessionFactory sessionFactory = null;
	protected Class<T> clazz = null;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public BaseDaoSupport() {
		// -- 子类继承父类无参构造函数,该构造方法在子类实例化时被调用
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		// -- 返回表示此类型实际类型参数的 Type 对象的数组。
		Type[] types = pt.getActualTypeArguments();
		// -- 此处只有一个参数<T>
		this.clazz = (Class<T>) types[0];
	}

	/**
	 * 获取hibernate session
	 * 
	 * @return
	 */
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void save(T o) {
		getSession().save(o);
	}

	@Override
	public void delete(T o) {
		getSession().delete(o);
	}

	@Override
	public void deleteById(Serializable id) {
		T o = (T) getSession().get(clazz, id);
		getSession().delete(o);
	}

	@Override
	public void update(T o) {
		getSession().update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		getSession().saveOrUpdate(o);
	}

	@Override
	public T queryById(Serializable id) {
		return (id != null) ? (T) getSession().createQuery(//
				"FROM " + clazz.getSimpleName() + " t WEHRE t.id = ?")//
				.setParameter(0, id)//
				.uniqueResult() : null;
	}

	@Override
	public List<T> queryAll() {
		return getSession().createQuery("FROM " + clazz.getSimpleName()).list();
	}

	@Override
	public List<T> querySome(Serializable[] ids) {
		if (ids == null || ids.length <= 0) {
			return null;
		}

		return (List<T>) getSession().createQuery(//
				"FROM " + clazz.getSimpleName() + " WHERE id in (:ids)")//
				.setParameterList("ids", ids)//
				.list();
	}

	@Override
	public List<T> queryPageByHQL(String hql, Object[] params, Integer page,
			Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = getSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return (List<T>) q.setFirstResult((page - 1) * rows)//
				.setMaxResults(rows)//
				.list();
	}

	@Override
	public List<T> queryPageByHQL(String hql, List<Object> params,
			Integer page, Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = getSession().createQuery(hql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
		}
		return (List<T>) q.setFirstResult((page - 1) * rows)//
				.setMaxResults(rows)//
				.list();
	}

	@Override
	public T get(Class<T> c, Serializable id) {
		return id != null ? //
		(T) getSession().get(clazz, id)//
				: null;
	}

	@Override
	public List<T> queryByHQL(String hql) {
		return (List<T>) getSession().createQuery(hql).list();
	}

	@Override
	public List<T> queryByHQL(String hql, Object[] params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return (List<T>) q.list();
	}

	@Override
	public List<T> queryByHQL(String hql, List<Object> params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
		}
		return (List<T>) q.list();
	}

	@Override
	public Long count(String hql) {
		return (Long) getSession().createQuery(hql)//
				.uniqueResult();
	}

	@Override
	public Long count(String hql, Object[] params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return (Long) q.uniqueResult();
	}

	@Override
	public Long count(String hql, List<Object> params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
		}
		return (Long) q.uniqueResult();
	}

	@Override
	public Integer executeHQL(String hql) {
		return getSession().createQuery(hql).executeUpdate();
	}

	@Override
	public Integer executeHQL(String hql, Object[] params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.executeUpdate();
	}

	@Override
	public Integer executeHQL(String hql, List<Object> params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
		}
		return q.executeUpdate();
	}

}
