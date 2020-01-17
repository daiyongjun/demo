package cn.yanwei.study.elastic.search.query.api.models;

import cn.yanwei.study.elastic.search.query.api.constants.OperationKey;
import lombok.Data;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * Criteria is the central class when constructing queries. It follows more or less a fluent API style, which allows to
 * easily chain together multiple criteria.
 *
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Franck Marchand
 * @author Peter-Josef Meisch
 */
@Data
@SuppressWarnings("unused")
public class Criteria implements Serializable, Cloneable {
    public static final String WILDCARD = "*";

    private int recursion;
    private static final String CRITERIA_VALUE_SEPERATOR = " ";
    private static final String OR_OPERATOR = " OR ";
    private static final String AND_OPERATOR = " AND ";

    private Field field;
    private float boost = Float.NaN;
    private boolean negating = false;

    private List<Criteria> criteriaChain = new ArrayList<>(1);

    private Set<CriteriaEntry> queryCriteria = new LinkedHashSet<>();

    @Override
    public Criteria clone() {
        Criteria clone = null;
        try {
            clone = (Criteria) super.clone();
        } catch (CloneNotSupportedException ignored) {

        }
        return clone;
    }

    public Criteria() {
    }

    /**
     * Creates a new Criteria with provided field name
     *
     * @param fieldname 操作的列名
     */
    public Criteria(String fieldname) {
        this(new SimpleField(fieldname));
    }

    /**
     * Creates a new Criteria for the given field
     *
     * @param field 操作的列对象
     */
    private Criteria(Field field) {
        Assert.notNull(field, "Field for criteria must not be null");
        Assert.hasText(field.getName(), "Field.name for criteria must not be null/empty");
        this.criteriaChain.add(this);
        this.field = field;
    }

    private Criteria(List<Criteria> criteriaChain, String fieldname) {
        this(criteriaChain, new SimpleField(fieldname));
    }

    private Criteria(List<Criteria> criteriaChain, Field field) {
        Assert.notNull(criteriaChain, "CriteriaChain must not be null");
        Assert.notNull(field, "Field for criteria must not be null");
        Assert.hasText(field.getName(), "Field.name for criteria must not be null/empty");
        this.criteriaChain.addAll(criteriaChain);
        this.field = field;
        Criteria criteria = this.clone();
        criteria.recursion = 0;
        this.criteriaChain.add(criteria);
    }

    /**
     * Static factory method to create a new Criteria for field with given name
     *
     * @param field 操作的列对象
     * @return Criteria
     */
    public static Criteria where(String field) {
        return where(new SimpleField(field));
    }

    /**
     * Static factory method to create a new Criteria for provided field
     *
     * @param field 操作的列对象
     * @return Criteria
     */
    private static Criteria where(Field field) {
        return new Criteria(field);
    }

    /**
     * Chain using {@code AND}
     *
     * @param fieldName 操作的列名
     * @return Criteria
     */
    public Criteria and(String fieldName) {
        Criteria criteria = new Criteria(this.criteriaChain, fieldName);
        criteria.recursion = this.recursion;
        return criteria;
    }

    /**
     * Chain using {@code AND}
     *
     * @param criteria 操作的列对象
     * @return Criteria
     */
    public Criteria and(Criteria criteria) {
        if (this.getCriteriaChain().size() == 0) {
            Criteria appearance = new Criteria();
            this.criteriaChain.add(criteria);
            this.recursion += 1;
            appearance.criteriaChain.add(this);
            return appearance;
        } else {
            Criteria appearance = new Criteria();
            appearance.criteriaChain.add(criteria);
            appearance.recursion += 1;
            this.criteriaChain.add(appearance);
            return this;
        }
    }

    /**
     * Chain using {@code OR}
     *
     * @return Criteria
     */
    public Criteria or() {
        ListIterator<Criteria> chainIterator = this.getCriteriaChain().listIterator();
        List<Criteria> criteria = new ArrayList<>();
        while (chainIterator.hasNext()) {
            Criteria chainedCriteria = chainIterator.next();
            Criteria or = new OrCriteria();
            or.criteriaChain.addAll(chainedCriteria.criteriaChain);
            or.queryCriteria.addAll(chainedCriteria.queryCriteria);
            or.setField(this.getField());
            criteria.add(or);
        }
        this.criteriaChain.clear();
        this.criteriaChain.addAll(criteria);
        return this;
    }

