package webcrawler;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author lili
 * @date 2020/4/9 0:58
 * @description
 * @notes
 */

public class CompletableFutureVersion {

    private String downloadSite(final String site) {
        try {
            System.out.println("Downloading:"+site);
            final String res = IOUtils.toString(new URL("http://" + site), UTF_8);
            System.out.println("Done:"+site);
            return res;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Document
     * @param xml
     * @return
     */
    private Document parse(String xml) {
        return null;
    }

    private CompletableFuture<Double> calculateRelevance(Document doc) {
        return null;
    }


    public void work() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<String> topSites = Arrays.asList("www.google.com", "www.youtube.com", "www.yahoo.com", "www.msn.com");
        topSites.stream().map(site-> CompletableFuture.supplyAsync(()-> downloadSite(site), executor))
                .map(contentFuture->contentFuture.thenApply(this::parse))
                .map(docFuture -> docFuture.thenCompose(this::calculateRelevance))
                .collect(Collectors.<CompletableFuture<Double>>toList());

    }

    public static void main(String[] args) {

    }
}
