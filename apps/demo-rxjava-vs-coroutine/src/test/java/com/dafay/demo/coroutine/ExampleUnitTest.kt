package com.dafay.demo.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.ContinuationInterceptor

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test_CoroutineContext() {
        runBlocking(Dispatchers.Main){
            val scope = CoroutineScope(Job() + Dispatchers.Main + CoroutineName("parent"))
            println("-ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
            println("-Job:" + scope.coroutineContext[Job])
            println("-CoroutineName:" + scope.coroutineContext[CoroutineName])
            val job = scope.launch(Dispatchers.IO) {
                println("ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                println("Job:" + this.coroutineContext[Job])
                println("CoroutineName:" + this.coroutineContext[CoroutineName])
                delay(1000) //
                println("I'm sleeping  ...")
            }
            println("job:" + job)
            job.join()
            println("-ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
            println("-Job:" + scope.coroutineContext[Job])
            println("-CoroutineName:" + scope.coroutineContext[CoroutineName])
        }
    }


    @Test
    fun test_job_cancel7() {

        var acquired = 0

        class Resource {
            init {
                acquired++
            } // Acquire the resource

            fun close() {
                acquired--
            } // Release the resource
        }

        runBlocking {
            repeat(10_000) { // Launch 10K coroutines
                launch {
                    val resource = withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        Resource() // Acquire a resource and return it from withTimeout block
                    }
                    resource.close() // Release the resource
                }
            }
        }
        // Outside of runBlocking all coroutines have completed
        println("" + acquired) // Print the number of resources still acquired
    }

    @Test
    fun test_job_cancel6() {
        runBlocking {
            val result = withTimeoutOrNull(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
                "Done" // 在它运行得到结果之前取消它
            }
            println("Result is $result")
        }
    }

    @Test
    fun test_job_cancel5() {
        runBlocking {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        }
    }

    @Test
    fun test_job_cancel4() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("job: I'm running finally")
                        delay(1000L)
                        println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // 延迟一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消该作业并等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun test_job_cancel3() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    println("job: I'm running finally")
                }
            }
            delay(1300L) // 延迟一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消该作业并且等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun test_job_cancel2() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) { // 可以被取消的计算循环
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消该作业并等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun test_job_cancel1() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                repeat(5) { i ->
                    try {
                        // print a message twice a second
                        println("job: I'm sleeping $i ...")
                        delay(500)
                    } catch (e: Exception) {
                        // log the exception
                        println(e)
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun test_job_cancel() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun test_firstCoroutine() {
        runBlocking {
            launch(Dispatchers.IO) {
                delay(1000L)
                println("World!") // print after delay
            }
            println("Hello") // main coroutine continues while a previous one is delayed
        }
    }

    @Test
    fun test_async1() {
        // 创建一个新的协程作用域
        runBlocking {
            println("before async...")
            // 启动一个异步任务，返回一个 Deferred 对象
            val deferredResult1: Deferred<Int> = async {
                println("in async...")
                delay(1000) // 模拟异步操作
                return@async 42
            }
            val deferredResult2: Deferred<Int> = async {
                println("in async...")
                delay(2000) // 模拟异步操作
                return@async 42
            }
            println("Do something while async task is running...")
            deferredResult1.cancelAndJoin()
            // 等待异步任务完成并获取结果
//            val result1 = deferredResult1.await()
            val result2 = deferredResult2.await()
            println("Async task result: ${result2}")
        }
    }


    @Test
    fun test_async() {
        // 创建一个新的协程作用域
        runBlocking {
            println("before async...")
            // 启动一个异步任务，返回一个 Deferred 对象
            val deferredResult: Deferred<Int> = async {
                println("in async...")
                delay(1000) // 模拟异步操作
                return@async 42
            }
            println("Do something while async task is running...")
            // 等待异步任务完成并获取结果
            val result = deferredResult.await()
            println("Async task result: $result")
        }
    }

    @Test
    fun test_launch() {
        runBlocking(CoroutineName("RunBlocking Scope") + Dispatchers.Default) { // this: CoroutineScope
            println("current scope is ${coroutineContext[CoroutineName]}")
            launch() { // launch a new coroutine and continue
                println("curret scope is ${coroutineContext[CoroutineName]}")
                delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
                println("World!") // print after delay
            }

            launch {
                println("curret scope is ${coroutineContext[CoroutineName]}")
                delay(1000L)
                println("Welcome To Coding World")
            }
            println("Hello") // main coroutine continues while a previous one is delayed
        }
    }

    @Test
    fun test_job1() {
        runBlocking {
            // sampleStart
            val job = launch {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            }
            println("main: I'm tired of waiting!")
            job.cancel() // 取消该作业
            job.join() // 等待作业执行结束
            println("main: Now I can quit.")
            // sampleEnd
        }
    }

    @Test
    fun test_job0() {
        runBlocking {
            //sampleStart
            val job = launch {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L) // 延迟一段时间
            println("main: I'm tired of waiting!")
            job.cancel() // 取消该作业
            job.join() // 等待作业执行结束
            println("main: Now I can quit.")
            //sampleEnd
        }
    }


    /**
     * 协程的取消是 协作 的。一段协程代码必须协作才能被取消。 所有 kotlinx.coroutines 中的挂起函数都是 可被取消的 。它们检查协程的取消， 并在取消时抛出 CancellationException。 然而，如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的
     */
    @Test
    fun test_job2() {
        runBlocking {
            // sampleStart
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            println("main: Now I can quit.")
            //sampleEnd
        }
    }

    @Test
    fun test_job3() {
        runBlocking {
            // sampleStart
            val job = launch(Dispatchers.Default) {
                repeat(5) { i ->
                    try {
                        // print a message twice a second
                        println("job: I'm sleeping $i ...")
                        delay(500)
                    } catch (e: Exception) {
                        // log the exception
                        println(e)
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
            // sampleEnd
        }
    }
}