package com.edgardrake.gw2.achievement.library

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar_fixed.*
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    protected var httpCallbacks = CompositeDisposable()
    protected var prevTitles = Stack<String>()
    protected var currentTitle: String?
        get() = supportActionBar?.title.toString()
        set(value) {
            supportActionBar?.title = value
        }

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            if (!prevTitles.empty()) currentTitle = prevTitles.pop()
        } else {
            super.onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 1)
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

    /**
     * Set the [fragment] to the specified layout ID [resID]. If [title] is not empty String,
     * change the support action bar title to the [title]. If [backstack] is true, then perform
     * fragment replacement rather than set up a fragment to other location
     */
    fun setFragment(fragment: Fragment, title: String, @IdRes resID: Int, backstack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(resID, fragment, title)

        if (backstack) {
            transaction.addToBackStack(title)
                .commit()
            // Push a title for new fragment. Must not be null
            var nextTitle = if (TextUtils.isEmpty(title)) currentTitle else title
            prevTitles.push(currentTitle)
            currentTitle = nextTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            transaction.commitNow()
        }
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