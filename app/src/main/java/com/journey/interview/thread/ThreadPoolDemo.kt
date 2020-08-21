package com.journey.interview.thread

import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @By Journey 2020/8/18
 * @Description
 * @LINk: https://www.jianshu.com/p/5a9403305fc2
 *        https://blog.csdn.net/qq_33453910/article/details/81413285
 */
object ThreadPoolDemo {
    /**
     * 1、线程池是什么？
     * 线程池是一种多线程处理形式，处理过程中将任务添加队列，然后在创建线程后自动启动这些任务，每个
     * 线程都使用默认的堆栈大小，以默认的优先级运行，并处在多线程单元中，如果某个线程在托管代码中
     * 空闲，则线程池将插入另一个辅助线程来使所有处理器保持繁忙。如果所有线程池都始终保持繁忙，但队列
     * 中包含挂起的工作，则线程池将在一段时间后辅助线程的数目永远不会超过最大值。超过最大值的线程可以排队，
     * 但他们要等到其他线程完成后才能启动
     *
     * Java里面的线程池的顶级接口是Executor，Executor并不是一个线程池，而只是一个执行线程的工具，
     * 而真正的线程池是ExecutorService
     *
     * Java中线程池类型：
     * 1）newCachedThreadPool 创建一个可缓存的线程池
     * 2）newFixedThreadPool 创建一个定长线程池
     * 3）newScheduledThreadPool 创建一个定长线程池
     * 4）newSingleThreadExecutor 创建一个单线程化的线程池
     *
     */

    /**
     * 使用线程池的优点：
     * 1、重用线程池的线程，避免因为线程的创建和销毁带来的性能开销
     * 2、有效控制线程池的最大并发数，避免大量的线程之间因抢占系统资源而阻塞
     * 3、能够对线程进行简单的管理，并提供一下特定的操作如：可以提供定时、定期、
     * 单线程、并发数控制等功能
     */

    /**
     * 线程池体系：
     * - Executor是线程池最顶层的接口，在Executor中只有一个execute方法，用于指向任务。
     * 线程的创建，调度等细节由子类实现；
     * - ExecutorService继承并拓展了Executor，在ExecutorService内部提供了更全面的任务
     * 提交机制（submit）以及线程池的关闭方法（shutdown）；
     * - ThreadPoolExecutor是ExecutorService的默认实现，线程池的核心部分封装在此类；
     * - ScheduledExecutorService继承自ExecutorService，增加了定时任务的方法schedule，
     * scheduleAtFixedRate等方法
     * - ScheduledThreadPoolExecutor继承自ThreadPoolExecutor并实现了ScheduledExecutorService
     */

    /**
     * ThreadPoolExecutor构造方法参数解析：
     * - corePoolSize：表示线程池的核心线程数量，数量一般情况下设置为CPU核数的二倍即可；
     * - maximumPoolSize：表示线程池最大能够容纳同时执行的线程数，必须大于等于1，如果和corePoolSize相等，
     * 就是固定大小线程池；
     * - keepAlivePoolSize：表示线程池中线程的空闲时间，当空闲时间达到此值时，线程会被销毁直到剩下corePoolSize个线程；
     * - unit：keepAlivePoolSize的时间单位，毫秒、秒、分钟、小时...；
     * - workQueue：等待队列，BlockingQueue类型，当请求的任务数大于corePoolSize时，任务会被缓存在此
     * BlockingQueue队列中；
     * - threadFactory：线程工厂，线程池的线程使用它来创建线程，如果传入null，则使用默认的工厂类DefaultThreadFactory，
     * 通常我们自定义线程工厂可以指定线程的优先级setPriority；
     * - handler：执行拒绝策略的对象，当workQueue满了之后并且执行的任务数大于maximumPoolSize时，线程池通过该策略请求。
     *
     * 注：
     * 1）当ThreadPoolExecutor的allowCoreThreadTimeOut设置为true时，核心线程超时也会被销毁。
     * 2）常见workQueue有四种：
     * 见/app/image/workQueue.png
     *
     */
    fun testThreadPoolExecutor() {
        val mThreadPoolExecutor = ThreadPoolExecutor(
            2,
            5,
            60,
            TimeUnit.SECONDS,
            null,
            null,
            null
        )
    }


