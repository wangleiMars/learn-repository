## 多线程 multi-thread

### learn-multi-thread：

#### 《Java并发编程的艺术》学习过程代码

##### 1.1 上下文切换 ConcurrencyTest DeadLockDemo

1. 单核cpu也是支持多线程操作的
2. 时间片一般是十几毫秒
3. cpu通过时间片分配算法循环执行任务，当前执行任务执行一个时间片后切换到下一个任务，在切换前会保存上一个任务的状态， 以便下次切换回来继续执行，这个切换动作叫做上下文切换
4. 上下文的切换会影响多线程的执行效率
5. 任务数量不够量级时，使用多线程反而是影响效率：频繁的切换上下文会影响效率
6. idea asyn profiler 查看火焰图，调用书，时间线，事件
7. 减少切换上下文的方法：cas算法，无锁多线程，使用最少的线程数,协程（单线程实现多任务调度）
8. cas算法：乐观锁 比较替换的方式，会有自旋性能问题， 注意aba问题 可以依赖AtomicStampedReference 在增加一个类似版本号参数
9. 避免死锁：
10. 避免一个线程持有多个锁
11. 避免一个线程内占用多个资源
12. 使用定时锁，lock。trylock（timeout）替代使用内部锁机制
13. 上锁和解锁要成对出现，避免异常情况下锁未释放

##### 1.2 java并发机制的底层实现原理

1. volatile

    1. volatile 轻量级的synchronized，在多处理器开发中保证了共享变量的可见性，他不会引起线程上下文的切换和调度
    2. volatile转换成汇编代码，会多出lock指令，lock指令会引发两件事，将当前处理器缓存行写入内存。这个写入内存的操作会是其他cpu缓存此地址的数据变为无效
2. Synchroized

    1. Synchroized 在1.6之后减少了锁和释放锁带来的性能损耗，引入了偏量锁和轻量级锁，以及锁的存储结果和升级过程

        1. Synchroized 三种形式：
            1. 普通方法，锁的是当前示例
            2. 静态同步方法，锁的是当前类的class对象
            3. 同步方法快，锁的是Synchroized括号里配置的对象
    2. Synchroized 在jvm实现原理，jvm基于进入和退出Monitor对象实现方法同步和代码块同步
    3. 锁的级别：无锁状态 -> 偏向锁状态 -> 轻量锁 -> 重量级锁 锁会随着竞争状态的升级而升级，锁可以升级但是不可以降级
    4. 偏量锁依赖对象头 当一个线程访问同步块并获取锁时，会在对象头和栈帧中的锁记录里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的Mark Word里是否存储着
       指向当前线程的偏向锁。如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1（表示当前是偏向锁）：如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS将对象
       头的偏向锁指向当前线程。<br />
       偏向锁在1.6之后默认启动，会在应用程序启动几秒钟后才激活，-XX:BiasedLockingStartupDelay=0 关闭延迟 <br />
       关闭偏向锁：-XX:-UseBiasedLocking=false
    5. 轻量级锁 当前线程的栈桢中创建用于存储锁记录的空间，并将对象头中的Mark Word复制到锁记录中，线程尝试使用CAS将对象头中的Mark
       Word替换为指向锁记录的指针。如果成功，当前线程获得锁，如果失败，表示其他线程竞争锁， 当前线程便尝试使用自旋来获取锁。 轻量级解锁时，会使用原子的CAS操作将Displaced MarkWord替换回到对象头，如果成功，则
       表示没有竞争发生。如果失败，表示当前锁存在竞争，锁就会膨胀成重量级锁
    6. 锁的优缺点

| 锁       |                                  优点                                  |                                                 缺点 |                           适用场景 |
   | ---------- | :----------------------------------------------------------------------: | -----------------------------------------------------: | -----------------------------------: |
