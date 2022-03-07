/*
 * This file is generated by jOOQ.
 */
package com.tinge.nebula.entity.rule_dict.tables.records;


import com.tinge.nebula.entity.rule_dict.tables.RuleWord;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record18;
import org.jooq.Row18;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RuleWordRecord extends UpdatableRecordImpl<RuleWordRecord> implements Record18<Long, Integer, Long, String, Integer, Integer, Integer, String, String, String, String, Integer, Integer, String, String, String, Timestamp, Timestamp> {

    private static final long serialVersionUID = 916874303;

    /**
     * Setter for <code>rule_dict.rule_word.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>rule_dict.rule_word.source</code>.
     */
    public void setSource(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.source</code>.
     */
    public Integer getSource() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>rule_dict.rule_word.operator_id</code>.
     */
    public void setOperatorId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.operator_id</code>.
     */
    public Long getOperatorId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>rule_dict.rule_word.word</code>.
     */
    public void setWord(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.word</code>.
     */
    public String getWord() {
        return (String) get(3);
    }

    /**
     * Setter for <code>rule_dict.rule_word.word_type</code>.
     */
    public void setWordType(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.word_type</code>.
     */
    public Integer getWordType() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>rule_dict.rule_word.match_type</code>.
     */
    public void setMatchType(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.match_type</code>.
     */
    public Integer getMatchType() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>rule_dict.rule_word.entity_type</code>.
     */
    public void setEntityType(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.entity_type</code>.
     */
    public Integer getEntityType() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>rule_dict.rule_word.exclude_words</code>.
     */
    public void setExcludeWords(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.exclude_words</code>.
     */
    public String getExcludeWords() {
        return (String) get(7);
    }

    /**
     * Setter for <code>rule_dict.rule_word.exclude_users</code>.
     */
    public void setExcludeUsers(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.exclude_users</code>.
     */
    public String getExcludeUsers() {
        return (String) get(8);
    }

    /**
     * Setter for <code>rule_dict.rule_word.exclude_customers</code>.
     */
    public void setExcludeCustomers(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.exclude_customers</code>.
     */
    public String getExcludeCustomers() {
        return (String) get(9);
    }

    /**
     * Setter for <code>rule_dict.rule_word.industries</code>.
     */
    public void setIndustries(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.industries</code>.
     */
    public String getIndustries() {
        return (String) get(10);
    }

    /**
     * Setter for <code>rule_dict.rule_word.grade</code>.
     */
    public void setGrade(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.grade</code>.
     */
    public Integer getGrade() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>rule_dict.rule_word.status</code>.
     */
    public void setStatus(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.status</code>.
     */
    public Integer getStatus() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>rule_dict.rule_word.remark</code>.
     */
    public void setRemark(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.remark</code>.
     */
    public String getRemark() {
        return (String) get(13);
    }

    /**
     * Setter for <code>rule_dict.rule_word.risk_tag</code>.
     */
    public void setRiskTag(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.risk_tag</code>.
     */
    public String getRiskTag() {
        return (String) get(14);
    }

    /**
     * Setter for <code>rule_dict.rule_word.extra_data</code>.
     */
    public void setExtraData(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.extra_data</code>.
     */
    public String getExtraData() {
        return (String) get(15);
    }

    /**
     * Setter for <code>rule_dict.rule_word.create_time</code>.
     */
    public void setCreateTime(Timestamp value) {
        set(16, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.create_time</code>.
     */
    public Timestamp getCreateTime() {
        return (Timestamp) get(16);
    }

    /**
     * Setter for <code>rule_dict.rule_word.modify_time</code>.
     */
    public void setModifyTime(Timestamp value) {
        set(17, value);
    }

    /**
     * Getter for <code>rule_dict.rule_word.modify_time</code>.
     */
    public Timestamp getModifyTime() {
        return (Timestamp) get(17);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record18 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row18<Long, Integer, Long, String, Integer, Integer, Integer, String, String, String, String, Integer, Integer, String, String, String, Timestamp, Timestamp> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row18<Long, Integer, Long, String, Integer, Integer, Integer, String, String, String, String, Integer, Integer, String, String, String, Timestamp, Timestamp> valuesRow() {
        return (Row18) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return RuleWord.RULE_WORD.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return RuleWord.RULE_WORD.SOURCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return RuleWord.RULE_WORD.OPERATOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return RuleWord.RULE_WORD.WORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return RuleWord.RULE_WORD.WORD_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return RuleWord.RULE_WORD.MATCH_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return RuleWord.RULE_WORD.ENTITY_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return RuleWord.RULE_WORD.EXCLUDE_WORDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return RuleWord.RULE_WORD.EXCLUDE_USERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return RuleWord.RULE_WORD.EXCLUDE_CUSTOMERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return RuleWord.RULE_WORD.INDUSTRIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return RuleWord.RULE_WORD.GRADE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return RuleWord.RULE_WORD.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return RuleWord.RULE_WORD.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field15() {
        return RuleWord.RULE_WORD.RISK_TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field16() {
        return RuleWord.RULE_WORD.EXTRA_DATA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field17() {
        return RuleWord.RULE_WORD.CREATE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field18() {
        return RuleWord.RULE_WORD.MODIFY_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getOperatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component5() {
        return getWordType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component6() {
        return getMatchType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component7() {
        return getEntityType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getExcludeWords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getExcludeUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getExcludeCustomers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getIndustries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component12() {
        return getGrade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component13() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component14() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component15() {
        return getRiskTag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component16() {
        return getExtraData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component17() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component18() {
        return getModifyTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getOperatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getWordType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getMatchType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getEntityType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getExcludeWords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getExcludeUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getExcludeCustomers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getIndustries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getGrade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value15() {
        return getRiskTag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value16() {
        return getExtraData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value17() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value18() {
        return getModifyTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value2(Integer value) {
        setSource(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value3(Long value) {
        setOperatorId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value4(String value) {
        setWord(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value5(Integer value) {
        setWordType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value6(Integer value) {
        setMatchType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value7(Integer value) {
        setEntityType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value8(String value) {
        setExcludeWords(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value9(String value) {
        setExcludeUsers(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value10(String value) {
        setExcludeCustomers(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value11(String value) {
        setIndustries(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value12(Integer value) {
        setGrade(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value13(Integer value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value14(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value15(String value) {
        setRiskTag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value16(String value) {
        setExtraData(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value17(Timestamp value) {
        setCreateTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord value18(Timestamp value) {
        setModifyTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleWordRecord values(Long value1, Integer value2, Long value3, String value4, Integer value5, Integer value6, Integer value7, String value8, String value9, String value10, String value11, Integer value12, Integer value13, String value14, String value15, String value16, Timestamp value17, Timestamp value18) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RuleWordRecord
     */
    public RuleWordRecord() {
        super(RuleWord.RULE_WORD);
    }

    /**
     * Create a detached, initialised RuleWordRecord
     */
    public RuleWordRecord(Long id, Integer source, Long operatorId, String word, Integer wordType, Integer matchType, Integer entityType, String excludeWords, String excludeUsers, String excludeCustomers, String industries, Integer grade, Integer status, String remark, String riskTag, String extraData, Timestamp createTime, Timestamp modifyTime) {
        super(RuleWord.RULE_WORD);

        set(0, id);
        set(1, source);
        set(2, operatorId);
        set(3, word);
        set(4, wordType);
        set(5, matchType);
        set(6, entityType);
        set(7, excludeWords);
        set(8, excludeUsers);
        set(9, excludeCustomers);
        set(10, industries);
        set(11, grade);
        set(12, status);
        set(13, remark);
        set(14, riskTag);
        set(15, extraData);
        set(16, createTime);
        set(17, modifyTime);
    }
}