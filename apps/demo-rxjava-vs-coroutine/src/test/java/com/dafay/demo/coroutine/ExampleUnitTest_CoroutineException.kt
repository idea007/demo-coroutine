package com.dafay.demo.coroutine

import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

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

    /**
     * 协程的层次结构
     * 构建器用于创建一个根协程,A 协程异常，异常优先传递给父级协程
     */
    @Test
    fun test_coroutineException() {
        val scope = CoroutineScope(Job())
        val jobA = scope.launch(CoroutineName("A")) {
            println("A start")
            delay(100)
            throw NullPointerException()
            println("A end")
        }
        val jobB = scope.launch(CoroutineName("B")) {
            println("B start")
            delay(500)
            println("B end")
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }


    /**
     *协程构建器有两种形式：自动传播异常（launch 与 actor）或向用户暴露异常（async 与 produce）。
     * 当这些构建器用于创建一个根协程时，即该协程不是另一个协程的子协程， 前者这类构建器将异常视为未捕获异常，
     * 类似 Java 的 Thread.uncaughtExceptionHandler， 而后者则依赖用户来最终消费异常，
     * 例如通过 await 或 receive
     */
    @Test
    fun test_coroutineException1() {
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

    /**
     * 协程的层次结构
     * 构建器用于创建一个根协程,A 协程异常，异常优先传递给父级协程，B 协程
     */
    @Test
    fun test_coroutineException3() {
        val scope = CoroutineScope(SupervisorJob())
        val jobA = scope.launch(CoroutineName("A")) {
            println("A start")
            delay(100)
            throw NullPointerException()
            println("A end")
        }
        val jobB = scope.launch(CoroutineName("B")) {
            println("B start")
            delay(500)
            println("B end")
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }


    /**
     * async 根协程 异常捕获
     */
    @Test
    fun test_coroutineException4() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        runBlocking {
            val scope = CoroutineScope(Job())
            val deferredA = scope.async(CoroutineName("A")) {
                println("A start")
                delay(100)
                throw NullPointerException()
                println("A end")
            }
            try {
                deferredA.await()
            } catch (e: Exception) {
                println(e)
            }
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }

    /**
     * async 子协程 无法自己捕获异常
     */
    @Test
    fun test_coroutineException5() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val scope = CoroutineScope(Job())
        scope.launch {
            val deferredA = async(CoroutineName("A")) {
                println("A start")
                delay(100)
                throw NullPointerException()
                println("A end")
            }
            try {
                deferredA.await()
            } catch (e: Exception) {
                println(e)
            }
        }

        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }

    /**
     * async 子协程 无法自己捕获异常
     */
    @Test
    fun test_coroutineException6() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        runBlocking {
            val deferredA = async(CoroutineName("A") + SupervisorJob()) {
                println("A start")
                delay(100)
                throw NullPointerException()
                println("A end")
            }
            try {
                deferredA.await()
            } catch (e: Exception) {
                println(e)
            }
        }
        // 保证执行完后再退出
        Thread.sleep(5000)
        println("......")
    }

}