| 偏量锁   | 加锁和解锁不需要额外的消耗，<br />和执行非同步方法相比存在纳秒级的差距 | 如果线程间存在锁竞争，<br />会带来额外的锁撤销的消耗 | 适用于是有一个线程访问同步块的场景 |
| 轻量级锁 |                  竞争的线程不会阻塞，提高程序的响应度                  |      如果始终得不到锁的线程，<br />使用自旋会消耗cpu | 追求响应时间，同步快执行速度非常快 |
| 重量级锁 |                    线程竞争不使用自旋，不会消耗cpu                    |                               线程阻塞，响应时间缓慢 |     追求吞吐量，同步快执行速度较长 |

3. 原子操作

    1. 术语定义：不可被中断的一个或一系列操作
    2. 处理器如何实现原子操作
        1. 使用总线锁保证：总线锁就是使用处理器提供的一个LOCK #信号，当一个处理器在总线上输出此信号时，其他处理器的请求将被阻塞住，那么该处理器可以独占共享内存。
        2. 使用缓存锁保证原子性：在同一时刻，我们只需保证对某个内存地址的操作是原子性即可，但总线锁定把CPU和内存之间的通信锁住了，这使得锁定期间，其他处理器不能操作其他内存地址的数据，
           所以总线锁定的开销比较大，目前处理器在某些场合下使用缓存锁定代替总线锁定来进行优化。
        3. 有两种情况下处理器不会使用缓存锁定。第一种情况是：当操作的数据不能被缓存在处理器内部，或操作的数据跨多个缓存行（cache line）时，则处理器会调用总线锁定。第二种情况是：有些处理器不支持缓存锁定。对于Intel
           486和
           Pentium处理器，就算锁定的内存区域在处理器的缓存行中也会调用总线锁定。针对以上两个机制，我们通过Intel处理器提供了很多Lock前缀的指令来实现。例如，位测试和修改指令：BTS、BTR、BTC；交换指令XADD、CMPXCHG，以及
           其他一些操作数和逻辑指令（如ADD、OR）等，被这些指令操作的内存区域就会加锁，导致其他处理器不能同时访问它。
    3. java如何实现原子操作
        1. 使用cas实现原子操作：利用了处理器提供的CMPXCHG指令实现的。自旋CAS实现的基本思路就是循环进行CAS操作直到成功为止。java.util.concurrent.atomic
        2. cas实现原子操作的三大问题：
            1. ABA问题，如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值没有发生变化，但是实际上却变化了。ABA问题的解决思路就是使用版本号。
               在变量前面追加上版本号，每次变量更新的时候把版本号加1，那么A→B→A就会变成1A→2B→3A atomic 包下的 AtomicStampedReference 类可以解决
            2. 循环时间长开销大。
            3. 只能保证一个共享变量的原子操作

##### 1.3 java内存模型

