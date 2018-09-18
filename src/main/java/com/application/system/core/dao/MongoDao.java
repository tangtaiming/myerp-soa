package com.application.system.core.dao;

/**
 * @auther ttm
 * @date 2018/9/17
 */

import com.application.system.core.orm.Id;
import com.mongodb.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

@Repository
public class MongoDao {
    @Autowired(required = false)
    private MongoTemplate mongoTemplate;
    private Class entityClass;
    private String collectionName;
    private String orderAscField;
    private String orderDescField;

    public MongoDao() {
    }

    public MongoDao(Class entityClass) {
        this.entityClass = entityClass;
        this.collectionName = this._getCollectionName();
    }

    public MongoDao(Class entityClass, String collectionName) {
        this.entityClass = entityClass;
        this.collectionName = collectionName;
    }

    public long count() {
        return this.mongoTemplate.count(new Query(), this.collectionName);
    }

    public long count(Criteria criteria) {
        return this.mongoTemplate.count(new Query(criteria), this.collectionName);
    }

    public List find(Criteria criteria) {
        Query query = new Query(criteria);
        this._sort(query);
        return this.mongoTemplate.find(query, this.entityClass, this.collectionName);
    }

    public Object group(Criteria criteria, GroupBy groupBy) {
        return null == criteria
                ? this.mongoTemplate.group(this.collectionName, groupBy, this.entityClass)
                : this.mongoTemplate.group(criteria, this.collectionName, groupBy, this.entityClass);
    }

    public List find(Criteria criteria, Integer pageSize) {
        Query query = (new Query(criteria)).limit(pageSize.intValue());
        this._sort(query);
        return this.mongoTemplate.find(query, this.entityClass, this.collectionName);
    }

    public List find(Criteria criteria, Integer pageSize, Integer pageNumber) {
        Query query = (new Query(criteria)).skip((pageNumber.intValue() - 1) * pageSize.intValue())
                .limit(pageSize.intValue());
        this._sort(query);
        return this.mongoTemplate.find(query, this.entityClass, this.collectionName);
    }

    public Object findAndModify(Criteria criteria, Update update) {
        return this.mongoTemplate.findAndModify(new Query(criteria), update, this.entityClass, this.collectionName);
    }

    public Object findAndRemove(Criteria criteria) {
        return this.mongoTemplate.findAndRemove(new Query(criteria), this.entityClass, this.collectionName);
    }

    public List findAll() {
        return this.mongoTemplate.findAll(this.entityClass, this.collectionName);
    }

    public Object findById(Object id) {
        return this.mongoTemplate.findById(id, this.entityClass, this.collectionName);
    }

    public List findIds(Criteria criteria) {
        return this.mongoTemplate.find(new Query(criteria), Id.class, this.collectionName);
    }

    public Boolean checkExists(Criteria criteria) {
        Query query = (new Query(criteria)).limit(1);
        this._sort(query);
        return Boolean.valueOf(null != this.mongoTemplate.findOne(query, this.entityClass, this.collectionName));
    }

    public Object findOne(Criteria criteria) {
        Query query = (new Query(criteria)).limit(1);
        this._sort(query);
        return this.mongoTemplate.findOne(query, this.entityClass, this.collectionName);
    }

    public Object findOne(Criteria criteria, Integer skip) {
        Query query = (new Query(criteria)).skip(skip.intValue()).limit(1);
        this._sort(query);
        return this.mongoTemplate.findOne(query, this.entityClass, this.collectionName);
    }

    public Object findOne(Integer skip) {
        Query query = (new Query()).skip(skip.intValue()).limit(1);
        this._sort(query);
        return this.mongoTemplate.findOne(query, this.entityClass, this.collectionName);
    }

