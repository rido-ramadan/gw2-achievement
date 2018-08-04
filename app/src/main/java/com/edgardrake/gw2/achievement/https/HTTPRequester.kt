package com.edgardrake.gw2.achievement.https

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Wrapper type for [Observable]. Shorter version of Observable<Response<T>>
 */
typealias HTTPResponse<T> = Observable<Response<T>>

/**
 * [Int]: HTTP Response code [400-5xx]
 * [String]: HTTP Error message
 * [ResponseBody]: Retrofit response
 */
typealias HTTPError = (code: Int, message: String, response: ResponseBody?) -> Unit

/**
 * [Throwable]: Exception error class
 */
typealias ExceptionError = ((exception: Throwable) -> Unit)

@Suppress("ProtectedInFinal")
/**
 * Created by Rido Ramadan (rido.ramadan@gmail.com) on 31/07/18
 */
class HTTPRequester {

    protected var httpCallbacks : CompositeDisposable = CompositeDisposable()

    fun onDestroy() {
        httpCallbacks.dispose()
    }

    @JvmOverloads
    fun <T> call(request: HTTPResponse<T>,
                 onHttpSuccess: ((T, Headers) -> Unit),
                 onHttpError: HTTPError? = null,
                 onGenericError: ExceptionError? = { exception -> throw exception}) {

        val onError: ExceptionError = { error: Throwable ->
            if (error is HttpException) {
                Log.e("HTTP-Error", "${error.code()}: ${error.message()}")
                onHttpError?.invoke(error.code(),
                    error.response().message(),
                    error.response().errorBody())
            } else {
                Log.e("Exception", "${error.message}")
                onGenericError?.invoke(error)
            }
        }

        val callback: (Response<T>) -> Unit = { response ->
            if (response.code() == 200) {
                response.body()?.let {
                    onHttpSuccess(it, response.headers())
                }
            } else {
                val code = response.code()
                response.errorBody()?.let {
                    onHttpError?.invoke(code, it.string(), it)
                }
            }
        }

        httpCallbacks.add(request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback, onError))
    }
}