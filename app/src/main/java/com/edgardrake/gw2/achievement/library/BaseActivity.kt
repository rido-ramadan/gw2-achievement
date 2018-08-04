package com.edgardrake.gw2.achievement.library

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.edgardrake.multipurpose.https.HTTPRequester
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : AppCompatActivity() {

    protected var httpClient = HTTPRequester()
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

    private fun initializeView() {
//        ButterKnife.bind(this)
//        findViewById<Toolbar>(R.id.toolbar)?.let {
//            setSupportActionBar(it)
//        }
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
        httpClient.onDestroy()
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
    fun setFragment(fragment: Fragment, title: String?, @IdRes resID: Int, backstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(resID, fragment, title)

        if (backstack) {
            transaction.addToBackStack(title)
                .commit()
            // Push a title for new fragment. Must not be null
            val nextTitle = if (title.isNullOrEmpty()) currentTitle else title
            prevTitles.push(currentTitle)
            currentTitle = nextTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            transaction.commitNow()
        }
    }

    /**
     * Inflate the [fragment] to the specified [parent]. If [title] is not empty String,
     * change the support action bar title to the [title]. If [backstack] is true, then perform
     * fragment replacement rather than set up a fragment to other location
     */
    fun setFragment(fragment: Fragment, title: String, parent: ViewGroup, backstack: Boolean) {
        setFragment(fragment, title, parent.id, backstack)
    }
}