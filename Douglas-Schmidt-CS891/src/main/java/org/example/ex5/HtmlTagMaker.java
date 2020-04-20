package org.example.ex5;

import java.util.function.Function;

/**
 * @author lili
 * @date 2020/4/20 23:52
 * @description
 * @notes
 */
public class HtmlTagMaker {
    static String addLessThan(String t) {
        return "<" + t;
    }

    static String addGreaterThan(String t) {
        return t + ">";
    }

    public static void main(String[] args) {
        Function<String, String> lessThan = HtmlTagMaker::addLessThan;
        Function<String, String> tagger = lessThan.andThen(HtmlTagMaker::addGreaterThan);
        System.out.println(tagger.apply("HTML") + tagger.apply("BODY")
                + tagger.apply("/BODY") + tagger.apply("/HTML"));

    }

}
