如何研究API-以Thread为例子-任务执行机制
1 如何创建线程？new Thread(); new Thread(ThreadGroup group, Runnable target, String name, long stackSize);
2 ThreadFactory();   DefaultThreadFactory()
3 ThreadBuilder();   https://doc.rust-lang.org/std/thread/struct.Builder.html   
4 ThreadFactoryBuilder();   


从Thread到TaskExectors
Task：
Callable
Future
CompletableFuture
ListenableFuture
ListenableFutureTask
FutureTask
ForkJoinTask
RecursiveTask
RecursiveAction

SpringTask：



ThreadFactoryBuilder
API
研究API时候，公有API必须全部看！！！！，因为提现了API设计。