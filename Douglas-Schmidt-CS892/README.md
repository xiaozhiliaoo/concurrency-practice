### CS 892: Concurrent Object-Oriented Programming in Java and Android
**SITE:** http://www.dre.vanderbilt.edu/~schmidt/cs891s/

http://www.dre.vanderbilt.edu/~schmidt/cs891s/2019-PDFs/

**PLAYLIST:** https://www.youtube.com/playlist?list=PLZ9NgFYEMxp6r_dM7BNs-9PzTc-ZKjomg

Count Semaphore

Binary Semaphore

entry barrier, exit barrier

CountdownLatch 人不够不能开车，人不够不能离开，只能用一次

CyclicBarrier metaphor：流水线，Cyclic 循环的，多次，但是只能是n

Phaser 有不同parties，而不是 CyclicBarrier固定的n

Big Picture

红绿灯的实现，车：线程

如何分析一个类？

AQS:FIFO队列实现的Synchronied

线程池：类似呼叫中心。隐喻，如果接线很忙，请等待，还有多少人排队中。

Forkjoin  每个线程都有自己Deque，

Executors.newWorkStealingPool()  平衡fixed和cached

线程池的实现方式：queue, half-sync/half-async, leader-fllowers

android:AsyncTask

Callable:Active Object Pattern

Future:肯德基给你小票，让你等着，小票就是Future。
Future：有Pollmodel(JDK POLL)，有Callback model

very powerful, very complicated freamwork executor framwork

任务，任务执行，任务传递(Queue)分离框架。
任务：Runnable，Callable，  任务执行 Excecutor Service

Java ExecutorService interface and related interfaces/classes.

ThreadPoolExector 实现了 ExecutorService。



invokeall：所有完成的，
invokeany：只返回第一个完成的，别的取消。deep learing，很多方法，只需要第一个返回的，很多排序算法，只返回第一个。
然后取消别的。

Mutual Exclusion Lock:会议室的门

ReentrantLock：Bridge Pattern

Reentrant_mutex

ArrayBlockingQueue is a bounded blocking FIFO queue

Guarded Suspension Pattren  ReentrantLock+Condition Object
https://en.wikipedia.org/wiki/Guarded_suspension

A concurrent collection is thread-safe, but is not governed by only a single exclusion lock

memoize vs cache

Java CompletionService’s interface defines a framework for
submitting async taks & handling their completion

ExecutorCompletionService 

CompletionService can implement the  Proactor pattern

async future -> ExecutorCompletionService 
synchronous future -> ExecutorService 
