package org.example.ex12;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author lili
 * @date 2020/4/24 23:56
 * @description
 * @notes
 */
public class myex12 {
    public static void main(String[] args) {
        Stream<String> stringStream = Stream.of("11", "22", "33");

        List<String> l1 = Arrays.asList("11", "22", "33");
        List<String> l2 = Arrays.asList("111", "222", "333");
        List<String> l3 = Arrays.asList("1111", "2222", "3333");
        Stream.of(l1, l2, l3).flatMap(List::stream).forEach(System.out::println);

//        StreamSupport.stream();
        //没有终止操作，流没有做任何事
        Stream.of("11", "22", "33").sorted();

        Stream<String> stream = Stream.of("11", "22", "33", "44");
        stream.forEach(System.out::println);
        //流只能被用一次
        stream.forEach(System.out::println);

        //流的起点是data source


    }
}