1. java内存模型基础
    1. 两个关键问题：线程之间如何通信及线程之间如何同步。通信是指线程之间以何种机制来交换信息。在命令式编程中，线程之间的通信机制有两种：共享内存和消息传递。
    2. Java的并发采用的是共享内存模型，Java线程之间的通信总是隐式进行
    3. 在执行程序时，为了提高性能，编译器和处理器常常会对指令做重排序。重排序分3种类型。

        1. 编译器优化的重排序。编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序
        2. 指令级并行的重排序
        3. 内存系统的重排序
    4. java指令重排：源代码 -> 编译器优化重排序 -> 指令级并行重排序 -> 内存系统重排 -> 最终执行的序列
    5. happens-before jdk5开始java采用jsr-133内存模型，jsr-133使用的是happens-before概念来阐述操作之间的内存可见性。两个操作之间具有happens-before关系，
       并不意味着前一个操作必须要在后一个操作之前执行！happens-before仅仅要求前一个操作（执行的结果）对后一个操作可见，且前一个操作按顺序排在第二个操作之前。
    6. 重排序：编译器和处理器可能会对操作做重排序。编译器和处理器在重排序时，会遵守数据依赖性，编译器和处理器不会改变存在数据依赖关系的两个操作的执行顺序。数据依赖性
       仅针对单个处理器中执行的指令序列和单个线程中执行的操作，不同处理器之间和不同线程之间的数据依赖性不被编译器和处理器考虑。
    7. as-if-serial语义的意思是：不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守as-if-serial语义。
    8. 总线的这些工作机制可以把所有处理器对内存的访问以串行化的方式来执行。在任意时间点，最多只能有一个处理器可以访问内存。这个特性确保了单个总线事务之中的内存读/写操作具有原
       子性。在一些32位的处理器上，如果要求对64位数据的写操作具有原子性，会有比较大的开销。为了照顾这种处理器，Java语言规范鼓励但不强求JVM对64位的long型变量和double型变量
       的写操作具有原子性。当JVM在这种处理器上运行时，可能会把一个64位long/double型变量的写操作拆分为两个32位的写操作来执行。这两个32位的写操作可能会被分配到不同的总线事务
       中执行，此时对这个64位变量的写操作将不具有原子性。
    9. 注意，在JSR -133之前的旧内存模型中，一个64位long/double型变量的读/写操作可以被拆分为两个32位的读/写操作来执行。从JSR -133内存模型开始（即从JDK5开始），仅仅只允许
       把一个64位long/double型变量的写操作拆分为两个32位的写操作来执行，任意的读操作在JSR-133中都必须具有原子性（即任意读操作必须要在单个读事务中执行）。
    10. volatile 变量对单个值的读写做了同步操作，相当于增加同步锁
    11. volatile特性：

        1. 可见性：对volatile变量的读，任意线程总能看到 volatile变量最后的写入
        2. 原子性：对任意单个volatile变量的读/写具有原子性，但类似于volatile++这种复合操作不具有原子性。
    12. 当写一个volatile变量时，JMM会把该线程对应的本地内存中的共享变量值刷新到主内存。
    13. 当第二个操作是volatile写时，不管第一个操作是什么，都不能重排序。这个规则确保volatile写之前的操作不会被编译器重排序到volatile写之后。
        当第一个操作是volatile读时，不管第二个操作是什么，都不能重排序。这个规则确保volatile读之后的操作不会被编译器重排序到volatile读之前。
        当第一个操作是volatile写，第二个操作是volatile读时，不能重排序。
    14. 处理器的重排序规则

       | 屏障类型            |          指令示例          |                                                                                                                                                                                                    说明 |
       | --------------------- | :--------------------------: | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
       | LoadLoad   Barriers |   Load1；LoadLoad；Load2   |                                                                                                                                                    确保Load1数据的装载先于load2及所有后续装载指令的装载 |
       | StoreStore Barriers | Store1；storestore；store2 |                                                                                                                          确保store1数据对其他处理器可见（刷新到内存）先于store2及所有后续存储指令的存储 |
       | LoadStore  Barriers |  Load1；loadstore；store2  |                                                                                                                                               确保Load1数据装载先于store2及所有后续的储存指令刷新到内存 |
       | StoreLoad  Barriers |  store1；storeload；load2  | 确保store1数据对其他处理器变得可见（指刷新到内存）先于load2及所有后续装载指令的装载。<br /> StoreLoad Barriers 会使该屏障之前的所有内存访问指令（存储和装载指令）完成后，才执行该屏障之后的内存访问指令 |

15. 基于保守策略的JMM内存屏障插入策略。

    1. 在每个volatile写操作的前面插入一个StoreStore屏障。
    2. 在每个volatile写操作的后面插入一个StoreLoad屏障。
    3. 在每个volatile读操作的后面插入一个LoadLoad屏障。
    4. 在每个volatile读操作的后面插入一个LoadStore屏障。
16. 公平锁 依赖volatile，非公平锁依赖 cas算法
17. CAS同时具有volatile读和volatile写的内存
18. 对公平锁和非公平锁的内存语义

    1. 公平锁和非公平锁释放时，最后都要写一个volatile变量state。
    2. 公平锁获取时，首先会去读volatile变量。
    3. 非公平锁获取时，首先会用CAS更新volatile变量，这个操作同时具有volatile读和volatile写的内存语义。
