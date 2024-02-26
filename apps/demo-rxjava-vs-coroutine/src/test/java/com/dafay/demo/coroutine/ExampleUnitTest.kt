package com.dafay.demo.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin

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
    fun test_job() {
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
    fun test_job2(){
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
    fun test_job3(){
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