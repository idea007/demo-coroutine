package com.dafay.demo.coroutine.test

import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.lib.base.net.RetrofitFamily
import com.dafay.demo.lib.base.utils.debug
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    fun testLaunch1() {
        GlobalScope.launch() {

            withContext(Dispatchers.IO) {
                debug("获取数据...")
                delay(2000)
                debug("获取数据结束")
            }

            coroutineScope {
                debug("获取数据...")
                delay(2000)
                debug("获取数据结束")
            }
        }
    }

    fun testLaunch() {
        // launch: 在不阻塞当前线程的情况下启动新的协程，并将对协程的引用作为作业返回。当生成的作业被取消时，协程将被取消。
        GlobalScope.launch(Dispatchers.Default) {
            mockQueryPhotos(1)
        }

        // runBlocking: 运行新的协程，并可中断地阻塞当前线程，直到其完成。不应从协程中使用此函数。它旨在将常规阻塞代码桥接到以挂起方式编写的库，用于主函数和测试
        runBlocking(Dispatchers.IO) {
            mockQueryPhotos(2)
        }

        GlobalScope.launch(CoroutineName("MyCoroutine")) {
            debug("current coroutineName=${coroutineContext[CoroutineName]}")
            // 使用给定的协程上下文调用指定的挂起块，挂起直到完成，然后返回结果
            // 它并不启动协程，只会(可能会)导致线程的切换。用它执行的挂起块中的上下文是当前协程的上下文和由它执行的上下文的合并结果。 withContext的目的不在于启动子协程，它最初用于将长耗时操作从UI线程切走，完事再切回来。
            withContext(Dispatchers.IO) {
                mockQueryPhotos(3)
            }
            // 一个suspend方法，创建一个新的作用域，并在该作用域内执行指定代码块，它并不启动协程。其存在的目的是进行符合结构化并发的并行分解（即，将长耗时任务拆分为并发的多个短耗时任务，并等待所有并发任务完成后再返回）。
            coroutineScope {
                mockQueryPhotos(4)
            }
        }
    }


    /**
     * 模拟网络请求操作
     */
    private suspend fun mockQueryPhotos(page: Int): String {
        debug("queryPhotos start page=${page}")
        delay(2000L)
        val result = RetrofitFamily.createService(PexelsApi::class.java).queryPhotos("art", page, 10)
        val str = "queryPhotos success:page=${page} photos.size=${result.photos.size}"
        debug(str)
        return str
    }

    /**
     * 模拟网络请求异常
     */
    private suspend fun mockQueryPhotosCrash(page: Int): String {
        debug("queryPhotos start page=${page}")
        val result = RetrofitFamily.createService(PexelsApi::class.java).queryPhotos("art", page, 10)
        throw RuntimeException("queryPhotos Crash")
    }

    /**
     * 模拟网络请求异常
     */
    private suspend fun mockQueryPhotosFail(page: Int): String {
        debug("queryPhotos start page=${page}")
        val result = RetrofitFamily.createService(PexelsApi::class.java).queryPhotosError("art", page, 10)
        val str = "queryPhotos success:page=${page} photos.size=${result.photos.size}"
        debug(str)
        return str
    }

}