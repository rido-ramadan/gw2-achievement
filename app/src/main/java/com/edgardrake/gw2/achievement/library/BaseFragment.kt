package com.edgardrake.gw2.achievement.library

import android.support.v4.app.Fragment
import android.util.Log
import com.edgardrake.gw2.achievement.https.HTTPRequester
import com.edgardrake.gw2.achievement.https.HTTPResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseFragment : Fragment() {

    protected var httpClient = HTTPRequester()

    override fun onDestroy() {
        super.onDestroy()
        httpClient.onDestroy()
    }

    val hostActivity get() = requireActivity() as BaseActivity
}