    /**
     * Chain using {@code OR}
     *
     * @param fieldName 操作的列
     * @return Criteria
     */
    public Criteria or(String fieldName) {
        return or(new SimpleField(fieldName));
    }

    /**
     * Chain using {@code OR}
     *
     * @param field 操作的列对象
     * @return Criteria
     */
    private Criteria or(Field field) {
        return new OrCriteria(this.criteriaChain, field);
    }

    /**
     * Chain using {@code OR}
     *
     * @param criteria 操作的列对象
     * @return Criteria
     */
    public Criteria or(Criteria criteria) {
        Assert.notNull(criteria, "Cannot chain 'null' criteria.");
        Criteria appearance = new OrCriteria();
        Criteria orConnectedCritiera = new OrCriteria();
        orConnectedCritiera.criteriaChain.addAll(criteria.criteriaChain);
        orConnectedCritiera.queryCriteria.addAll(criteria.queryCriteria);
        orConnectedCritiera.setField(criteria.getField());
        if (this.getCriteriaChain().size() == 0) {
            Criteria replace = new OrCriteria();
            replace.criteriaChain.add(orConnectedCritiera);
            replace.recursion += 1;
            appearance.criteriaChain.add(replace);
            return appearance;
        } else {
            appearance.criteriaChain.add(orConnectedCritiera);
            appearance.recursion += 1;
            this.criteriaChain.add(appearance);
            return this;
        }
    }

    /**
     * Crates new CriteriaEntry with trailing -
     *
     * @return Criteria
     */
    public Criteria not() {
        this.negating = true;
        return this;
    }

    /**
     * Crates new CriteriaEntry without any wildcards
     *
     * @param o 需要匹配的对象
     * @return Criteria
     */
    public Criteria is(Object o) {
        queryCriteria.add(new CriteriaEntry(OperationKey.EQUALS, o));
        return this;
    }

    /**
     * Crates new CriteriaEntry match
     *
     * @param o 需要匹配的对象
     * @return Criteria
     */
    public Criteria match(Object o) {
        queryCriteria.add(new CriteriaEntry(OperationKey.MATCH, o));
        return this;
    }

    /**
     * Crates new CriteriaEntry phrase
     *
     * @param o 需要匹配的对象
     * @return Criteria
     */
    public Criteria phrase(Object o) {
        queryCriteria.add(new CriteriaEntry(OperationKey.MATCH_PHRASE, o));
        return this;
    }

    /**
     * Crates new CriteriaEntry contains
     *
     * @param s 需要匹配的对象
     * @return Criteria
     */
    public Criteria contains(String s) {
        assertNoBlankInWildcardedQuery(s, true, true);
        queryCriteria.add(new CriteriaEntry(OperationKey.CONTAINS, s));
        return this;
    }

    /**
     * Crates new CriteriaEntry starts_with
     *
     * @param s 需要匹配的对象
     * @return Criteria
     */
    public Criteria startsWith(String s) {
        assertNoBlankInWildcardedQuery(s, true, false);
        queryCriteria.add(new CriteriaEntry(OperationKey.STARTS_WITH, s));
        return this;
    }

    /**
     * Crates new CriteriaEntry ends_with
     *
     * @param s 需要匹配的对象
     * @return Criteria
     */
    public Criteria endsWith(String s) {
        assertNoBlankInWildcardedQuery(s, false, true);
        queryCriteria.add(new CriteriaEntry(OperationKey.ENDS_WITH, s));
        return this;
    }

    /**
     * Crates new CriteriaEntry fuzzy
     *
     * @param s 需要匹配的对象
     * @return Criteria
     */
    public Criteria fuzzy(String s) {
        queryCriteria.add(new CriteriaEntry(OperationKey.FUZZY, s));
        return this;
    }

    /**
     * Crates new CriteriaEntry boost
     *
     * @param boost 需要匹配的对象
     * @return Criteria
     */
    public Criteria boost(float boost) {
        if (boost < 0) {
            throw new InvalidDataAccessApiUsageException("Boost must not be negative.");
        }
        this.boost = boost;
        return this;
    }

