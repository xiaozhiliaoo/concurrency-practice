### The Art of Multiprocessor Programming

https://booksite.elsevier.com/9780123705914/

修订版：https://booksite.elsevier.com/9780123973375/

https://booksite.elsevier.com/9780123705914/?ISBN=9780123705914

互斥，同步，并发，生产者消费者问题?

生产者和消费者协议解决不了互斥问题。

协议：互斥，共享，

协作问题

互斥：不共戴天

互斥的本质是等待，无论互斥协议多么巧妙，无法避免等待。

并发对象的正确性指的是什么？

共享存储器的并发计算

Progress Conditions
类的方法基于锁的同步，那么最多保证无死锁和无饥饿的演进特性。
阻塞演进：无死锁和无饥饿
非阻塞演进：无障碍，无锁，无等待
无饥饿意味着无死锁
无锁算法肯定无障碍，反之不成立，无等待算法肯定无锁，但是无锁不一定无等待。

为一个并发对象实现选择演进条件取决于应用需求和底层平台的特性。
无锁，无等待任意平台运作，但是无障碍，无死锁和无饥饿取决于平台。

https://en.wikipedia.org/wiki/Synchronization_(computer_science)

同步解决方案之一：互斥。

互斥是多处理器设计常见协作方式，任何互斥协议都会遇到：如果不能获取锁，该怎么做？ 1 尝试(自旋) 2 阻塞

经典同步问题：1  生产者消费者问题(Bounded Buffer Problem) 2 读写问题 3  哲学家就餐问题

https://en.wikipedia.org/wiki/Concurrent_data_structure

Michael L. Scott : https://www.cs.rochester.edu/~scott/
