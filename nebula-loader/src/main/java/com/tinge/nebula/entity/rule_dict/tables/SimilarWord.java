/*
 * This file is generated by jOOQ.
 */
package com.tinge.nebula.entity.rule_dict.tables;


import com.tinge.nebula.entity.rule_dict.Indexes;
import com.tinge.nebula.entity.rule_dict.Keys;
import com.tinge.nebula.entity.rule_dict.RuleDict;
import com.tinge.nebula.entity.rule_dict.tables.records.SimilarWordRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SimilarWord extends TableImpl<SimilarWordRecord> {

    private static final long serialVersionUID = -364952029;

    /**
     * The reference instance of <code>rule_dict.similar_word</code>
     */
    public static final SimilarWord SIMILAR_WORD = new SimilarWord();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SimilarWordRecord> getRecordType() {
        return SimilarWordRecord.class;
    }

    /**
     * The column <code>rule_dict.similar_word.id</code>.
     */
    public final TableField<SimilarWordRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>rule_dict.similar_word.operator_id</code>.
     */
    public final TableField<SimilarWordRecord, Long> OPERATOR_ID = createField("operator_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("'-1'", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>rule_dict.similar_word.source</code>.
     */
    public final TableField<SimilarWordRecord, Integer> SOURCE = createField("source", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>rule_dict.similar_word.original_word</code>.
     */
    public final TableField<SimilarWordRecord, String> ORIGINAL_WORD = createField("original_word", org.jooq.impl.SQLDataType.VARCHAR(1024).nullable(false).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>rule_dict.similar_word.similar_word</code>.
     */
    public final TableField<SimilarWordRecord, String> SIMILAR_WORD_ = createField("similar_word", org.jooq.impl.SQLDataType.VARCHAR(4096).nullable(false).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>rule_dict.similar_word.status</code>.
     */
    public final TableField<SimilarWordRecord, Integer> STATUS = createField("status", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("'1'", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>rule_dict.similar_word.remark</code>.
     */
    public final TableField<SimilarWordRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR(1024).nullable(false).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>rule_dict.similar_word.extra_data</code>.
     */
    public final TableField<SimilarWordRecord, String> EXTRA_DATA = createField("extra_data", org.jooq.impl.SQLDataType.VARCHAR(1024).nullable(false).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>rule_dict.similar_word.create_time</code>.
     */
    public final TableField<SimilarWordRecord, Timestamp> CREATE_TIME = createField("create_time", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6).nullable(false).defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP()", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>rule_dict.similar_word.modify_time</code>.
     */
    public final TableField<SimilarWordRecord, Timestamp> MODIFY_TIME = createField("modify_time", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6).nullable(false).defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP()", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>rule_dict.similar_word</code> table reference
     */
    public SimilarWord() {
        this(DSL.name("similar_word"), null);
    }

    /**
     * Create an aliased <code>rule_dict.similar_word</code> table reference
     */
    public SimilarWord(String alias) {
        this(DSL.name(alias), SIMILAR_WORD);
    }

    /**
     * Create an aliased <code>rule_dict.similar_word</code> table reference
     */
    public SimilarWord(Name alias) {
        this(alias, SIMILAR_WORD);
    }

    private SimilarWord(Name alias, Table<SimilarWordRecord> aliased) {
        this(alias, aliased, null);
    }

    private SimilarWord(Name alias, Table<SimilarWordRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> SimilarWord(Table<O> child, ForeignKey<O, SimilarWordRecord> key) {
        super(child, key, SIMILAR_WORD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return RuleDict.RULE_DICT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<SimilarWordRecord, Long> getIdentity() {
        return Keys.IDENTITY_SIMILAR_WORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SimilarWordRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SimilarWordRecord>> getKeys() {
        return Arrays.<UniqueKey<SimilarWordRecord>>asList(Keys.CONSTRAINT_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimilarWord as(String alias) {
        return new SimilarWord(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimilarWord as(Name alias) {
        return new SimilarWord(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SimilarWord rename(String name) {
        return new SimilarWord(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SimilarWord rename(Name name) {
        return new SimilarWord(name, null);
    }
}
