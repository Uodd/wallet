package com.example.wallet

import android.content.Context
import android.util.Log
import androidx.compose.animation.fadeIn
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.concurrent.Executor
import java.util.concurrent.Executors


private const val TAGHTTP = "UrlRequestCallback"


fun HttpReq(context: Context,input:String){
    val myBuilder = CronetEngine.Builder(context)
    val cronetEngine: CronetEngine = myBuilder.enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, 100 * 1024.toLong())
        .enableHttp2(true)
        .enableQuic(true)
        .build()
    val executor: Executor = Executors.newSingleThreadExecutor()
    val reqhand=RequestCallback()
    val requestBuilder = cronetEngine.newUrlRequestBuilder(
        "https://rapidapi.com/zingzy/api/qrcode68",
        reqhand,
        executor
    )
    requestBuilder.addHeader("Content-Type", "multipart/form-data")
    requestBuilder.addHeader("x-rapidapi-host","qrcode68.p.rapidapi.com")
    requestBuilder.addHeader("-rapidapi-key","Sign Up for Key")
    requestBuilder.addHeader("form","text=$input")
    requestBuilder.addHeader("form","gradient1=(106,26,76)")
    requestBuilder.addHeader("form","gradient2=(64,53,60)")

    val request: UrlRequest = requestBuilder.build()
    request.start()

}

class RequestCallback() : UrlRequest.Callback() {

    override fun onRedirectReceived(request: UrlRequest?, info: UrlResponseInfo?, newLocationUrl: String?) {
        Log.i(TAGHTTP, "onRedirectReceived method called.")
        // You should call the request.followRed`irect() method to continue
        // processing the request.
        request?.followRedirect()
    }
    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAGHTTP, "onResponseStarted method called.")
        // You should call the request.read() method before the request can be
        // further processed. The following instruction provides a ByteBuffer object
        // with a capacity of 102400 bytes for the read() method. The same buffer
        // with data is passed to the onReadCompleted() method.
        request?.read(ByteBuffer.allocateDirect(102400))
    }

    override fun onReadCompleted(request: UrlRequest?, info: UrlResponseInfo?, byteBuffer: ByteBuffer?) {
        Log.i(TAGHTTP, "onReadCompleted method called.")

        // You should keep reading the request until there's no more data.
        byteBuffer?.clear()
        request?.read(byteBuffer)
        Log.v(TAGHTTP,byteBuffer.toString())

    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAGHTTP, "onSucceeded method called.")

    }

    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        TODO("Not yet implemented")
    }

}



