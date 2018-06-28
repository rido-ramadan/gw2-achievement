package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

    protected var httpCallbacks : CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        httpCallbacks.dispose()
    }

    protected fun getHostActivity() : BaseActivity {
        return requireActivity() as BaseActivity
    }
}