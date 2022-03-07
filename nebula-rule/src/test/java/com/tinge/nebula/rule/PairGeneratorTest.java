package com.tinge.nebula.rule;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.tinge.nebula.rule.filter.wordfilter.DefaultWordFilter;
import com.tinge.nebula.rule.filter.wordfilter.PredicateFactory;
import com.tinge.nebula.rule.filter.wordfilter.WordFilter;
import com.tinge.nebula.rule.pair.Pair;
import com.tinge.nebula.rule.pair.PairMatcher;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class PairGeneratorTest {


    @Test
    public void test() {
        Criterion idea = Criterion.builder()
                .id(123L)
                .texts(new String[]{"你喜欢什么颜色{选择}呢"}).build();
        Criterion idea2 = Criterion.builder()
                .id(1234L)
                .texts(new String[]{"你喜欢什么颜色{选择}{}呢"}).build();
        PairMatcher generator = new PairMatcher(null, null);//(Arrays.asList(idea, idea2), Sets.newHashSet("情况", "中国"));
        String[] words = new String[]{"情况", "中国"};
        List<Pair> pairs = generator.genPairs(Arrays.asList(idea, idea2), words);
        pairs.forEach(pair -> System.out.println(pair.getText() + ":" + pair.getWord()));
        assertThat(pairs.size(), equalTo(4));
    }


    @Test
    public void testFilter() throws IOException, ClassNotFoundException {
        WordFilter filter = new DefaultWordFilter(Arrays.asList("色情", "情色电影中心"), PredicateFactory::triePredicate);

        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
        outputStream.writeObject(filter);
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteOutputStream.getBytes()));
        filter = (WordFilter) inputStream.readObject();
        
        String text = "你喜欢什么颜色{选择}{}呢";
        int begin = text.indexOf("{");
        int end = text.indexOf("}");
        assertThat(filter.filter(text, begin, end, "情况"), equalTo(true));
        assertThat(filter.filter(text, begin, end, "电影中国"), equalTo(true));
        assertThat(filter.filter(text, begin, end, "情色"), equalTo(true));
        assertThat(filter.filter(text, begin, end, "色情"), equalTo(false));
        begin = text.indexOf("{", begin + 1);
        end = text.indexOf("}", end + 1);
        assertThat(filter.filter(text, begin, end, "情呢"), equalTo(false));
    }


}
