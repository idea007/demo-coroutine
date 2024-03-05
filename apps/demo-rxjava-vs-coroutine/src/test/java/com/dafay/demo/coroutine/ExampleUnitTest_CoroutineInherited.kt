package com.dafay.demo.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CoroutineExceptionHandler
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
 * 协程上下文基础关系测试
 */
class ExampleUnitTest_CoroutineInherited {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    /**
     * 协程上下文继承关系
     */
    @Test
    fun test_CoroutineContext() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        runBlocking {
            val parentCoroutineContext = Job() + Dispatchers.Default + CoroutineName("Parent") + handler
            val scope = CoroutineScope(parentCoroutineContext)
            println("-Job:" + scope.coroutineContext[Job])
            println("-ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
            println("-CoroutineName:" + scope.coroutineContext[CoroutineName])
            println("-CoroutineExceptionHandler:" + scope.coroutineContext[CoroutineExceptionHandler])
            val jobA = scope.launch(Dispatchers.IO + CoroutineName("A")) {
                println("--Job:" + this.coroutineContext[Job])
                println("--ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                println("--CoroutineName:" + this.coroutineContext[CoroutineName])
                println("--CoroutineExceptionHandler:" + this.coroutineContext[CoroutineExceptionHandler])
                val jobA1 = launch {
                    println("---Job:" + this.coroutineContext[Job])
                    println("---ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                    println("---CoroutineName:" + this.coroutineContext[CoroutineName])
                    println("---CoroutineExceptionHandler:" + this.coroutineContext[CoroutineExceptionHandler])
                }
                delay(1000)
                println("A task end  ")
            }
            val jobB = scope.launch() {
                println("-- B Job:" + this.coroutineContext[Job])
                println("-- B ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                println("-- B CoroutineName:" + this.coroutineContext[CoroutineName])
                println("-- B CoroutineExceptionHandler:" + this.coroutineContext[CoroutineExceptionHandler])
                delay(1000)
                println("B task end")
            }
        }
        Thread.sleep(5000)
    }


}