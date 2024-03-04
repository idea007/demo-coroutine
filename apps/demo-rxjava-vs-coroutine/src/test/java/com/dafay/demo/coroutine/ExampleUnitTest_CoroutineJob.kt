package com.dafay.demo.coroutine

import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 协程 Job
 */
class ExampleUnitTest_CoroutineJob {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    /**
     *当启动多个协程时，跟踪它们或单独取消每个协程可能会很痛苦。相反，我们可以依靠取消启动的整个范围协程，因为这将取消创建的所有子协程：
     */
    @Test
    fun test_coroutineJob() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        runBlocking {
            val parentCoroutineContext = Job() + Dispatchers.Default + CoroutineName("Parent") + handler
            val scope = CoroutineScope(parentCoroutineContext)
            val jobA = scope.launch(Dispatchers.IO + CoroutineName("A")) {
                delay(1000)
                println("A task end  ")
            }
            val jobB = scope.launch() {
                delay(2000)
                println("B task end")
            }
            scope.cancel()
        }
        Thread.sleep(5000)
    }


    @Test
    fun test_coroutineJob1() {
        runBlocking {
            val parentCoroutineContext = Job() + Dispatchers.Default + CoroutineName("Parent")
            val scope = CoroutineScope(parentCoroutineContext)
            val jobA = scope.launch(Dispatchers.IO + CoroutineName("A")) {
                delay(1000)
                println("A task end  ")
            }
            jobA.join()
            val jobB = scope.launch() {
                println("B task start")
                delay(2000)
                println("B task end")
            }
        }
        Thread.sleep(5000)
    }

    /**
     * 正常取消
     */
    @Test
    fun test_coroutineCancel() {
        runBlocking {
            val scope = CoroutineScope(Job() + Dispatchers.Default + CoroutineName("Parent"))
            val jobA = scope.launch(Dispatchers.IO + CoroutineName("A")) {
                delay(1000)
                println("A task end  ")
            }
            val jobB = scope.launch() {
                println("B task start")
                delay(2000)
                println("B task end")
            }
            jobA.cancel()
        }
        Thread.sleep(5000)
    }

    /**
     * 取消异常
     */
    @Test
    fun test_coroutineCancel2() {
        runBlocking {
            val scope = CoroutineScope(Job() + Dispatchers.Default + CoroutineName("Parent"))
            val jobA = scope.launch(Dispatchers.IO + CoroutineName("A")) {
                try {
                    delay(2000)
                    println("A task end  ")
                } catch (e: Exception) {
                    println(e)
                }
            }
            val jobB = scope.launch() {
                println("B task start")
                delay(2000)
                println("B task end")
            }
            delay(1300)
            jobA.cancel(CancellationException("主动取消"))
        }
        Thread.sleep(5000)
    }

    /**
     * 取消异常
     */
    @Test
    fun test_coroutineCancel3() {
        runBlocking {
            val scope = CoroutineScope(Job() + Dispatchers.Default + CoroutineName("Parent"))
            val deferredA = scope.async(Dispatchers.IO + CoroutineName("A")) {
                try {
                    delay(2000)
                    println("A task end  ")
                } catch (e: Exception) {
                    println(e)
                }
                return@async "A"
            }
            val jobB = scope.launch() {
                println("B task start")
                delay(2000)
                println("B task end")
            }
            delay(1300)
            deferredA.cancel(CancellationException("主动取消"))
        }
        Thread.sleep(5000)
    }

    /**
     * 取消异常
     */
    @Test
    fun test_coroutineCancel4() {
        runBlocking {
            val scope = CoroutineScope(Job() + Dispatchers.Default + CoroutineName("Parent"))
            val jobA = scope.launch(CoroutineName("A")) {
                println("A task start")
                delay(1000)
                println("A task end")
            }
            val jobB = scope.launch {
                println("B task start")
                delay(2000)
                println("B task end")
            }
            scope.cancel()
        }
        Thread.sleep(5000)
    }

    /**
     * 取消异常
     */
    @Test
    fun test_coroutineCancel5() {
        runBlocking {
            val scope = CoroutineScope(Job() + Dispatchers.Default + CoroutineName("Parent"))
            val jobA = scope.launch(CoroutineName("A")) {
                println("A task start")
                delay(1000)
                println("A task end")
            }
            val jobB = scope.launch {
                println("B task start")
                delay(2000)
                println("B task end")
            }
            jobA.cancel()
        }
        Thread.sleep(5000)
    }

}