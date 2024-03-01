package com.dafay.demo.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest_CoroutineException {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test_coroutineCancel2() {
        runBlocking {
            val jobA = launch(CoroutineName("A")) {
                val jobChildA = launch(CoroutineName("child-A")) {
                    delay(1000)
                    println("xxx")
                }
                // jobChildA.cancel()
            }
            val jobB = launch(CoroutineName("B")) {
                delay(500)
                println("xxx")
            }

            delay(3000)
            println("父协程。。。")
        }

//        runBlocking {
//            val scope = CoroutineScope(Job()+Dispatchers.IO)
//            val jobA = scope.launch(CoroutineName("A")) {
//                val jobChildA = launch(CoroutineName("child-A")) {
//                    delay(1000)
//                    println("xxx")
//                }
//                // jobChildA.cancel()
//            }
//            val jobB = scope.launch(CoroutineName("B")) {
//                delay(500)
//                println("xxx")
//            }
//            scope.cancel()
//            joinAll(jobA,jobB)
////            jobA.join()
////            jobB.join()
////            scope.cancel()
//        }

    }

    @Test
    fun test_coroutineCancel1() {
        runBlocking {
            val jobA = launch(CoroutineName("A")) {
                println("A statr")
                delay(500)
                throw NullPointerException()
            }
            val jobB = launch(CoroutineName("B")) {
                println("B start")
                delay(1500)
                println("B end")
            }
            delay(5000)
            println("......")
        }


    }

    @Test
    fun test_coroutineCancel() {
        val scope = CoroutineScope(Job())
        val jobA = scope.launch(CoroutineName("A")) {
            val jobChildA = launch(CoroutineName("child-A")) {
                delay(1000)
                println("xxx")
            }
            // jobChildA.cancel()
        }
        val jobB = scope.launch(CoroutineName("B")) {
            delay(500)
            println("xxx")
        }
//        scope.cancel()
        Thread.sleep(5000)
        println("......")
    }


    @Test
    fun test_coroutineException6() {
//        val handler = CoroutineExceptionHandler { _, exception ->
//            println("CoroutineExceptionHandler got $exception")
//        }
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(t: Thread, e: Throwable) {
                println("t.name=${t.name}" + " error:" + e.toString())
            }
        })
        val scope = CoroutineScope(SupervisorJob())
        try {
            //A
            scope.launch {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        Thread.sleep(5000)
        println("......")
    }

    @Test
    fun test_coroutineException8() {
        runBlocking {
            val handler = CoroutineExceptionHandler { _, exception ->
                println("CoroutineExceptionHandler got $exception")
            }
            val scope = CoroutineScope(Job() + handler)
            val deferredA = scope.async(CoroutineName("A")) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name} start")
                delay(100)
                throw NullPointerException()
                println("${this.coroutineContext[CoroutineName]} end")
            }
            val deferredB = scope.async(CoroutineName("B")) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name} start")
                delay(500)
                println("${this.coroutineContext[CoroutineName]} end")
            }
            try {
                deferredA.await()
            } catch (e: Exception) {
                println(e)
            }
            deferredB.await()
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }

    @Test
    fun test_coroutineException7() {
        // 2. 设置 CoroutineExceptionHandler 给父协程，或者协程 A
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        // 3. 传递给默认线程的 ExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(t: Thread, e: Throwable) {
                println("t.name=${t.name}" + " error:" + e.toString())
            }
        })

        val scope = CoroutineScope(Job())
        val jobA = scope.launch(CoroutineName("A")) {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name} start")
            delay(100)
            //            1. 主动处理异常
            //            try {
            //                throw NullPointerException()
            //            } catch (e: Exception) {
            //                e.printStackTrace()
            //            }
            throw NullPointerException()
            println("${this.coroutineContext[CoroutineName]} end")
        }
        val jobB = scope.launch(CoroutineName("B")) {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name} start")
            delay(500)
            println("${this.coroutineContext[CoroutineName]} end")
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }


    /**
     * TODO
     *
     */
    @Test
    fun test_coroutineException54() {

        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(CoroutineName("Parent"))
        scope.launch {
            val deferredA = async(CoroutineName("A")) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
                println("A start")
                delay(100)
                println("A 抛出异常")
                throw NullPointerException()
            }
            val deferredB = async(CoroutineName("B")) {
                println("B start")
                delay(500)
                println("B end")
            }
            kotlin.runCatching {
                deferredA.await()
            }
            deferredB.await()
        }


        Thread.sleep(5000)
        println("......")
    }

    /**
     * TODO
     *
     */
    @Test
    fun test_coroutineException53() {

        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(CoroutineName("Parent") + handler)
        scope.launch {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
            val deferredA = async(SupervisorJob() + CoroutineName("A")) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
                println("A start")
                delay(100)
                println("A 抛出异常")
                throw NullPointerException()
            }
            val deferredB = async(CoroutineName("B")) {
                println("B start")
                delay(500)
                println("B end")
            }

            deferredA.await()
            deferredB.await()
        }


        Thread.sleep(5000)
        println("......")
    }

    /**
     * 我们在 scope.launch 时传递了 SupervisorJob ,看着似乎没什么问题😕，我们期望的是 SupervisorJob 也会传递到子协程。但实则不会，因为子协程在 launch 时会创建新的协程作用域，其会使用默认新的 Job 替代我们传递 SupervisorJob ,所以导致我们传递的 SupervisorJob 被覆盖。所以如果我们想让子协程不影响父协程或者其他子协程，此时就必须再显示添加 SupervisorJob。
     *
     */
    @Test
    fun test_coroutineException52() {

        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(CoroutineName("Parent") + Dispatchers.IO)
        scope.launch {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
            launch(SupervisorJob() + CoroutineName("A") + Dispatchers.Default + handler) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
                println("A start")
                delay(100)
                println("A 抛出异常")
                throw NullPointerException()
            }
            launch(CoroutineName("B")) {
                println("B start")
                delay(500)
                println("B end")
            }
        }


        Thread.sleep(5000)
        println("......")
    }

    /**
     *  SupervisorJob 改变异常的传递方式，当使用它时,我们子协程的失败不会影响到其他子协程与父协程
     *  俗点理解就是:子协程会自己处理异常，并不会影响其兄弟协程或者父协程
     */
    @Test
    fun test_coroutineException51() {

        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(handler + CoroutineName("Parent") + SupervisorJob())
        println("Name:${scope.coroutineContext[CoroutineName]} Job:${scope.coroutineContext[Job]}#${scope.coroutineContext[Job]?.parent?.javaClass?.name}")

        scope.launch(CoroutineName("A")) {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
            println("A start")
            delay(100)
            println("A 抛出异常")
            throw NullPointerException()
        }
        scope.launch(CoroutineName("B")) {
            println("B start")
            delay(500)
            println("B end")
        }

        Thread.sleep(5000)
        println("......")
    }

    // 子协程发生异常时，它会优先将异常委托给父协程区处理
    @Test
    fun test_coroutineException5() {
        // 子协程发生异常时，它会优先将异常委托给父协程区处理
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(CoroutineName("Parent"))
        scope.launch {
            println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
            launch(SupervisorJob() + CoroutineName("A") + handler) {
                println("Name:${this.coroutineContext[CoroutineName]} Job:${this.coroutineContext[Job]}#${this.coroutineContext[Job]?.parent?.javaClass?.name}")
                println("A start")
                delay(100)
                println("A 抛出异常")
                throw NullPointerException()
            }
            launch(CoroutineName("B")) {
                println("B start")
                delay(500)
                println("B end")
            }
        }

        Thread.sleep(5000)
        println("......")
    }

    @Test
    fun test_coroutineException4() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(SupervisorJob() + handler + Dispatchers.IO)
        println("ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
        println("Job:" + scope.coroutineContext[Job])
        println("CoroutineName:" + scope.coroutineContext[CoroutineName])

        val jobA = scope.launch(CoroutineName("A")) {
            println("Job:" + this.coroutineContext[Job] + " ${this.coroutineContext[Job]?.parent?.javaClass?.name}")
            println("ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
            println("Job:" + this.coroutineContext[Job])
            println("CoroutineName:" + this.coroutineContext[CoroutineName])
            println("ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
            println("${this.coroutineContext[CoroutineName]} start")
            delay(100)
            println("${this.coroutineContext[CoroutineName]} 抛出异常")
            throw NullPointerException()
        }

        println("jobA:" + jobA + " jobA is SupervisorJob ${jobA.javaClass.name}")


        val jobB = scope.launch(CoroutineName("B")) {
            println("${this.coroutineContext[CoroutineName]} start")
            delay(500)
            println("${this.coroutineContext[CoroutineName]} end")
        }

        Thread.sleep(5000)
        println("......")
    }

    @Test
    fun test_coroutineException3() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }

        val scope = CoroutineScope(Job() + handler)
        val jobA = scope.launch(CoroutineName("A")) {
            println("A start")
            delay(100)
            println("A 抛出异常")
            throw NullPointerException()
        }

        Thread.sleep(5000)
        println("......")
    }

    @Test
    fun test_coroutineException2() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }

        runBlocking {
            val job = launch(handler) {
                val child = launch(handler) {
                    delay(Long.MAX_VALUE)
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } catch (e:Exception){
//                        println(e)
//                    }finally {
//                        println("Child is cancelled")
//                    }
                }
                yield()
                println("Cancelling child")
                child.cancel()
                child.join()
                yield()
                println("Parent is not cancelled")
            }
            job.join()
        }
    }


    @Test
    fun test_coroutineException1() {
        runBlocking {
            val handler = CoroutineExceptionHandler { _, exception ->
                println("CoroutineExceptionHandler got $exception")
            }
            val job = GlobalScope.launch(handler) { // 根协程，运行在 GlobalScope 中
                throw AssertionError()
            }
            val deferred = GlobalScope.async(handler) { // 同样是根协程，但使用 async 代替了 launch
                throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
            }
            job.join()
            deferred.await()
//            joinAll(job, deferred)
        }
    }

    @Test
    fun test_coroutineException() {
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(t: Thread, e: Throwable) {
                println("t.name=${t.name}" + " error:" + e.toString())
            }
        })
        runBlocking {
            val job = GlobalScope.launch { // launch 根协程
                println("Throwing exception from launch")
                throw IndexOutOfBoundsException() // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
            }
            job.join()
            println("Joined failed job")
            val deferred = GlobalScope.async { // async 根协程
                println("Throwing exception from async")
                throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
            }
            try {
                deferred.await()
                println("Unreached")
            } catch (e: ArithmeticException) {
                println("Caught ArithmeticException")
            }
        }
    }

}