    /**
     * 线程池流程解析：
     * 当我们调用execute或者submit，把一个任务提交给线程池，线程池收到这个任务请求后，有以下几种处理情况：
     * - 当线程池中运行的线程数量还没有达到corePoolSize大小时，线程池会创建一个新的线程执行提交的任务，
     * 无论之前创建的线程是否处于空闲状态；
     * - 当前线程池中运行的线程数量已经达到了corePoolSize大小时，线程池会把任务添加到等待队列中，直到某一个
     * 核心线程空闲了，线程池会根据设置的等待队列规则，从队列中取出一个新的任务执行；
     * - 如果线程数大于corePoolSize数量，但是还没有达到maximumPoolSize，并且等待队列已满，则线程池会
     * 创建新的线程来执行任务；
     * - 最后如果提交的任务，无法被核心线程直接执行，又无法加入等待队列，又无法创建非核心线程直接执行，
     * 线程池会根据定义的拒绝策略来处理这个任务。
     */


    /**
     * 线程池的四种拒绝策略：
     * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize时，如果还有任务到来就会采取
     * 任务拒绝策略，通常有以下四种策略：
     * 1）ThreadPoolExecutor.AbortPolicy：丢弃任务并抛出RejectedExecutionException异常。这是线程池
     * 的默认拒绝策略，在任务不能被提交的时候抛出异常，及时反馈程序运行状态；
     * 2）ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。使用此策略我们无法感知系统的异常状态，
     * 不建议使用此种策略；
     * 3）ThreadPoolExecutor.DiscardOldPolicy：丢弃队列最近的任务，并执行当前的任务；
     * 4）ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务，此策略提供简单的反馈
     * 控制机制，能够减缓新任务的提交速度。
     */


    /**
     * 线程池的五种状态：
     * 1）RUNNING：
     * 状态说明：线程池处在RUNNING状态时，能够接受新任务，以及对已添加的任务进行处理。
     * 状态切换：线程池的初始化状态是RUNNING，换句话说，线程池一旦被创建，就处于RUNNING
     * 状态，并且线程池中的任务数为0！
     * 2）SHUTDOWN：
     * 状态说明：线程池处在SHUTDOWN状态时，不接受新任务，但能处理已经添加的任务。
     * 状态切换：调用线程池的shutdown接口时，线程池由RUNNING->SHUTDOWN。
     * 3）STOP：
     * 状态说明：线程池处在STOP状态时，不接受新任务，不处理已添加的任务，并且会中断正在处理的任务。
     * 状态切换：调用线程池的shutdownNow接口时，线程池由（RUNNING or SHUTDOWN）->STOP。
     * 4）TIDYING：
     * 状态说明：当所有的任务已终止，ctl记录的“任务数量”为0，线程池会变为TIDYING状态，当线程池
     * 变为TIDYING状态时，会执行钩子函数terminated()。terminated在ThreadPoolExecutor类
     * 中是空的，若用户想在线程池变为TIDYING时，进行相应的处理，可以通过重载terminated函数来实现；
     * 状态切换：当线程池在SHUTDOWN状态下，阻塞队列为空并且线程池中执行的任务也为空时，就会由
     * SHUTDOWN->TIDYING。
     * 5）TERMINATED：
     * 状态说明：线程池彻底终止，就变成了TERMINATED状态。
     * 状态切换：线程池处在TIDYING状态时，执行完terminated之后，就会由TIDYING->TERMINATED
     */


