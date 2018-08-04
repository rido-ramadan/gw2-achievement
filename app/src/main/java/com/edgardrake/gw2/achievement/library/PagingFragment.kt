package com.edgardrake.gw2.achievement.library

import android.support.v7.widget.RecyclerView
import com.edgardrake.gw2.achievement.widgets.recycler.PagingRecyclerViewAdapter

/**
 * Created by Rido Ramadan (rido.ramadan@gmail.com) on 04/08/18
 */
abstract class PagingFragment : BaseFragment() {

    /**
     * [RecyclerView.Adapter]
     */
    protected lateinit var adapter: PagingRecyclerViewAdapter<*>
    protected var isPagingEnabled: Boolean = true
    protected var currentPage: Int = DEFAULT_PAGE

    protected fun syncFragmentWithAdapter() {
        isPagingEnabled = adapter.isPagingEnabled
    }

    /**
     * Helper method to clear [adapter] dataset, reset [currentPage] to its [DEFAULT_PAGE],
     * and perform [syncFragmentWithAdapter]
     */
    protected fun adapterReset() {
        currentPage = DEFAULT_PAGE
        adapter.reset()
        syncFragmentWithAdapter()
    }

    /**
     * Helper method to stop paging (no more [PagingRecyclerViewAdapter.onNextPageLoaded] call)
     * and perform [syncFragmentWithAdapter]
     */
    protected fun adapterStop() {
        adapter.stop()
        syncFragmentWithAdapter()
    }

    companion object {
        const val DEFAULT_PAGE = 0
    }
}