    public Boolean remove(Object object) {
        try {
            this.mongoTemplate.remove(object);
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public Boolean remove(Criteria criteria) {
        try {
            this.mongoTemplate.remove(new Query(criteria), this.collectionName);
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public WriteResult updateMulti(Criteria criteria, Update update) {
        return this.mongoTemplate.updateMulti(new Query(criteria), update, this.collectionName);
    }

    public Boolean saveOrUpdate(Object object) {
        try {
            this.mongoTemplate.save(object, this.collectionName);
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public Boolean insert(Collection batchToSave) {
        try {
            this.mongoTemplate.insert(batchToSave, this.collectionName);
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public MongoOperations getMongoOperation() {
        return this.mongoTemplate;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public Class getEntityClass() {
        return this.entityClass;
    }

    public String getNextId() {
        return this.getNextId(this.getCollectionName());
    }

    public String getNextId(String seq_name) {
        String sequence_collection = "seq";
        String sequence_field = "seq";
        DBCollection seq = this.mongoTemplate.getCollection(sequence_collection);
        BasicDBObject query = new BasicDBObject();
        query.put("_id", seq_name);
        BasicDBObject change = new BasicDBObject(sequence_field, Integer.valueOf(1));
        BasicDBObject update = new BasicDBObject("$inc", change);
        DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return res.get(sequence_field).toString();
    }

    private void _sort(Query query) {
        String[] fields;
        String[] arg2;
        int arg3;
        int arg4;
        String field;
        if (null != this.orderAscField) {
            fields = this.orderAscField.split(",");
            arg2 = fields;
            arg3 = fields.length;

            for (arg4 = 0; arg4 < arg3; ++arg4) {
                field = arg2[arg4];
                if ("id".equals(field)) {
                    field = "_id";
                }

                query.with(new Sort(Direction.ASC, new String[]{field}));
            }
        } else if (null != this.orderDescField) {
            fields = this.orderDescField.split(",");
            arg2 = fields;
            arg3 = fields.length;

            for (arg4 = 0; arg4 < arg3; ++arg4) {
                field = arg2[arg4];
                if ("id".equals(field)) {
                    field = "_id";
                }

                query.with(new Sort(Direction.DESC, new String[]{field}));
            }
        }

    }

    private String _getCollectionName() {
        String className = this.entityClass.getName();
        Integer lastIndex = Integer.valueOf(className.lastIndexOf("."));
        className = className.substring(lastIndex.intValue() + 1);
        return StringUtils.uncapitalize(className);
    }

    private static Criteria _parseRequestRestrictionOr(Map<String, Object> query) {
        Criteria allOrCriteria = new Criteria();
        ArrayList criterias = new ArrayList();
        if (null != query) {
            Iterator arg2 = query.keySet().iterator();

            while (arg2.hasNext()) {
                String key = (String) arg2.next();
                Object value = query.get(key);
                if (StringUtils.startsWith(key, "$and")) {
                    criterias.add(getRequestRestriction((Map) value));
                } else {
                    criterias.addAll(_parseCriteriaOr(key, value));
                }
            }
        }

        if (!criterias.isEmpty()) {
            allOrCriteria.orOperator((Criteria[]) criterias.toArray(new Criteria[criterias.size()]));
        }

        return allOrCriteria;
    }

    private static List<Criteria> _parseCriteriaOr(String key, Object value) {
        if ("id".equals(key)) {
            key = "_id";
        }

        ArrayList criterias = new ArrayList();
        if (value instanceof Map) {
            Map compareValue = (Map) value;
            Iterator arg3 = compareValue.keySet().iterator();

            while (arg3.hasNext()) {
                String compare = (String) arg3.next();
                Object _compareValue = compareValue.get(compare);
                if ("$ge".equals(compare)) {
                    criterias.add(Criteria.where(key).gte(_compareValue));
                } else if ("$le".equals(compare)) {
                    criterias.add(Criteria.where(key).lte(_compareValue));
                } else if ("$gt".equals(compare)) {
                    criterias.add(Criteria.where(key).gt(_compareValue));
                } else if ("$lt".equals(compare)) {
                    criterias.add(Criteria.where(key).lt(_compareValue));
                } else if ("$in".equals(compare)) {
                    criterias.add(Criteria.where(key).in((Collection) _compareValue));
                } else if ("$like".equals(compare)) {
                    criterias.add(Criteria.where(key).regex(Pattern.compile(Pattern.quote((String) _compareValue), 2)));
                } else if ("$left_like".equals(compare)) {
                    criterias.add(
                            Criteria.where(key).regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2)));
                } else if ("$right_like".equals(compare)) {
                    criterias.add(
                            Criteria.where(key).regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2)));
                } else if ("$not_like".equals(compare)) {
                    criterias.add(Criteria.where(key).not().regex((String) _compareValue));
                } else if ("$left_like".equals(compare)) {
                    criterias.add(Criteria.where(key).not()
                            .regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2)));
                } else if ("$not_right_like".equals(compare)) {
                    criterias.add(Criteria.where(key).not()
                            .regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2)));
                } else if ("$ne".equals(compare)) {
                    criterias.add(Criteria.where(key).ne(_compareValue));
                } else if ("$null".equals(compare)) {
                    criterias.add(Criteria.where(key).is((Object) null));
                } else if ("$not_null".equals(compare)) {
                    criterias.add(Criteria.where(key).not().is((Object) null));
                } else if ("$not_in".equals(compare)) {
                    criterias.add(Criteria.where(key).not().in((Collection) _compareValue));
                } else if ("$where".equals(compare)) {
                    criterias.add(Criteria.where("$where").is(_compareValue));
                }
            }
        } else {
            criterias.add(Criteria.where(key).is(value));
        }

        return criterias;
    }

    private static List<Criteria> _parseCriteria(String key, Object value) {
        if ("id".equals(key)) {
            key = "_id";
        }

        ArrayList criterias = new ArrayList();
        Criteria criteriaObj = null;
        Criteria criteria = null;
        boolean isElem = false;
        if (key.contains(".")) {
            String[] compareValue = key.split("\\.");
            criteriaObj = Criteria.where(compareValue[0]);
            criteria = Criteria.where(compareValue[1]);
            isElem = true;
        } else {
            criteria = Criteria.where(key);
        }

        if (value instanceof Map) {
            Map compareValue1 = (Map) value;
            Iterator arg6 = compareValue1.keySet().iterator();

            while (arg6.hasNext()) {
                String compare = (String) arg6.next();
                Object _compareValue = compareValue1.get(compare);
                if ("$ge".equals(compare)) {
                    criteria.gte(_compareValue);
                } else if ("$le".equals(compare)) {
                    criteria.lte(_compareValue);
                } else if ("$gt".equals(compare)) {
                    criteria.gt(_compareValue);
                } else if ("$lt".equals(compare)) {
                    criteria.lt(_compareValue);
                } else if ("$in".equals(compare)) {
                    criteria.in((Collection) _compareValue);
                } else if ("$like".equals(compare)) {
                    criteria.regex(Pattern.compile(Pattern.quote((String) _compareValue), 2));
                } else if ("$left_like".equals(compare)) {
                    criteria.regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2));
                } else if ("$right_like".equals(compare)) {
                    criteria.regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2));
                } else if ("$not_like".equals(compare)) {
                    criteria.not().regex((String) _compareValue);
                } else if ("$left_like".equals(compare)) {
                    criteria.not().regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2));
                } else if ("$not_right_like".equals(compare)) {
                    criteria.not().regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2));
                } else if ("$ne".equals(compare)) {
                    criteria.ne(_compareValue);
                } else if ("$null".equals(compare)) {
                    criteria.is((Object) null);
                } else if ("$not_null".equals(compare)) {
                    criteria.not().is((Object) null);
                } else if ("$not_in".equals(compare)) {
                    criteria.not().in((Collection) _compareValue);
                } else if ("$where".equals(compare)) {
                    criteria.is(_compareValue);
                }
            }
        } else {
            criteria.is(value);
        }

        if (criteria != null && isElem) {
            criteriaObj.elemMatch(criteria);
            criterias.add(criteriaObj);
        } else {
            criterias.add(criteria);
        }

        return criterias;
    }

    public static Criteria getRequestRestriction(Map<String, Object> query) {
        Criteria allCriteria = new Criteria();
        ArrayList criterias = new ArrayList();
        if (null != query) {
            HashMap elemQuery = new HashMap();
            HashMap newQuery = new HashMap();
            Iterator arg4 = query.keySet().iterator();

            String key;
            while (arg4.hasNext()) {
                key = (String) arg4.next();
                if (key.contains(".")) {
                    String[] keys = key.split("\\.");
                    String value = keys[0];
                    String key2 = keys[1];
                    if (elemQuery.containsKey(value)) {
                        Map keyMap = (Map) elemQuery.get(value);
                        keyMap.put(key2, query.get(key));
                        elemQuery.put(value, keyMap);
                    } else {
                        HashMap keyMap1 = new HashMap();
                        keyMap1.put(key2, query.get(key));
                        elemQuery.put(value, keyMap1);
                    }
                } else {
                    newQuery.put(key, query.get(key));
                }
            }

            arg4 = elemQuery.keySet().iterator();

            Map value1;
            Object value2;
            while (arg4.hasNext()) {
                key = (String) arg4.next();
                if ("$or".equals(key)) {
                    value1 = (Map) elemQuery.get(key);
                    criterias.add(_parseRequestRestrictionOr(value1));
                } else {
                    value2 = elemQuery.get(key);
                    criterias.addAll(_parseCriteria((Map) value2, key));
                }
            }

            arg4 = newQuery.keySet().iterator();

            while (arg4.hasNext()) {
                key = (String) arg4.next();
                if ("$or".equals(key)) {
                    value1 = (Map) query.get(key);
                    criterias.add(_parseRequestRestrictionOr(value1));
                } else {
                    value2 = query.get(key);
                    criterias.addAll(_parseCriteria(key, value2));
                }
            }

            if (!criterias.isEmpty()) {
                allCriteria.andOperator((Criteria[]) ((Criteria[]) criterias.toArray(new Criteria[criterias.size()])));
            }
        }

        return allCriteria;
    }

    private static List<Criteria> _parseCriteria(Map<String, Object> criMap, String key) {
        if ("id".equals(key)) {
            key = "_id";
        }

        ArrayList criterias = new ArrayList();
        Criteria criteriaObj = Criteria.where(key);
        Criteria criteria = null;
        Iterator arg4 = criMap.keySet().iterator();

        while (true) {
            while (arg4.hasNext()) {
                String filedKey = (String) arg4.next();
                if (criteria == null) {
                    criteria = Criteria.where(filedKey);
                } else {
                    criteria = criteria.and(filedKey);
                }

                Object value = criMap.get(filedKey);
                if (value instanceof Map) {
                    Map compareValue = (Map) value;
                    Iterator arg8 = compareValue.keySet().iterator();

                    while (arg8.hasNext()) {
                        String compare = (String) arg8.next();
                        Object _compareValue = compareValue.get(compare);
                        if ("$ge".equals(compare)) {
                            criteria.gte(_compareValue);
                        } else if ("$le".equals(compare)) {
                            criteria.lte(_compareValue);
                        } else if ("$gt".equals(compare)) {
                            criteria.gt(_compareValue);
                        } else if ("$lt".equals(compare)) {
                            criteria.lt(_compareValue);
                        } else if ("$in".equals(compare)) {
                            criteria.in((Collection) _compareValue);
                        } else if ("$like".equals(compare)) {
                            criteria.regex(Pattern.compile(Pattern.quote((String) _compareValue), 2));
                        } else if ("$left_like".equals(compare)) {
                            criteria.regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2));
                        } else if ("$right_like".equals(compare)) {
                            criteria.regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2));
                        } else if ("$not_like".equals(compare)) {
                            criteria.not().regex((String) _compareValue);
                        } else if ("$left_like".equals(compare)) {
                            criteria.not().regex(Pattern.compile(Pattern.quote((String) _compareValue + "$"), 2));
                        } else if ("$not_right_like".equals(compare)) {
                            criteria.not().regex(Pattern.compile(Pattern.quote("^" + (String) _compareValue), 2));
                        } else if ("$ne".equals(compare)) {
                            criteria.ne(_compareValue);
                        } else if ("$null".equals(compare)) {
                            criteria.is((Object) null);
                        } else if ("$not_null".equals(compare)) {
                            criteria.not().is((Object) null);
                        } else if ("$not_in".equals(compare)) {
                            criteria.not().in((Collection) _compareValue);
                        } else if ("$where".equals(compare)) {
                            criteria.is(_compareValue);
                        }
                    }
                } else {
                    criteria.is(value);
                }
            }

            criteriaObj.elemMatch(criteria);
            criterias.add(criteriaObj);
            return criterias;
        }
    }

    public List fetchCollection(Map<String, Object> requestArgs) {
        Criteria criteria = getRequestRestriction((HashMap) requestArgs.get("query"));
        String sortField = CommonDaoHelper.getRequestSortField(requestArgs);
        String sortDirection = CommonDaoHelper.getRequestSortDirection(requestArgs);
        Integer pageSize = CommonDaoHelper.getRequestPageSize(requestArgs);
        Integer pageNumber = CommonDaoHelper.getRequestPageNumber(requestArgs);
        if ("-1".equals(sortDirection)) {
            this.setOrderDescField(sortField);
            this.setOrderAscField((String) null);
        } else {
            this.setOrderAscField(sortField);
            this.setOrderDescField((String) null);
        }

        return (ArrayList) this.find(criteria, pageSize, pageNumber);
    }

    public Long fetchCollectionCount(Map<String, Object> requestArgs) {
        Criteria criteria = getRequestRestriction((HashMap) requestArgs.get("query"));
        return Long.valueOf(this.count(criteria));
    }

    public Object load(Object id) {
        return this.findById(id);
    }

    public Object fetchRow(Map<String, Object> requestArgs) {
        Criteria criteria = getRequestRestriction((HashMap) requestArgs.get("query"));
        return this.findOne(criteria);
    }

    public Boolean batchUpdate(Map<String, Object> requestArgs) {
        try {
            Object e = requestArgs.get("ids");
            if (null != e) {
                Update allUpdates = new Update();
                Map updates = (Map) requestArgs.get("updates");
                updates.remove("id");
                updates.remove("ids");
                updates.remove("class");
                Iterator perUpdates = updates.keySet().iterator();

                while (perUpdates.hasNext()) {
                    String id = (String) perUpdates.next();
                    allUpdates.set(id, updates.get(id));
                }

                this.updateMulti(Criteria.where("_id").in((List) e), allUpdates);
            } else {
                List allUpdates1 = (List) requestArgs.get("updates");
                Iterator updates1 = allUpdates1.iterator();

                while (true) {
                    Map perUpdates1;
                    Object id1;
                    do {
                        if (!updates1.hasNext()) {
                            return Boolean.valueOf(true);
                        }

                        perUpdates1 = (Map) updates1.next();
                        id1 = perUpdates1.get("id");
                    } while (null == id1);

                    Update update = new Update();
                    perUpdates1.remove("id");
                    perUpdates1.remove("class");
                    Iterator arg7 = perUpdates1.keySet().iterator();

                    while (arg7.hasNext()) {
                        String key = (String) arg7.next();
                        update.set(key, perUpdates1.get(key));
                    }

                    this.findAndModify(Criteria.where("id").is(id1), update);
                }
            }
        } catch (Exception arg9) {
            arg9.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public Boolean update(Map<String, Object> requestArgs) {
        Object id = requestArgs.get("id");
        if (null == id) {
            return Boolean.valueOf(false);
        } else {
            try {
                Update e = new Update();
                Map updates = (Map) requestArgs.get("updates");
                updates.remove("id");
                updates.remove("class");
                Iterator arg4 = updates.keySet().iterator();

                while (arg4.hasNext()) {
                    String key = (String) arg4.next();
                    e.set(key, updates.get(key));
                }

                this.findAndModify(Criteria.where("id").is(id), e);
                return Boolean.valueOf(true);
            } catch (Exception arg6) {
                arg6.printStackTrace();
                return Boolean.valueOf(false);
            }
        }
    }

    public Boolean save(Map<String, Object> requestArgs) {
        try {
            Object e = this.getEntityClass().newInstance();
            if (null == requestArgs.get("id")) {
                requestArgs.put("id", this.getNextId());
            }

            BeanUtils.populate(e, requestArgs);
            this.saveOrUpdate(e);
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    public void delete(Object object) {
        this.remove(object);
    }

    public Boolean deleteById(Object id) {
        Object object = this.load(id);
        return null == object ? Boolean.valueOf(false) : this.remove(object);
    }

    public BasicDBList group(Map<String, Object> requestArgs) throws Exception {
        Criteria criteria = getRequestRestriction((HashMap) requestArgs.get("query"));
        HashMap groupConditions = (HashMap) requestArgs.get("group");
        if (null == groupConditions) {
            return null;
        } else {
            String groupKey = (String) groupConditions.get("key");
            String groupInitialDocument = (String) groupConditions.get("initialDocument");
            String groupReduceFunction = (String) groupConditions.get("reduceFunction");
            if (null != groupKey && null != groupInitialDocument && null != groupReduceFunction) {
                groupInitialDocument = URLDecoder.decode(groupInitialDocument, "UTF-8")
                        .replace("TOMTOP___PLUS____REPLACE___TOKEN", "+");
                groupReduceFunction = URLDecoder.decode(groupReduceFunction, "UTF-8")
                        .replace("TOMTOP___PLUS____REPLACE___TOKEN", "+");
                GroupBy groupBy = GroupBy.key(groupKey.split(",")).initialDocument(groupInitialDocument)
                        .reduceFunction(groupReduceFunction);
                GroupByResults result = (GroupByResults) this.group(criteria, groupBy);
                BasicDBList o = (BasicDBList) result.getRawResults().get("retval");
                return o;
            } else {
                return null;
            }
        }
    }

    public String getOrderAscField() {
        return this.orderAscField;
    }

    public void setOrderAscField(String orderAscField) {
        this.orderAscField = orderAscField;
    }

    public String getOrderDescField() {
        return this.orderDescField;
    }

    public void setOrderDescField(String orderDescField) {
        this.orderDescField = orderDescField;
    }
}
