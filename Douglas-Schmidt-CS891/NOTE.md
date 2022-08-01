# 课程

## 17 Overview of Java Concurrency

Thread（State）

Shared Object(Mutual exclusion，Coordination)

Java Synchronizer: Synchronize Statement/method，Reentrant, Atomic Operation, Semaphore, Condition object, CAS

## 18 Overview of Parallelism in Java

Split/Apply/Combine

必须要高效分拆任务以及高效组合结果。

Parallelism work best when thread share no mutable state and do not block

fork join + work-stealing

Java并行三框架：Parallel Stream（函数式框架），Completable Future（反应式异步框架），ForkJoinPool（面向对象的框架）

forkjoin不要阻塞，所以可以和异步结合起来。一个操作，

## 19 A Brief History Of Concurrency in Java

Java1.0 thread,wait,notify,synchronize

Java5: executor framework,synchronizer,blocking queue, atomic concurrent collections 【coarse grained task parallelism】
tedious and error-prone

Java7：fork join pool，same computation on different data elements，data parallelism，same task。大任务分成小任务。分的是数据。
tedious to program directly，OO

Javv8：Parallel stream functional and reactive，productivity and performance

## 20 Evaluation of Concurrency and Parallelism

Java并发包：1 面向对象的并发设计，面向共享对象（low level classes，框架开发） 2 面向函数式的并行设计，面向数据（high level class，应用开发，CF，RxJava）。

## 72 Overview of Java Parallelism Frameworks

fj：object-oriented functional，divide-conquer，fine-grained task execution for data parallelism
ps：synchronous functional.
cf：reactive asynchronous functional.

## 75 Overview of Java Parallel Streams: Transitioning to Parallelism

避免共享状态需要同步

## 76 Overview of Java Parallel Streams: Phases

Split（Spliterator）-Apply（）-Combine

## 77 Overview of Java Parallel Streams: Avoiding Programming Hazards

避免共享状态，没有Side-effect，所以不用显示同步了。

如果可变状态必须访问，那么使用并发集合。

## 80 Java Parallel Streams Internals: Introduction

• Split – Uses a spliterator to partition stream elements into multiple chunks（自己定义）
• Apply – Independently processes these chunks in the common fork-join pool（很难自定义）
• Combine – Joins partial sub-results into a single result（自己定义）

Concurrency Collector

## 81 Java SearchWithParallelStreams Example: Implementing Hook Methods

IO任务，线程数多于核心数。并行流的common-pool是固定的。

## 82 Java SearchWithParallelStreams Example: Evaluating Pros and Cons

## 83 Java Parallel Streams Internals: Splitting, Combining, & Pooling

Java集合Collection已经定义了spliterator和parallelStream

Collectors

ManagedBlocker可以创建比common-pool更多的线程。用于自定义common-pool的线程。

## 84 Java Parallel Streams Internals: Order of Processing

顺序无法控制，因为分成了chunks

work-stealing no-deterministic optimization

这一步无法控制。Splitting, Combining,可以。

## 85 Java Parallel Streams Internals: Order of Results (Part 1)

Encounter order

## 86 Java Parallel Streams Internals: Order of Results (Part 2)

Collections affect result order.

## 87 Java Parallel Streams Internals: Order of Results (Part 3)

Operations affect result order.(sorted,unordered,skip,limit)

stateful导致全局操作，会使得速度变慢。

forEachOrder forEach

## 88 Java Parallel Stream Internals: Partitioning

trySplit- partition

ArrayList trySplit tryAdvance

LinkedList trySplit tryAdvance 分割找到mid比较难

## 89 Java Parallel Stream Internals: Parallel Processing via the Common ForkJoinPool

ForkJoinPool executes Fork

FJ Task比线程轻量级。

很多任务运行在很少的worker线程。、

并行流是fj的facade。

fjp is best used for program that not match the parallel stream model.

所有并行流共享common pool。

## 90 Java Parallel Stream Internals: Mapping onto the Common Fork-Join Pool

Goal is keep worker thread busy.

WorkQueue is Deque.

AbstractTask:java.util.stream.AbstractTask，是ForkJoinTask.

很少等待，sleep，waiting要减少。

work-steal平衡处理速度。为了线程一直在运行。

## 91 Java Parallel Streams Internals: Configuring the Common Fork-Join Pool

并行流使用所有核，甚至会使用main线程

并行流中使用阻塞操作会有问题，如下载10张图片，但是只有4个线程，那么线程会阻塞住，cpu利用率很低。其他6个线程应该去下载，但是由于线程数是固定的。

线程阻塞在io，并且没有更多线程了，cp利用率很低。