19. 锁释放-获取的内存语义的实现至少有下面两种方式

    1. 利用volatile变量的写-读所具有的内存语义。
    2. 利用CAS所附带的volatile读和volatile写的内存语义。
20. concurrent包下源代码实现，通用化模式：

    1. 首先声明共享变量volatile
    2. 使用cas的原子条件更新来实现线程之间的同步
    3. 配合volatile的读/写和CAS所具有的volatile读和写的内存语义来实现线程之间的通讯
21. AbstractQueuedSynchronizer（AQS）,抽象的队列式的同步器 concurrent实现示意图：

    1. volatile变量的读/写 CAS || VV
    2. AQS 非阻塞数据结构 原子变量类 || vv
    3. LOCK 同步器 阻塞队列 Executor 并发容器
22. fianl域的重排序规则

    1. 在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
    2. 初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能重排序。
23. 对于引用类型，写final域的重排序规则对编译器和处理器增加了如下约束：在构造函数内对一个final引用的对象的成员域的写入，与随后在构造函数外把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
24. JMM把happens-before要求禁止的重排序分为了下面两类。

    1. 会改变程序执行结果的重排序。
    2. 不会改变程序执行结果的重排序。
25. JMM对这两种不同性质的重排序，采取了不同的策略

    1. 对于会改变程序执行结果的重排序，JMM要求编译器和处理器必须禁止这种重排序。
    2. 对于不会改变程序执行结果的重排序，JMM对编译器和处理器不做要求（JMM允许这种重排序）。
26. happens-before关系的定义

    1. 如果一个操作happens-before另一个操作，那么第一个操作的执行结果将对第二个操作可见，而且第一个操作的执行顺序排在第二个操作之前。
    2.
    两个操作之间存在happens-before关系，并不意味着Java平台的具体实现必须要按照happens-before关系指定的顺序来执行。如果重排序之后的执行结果，与按happens-before关系来执行的结果一致，那么这种重排序并不非法（也就是说，JMM允许这种重排序）。
27. happens-before和as-if-serial

    1. as-if-serial语义保证单线程内程序的执行结果不被改变，happens-before关系保证正确同步的多线程程序的执行结果不被改变。
    2.
    as-if-serial语义给编写单线程程序的程序员创造了一个幻境：单线程程序是按程序的顺序来执行的。happens-before关系给编写正确同步的多线程程序的程序员创造了一个幻境：正确同步的多线程程序是按happens-before指定的顺序来执行的。
28. happens-before规则

    1. 程序顺序规则：一个线程中的每个操作，happens-before于该线程中的任意后续操作。
    2. 监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。
    3. volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。
    4. 传递性：如果A happens-before B，且B happens-beforeC，那么A happens-before C。
    5. start()规则：如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作。
    6. join()规则：如果线程A执行操作ThreadB. join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB. join()操作成功返回。
29. 基于volatile的解决方案 双重锁

    ```
    public class SafeDoubleCheckedLocking {

        private volatile static InstanceSafeDouble instance;

        public static InstanceSafeDouble getInstance() {
            if (instance == null) {
                /**
                 * 这里防止内存指令重排，实际在new会做三件事
                 * 1.分配对象的内存
                 * 2.初始化对象
                 * 3.设置instance指向内存空间
                 * 若 无锁 synchronized 这三个指令可能会导致重排，向 3，2，1 会出现访问返回null
                 */
                synchronized (SafeDoubleCheckedLocking.class) {
                    if (instance == null) {
                        instance = new InstanceSafeDouble();
                    }
                }
            }
            return instance;
        }

        public static class InstanceSafeDouble {

        }
    }


    ```
30. 双重锁 另一种实现：JVM在类的初始化阶段（即在Class被加载后，且被线程使用之前），会执行类的初始化。在执行类的初始化期间，JVM会去获取一个锁。这个锁可以同步多个线程对同一个类的初始化。

    ```
    public class InstanceFactory {
        private static class InstanceHolder{
            public static Instance instance = new Instance();
        }
         //允许 重排序，但不允许非构造线程（这里指线程B）“看到”这个重排序。
        public static Instance getInstance(){
             //这里将导致instanceHolder类被初始化
            return InstanceHolder.instance;
        }

        public static class Instance{

        }
    }

    ```
