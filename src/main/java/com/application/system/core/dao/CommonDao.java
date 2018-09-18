package com.application.system.core.dao;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @auther ttm
 * @date 2018/9/17
 */
@Repository
public class CommonDao {
    private Boolean orderAsc = Boolean.valueOf(false);
    private Boolean orderDesc = Boolean.valueOf(true);
    private List<String> queryFields = new ArrayList();
    private ProjectionList projectionList = Projections.projectionList();
    @Autowired(required = false)
    protected SessionFactory sessionFactory;

    public Session getNewSession() {
        return this.sessionFactory.openSession();
    }

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public Object load(Class clazz, String id) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            Object o = session.get(clazz, Long.valueOf(id));
            tr.commit();
            session.close();
            return o;
        } catch (Exception arg6) {
            tr.rollback();
            session.close();
            return null;
        }
    }

    public Object load(Class clazz, Long id) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            Object o = session.get(clazz, id);
            tr.commit();
            session.close();
            return o;
        } catch (Exception arg6) {
            tr.rollback();
            session.close();
            return null;
        }
    }

    public Object load(Class clazz, int id) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            Object o = session.get(clazz, Integer.valueOf(id));
            tr.commit();
            session.close();
            return o;
        } catch (Exception arg6) {
            arg6.printStackTrace();
            tr.rollback();
            session.close();
            return null;
        }
    }

    public Boolean merge(Object obj) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            session.merge(obj);
            tr.commit();
            session.close();
        } catch (Exception arg4) {
            arg4.printStackTrace();
            tr.rollback();
            session.close();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public Boolean save(Object obj) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            session.save(obj);
            tr.commit();
            session.close();
        } catch (Exception arg4) {
            tr.rollback();
            session.close();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public boolean saveOrUpdate(Object obj) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            session.saveOrUpdate(obj);
            tr.commit();
            session.close();
            return true;
        } catch (Exception arg4) {
            tr.rollback();
            session.close();
            return false;
        }
    }

    public Boolean delete(Object obj) {
        Session session = null;
        Transaction tr = null;

        try {
            session = this.getNewSession();
            session.setCacheMode(CacheMode.IGNORE);
            tr = null != session.getTransaction() && session.getTransaction().isActive()
                    ? session.getTransaction()
                    : session.beginTransaction();
            session.delete(obj);
            tr.commit();
            session.close();
        } catch (Exception arg4) {
            arg4.printStackTrace();
            tr.rollback();
            session.close();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public void deleteAll(Collection entities) {
        if (null != entities) {
            Session session = null;
            Transaction tr = null;

            try {
                session = this.getNewSession();
                session.setCacheMode(CacheMode.IGNORE);
                tr = null != session.getTransaction() && session.getTransaction().isActive()
                        ? session.getTransaction()
                        : session.beginTransaction();
                Iterator e = entities.iterator();

                while (e.hasNext()) {
                    Object e1 = e.next();
                    session.delete(e1);
                }

                tr.commit();
                session.close();
            } catch (Exception arg5) {
                tr.rollback();
                session.close();
            }

        }
    }

    public Boolean checkExist(Class clazz, Criterion simpleEx) {
        return null == this.getRow(clazz, simpleEx) ? Boolean.valueOf(false) : Boolean.valueOf(true);
    }

    public Object getRow(Class clazz, Criterion simpleEx) {
        Session session = null;

        try {
            session = this.getNewSession();
            List e = session.createCriteria(clazz).add(simpleEx).setMaxResults(1).list();
            session.close();
            return e.size() > 0 ? e.get(0) : null;
        } catch (Exception arg4) {
            arg4.printStackTrace();
            session.close();
            return null;
        }
    }

    public Object getRow(Class clazz, Criterion simpleEx, Session session) {
        List result = session.createCriteria(clazz).add(simpleEx).setMaxResults(1).list();
        return result.size() > 0 ? result.get(0) : null;
    }

    public Object getRow(Class clazz, Criterion simpleEx, String orderBy) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz).add(simpleEx).setMaxResults(1);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result = e.list();
            session.close();
            return result.size() > 0 ? result.get(0) : null;
        } catch (Exception arg6) {
            session.close();
            return null;
        }
    }

    public Boolean checkExist(Class clazz, List<Criterion> simpleExList) {
        return null == this.getRow(clazz, simpleExList) ? Boolean.valueOf(false) : Boolean.valueOf(true);
    }

    public Boolean checkExist(Class clazz, Criterion simpleEx, Session sess) {
        return null == this.getRow(clazz, simpleEx, sess) ? Boolean.valueOf(false) : Boolean.valueOf(true);
    }

    public Boolean checkExist(Class clazz, List<Criterion> simpleExList, Session sess) {
        return null == this.getRow(clazz, simpleExList, sess) ? Boolean.valueOf(false) : Boolean.valueOf(true);
    }

    public Object getRow(Class clazz, Criterion... simpleExList) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Criterion[] result = simpleExList;
            int arg5 = simpleExList.length;

            for (int arg6 = 0; arg6 < arg5; ++arg6) {
                Criterion simpleEx = result[arg6];
                e.add(simpleEx);
            }

            e.setMaxResults(1);
            List arg9 = e.list();
            session.close();
            return arg9.size() > 0 ? arg9.get(0) : null;
        } catch (Exception arg8) {
            session.close();
            return null;
        }
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion simpleEx = (Criterion) result.next();
                e.add(simpleEx);
            }

            e.setMaxResults(1);
            List result1 = e.list();
            session.close();
            return result1.size() > 0 ? result1.get(0) : null;
        } catch (Exception arg6) {
            session.close();
            return null;
        }
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, String orderBy) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion simpleEx = (Criterion) result.next();
                e.add(simpleEx);
            }

            e.setMaxResults(1);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result1 = e.list();
            session.close();
            return result1.size() > 0 ? result1.get(0) : null;
        } catch (Exception arg7) {
            session.close();
            return null;
        }
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, Session session) {
        Criteria cri = session.createCriteria(clazz);
        Iterator result = simpleExList.iterator();

        while (result.hasNext()) {
            Criterion simpleEx = (Criterion) result.next();
            cri.add(simpleEx);
        }

        cri.setMaxResults(1);
        List result1 = cri.list();
        return result1.size() > 0 ? result1.get(0) : null;
    }

    public Object getRow(Class clazz, Criterion criterion, String orderBy, Session session) {
        Criteria cri = session.createCriteria(clazz);
        cri.add(criterion);
        cri.setMaxResults(1);
        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result = cri.list();
        return result.size() > 0 ? result.get(0) : null;
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, String orderBy, Session session) {
        Criteria cri = session.createCriteria(clazz);
        Iterator result = simpleExList.iterator();

        while (result.hasNext()) {
            Criterion simpleEx = (Criterion) result.next();
            cri.add(simpleEx);
        }

        cri.setMaxResults(1);
        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result1 = cri.list();
        return result1.size() > 0 ? result1.get(0) : null;
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion criName = (Criterion) result.next();
                e.add(criName);
            }

            result = criterias.keySet().iterator();

            while (true) {
                String criName1;
                do {
                    if (!result.hasNext()) {
                        e.setMaxResults(1);
                        List result1 = e.list();
                        session.close();
                        if (result1.size() > 0) {
                            return result1.get(0);
                        }

                        return null;
                    }

                    criName1 = (String) result.next();
                    e.createCriteria(criName1, criName1.replace('.', '_'));
                } while (null == criterias.get(criName1));

                Iterator arg7 = ((List) criterias.get(criName1)).iterator();

                while (arg7.hasNext()) {
                    Criterion simpleEx = (Criterion) arg7.next();
                    e.add(simpleEx);
                }
            }
        } catch (Exception arg9) {
            session.close();
            return null;
        }
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias,
                         Session session) {
        try {
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion criName = (Criterion) result.next();
                e.add(criName);
            }

            result = criterias.keySet().iterator();

            while (true) {
                String criName1;
                do {
                    if (!result.hasNext()) {
                        e.setMaxResults(1);
                        List result1 = e.list();
                        if (result1.size() > 0) {
                            return result1.get(0);
                        }

                        return null;
                    }

                    criName1 = (String) result.next();
                    e.createCriteria(criName1, criName1.replace('.', '_'));
                } while (null == criterias.get(criName1));

                Iterator arg7 = ((List) criterias.get(criName1)).iterator();

                while (arg7.hasNext()) {
                    Criterion simpleEx = (Criterion) arg7.next();
                    e.add(simpleEx);
                }
            }
        } catch (Exception arg9) {
            return null;
        }
    }

    public Object getRow(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias,
                         String orderBy) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion criName = (Criterion) result.next();
                e.add(criName);
            }

            result = criterias.keySet().iterator();

            while (true) {
                String criName1;
                do {
                    if (!result.hasNext()) {
                        if (this.getOrderDesc().booleanValue()) {
                            e.addOrder(Order.desc(orderBy));
                        } else if (this.getOrderAsc().booleanValue()) {
                            e.addOrder(Order.asc(orderBy));
                        }

                        e.setMaxResults(1);
                        List result1 = e.list();
                        session.close();
                        if (result1.size() > 0) {
                            return result1.get(0);
                        }

                        return null;
                    }

                    criName1 = (String) result.next();
                    e.createCriteria(criName1, criName1.replace('.', '_'));
                } while (null == criterias.get(criName1));

                Iterator arg8 = ((List) criterias.get(criName1)).iterator();

                while (arg8.hasNext()) {
                    Criterion simpleEx = (Criterion) arg8.next();
                    e.add(simpleEx);
                }
            }
        } catch (Exception arg10) {
            session.close();
            return null;
        }
    }

    public Object getRowAsc(Class clazz, Criterion simpleEx, String orderBy) {
        Session session = null;

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz).add(simpleEx).setMaxResults(1);
            e.addOrder(Order.asc(orderBy));
            List result = e.list();
            session.close();
            return result.size() > 0 ? result.get(0) : null;
        } catch (Exception arg6) {
            session.close();
            return null;
        }
    }

    public Object getRowAsc(Class clazz, Criterion simpleEx, String orderBy, Session session) {
        Criteria cri = session.createCriteria(clazz);
        cri.add(simpleEx);
        cri.setMaxResults(1);
        cri.addOrder(Order.asc(orderBy));
        List result = cri.list();
        return result.size() > 0 ? result.get(0) : null;
    }

    public List getResult(Class clazz, Criterion simpleEx) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (simpleEx != null) {
                e.add(simpleEx);
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg5) {
            arg5.printStackTrace();
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, Criterion simpleEx, String... fields) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (simpleEx != null) {
                e.add(simpleEx);
            }

            ProjectionList pl = Projections.projectionList();
            this.addFields(pl, fields);
            e.setProjection(pl);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg7) {
            arg7.printStackTrace();
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg5 = simpleExList.iterator();

            while (arg5.hasNext()) {
                Criterion simpleEx = (Criterion) arg5.next();
                e.add(simpleEx);
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg7) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg6 = simpleExList.iterator();

            while (arg6.hasNext()) {
                Criterion simpleEx = (Criterion) arg6.next();
                e.add(simpleEx);
            }

            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg8) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg6 = simpleExList.iterator();

            while (arg6.hasNext()) {
                Criterion simpleEx = (Criterion) arg6.next();
                e.add(simpleEx);
            }

            int arg8;
            String orderBy;
            String[] arg12;
            int arg13;
            if (this.getOrderDesc().booleanValue()) {
                arg12 = orderBys;
                arg13 = orderBys.length;

                for (arg8 = 0; arg8 < arg13; ++arg8) {
                    orderBy = arg12[arg8];
                    e.addOrder(Order.desc(orderBy));
                }
            } else if (this.getOrderAsc().booleanValue()) {
                arg12 = orderBys;
                arg13 = orderBys.length;

                for (arg8 = 0; arg8 < arg13; ++arg8) {
                    orderBy = arg12[arg8];
                    e.addOrder(Order.asc(orderBy));
                }
            }

            List arg11 = e.list();
            session.close();
            return arg11;
        } catch (Exception arg10) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Integer pageSize, String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg7 = simpleExList.iterator();

            while (arg7.hasNext()) {
                Criterion simpleEx = (Criterion) arg7.next();
                e.add(simpleEx);
            }

            int arg9;
            String orderBy;
            String[] arg13;
            int arg14;
            if (this.getOrderDesc().booleanValue()) {
                arg13 = orderBys;
                arg14 = orderBys.length;

                for (arg9 = 0; arg9 < arg14; ++arg9) {
                    orderBy = arg13[arg9];
                    e.addOrder(Order.desc(orderBy));
                }
            } else if (this.getOrderAsc().booleanValue()) {
                arg13 = orderBys;
                arg14 = orderBys.length;

                for (arg9 = 0; arg9 < arg14; ++arg9) {
                    orderBy = arg13[arg9];
                    e.addOrder(Order.asc(orderBy));
                }
            }

            e.setMaxResults(pageSize.intValue());
            List arg12 = e.list();
            session.close();
            return arg12;
        } catch (Exception arg11) {
            arg11.printStackTrace();
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy, Integer pageSize) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg7 = simpleExList.iterator();

            while (arg7.hasNext()) {
                Criterion simpleEx = (Criterion) arg7.next();
                e.add(simpleEx);
            }

            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            e.setMaxResults(pageSize.intValue());
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg9) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy, Integer pageSize,
                          Session session) {
        new ArrayList();
        Criteria cri = session.createCriteria(clazz);
        Iterator arg7 = simpleExList.iterator();

        while (arg7.hasNext()) {
            Criterion simpleEx = (Criterion) arg7.next();
            cri.add(simpleEx);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        cri.setMaxResults(pageSize.intValue());
        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        Iterator arg5 = simpleExList.iterator();

        while (arg5.hasNext()) {
            Criterion simpleEx = (Criterion) arg5.next();
            cri.add(simpleEx);
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, Criterion critrion, Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        if (null != critrion) {
            cri.add(critrion);
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Session sess, String... orderBys) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        Iterator arg6 = simpleExList.iterator();

        while (arg6.hasNext()) {
            Criterion simpleEx = (Criterion) arg6.next();
            cri.add(simpleEx);
        }

        int arg8;
        String orderBy;
        String[] arg10;
        int arg11;
        if (this.getOrderDesc().booleanValue()) {
            arg10 = orderBys;
            arg11 = orderBys.length;

            for (arg8 = 0; arg8 < arg11; ++arg8) {
                orderBy = arg10[arg8];
                cri.addOrder(Order.desc(orderBy));
            }
        } else if (this.getOrderAsc().booleanValue()) {
            arg10 = orderBys;
            arg11 = orderBys.length;

            for (arg8 = 0; arg8 < arg11; ++arg8) {
                orderBy = arg10[arg8];
                cri.addOrder(Order.asc(orderBy));
            }
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy, Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        Iterator arg6 = simpleExList.iterator();

        while (arg6.hasNext()) {
            Criterion simpleEx = (Criterion) arg6.next();
            cri.add(simpleEx);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result = cri.list();
        return result;
    }

    public List getResultDistinct(Class clazz, List<Criterion> simpleExList, String orderBy, String distinct,
                                  Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        Iterator arg7 = simpleExList.iterator();

        while (arg7.hasNext()) {
            Criterion simpleEx = (Criterion) arg7.next();
            cri.add(simpleEx);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy, Integer pageSize, String distinct,
                          Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        Iterator arg8 = simpleExList.iterator();

        while (arg8.hasNext()) {
            Criterion simpleEx = (Criterion) arg8.next();
            cri.add(simpleEx);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        cri.setMaxResults(pageSize.intValue());
        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, Criterion simpleEx, String orderBy, Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        if (simpleEx != null) {
            cri.add(simpleEx);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, Criterion simpleEx, String orderBy) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (simpleEx != null) {
                e.add(simpleEx);
            }

            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg6) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, Criterion simpleEx, String orderBy, Integer pageSize) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (simpleEx != null) {
                e.add(simpleEx);
            }

            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            e.setMaxResults(pageSize.intValue());
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg7) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, String orderBy, String distinct,
                          String groupProperty, Session session) {
        Criteria cri = session.createCriteria(clazz);
        Iterator pl = simpleExList.iterator();

        while (pl.hasNext()) {
            Criterion obj = (Criterion) pl.next();
            cri.add(obj);
        }

        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        ProjectionList pl1 = Projections.projectionList();
        pl1.add(Projections.distinct(Projections.property(distinct)));
        List obj1 = cri.setProjection(pl1).list();
        return obj1;
    }

    public List getResult(Class clazz, Criterion criterion, int pageSize) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            e.add(criterion);
            e.setMaxResults(pageSize);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg6) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, int pageSize) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg6 = simpleExList.iterator();

            while (arg6.hasNext()) {
                Criterion simpleEx = (Criterion) arg6.next();
                e.add(simpleEx);
            }

            e.setMaxResults(pageSize);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg8) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, int pageSize, int pageNumber) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg7 = simpleExList.iterator();

            while (arg7.hasNext()) {
                Criterion simpleEx = (Criterion) arg7.next();
                e.add(simpleEx);
            }

            e.setFirstResult((pageNumber - 1) * pageSize);
            e.setMaxResults(pageSize);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg9) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, int pageSize, int pageNumber, String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator pl = simpleExList.iterator();

            while (pl.hasNext()) {
                Criterion simpleEx = (Criterion) pl.next();
                e.add(simpleEx);
            }

            int arg10;
            String orderBy;
            String[] arg14;
            int arg16;
            if (this.getOrderDesc().booleanValue()) {
                arg14 = orderBys;
                arg16 = orderBys.length;

                for (arg10 = 0; arg10 < arg16; ++arg10) {
                    orderBy = arg14[arg10];
                    e.addOrder(Order.desc(orderBy));
                }
            } else if (this.getOrderAsc().booleanValue()) {
                arg14 = orderBys;
                arg16 = orderBys.length;

                for (arg10 = 0; arg10 < arg16; ++arg10) {
                    orderBy = arg14[arg10];
                    e.addOrder(Order.asc(orderBy));
                }
            }

            if (!this.queryFields.isEmpty()) {
                ProjectionList arg15 = Projections.projectionList();
                this.addFields(arg15, this.queryFields);
                e.setProjection(arg15);
            }

            e.setFirstResult((pageNumber - 1) * pageSize);
            e.setMaxResults(pageSize);
            List arg13 = e.list();
            session.close();
            return arg13;
        } catch (Exception arg12) {
            arg12.printStackTrace();
            session.close();
            return result;
        }
    }

    private void _parseCriterias(Criteria cri, Map<String, List<Criterion>> criterias, List<Criterion> criterions) {
        ArrayList conditions = new ArrayList();
        Iterator arg4;
        if (null != criterions) {
            arg4 = criterions.iterator();

            while (arg4.hasNext()) {
                Criterion criName = (Criterion) arg4.next();
                String condition = criName.toString();
                if (!conditions.contains(condition)) {
                    cri.add(criName);
                    conditions.add(condition);
                }
            }
        }

        arg4 = criterias.keySet().iterator();

        while (true) {
            String criName1;
            do {
                if (!arg4.hasNext()) {
                    return;
                }

                criName1 = (String) arg4.next();
                cri.createCriteria(criName1, criName1.replace('.', '_'));
            } while (null == criterias.get(criName1));

            Iterator condition2 = ((List) criterias.get(criName1)).iterator();

            while (condition2.hasNext()) {
                Criterion simpleEx = (Criterion) condition2.next();
                String condition1 = simpleEx.toString();
                if (!conditions.contains(condition1)) {
                    cri.add(simpleEx);
                    conditions.add(condition1);
                }
            }
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias,
                          Integer pageSize, String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, simpleExList);
            String[] arg8;
            int arg9;
            int arg10;
            String orderBy;
            if (this.getOrderDesc().booleanValue()) {
                arg8 = orderBys;
                arg9 = orderBys.length;

                for (arg10 = 0; arg10 < arg9; ++arg10) {
                    orderBy = arg8[arg10];
                    e.addOrder(Order.desc(orderBy));
                }
            } else if (this.getOrderAsc().booleanValue()) {
                arg8 = orderBys;
                arg9 = orderBys.length;

                for (arg10 = 0; arg10 < arg9; ++arg10) {
                    orderBy = arg8[arg10];
                    e.addOrder(Order.asc(orderBy));
                }
            }

            e.setMaxResults(pageSize.intValue());
            List arg13 = e.list();
            session.close();
            return arg13;
        } catch (Exception arg12) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, Criterion criterion, Map<String, List<Criterion>> criterias) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (null != criterion) {
                e.add(criterion);
            }

            this._parseCriterias(e, criterias, (List) null);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg6) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, Criterion criterion, Map<String, List<Criterion>> criterias, String orderBy) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (null != criterion) {
                e.add(criterion);
            }

            this._parseCriterias(e, criterias, (List) null);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg7) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, Criterion criterion, Map<String, List<Criterion>> criterias, String orderBy,
                          Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        if (null != criterion) {
            cri.add(criterion);
        }

        this._parseCriterias(cri, criterias, (List) null);
        if (this.getOrderDesc().booleanValue()) {
            cri.addOrder(Order.desc(orderBy));
        } else if (this.getOrderAsc().booleanValue()) {
            cri.addOrder(Order.asc(orderBy));
        }

        List result = cri.list();
        return result;
    }

    public List getResult(Class clazz, Criterion criterion, Map<String, List<Criterion>> criterias, Session sess) {
        new ArrayList();
        Criteria cri = sess.createCriteria(clazz);
        if (null != criterion) {
            cri.add(criterion);
        }

        Iterator arg6 = criterias.keySet().iterator();

        while (true) {
            String criName;
            do {
                if (!arg6.hasNext()) {
                    List result = cri.list();
                    return result;
                }

                criName = (String) arg6.next();
                cri.createCriteria(criName, criName.replace('.', '_'));
            } while (null == criterias.get(criName));

            Iterator arg8 = ((List) criterias.get(criName)).iterator();

            while (arg8.hasNext()) {
                Criterion simpleEx = (Criterion) arg8.next();
                cri.add(simpleEx);
            }
        }
    }

    public List getResult(Class clazz, Criterion criterion, Map<String, List<Criterion>> criterias, String orderBy,
                          Integer pageSize) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            if (null != criterion) {
                e.add(criterion);
            }

            this._parseCriterias(e, criterias, (List) null);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            e.setMaxResults(pageSize.intValue());
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg8) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg6) {
            session.close();
            return result;
        }
    }

    public List getResultDistinct(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator arg6;
            if (null != criterions) {
                arg6 = criterions.iterator();

                while (arg6.hasNext()) {
                    Criterion criName = (Criterion) arg6.next();
                    e.add(criName);
                }
            }

            arg6 = criterias.keySet().iterator();

            while (true) {
                String criName1;
                do {
                    if (!arg6.hasNext()) {
                        e.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                        List result1 = e.list();
                        session.close();
                        return result1;
                    }

                    criName1 = (String) arg6.next();
                    e.createCriteria(criName1, criName1.replace('.', '_'));
                } while (null == criterias.get(criName1));

                Iterator arg8 = ((List) criterias.get(criName1)).iterator();

                while (arg8.hasNext()) {
                    Criterion simpleEx = (Criterion) arg8.next();
                    e.add(simpleEx);
                }
            }
        } catch (Exception arg10) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias,
                          String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            String[] arg7;
            int arg8;
            int arg9;
            String orderBy;
            if (this.getOrderDesc().booleanValue()) {
                arg7 = orderBys;
                arg8 = orderBys.length;

                for (arg9 = 0; arg9 < arg8; ++arg9) {
                    orderBy = arg7[arg9];
                    e.addOrder(Order.desc(orderBy));
                }
            } else if (this.getOrderAsc().booleanValue()) {
                arg7 = orderBys;
                arg8 = orderBys.length;

                for (arg9 = 0; arg9 < arg8; ++arg9) {
                    orderBy = arg7[arg9];
                    e.addOrder(Order.asc(orderBy));
                }
            }

            List arg12 = e.list();
            session.close();
            return arg12;
        } catch (Exception arg11) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias,
                          String orderBy) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            List result1 = e.list();
            session.close();
            return result1;
        } catch (Exception arg7) {
            session.close();
            return result;
        }
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias,
                          String orderBy, Integer pageSize, Integer pageNumber) {
        Session session = null;
        new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            e.setFirstResult((pageNumber.intValue() - 1) * pageSize.intValue());
            e.setMaxResults(pageSize.intValue());
            List result = e.list();
            session.close();
            return result;
        } catch (Exception arg9) {
            session.close();
            return null;
        }
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias,
                          String orderBy, Integer pageSize) {
        Session session = null;
        new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            if (this.getOrderDesc().booleanValue()) {
                e.addOrder(Order.desc(orderBy));
            } else if (this.getOrderAsc().booleanValue()) {
                e.addOrder(Order.asc(orderBy));
            }

            e.setMaxResults(pageSize.intValue());
            List result = e.list();
            session.close();
            return result;
        } catch (Exception arg8) {
            session.close();
            return null;
        }
    }

    public Long getResultCount(Class clazz, Criterion simpleEx) {
        Session session = null;
        Long count = Long.valueOf(0L);

        try {
            session = this.getNewSession();
            List e = session.createCriteria(clazz).add(simpleEx)
                    .setProjection(Projections.projectionList().add(Projections.rowCount())).list();
            session.close();
            count = new Long(String.valueOf(e.get(0)));
            return count;
        } catch (Exception arg5) {
            session.close();
            return Long.valueOf(0L);
        }
    }

    public Long getResultCount(Class clazz, List<Criterion> simpleExList) {
        Session session = null;
        Long count = Long.valueOf(0L);

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator result = simpleExList.iterator();

            while (result.hasNext()) {
                Criterion simpleEx = (Criterion) result.next();
                e.add(simpleEx);
            }

            e.setProjection(Projections.projectionList().add(Projections.rowCount()));
            List result1 = e.list();
            session.close();
            count = new Long(String.valueOf(result1.get(0)));
            return count;
        } catch (Exception arg7) {
            session.close();
            return Long.valueOf(0L);
        }
    }

    public List<Long> getResultCount(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias) {
        Session session = null;
        new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            ProjectionList pl = Projections.projectionList();
            pl.add(Projections.count("id"));
            ArrayList counts = new ArrayList();
            if (0 == e.setProjection(pl).list().size()) {
                counts.add(Long.valueOf(0L));
            } else {
                counts.add((Long) e.setProjection(pl).list().get(0));
            }

            session.close();
            return counts;
        } catch (Exception arg7) {
            session.close();
            return null;
        }
    }

    public List<Long> getResultCount(Class clazz, List<Criterion> criterions, List<String> criterias) {
        Session session = null;
        new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            Iterator pl = criterions.iterator();

            while (pl.hasNext()) {
                Criterion criName = (Criterion) pl.next();
                e.add(criName);
            }

            pl = criterias.iterator();

            while (pl.hasNext()) {
                String criName1 = (String) pl.next();
                e.createCriteria(criName1, criName1.replace('.', '_'));
            }

            ProjectionList pl1 = Projections.projectionList();
            pl1.add(Projections.count("id"));
            ArrayList counts = new ArrayList();
            counts.add((Long) e.setProjection(pl1).list().get(0));
            session.close();
            return counts;
        } catch (Exception arg8) {
            arg8.printStackTrace();
            session.close();
            return null;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias,
                          Session session) {
        try {
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, simpleExList);
            List result = e.list();
            return result;
        } catch (Exception arg6) {
            return null;
        }
    }

    public List getResult(Class clazz, List<Criterion> simpleExList, Map<String, List<Criterion>> criterias,
                          Session session, String... orderBys) {
        try {
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, simpleExList);
            String[] result = orderBys;
            int arg7 = orderBys.length;

            for (int arg8 = 0; arg8 < arg7; ++arg8) {
                String orderBy = result[arg8];
                if (orderBy.contains("asc")) {
                    e.addOrder(Order.asc(orderBy.replace("_asc", "")));
                } else if (orderBy.contains("desc")) {
                    e.addOrder(Order.desc(orderBy.replace("_desc", "")));
                }
            }

            List arg11 = e.list();
            return arg11;
        } catch (Exception arg10) {
            return null;
        }
    }

    public List getResult2(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias, String type,
                           String... orderBys) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            String[] arg8 = orderBys;
            int arg9 = orderBys.length;

            for (int arg10 = 0; arg10 < arg9; ++arg10) {
                String orderBy = arg8[arg10];
                if (orderBy.contains("asc")) {
                    e.addOrder(Order.asc(orderBy.replace("_asc", "")));
                } else if (orderBy.contains("desc")) {
                    e.addOrder(Order.desc(orderBy.replace("_desc", "")));
                }
            }

            List arg13 = e.list();
            session.close();
            return arg13;
        } catch (Exception arg12) {
            session.close();
            return result;
        }
    }

    public Boolean getOrderAsc() {
        return this.orderAsc;
    }

    public void setOrderAsc(Boolean orderAsc) {
        this.orderAsc = orderAsc;
        this.orderDesc = Boolean.valueOf(!orderAsc.booleanValue());
    }

    public Boolean getOrderDesc() {
        return this.orderDesc;
    }

    public void setOrderDesc(Boolean orderDesc) {
        this.orderDesc = orderDesc;
        this.orderAsc = Boolean.valueOf(!orderDesc.booleanValue());
    }

    public void addFields(ProjectionList pl, String... fields) {
        String[] arg2 = fields;
        int arg3 = fields.length;

        for (int arg4 = 0; arg4 < arg3; ++arg4) {
            String field = arg2[arg4];
            pl.add(Projections.property(field), field);
        }

    }

    public void addFields(ProjectionList pl, List<String> fields) {
        Iterator arg2 = fields.iterator();

        while (arg2.hasNext()) {
            String field = (String) arg2.next();
            pl.add(Projections.property(field), field);
        }

    }

    public void setQueryFields(List<String> queryFields) {
        this.queryFields = queryFields;
    }

    public List<String> getQueryFields() {
        return this.queryFields;
    }

    public List getResult(Class clazz, List<Criterion> criterions, Map<String, List<Criterion>> criterias, int pageSize,
                          int pageNumber) {
        Session session = null;
        ArrayList result = new ArrayList();

        try {
            session = this.getNewSession();
            Criteria e = session.createCriteria(clazz);
            this._parseCriterias(e, criterias, criterions);
            if (this.projectionList.getLength() > 0) {
                e.setProjection(this.projectionList);
            }

            e.setFirstResult((pageNumber - 1) * pageSize);
            e.setMaxResults(pageSize);
            e.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            result = new ArrayList(e.list());
            session.close();
            return result;
        } catch (Exception arg8) {
            session.close();
            return result;
        }
    }

    public ArrayList<Criterion> getRequestRestriction(Map<String, Object> query) {
        ArrayList restrictions = new ArrayList();
        if (null != query) {
            Iterator arg2 = query.keySet().iterator();

            while (true) {
                while (arg2.hasNext()) {
                    String key = (String) arg2.next();
                    Object value = query.get(key);
                    if (value instanceof Map) {
                        Map compareValue = (Map) value;
                        Iterator arg6 = compareValue.keySet().iterator();

                        while (arg6.hasNext()) {
                            String compare = (String) arg6.next();
                            if ("$ge".equals(compare)) {
                                restrictions.add(Restrictions.ge(key, compareValue.get(compare)));
                            } else if ("$le".equals(compare)) {
                                restrictions.add(Restrictions.le(key, compareValue.get(compare)));
                            } else if ("$gt".equals(compare)) {
                                restrictions.add(Restrictions.gt(key, compareValue.get(compare)));
                            } else if ("$lt".equals(compare)) {
                                restrictions.add(Restrictions.lt(key, compareValue.get(compare)));
                            } else if ("$in".equals(compare)) {
                                restrictions.add(Restrictions.in(key, (Collection) compareValue.get(compare)));
                            } else if ("$like".equals(compare)) {
                                restrictions.add(Restrictions.ilike(key, "%" + compareValue.get(compare) + "%"));
                            }
                        }
                    } else {
                        restrictions.add(Restrictions.eq(key, query.get(key)));
                    }
                }

                return restrictions;
            }
        } else {
            return restrictions;
        }
    }

    public Long fetchCollectionCount(Class clazz, Map<String, Object> requestArgs) {
        ArrayList criterions = this.getRequestRestriction((HashMap) requestArgs.get("query"));
        return this.getResultCount(clazz, (List) criterions);
    }

    public List fetchCollection(Class clazz, Map<String, Object> requestArgs) {
        ArrayList criterions = this.getRequestRestriction((HashMap) requestArgs.get("query"));
        String sortField = CommonDaoHelper.getRequestSortField(requestArgs);
        String sortDirection = CommonDaoHelper.getRequestSortDirection(requestArgs);
        Integer pageSize = CommonDaoHelper.getRequestPageSize(requestArgs);
        Integer pageNumber = CommonDaoHelper.getRequestPageNumber(requestArgs);
        if ("-1".equals(sortDirection)) {
            this.setOrderDesc(Boolean.valueOf(true));
        } else {
            this.setOrderAsc(Boolean.valueOf(true));
        }

        this.setQueryFields(CommonDaoHelper.getRequestFields(requestArgs));
        return (ArrayList) this.getResult(clazz, criterions, pageSize.intValue(), pageNumber.intValue(),
                new String[]{sortField});
    }

    public Object fetchRow(Class clazz, Map<String, Object> query) {
        ArrayList criterions = this.getRequestRestriction(query);
        return this.getRow(clazz, (List) criterions);
    }

    public Boolean update(Class clazz, Map<String, Object> requestArgs) {
        Object id = requestArgs.get("id");
        if (null == id) {
            return Boolean.valueOf(false);
        } else {
            Session sess = null;
            Transaction tr = null;

            try {
                sess = this.getNewSession();
                tr = null != sess.getTransaction() && sess.getTransaction().isActive()
                        ? sess.getTransaction()
                        : sess.beginTransaction();
                Object object = sess.get(clazz, new Integer(id.toString()));
                requestArgs.remove("id");
                BeanUtils.populate(object, (Map) requestArgs.get("updates"));
                sess.saveOrUpdate(object);
                tr.commit();
            } catch (Exception arg7) {
                arg7.printStackTrace();
                tr.rollback();
                return Boolean.valueOf(false);
            }

            return Boolean.valueOf(true);
        }
    }

    public Boolean batchUpdate(Class clazz, Map<String, Object> requestArgs) {
        List ids = (List) requestArgs.get("ids");
        if (null == ids) {
            return Boolean.valueOf(false);
        } else {
            Session sess = null;
            Transaction tr = null;

            try {
                sess = this.getNewSession();
                tr = null != sess.getTransaction() && sess.getTransaction().isActive()
                        ? sess.getTransaction()
                        : sess.beginTransaction();
                Map e = (Map) requestArgs.get("updates");
                e.remove("ids");
                Iterator arg6 = ids.iterator();

                while (arg6.hasNext()) {
                    Integer id = (Integer) arg6.next();
                    Object object = sess.get(clazz, id);
                    BeanUtils.populate(object, e);
                    sess.saveOrUpdate(object);
                }

                tr.commit();
                return Boolean.valueOf(true);
            } catch (Exception arg9) {
                arg9.printStackTrace();
                tr.rollback();
                return Boolean.valueOf(false);
            }
        }
    }

    public Boolean save(Class clazz, Map<String, Object> requestArgs) {
        Session sess = null;
        Transaction tr = null;

        try {
            sess = this.getNewSession();
            tr = null != sess.getTransaction() && sess.getTransaction().isActive()
                    ? sess.getTransaction()
                    : sess.beginTransaction();
            Object e = clazz.newInstance();
            requestArgs.remove("id");
            BeanUtils.populate(e, requestArgs);
            sess.saveOrUpdate(e);
            tr.commit();
        } catch (Exception arg5) {
            arg5.printStackTrace();
            tr.rollback();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public Boolean deleteById(Class clazz, Integer id) {
        Object object = this.load(clazz, id.intValue());
        return null == object ? Boolean.valueOf(false) : this.delete(object);
    }
}