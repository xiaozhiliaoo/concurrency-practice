package webcrawler;

import org.apache.commons.io.IOUtils;
import vo.Result;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author lili
 * @date 2020/4/9 16:21
 * @description
 * @notes
 */


public class ExecutorCompletionServiceVersion {

    static final List<String> topSites = Arrays.asList(
            "www.google.com", "www.youtube.com", "www.yahoo.com", "www.msn.com",
            "www.wikipedia.org", "www.baidu.com", "www.microsoft.com", "www.qq.com",
            "www.bing.com", "www.ask.com", "www.adobe.com", "www.taobao.com",
            "www.youku.com", "www.soso.com", "www.wordpress.com", "www.sohu.com",
            "www.windows.com", "www.163.com", "www.tudou.com", "www.amazon.com"
    );


    public void method1() throws ExecutionException, InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<String>> contentsFutures = new ArrayList<>(topSites.size());
        for (final String site : topSites) {
            final Future<String> contentFuture = pool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return IOUtils.toString(new URL("http://" + site), StandardCharsets.UTF_8);
                }
            });
            contentsFutures.add(contentFuture);
        }

        for (Future<String> contentFuture : contentsFutures) {

            final String content = contentFuture.get();
            //...process contents

        }
    }

    public void method2() throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(5);
        final ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(pool);
        for (final String site : topSites) {
            completionService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return IOUtils.toString(new URL("http://" + site), StandardCharsets.UTF_8);
                }
            });
        }

        for (int i = 0; i < topSites.size(); ++i) {
            final Future<String> future = completionService.take();
            try {
                final String content = future.get();
                //...process contents
            } catch (ExecutionException e) {
                System.out.println("Error while downloading" + e.getCause());
            }
        }

    }

    void solve(Executor e, Collection<Callable<Result>> solvers)  throws InterruptedException,ExecutionException  {
        CompletionService<Result> ecs = new ExecutorCompletionService<>(e);
        for (Callable<Result> solver : solvers) {
            ecs.submit(solver);
        }
        int n = solvers.size();
        for (int i = 0; i < n; i++) {
            Result result = ecs.take().get();
            if (result != null) {
                use(result);
                //process result
            }
        }
    }

    void solve2(Executor e, Collection<Callable<Result>> solvers)
            throws InterruptedException {
        CompletionService<Result> ecs = new ExecutorCompletionService<Result>(e);
        int n = solvers.size();
        List<Future<Result>> futures = new ArrayList<Future<Result>>(n);
        Result result = null;
        try {
            for (Callable<Result> s : solvers) {
                futures.add(ecs.submit(s));
            }
            for (int i = 0; i < n; ++i) {
                try {
                    Result r = ecs.take().get();
                    if (r != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ignore) {
                }
            }
        } finally {
            for (Future<Result> f : futures)
                f.cancel(true);
        }

        if (result != null) {
            use(result);
        }
    }

    private void use(Result result) {

    }


    public static void main(String[] args) {

    }
}
