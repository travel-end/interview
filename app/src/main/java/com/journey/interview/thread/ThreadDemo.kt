package com.journey.interview.thread

/**
 * @By Journey 2020/8/18
 * @Description
 */
object ThreadDemo {
    /**
     * 线程的五种状态
     * 1）开始状态（new）：线程刚创建时就是new状态
     * 2）就绪状态（runnable）：线程调用了start方法之后，进入runnable状态，此时并未真正执行，
     * 需要和其他线程竞争cpu资源。说到竞争CPU资源，有些代码里可能会看到Thread.sleep(0)或者
     * Thread.sleep(50)一些简短的操作，实际意思就是不让该线程长时间占用CPU，释放时间片，注意
     * sleep不会释放锁，即抱着锁睡觉。
     * 3）运行状态（running）：该线程竞争到了CPU资源，则进入running状态；
     * 4）阻塞状态（blocked）：线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态之间
     * 处于blocked状态。
     * - 等待阻塞：运行的线程执行wait方法，该线程会释放占用的所有资源，JVM会把该线程放入“等待池”中，
     * 进入这个状态后，是不能自动唤醒的，必须依靠其他线程调用notify或者notifyAll方法才能被唤醒。
     * - 同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入“锁池”中。
     * - 其他阻塞：运行的线程执行sleep或者join方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。
     * 当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。
     * 这个状态在Android应用出现anr时可能会经常看到，诸如构成死锁，binder远端阻塞时。
     * 5） 结束状态（dead）：当线程正常执行结束会进入dead状态（一个未捕获的异常也会使线程终止）
     *
     * 注：
     *   yield()只是使当前线程重新回到runnable状态
     *   sleep()会让出cpu，不会释放锁，join()会让出cpu，释放锁
     *   wait() 和 notify() 方法与suspend()和 resume()的区别在于wait会释放锁，suspend不会释放锁。
     *   wait() 和 notify()只能运行在Synchronized代码块中，因为wait()需要释放锁，如果不在同步代码块中，就无锁可以释放
     *   当线程调用wait()方法后会进入等待队列（进入这个状态会释放所占有的所有资源，与阻塞状态不同），进入这个状态后，
     *   是不能自动唤醒的，必须依靠其他线程调用 notify()或notifyAll()方法才能被唤醒
     */

}