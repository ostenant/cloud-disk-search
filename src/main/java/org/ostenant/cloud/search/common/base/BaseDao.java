package org.ostenant.cloud.search.common.base;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {

	/**
	 * 保存一个对象
	 * 
	 * @param o
	 * @return
	 */
	public void save(T o);

	/**
	 * 删除一个对象
	 * 
	 * @param o
	 */
	public void delete(T o);

	/**
	 * 通过ID删除对象
	 * 
	 * @param id
	 */
	public void deleteById(Serializable id);

	/**
	 * 更新一个对象
	 * 
	 * @param o
	 */
	public void update(T o);

	/**
	 * 保存或更新对象
	 * 
	 * @param o
	 */
	public void saveOrUpdate(T o);

	/**
	 * 查询集合(带分页)
	 * 
	 * @param hql
	 * @param param
	 * @param page
	 *            查询第几页
	 * @param rows
	 *            每页显示几条记录
	 * @return
	 */
	public List<T> queryPageByHQL(String hql, Object[] params, Integer page,
			Integer rows);

	/**
	 * 查询集合(带分页)
	 * 
	 * @param hql
	 * @param param
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> queryPageByHQL(String hql, List<Object> params,
			Integer page, Integer rows);

	/**
	 * 获得一个对象
	 * 
	 * @param c
	 *            对象类型
	 * @param id
	 * @return Object
	 */
	public T get(Class<T> c, Serializable id);

	/**
	 * 查询所有列表
	 * @return
	 */
	public List<T> queryAll();
	
	/**
	 * 根据ID查询结果
	 * @param id
	 * @return
	 */
	public T queryById(Serializable id);
	
	/**
	 * 查询部分结果
	 * @param ids
	 * @return
	 */
	public List<T> querySome(Serializable[] ids);

	/**
	 * 获得一个集合
	 * 
	 * @param hql
	 * @return
	 */
	public List<T> queryByHQL(String hql);

	/**
	 * 
	 * 
	 * @param hql
	 * @param param
	 * @return Object
	 */
	public List<T> queryByHQL(String hql, Object[] params);

	/**
	 * 获得一个对象
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public List<T> queryByHQL(String hql, List<Object> param);

	/**
	 * select count(*) from 类
	 * 
	 * @param hql
	 * @return
	 */
	public Long count(String hql);

	/**
	 * select count(*) from 类
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public Long count(String hql, Object[] param);

	/**
	 * select count(*) from 类
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public Long count(String hql, List<Object> param);

	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @return 响应数目
	 */
	public Integer executeHQL(String hql);

	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @param param
	 * @return 响应数目
	 */
	public Integer executeHQL(String hql, Object[] param);

	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public Integer executeHQL(String hql, List<Object> param);

}
