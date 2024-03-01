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


class ExampleUnitTest_CoroutineCore {
    @Test
    fun test() {
        val scope = CoroutineScope(Job())
        scope.launch {
            val msg = getNewMessage()
            println(msg)
        }

        Thread.sleep(2000)
    }

    suspend fun getNewMessage(): String {
        return withContext(Dispatchers.IO) {
            delay(1000)
            "12345"
        }
    }


    suspend fun testSuspend() {}


}