    /**
     * Crates new CriteriaEntry for {@code RANGE [lowerBound TO upperBound]}
     *
     * @param lowerBound lower
     * @param upperBound upper
     * @return Criteria
     */
    public Criteria between(Object lowerBound, Object upperBound) {
        if (lowerBound == null && upperBound == null) {
            throw new InvalidDataAccessApiUsageException("Range [* TO *] is not allowed");
        }

        queryCriteria.add(new CriteriaEntry(OperationKey.BETWEEN, new Object[]{lowerBound, upperBound}));
        return this;
    }

    /**
     * Crates new CriteriaEntry for {@code RANGE [* TO upperBound]}
     *
     * @param upperBound upper
     * @return Criteria
     */
    public Criteria lessThanEqual(Object upperBound) {
        if (upperBound == null) {
            throw new InvalidDataAccessApiUsageException("UpperBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(OperationKey.LESS_EQUAL, upperBound));
        return this;
    }

    public Criteria lessThan(Object upperBound) {
        if (upperBound == null) {
            throw new InvalidDataAccessApiUsageException("UpperBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(OperationKey.LESS, upperBound));
        return this;
    }

    /**
     * Crates new CriteriaEntry for {@code RANGE [lowerBound TO *]}
     *
     * @param lowerBound low
     * @return Criteria
     */
    public Criteria greaterThanEqual(Object lowerBound) {
        if (lowerBound == null) {
            throw new InvalidDataAccessApiUsageException("LowerBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(OperationKey.GREATER_EQUAL, lowerBound));
        return this;
    }

    public Criteria greaterThan(Object lowerBound) {
        if (lowerBound == null) {
            throw new InvalidDataAccessApiUsageException("LowerBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(OperationKey.GREATER, lowerBound));
        return this;
    }

    /**
     * Crates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values 多个参数
     * @return Criteria
     */
    public Criteria in(Object... values) {
        return in(toCollection(values));
    }

    /**
     * Crates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values the collection containing the values to match against
     * @return Criteria
     */
    private Criteria in(Iterable<?> values) {
        Assert.notNull(values, "Collection of 'in' values must not be null");
        queryCriteria.add(new CriteriaEntry(OperationKey.IN, values));
        return this;
    }

    private List<Object> toCollection(Object... values) {
        boolean condition = values.length == 0 || (values.length > 1 && values[1] instanceof Collection);
        if (condition) {
            throw new InvalidDataAccessApiUsageException(
                    "At least one element " + (values.length > 0 ? ("of argument of type " + values[1].getClass().getName()) : "")
                            + " has to be present.");
        }
        return Arrays.asList(values);
    }

    private void assertNoBlankInWildcardedQuery(String searchString, boolean leadingWildcard, boolean trailingWildcard) {
        if (searchString != null && searchString.contains(CRITERIA_VALUE_SEPERATOR)) {
            throw new InvalidDataAccessApiUsageException("Cannot constructQuery '" + (leadingWildcard ? "*" : "") + "\""
                    + searchString + "\"" + (trailingWildcard ? "*" : "") + "'. Use expression or multiple clauses instead.");
        }
    }

    /**
     * Field targeted by this Criteria
     *
     * @return Field
     */
    public Field getField() {
        return this.field;
    }

    public Set<CriteriaEntry> getQueryCriteriaEntries() {
        return Collections.unmodifiableSet(this.queryCriteria);
    }


    /**
     * Conjunction to be used with this criteria (AND | OR)
     *
     * @return String
     */
    public String getConjunctionOperator() {
        return AND_OPERATOR;
    }

    public List<Criteria> getCriteriaChain() {
        return Collections.unmodifiableList(this.criteriaChain);
    }

    public boolean isNegating() {
        return this.negating;
    }

    public boolean isOr() {
        return OR_OPERATOR.equals(getConjunctionOperator());
    }

    public float getBoost() {
        return this.boost;
    }

    public static class OrCriteria extends Criteria {
        OrCriteria() {
            super();
        }

        OrCriteria(List<Criteria> criteriaChain, Field field) {
            super(criteriaChain, field);
        }

        @Override
        public String getConjunctionOperator() {
            return OR_OPERATOR;
        }
    }

    @Data
    public static class CriteriaEntry {

        private OperationKey key;
        private Object value;

        CriteriaEntry(OperationKey key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}