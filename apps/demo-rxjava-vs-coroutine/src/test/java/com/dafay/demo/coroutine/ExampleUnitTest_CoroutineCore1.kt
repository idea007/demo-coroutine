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
import java.io.File
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext


class ExampleUnitTest_CoroutineCore1 {

    @Test
    fun test() {
        runBlocking {
            val isSuccess = copyFileTo(File("old.mpr"), File("new.mp4"))
            println("copy:${isSuccess}")
        }
    }

    suspend fun copyFileTo(oldFile: File, newFile: File): Boolean {
        val isCopySuccess = withContext(Dispatchers.IO) {
            try {
                Thread.sleep(1000)
                true
            } catch (e: Exception) {
                false
            }
        }
        return isCopySuccess
    }


}