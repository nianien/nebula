package com.tinge.nebula.rule;

import com.tinge.nebula.rule.define.MatchType;
import com.tinge.nebula.rule.define.WordType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 词表规则<br/>
 * 注:
 * 1. 可根据{@link #getId()}和{@link #getWordType()}确定唯一规则<br/>
 * 2. 当使用RPC时, 使用关键字transient标记的字段不会回传到客户端
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WordRule implements Serializable {

    /**
     * ID生成器
     */
    private static AtomicLong gen = new AtomicLong(0);
    /**
     * 全局唯一标记,为了避免不同词表ID冲突
     */
    @ToString.Exclude
    private transient final long uuid = gen.incrementAndGet();
    /**
     * 规则Id
     */
    @Include
    private long id;
    /**
     * 规则词
     */
    private String word;

    /**
     * 词表类型,1:黑名单，2：商标词，3：竞品词，4：敏感词
     *
     * @see WordType
     */
    @Include
    private WordType wordType;
    /**
     * 匹配类型
     *
     * @see MatchType
     */
    private MatchType matchType;
    /**
     * 附加信息
     */
    @ToString.Exclude
    private WordExtra extra;


}
