package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {

    protected var httpCallback : Disposable? = null

    override fun onDestroy() {
        super.onDestroy()
        httpCallback?.dispose()
    }

    protected var hostActivity : BaseActivity? = null
        get() = activity as BaseActivity
}