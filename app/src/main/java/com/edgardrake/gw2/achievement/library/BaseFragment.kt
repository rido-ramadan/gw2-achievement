package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseFragment : Fragment() {

    protected var httpCallbacks : CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        httpCallbacks.dispose()
    }

    protected fun getHostActivity() : BaseActivity {
        return requireActivity() as BaseActivity
    }

    @JvmOverloads
    fun <T> httpCall(request: Observable<Response<T>>,
                     onHttpSuccess: ((T, Headers) -> Unit),
                     onHttpError: ((code: Int, message: String, response: ResponseBody?) -> Unit)? = null,
                     onGenericError: ((t: Throwable) -> Unit)? = {exception -> throw exception}) {
        val onError: (error: Throwable) -> Unit = { error: Throwable ->
            if (error is HttpException) {
                Log.e("HTTP-Error", "${error.code()}: ${error.message()}")
                onHttpError?.invoke(error.code(), error.response().message(), error.response().errorBody())
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