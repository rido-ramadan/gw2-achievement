package com.edgardrake.gw2.achievement.library

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar_fixed.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseActivity : AppCompatActivity() {

    protected var httpCallbacks = CompositeDisposable()

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initializeView()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initializeView()
    }

    fun initializeView() {
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        httpCallbacks.dispose()
    }

    protected fun getActivity() : BaseActivity {
        return this
    }

    fun getApp(): BaseApplication {
        return super.getApplication() as BaseApplication
    }

    fun <T> httpCall(request: Observable<T>,
                     callback: ((T) -> Unit),
                     onHttpError: ((code: Int, message: String, response: ResponseBody?) -> Unit)? = null,
                     onGenericError: ((message: String) -> Unit)? = null) {

        val onError: (error: Throwable) -> Unit = { error: Throwable ->
            if (error is HttpException) {
                Log.e("HTTP-Error", "${error.code()}: ${error.message()}")
                onHttpError?.invoke(error.code(), error.response().message(), error.response().errorBody())
            } else {
                Log.e("Exception", "${error.message}")
                onGenericError?.invoke(error.localizedMessage)
            }
        }

        httpCallbacks.add(request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback, onError))
    }
}