System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", numberOfDownloads)

ManagedBlocker add worker threads. 只能用在fjp.而不是并行流。

## 92 Java Parallel Streams Internals: Combining Results (Part 1)

reduce() create new immutable value.

collect() mutates an existing value.

## 93 Java Parallel Streams Internals: Combining Results (Part 2)

## 94 Java Parallel Streams Internals: Non-Concurrent and Concurrent Collections (Part 1)

## 96 Java Parallel Streams Internals: Non-Concurrent and Concurrent Collectors (Part 2)

## 98 SearchWithParallelSpliterator Example: PhraseMatchSpliterator and Fields

## 99 SearchWithParallelSpliterator Example: Constructor and tryAdvance()

## 100 SearchWithParallelSpliterator Example: trySplit()

## 101 SearchWithParallelSpliterator Example: Evaluating Pros and Cons

SearchWithParallelSpliterator trySplit方法非常复杂。也是核心方法。

## 102 When to Use Java Parallel Streams

使用：将任务分割成子任务，需要处理所有子任务，需要合并子任务结果。

使用场景： 行为有确认特征：1 独立 2 计算密集型 3 数据源很多元素

NQ模型：N is data elements to process per thread. Q quantifies how CPU-intensive the processing is

## 103 When Not to Use Java Parallel Streams

the source is expensive to split or split unevenly. LinkedList不适合并行流。java.util.LinkedList.LLSpliterator

the startup costs of parallelism overwhelm the amount data（并行性的启动成本超过了数据量）

combining partial results is costly

some streams operations do not sufficiently exploit parallelism（一些流操作没有充分利用并行性）

there are not many/any core

no build-in means to shutdown processing of a parallel stream

## 104 Java Parallel ImageStreamGang Example: Introduction

## 105 Java Parallel ImageStreamGang Example: Structure and Functionality

## 106 Java Parallel ImageStreamGang Example: Visualizing Behaviors

## 107 Java Parallel ImageStreamGang Example: Implementing Behaviors

blocking-io导致上下文切换。

## 108 Java Parallel ImageStreamGang Example: Evaluating Pros and Cons

CF比PS更能编程。比PS更加efficient和scalable。

## 109 Walkthrough of Assignment 3a

## 110 Java Parallel Streams: Evaluating Pros and Cons

有些问题不能被分解为：split-apply-combine，此时不能用PS。

所有PS共享一个common fjp，CF没有这个限制。

ManagedBlocker

CF is more efficient and scalable

## 111 The Java Fork-Join Pool: Introduction

解决并行任务类型是：分治。divide and conquer。子任务并行处理。

## 112 The Java Fork-Join Pool: Structure and Functionality (Part 1)

很多task 很少worker线程，cpu一直运行。

fork()类似于Thread.start()。join()类似于Thread.join()。

fork()返回值还是ForkJoinTask<V>。join()返回值是V

## 113 The Java Fork-Join Pool: Structure and Functionality (Part 2)

RecursiveAction：无返回值

RecursiveTask：有返回值

CountedCompleter：used for computations in which completed actions trigger other actions：用于已完成动作触发其他动作的计算

ForkJoinPool的Api很少，自定义控制粒度很少，但是ThreadPoolExecutor Api很多，控制粒度很细。

## 114 The Java Fork-Join Pool: Key Methods in ForkJoinPool

非fj client，提交的是AbstractExecutorService submit,execute.

invoke是fj client.

## 115 The Java Fork-Join Pool: Key Methods in ForkJoinTask

非fj client插入到共享队列。fj client插入到workqueue。

## 116 The Java Fork-Join Pool: Key Methods in RecursiveAction and RecursiveTask

RecursiveTask继承了ForkJoinTask，很多方法继承了。

## 117 The Java Fork-Join Pool: Worker Threads

WorkerThread run a loop scan for task(sub) to execute. keep WorkerThread busy.

fork插入deque队头。LIFO顺序。 join的时候从deque队头出。为了性能，data cache，局部性原理。locality of reference.

如果插入队尾，每次从头拿，会不会头部忽略？work steal保证不会出现这种情况。

## 118 The Java Fork-Join Pool: Work Stealing

common-pool用于全局资源管理情况会比较好。

work-stealing随机选择一个deque。

Task are stolen in FIFO order. Task are execute in FILO order.

push pop 只被workerthread调用。

poll 只被steal workerthread调用。

offer/poll源自队列（先进先出 => 尾进头出），所以添加到队尾，从队头删除；
push/pop源自栈（先进后出 => 头进头出），所以添加到队头，从队头删除；

## 119 The Java Fork-Join Pool: Overview of the Common Fork-Join Pool






