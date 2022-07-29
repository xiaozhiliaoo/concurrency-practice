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




