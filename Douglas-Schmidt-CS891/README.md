### CS 891: Parallel Functional Programming with Java and Android

https://www.dre.vanderbilt.edu/~schmidt/resume.html

**SITE:** http://www.dre.vanderbilt.edu/~schmidt/cs891f/

**PLAYLIST:** https://www.youtube.com/watch?v=kADtRTxPaf8&list=PLZ9NgFYEMxp5NbV2NQ2moSX-V_Jv1aiGH

学习策略：先看PDF，代码，最后看视频，直接看视频看不懂且理解不深。

https://github.com/douglascraigschmidt/LiveLessons

High Level -> Low Level

https://stackoverflow.com/questions/20974022/c-futures-promises-like-javascript

Classic Java Future(JDK1.5-2004) VS CompletableFuture(JDK1.8-2014)  VS ListenableFuture

ExecutorCompletionService VS ExecutorService

stream+CompletableFuture

juc=concurrency+parallelism

https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/CompletionStage.html

CompletableFuture Framework!
ForkJoinPool Framework!
Executor Framework!
Parallel stream Framework!

ThreadPoolExecutor -> Runnable and Callable
ForkJoinPool -> ForkJoinTask 也可以Runnable and Callable 但是不好

ForkJoinPool，ForkJoinTask，RecursiveAction，RecursiveTask, key method，structure and functionality

ForkJoinPool:WorkQueue,Work Steal

ThreadPool,ForkJoinPool,CompletableFuture

Do not make 方法引用或者lamda有side effect

并行流避免共享可变对象，

commonforkjoinpool is 单例
并行流底层是common fork join pool，但是看不到线程管理，本质等于thread.start,thread.join

流和linux管道操作