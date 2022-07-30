# 课程

## Overview of Java Concurrency

Thread（State）

Shared Object(Mutual exclusion，Coordination)

Java Synchronizer: Synchronize Statement/method，Reentrant, Atomic Operation, Semaphore, Condition object, CAS

## Overview of Parallelism in Java

Split/Apply/Combine

必须要高效分拆任务以及高效组合结果。

Parallelism work best when thread share no mutable state and do not block

fork join + work-stealing

Java并行三框架：Parallel Stream（函数式框架），Completable Future（反应式异步框架），ForkJoinPool（面向对象的框架）

forkjoin不要阻塞，所以可以和异步结合起来。一个操作，

## A Brief History Of Concurrency in Java

Java1.0 thread,wait,notify,synchronize

Java5: executor framework,synchronizer,blocking queue, atomic concurrent collections 【coarse grained task parallelism】
tedious and error-prone

Java7：fork join pool，same computation on different data elements，data parallelism，same task。大任务分成小任务。分的是数据。
tedious to program directly，OO

Javv8：Parallel stream functional and reactive，productivity and performance

## Evaluation of Concurrency and Parallelism

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

LinkedList trySplit tryAdvance  分割找到mid比较难

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






