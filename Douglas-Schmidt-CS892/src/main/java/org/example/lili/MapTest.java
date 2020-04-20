package org.example.lili;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author lili
 * @date 2020/4/19 23:19
 * @description
 * @notes
 */
public class MapTest {
    public static void main(String[] args) {
        Map<String,String> map = new ConcurrentHashMap<>();
        map.replace("1","2");
        map.replace("1","2","3");
        map.computeIfAbsent("1", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        });

        Map<String,String> map2 = new HashMap<>();
        map2.replace("1","2");
        map.computeIfAbsent("1",k->k);





    }
}
