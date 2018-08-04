package com.edgardrake.multipurpose.base

import android.support.v4.app.Fragment
import com.edgardrake.multipurpose.https.HTTPRequester

abstract class BaseFragment : Fragment() {

    protected var httpClient = HTTPRequester()

    override fun onDestroy() {
        super.onDestroy()
        httpClient.onDestroy()
    }

    val hostActivity get() = requireActivity() as BaseActivity
}