31. 在大多数时候，正常的初始化要优于延迟初始化。如果确实需要对实例字段使用线程安全的延迟初始化，请使用上面介绍的基于volatile的延迟初始化的方案；如果确实需要对静态字段使用线程安全的延迟初始化，请使用上面介绍的基于类初始化的方案。
32. Java程序的内存可见性保证可以分为下列3类

    1. 单线程程序。单线程程序不会出现内存可见性问题。编译器、runtime和处理器会共同确保单线程程序的执行结果与该程序在顺序一致性模型中的执行结果相同。
    2. 正确同步的多线程程序。正确同步的多线程程序的执行将具有顺序一致性（程序的执行结果与该程序在顺序一致性内存模型中的执行结果相同）。这是JMM关注的重点，JMM通过限制编译器和处理器的重排序来为程序员提供内存可见性保证。
    3. 未同步/未正确同步的多线程程序。JMM为它们提供了最小安全性保障：线程执行时读取到的值，要么是之前某个线程写入的值，要么是默认值（0、null、false）。
33. JSR-133对JDK 5之前的旧内存模型的修补主要有两个

    1. 增强volatile的内存语义。旧内存模型允许volatile变量与普通变量重排序。JSR-133严格限制volatile变量与普通变量的重排序，使volatile的写-读和锁的释放-获取具有相同的内存语义。
    2. 增强final的内存语义。在旧内存模型中，多次读取同一个final变量的值可能会不相同。为此，JSR-133为final增加了两个重排序规则。在保证final引用不会从构造函数内逸出的情况下，final具有了初始化安全性。

### java并发编程基础

1. 为什么要使用多线程
    1. 使用更多处理器核心
    2. 更快的响应时间
    3. 更好的编程模型：java提供良好的一致性编程模型，使开发人员更好的专注问题的解决，一旦建立好模型，稍微修改就能很方便的映射出多线程编程模型上
2. java线程中使用整型成员变量priority来控制优先级，默认优先级为：5 。设置线程优先级时，针对频繁阻塞（休眠或者I/O操作）的线程需要设置较高优先级，而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较低的
   优先级，确保处理器不会被独占。在不同的JVM以及操作系统上，线程规划会存在差异，有些操作系统甚至会忽略对线程优先级的设定
3. 线程的状态：
    1. new ：初始状态，线程被构造，但是还没调用start方法
    2. runable：运行状态，java将操作系统中的就绪和运行笼统的称作：运行中
    3. blocked：阻塞状态，表示线程阻塞与锁
    4. waiting：等待状态，表示线程进入等待状态，表示当前线程需要等待其他线程作出一些特定动作
    5. time_waiting: 超时等待状态，状态不同于waiting，可以在指定的时间返回的
    6. terminated：终止状态，表示当前线程已经执行完毕
4. 在构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源的逻辑
5. 一个新构造的线程对象是由其parent线程来进行空间分配的，而child线程继承了parent是否为Daemon、优先级和加载资源的contextClassLoader以及可继承的ThreadLocal，
   同时还会分配一个唯一的ID来标识这个child线程。至此，一个能够运行的线程对象就初始化好了，在堆内存中等待着运行。