    /**
     * newCachedThreadPool：是一种线程数量不定的线程池，并且其最大线程数量为Integer.MAX_VALUE，这个数
     * 是很大的，一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * 但是线程池中的空闲线程都有超时限制，这个超时时长是60s，超过60s闲置线程就会被回收。调用execute将
     * 重用以前构造的线程（如果线程可用）。
     * 这类线程池比较适合执行大量的耗时较少的任务，当整个线程池都处于闲置状态时，线程池中的线程都会超时被停止。
     *
     * CachedThreadPool的corePoolSize是0，即没有核心线程，全是非核心线程
     */
    fun testNewCachedThreadPool() {
        val mCachedThreadPool = Executors.newCachedThreadPool()
        for (i in 0..7) {
            Thread.sleep(2000)
            mCachedThreadPool.execute {
                Log.i("JG","第$i 个线程${Thread.currentThread().name}")
            }
        }
    }

    /**
     * newFixedThreadPool：创建一个指定工作线程数量的线程池，每当提交一个任务就创建一个工作线程，
     * 当线程处于空闲状态时，他们并不会回收，除非线程池被关闭了，如果工作线程数量达到线程池初始的最大数，
     * 则将提交的任务存入到池队列（没有大小限制）中等待，任务队列采用LinkedBlockingQueue，
     * 由于newFixedThreadPool只有核心线程并且这些核心线程不会被回收，这样它能更加快速的相应外界的请求；
     */
    fun testNewFixedThreadPool() {
        val mFixedThreadPool = Executors.newFixedThreadPool(5)
        for (i in 0..7) {
            mFixedThreadPool.execute {
                Log.i("JG","时间：${System.currentTimeMillis()} 第$i 个线程${Thread.currentThread().name}")
                Thread.sleep(2000)
            }
        }
    }

    /**
     * newScheduledThreadPool：创建一个线程池，它的核心线程数量是固定的，而非核心线程数是没有限制的，
     * 并且当非核心线程闲置时会被立即回收，它可安排给定延迟后运行命令或者定期地执行。这类线程池主要用于
     * 执行定时任务和具有固定周期的重复任务。该线程采用的队列DelayWorkQueue
     */
    // 延迟执行
    fun testNewScheduledThreadPool() {
        val mScheduledThreadPool = Executors.newScheduledThreadPool(2)
        Log.i("JG","时间：${System.currentTimeMillis()}")
        mScheduledThreadPool.schedule({
            Log.i("JG","时间：${System.currentTimeMillis()}")
        },4,TimeUnit.SECONDS)// 延迟4s执行
    }
    // 定期执行
    fun testNewScheduledThreadPool2() {
        val mScheduledThreadPool = Executors.newScheduledThreadPool(2)
        Log.i("JG","时间：${System.currentTimeMillis()}")
        mScheduledThreadPool.scheduleAtFixedRate({
            Log.i("JG","时间：${System.currentTimeMillis()}")
        },2,3,TimeUnit.SECONDS)// 延迟2s后每3s执行一次
    }

    /**
     * newSingleThreadExecutor：这类线程池内部只有一个核心线程，以无界队列方式来执行该线程，这使得这些
     * 任务之间不需要处理线程同步问题，它确保所有的任务都在同一个线程中按顺序执行，并且可以在任意给定的时间
     * 不会有多个线程是活动的。该线程池采用了链表阻塞队列LinkedBlockingQueue，先进先出原则，保证了
     * 任务的按顺序逐一进行。
     *
     * 执行下面的代码，可以发现是有顺序地去执行下面的6个线程
     */
    fun testNewSingleThreadExecutor() {
        val mSingleThreadExecutor = Executors.newSingleThreadExecutor()
        for (i in 0..7) {
            mSingleThreadExecutor.execute {
                Log.i("JG","时间：${System.currentTimeMillis()} 第$i 个线程${Thread.currentThread().name}")
                Thread.sleep(2000)
            }
        }
    }
}