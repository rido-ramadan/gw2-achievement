package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException

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
    fun <T> httpCall(request: Observable<T>,
                     callback: ((T) -> Unit),
                     onHttpError: ((code: Int, message: String, response: ResponseBody?) -> Unit)? = null,
                     onGenericError: ((t: Throwable) -> Unit)? = {exception -> throw exception}) {
        // On Error callback definition
        val onError: (error: Throwable) -> Unit = { error: Throwable ->
            if (error is HttpException) {
                Log.e("HTTP-Error", "${error.code()}: ${error.message()}")
                onHttpError?.invoke(error.code(), error.response().message(), error.response().errorBody())
            } else {
                Log.e("Exception", "${error.message}")
                onGenericError?.invoke(error)
            }
        }

        httpCallbacks.add(request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback, onError))
    }
}