6. 中断标识interrupt，声明抛出InterruptException异常会清理中断标识，此时调用isinterrupt返回的是false
7. 线程的suspend中断，resume恢复，stop中断，api是过时的，过时原因是：suspend调用后，不会释放已占有的资源，容易引发死锁，stop也不保证终止线程后，能完全释放资源
8. 同步块的实现使用了monitorenter和monitoreexit指令，同步方法依靠方法修饰符的ACC_SYNCHRONIZED，本质上是对一个的对象的monitor监视器进行获取，获取过程是排他的

   ```
   wanglei@wanglei test % javap -v SynchoroniezdTest.class
Classfile /Users/wanglei/Documents/myproject/githubout/learn-repository/multi-thread/learn-multi-thread/target/test-classes/com/mars/multi/thread/learn/test/SynchoroniezdTest.class
  Last modified 2021年12月17日; size 634 bytes
  MD5 checksum 3da980e3aaaaaed4be5abfb48745d857
  Compiled from "SynchoroniezdTest.java"
public class com.mars.multi.thread.learn.test.SynchoroniezdTest
  minor version: 0
  major version: 50
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #2                          // com/mars/multi/thread/learn/test/SynchoroniezdTest
  super_class: #4                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 3, attributes: 1
Constant pool:
   #1 = Methodref          #4.#23         // java/lang/Object."<init>":()V
   #2 = Class              #24            // com/mars/multi/thread/learn/test/SynchoroniezdTest
   #3 = Methodref          #2.#25         // com/mars/multi/thread/learn/test/SynchoroniezdTest.m2:()V
   #4 = Class              #26            // java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Utf8               Code
   #8 = Utf8               LineNumberTable
   #9 = Utf8               LocalVariableTable
  #10 = Utf8               this
  #11 = Utf8               Lcom/mars/multi/thread/learn/test/SynchoroniezdTest;
  #12 = Utf8               main
  #13 = Utf8               ([Ljava/lang/String;)V
  #14 = Utf8               args
  #15 = Utf8               [Ljava/lang/String;
  #16 = Utf8               StackMapTable
  #17 = Class              #15            // "[Ljava/lang/String;"
  #18 = Class              #26            // java/lang/Object
  #19 = Class              #27            // java/lang/Throwable
  #20 = Utf8               m2
  #21 = Utf8               SourceFile
  #22 = Utf8               SynchoroniezdTest.java
  #23 = NameAndType        #5:#6          // "<init>":()V
  #24 = Utf8               com/mars/multi/thread/learn/test/SynchoroniezdTest
  #25 = NameAndType        #20:#6         // m2:()V
  #26 = Utf8               java/lang/Object
  #27 = Utf8               java/lang/Throwable
{
  public com.mars.multi.thread.learn.test.SynchoroniezdTest();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/mars/multi/thread/learn/test/SynchoroniezdTest;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: ldc           #2                  // class com/mars/multi/thread/learn/test/SynchoroniezdTest
         2: dup
         3: astore_1
         4: monitorenter
         5: aload_1
         6: monitorexit
         7: goto          15
        10: astore_2
        11: aload_1
        12: monitorexit
        13: aload_2
        14: athrow
        15: invokestatic  #3                  // Method m2:()V
        18: return
      Exception table:
         from    to  target type
             5     7    10   any
            10    13    10   any
      LineNumberTable:
        line 6: 0
        line 8: 5
        line 9: 15
        line 10: 18
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      19     0  args   [Ljava/lang/String;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 10
          locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
          stack = [ class java/lang/Throwable ]
        frame_type = 250 /* chop */
          offset_delta = 4

  public static synchronized void m2();
    descriptor: ()V
    flags: (0x0029) ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
    Code:
      stack=0, locals=0, args_size=0
         0: return
      LineNumberTable:
        line 14: 0
}
SourceFile: "SynchoroniezdTest.java"

   
   ```

9. 等待/通知经典范式：
    1. 等待方遵循如下原则
        1. 获取对象的锁
        2. 如果条件不满足，那么调用对的wait()的方法，
        3. 条件满足则执行对应的逻辑
       ```
       synchronized（对象）{
         while(条件不满足){
             对象.wait();
         }
       }
       
       ```
    2. 通知方遵循如下原则
        1. 获取对象锁
        2. 改变条件
        3. 通知所有等待在对象的线程
       ```
       synchronized（对象）{
       改变条件
       对象.notifyAll();
       }      
       
       ```      
10. join 如果一个线程A执行了thread.join()语句，其含义是：当前线程A等待thread线程终止之后才从thread.join()返回。 