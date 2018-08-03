package com.edgardrake.gw2.achievement.widgets.recycler

import android.support.annotation.CallSuper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class PagingRecyclerViewAdapter<T>(val dataset: MutableList<T>,
                                            val onLoadNextPage: Any.() -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var hasNext: Boolean = true
        private set

    fun stop() {
        hasNext = false
        notifyItemRemoved(dataset.size)
    }

    fun reset() {
        hasNext = true
    }

    var isBusy : Boolean = false
        private set


    override fun getItemCount(): Int = dataset.size + (if (hasNext) 1 else 0)

    @CallSuper
    override fun getItemViewType(position: Int): Int = LoadingViewHolder.LAYOUT_ID

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LoadingViewHolder(parent)

    fun attachTo(host: RecyclerView) {
        host.adapter = this
        host.clearOnScrollListeners()
        host.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.layoutManager?.let { manager ->
                manager as LinearLayoutManager
                if (hasNext && !isBusy && manager.itemCount <=
                    manager.findFirstVisibleItemPosition() + manager.childCount) {
                    isBusy = true
                    onLoadNextPage()
                }
            }
        }
    }

    fun onLoadNextPageSuccess() {
        isBusy = false
    }
}