package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import com.edgardrake.gw2.achievement.https.HTTPRequester

abstract class BaseFragment : Fragment() {

    protected var httpClient = HTTPRequester()

    override fun onDestroy() {
        super.onDestroy()
        httpClient.onDestroy()
    }

    val hostActivity get() = requireActivity() as BaseActivity
}