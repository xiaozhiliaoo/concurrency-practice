package org.concurrency.parallel.functional.sample.cf;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lili
 * @date 2022/7/17 22:38
 */
class User {

}

class GeoLocation {

}

class Ticket {

}

class Flight {

}

class TravelAgency {


    public Flight search(User user, GeoLocation location) {
        return new Flight();
    }

    public CompletableFuture<Flight> searchAsync(User user, GeoLocation location) {
        return CompletableFuture.supplyAsync(() -> search(user, location));
    }
}


public class CompletableFutureDemo {

    public User findById(long id) {
        return new User();
    }

    public GeoLocation locate() {
        return new GeoLocation();
    }

    public Ticket book(Flight flight) {
        return new Ticket();
    }


    CompletableFuture<User> findByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    CompletableFuture<GeoLocation> locateAsync() {
        return CompletableFuture.supplyAsync(() -> locate());
    }

    CompletableFuture<Ticket> bookAsync(Flight flight) {
        return CompletableFuture.supplyAsync(() -> book(flight));
    }

    CompletableFuture<User> getUserDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> new User());
    }

    CompletableFuture<Double> getCreditRating(User user) {
        return CompletableFuture.supplyAsync(() -> Double.parseDouble(user.toString()));
    }


    @Test
    @SneakyThrows
    public void t02() {
        //结果组合。异构任务。
        CompletableFuture<String> userName = CompletableFuture.supplyAsync(() -> "用户姓名");
        CompletableFuture<Double> userHeight = CompletableFuture.supplyAsync(() -> 0.0);
        CompletableFuture<Map<String, Double>> merge = userName.thenCombine(userHeight, (n, h) -> {
            Map<String, Double> r = new HashMap<>();
            r.put(n, h);
            return r;
        });
        System.out.println(merge.get());
    }


    public CompletableFuture<String> downloadWebPage(String link) {
        return CompletableFuture.supplyAsync(() -> link);
    }


    @Test
    @SneakyThrows
    public void downloadWebPage() {
        //1000个url同时下载网页内容，等待下载完，统计1000个里面出现XXX的个数
        List<String> webPageLinks = new ArrayList<>(1000);
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(link -> downloadWebPage(link))
                .collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()]));
        CompletableFuture<List<String>> allPage = allFutures.thenApply(v -> {
            return pageContentFutures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        });
        CompletableFuture<Long> count = allPage.thenApply(content -> {
            return content.stream().filter(x -> x.contains("XXX")).count();
        });
        System.out.println(count.get());
    }


    @Test
    public void t1() {
        List<TravelAgency> agencyList = new ArrayList<>();
        CompletableFuture<User> user = findByIdAsync(1L);
        CompletableFuture<GeoLocation> location = locateAsync();
        CompletableFuture<Ticket> ticket = user.thenCombine(location, (User us, GeoLocation loc) ->
                        agencyList.stream()
                                .map(agency -> agency.searchAsync(us, loc))
                                .reduce((f1, f2) -> f1.applyToEither(f2, Function.identity()))
                                .get())
                .thenCompose(Function.identity())
                .thenCompose(this::bookAsync);
    }


    @Test
    public void t2() {
        List<TravelAgency> agencyList = new ArrayList<>();
        CompletableFuture<User> user = findByIdAsync(1L);
        CompletableFuture<GeoLocation> location = locateAsync();
        CompletableFuture<Ticket> ticket = user
                .thenCombine(location, (User us, GeoLocation loc) -> {
                    List<CompletableFuture<Flight>> fs = agencyList.stream()
                            .map(agency -> agency.searchAsync(us, loc))
                            .collect(Collectors.toList());

                    CompletableFuture[] fsArr = new CompletableFuture[fs.size()];
                    fs.toArray(fsArr);
                    return CompletableFuture.anyOf(fsArr).thenApply(x -> ((Flight) x));
                })
                .thenCompose(Function.identity())
                .thenCompose(this::bookAsync);
    }
}