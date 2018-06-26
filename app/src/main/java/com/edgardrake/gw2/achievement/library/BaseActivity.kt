package com.edgardrake.gw2.achievement.library

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.toolbar_fixed.*

abstract class BaseActivity : AppCompatActivity() {

    protected var httpCallback : Disposable? = null

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
        httpCallback?.dispose()
    }

    protected fun getActivity() : BaseActivity {
        return this
    }

    fun getApp(): BaseApplication {
        return super.getApplication() as BaseApplication
    }
}