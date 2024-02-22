package com.dafay.demo.coroutine.test

import com.dafay.demo.lib.base.utils.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/22
 */
object TestCoroutine {

    val myCoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = TODO("Not yet implemented")

    }

    private fun test_coroutine() {
        debug("before")
        runBlocking(Dispatchers.IO) { // MainScope 协程作用域
            val deferred = async { // 在后台启动一个新的协程并继续
                delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
                return@async "Hello, World!"
            }
            debug("Waiting for result...") // 协程已在等待时主线程还在继续
            debug(deferred.await())
        }
        debug("after")
    }

    private fun test_coroutine_5() {
        debug("before")
        MainScope().launch { // MainScope 协程作用域
            val deferred = async { // 在后台启动一个新的协程并继续
                delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
                return@async "Hello, World!"
            }
            debug("Waiting for result...") // 协程已在等待时主线程还在继续
            debug(deferred.await())
        }
        debug("after")
    }

    private fun test_coroutine_4() {
        debug("before")
        runBlocking { // 这里的 runBlocking 创建一个新的协程作用域
            val job = launch { // 在后台启动一个新的协程并继续
                delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
                debug("World!")
            }
            debug("Hello,") // 协程已在等待时主线程还在继续
            job.join() // 等待直到子协程执行结束
        }
        debug("after")
    }


    private fun test_coroutine_3() {
        debug("before")
        runBlocking(Dispatchers.IO) {
            debug("twoRequestFetch start")
            val slow = twoRequestFetch()
            debug("twoRequestFetch end ${slow}")
        }
        debug("after")
    }

    /**
     * 测试异常
     */
    private fun test_coroutine_2() {
        debug("before")
        MainScope().launch {
            debug("fetchWithCrash start")
            val slow = fetchDataCrash()
            debug("fetchWithCrash end ${slow}")
        }
        debug("after")
    }

    /**
     * 执行顺序
     */
    private fun test_coroutine_1() {
        debug("before")
        MainScope().launch {
            debug("firstFetch start")
            val slow = fetchPage1()
            debug("firstFetch end ${slow}")
        }
        debug("after")
    }

    /**
     * 两个请求顺序执行
     */
    suspend fun twoRequestFetch() =
        withContext(Dispatchers.IO) {
            val slow = fetchPage1()
            val another = fetchPage2()
            return@withContext slow + another
        }

    fun testAsync() {
        debug("before")
        GlobalScope.launch {
            debug("执行在协程中...")
            val deferred = async {
                debug("切换到另一个协程")
                Thread.sleep(2000)
                return@async "response data"
            }
            val response = deferred.await()
            debug("response:$response")
            val result = withContext(Dispatchers.IO) {
                //异步执行
                delay(1000L)
                debug("切换到另一个协程")
                return@withContext "1234"
            }
            debug("result:$result")
            debug("执行完毕...")
        }
        debug("end")
    }

    /**
     * launch{} CoroutineScope的扩展方法，启动一个协程，不阻塞当前协程，并返回新协程的Job。
     */
    fun testLaunch() {
        debug("before")
        val job = GlobalScope.launch {
            val slow = fetchPage1()
            debug("fetchPage1 result:${slow}")
        }
        debug("end")
    }


    /**
     * 模拟耗时操作
     */
    private suspend fun fetchPage1(): String {
        debug("exec fetchPage1 ...")
        delay(2000)
        return "page 1 data"
    }

    /**
     * 模拟耗时操作
     */
    private suspend fun fetchPage2(): String {
        debug("exec fetchPage2 ...")
        delay(2000)
        return "page 2 data"
    }

    /**
     * 模拟耗时操作异常
     */
    private suspend fun fetchDataCrash(): String {
        debug("exec fetchDataCrash ...")
        delay(2000)
        throw RuntimeException("fetchDataCrash")
    }

}