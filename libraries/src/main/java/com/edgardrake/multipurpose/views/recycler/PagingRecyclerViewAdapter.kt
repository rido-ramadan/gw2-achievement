package com.edgardrake.multipurpose.views.recycler

import android.support.annotation.CallSuper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.view.ViewGroup

abstract class PagingRecyclerViewAdapter<T> (val dataset: MutableList<T>,
                                             val onLoadNextPage: () -> Unit,
                                             enablePaging: Boolean = true) : Adapter<ViewHolder>() {

    /**
     * If [hasNext] is true, it means paging is enabled.
     * When the scroll almost reach bottom, allow adapter to perform its [onLoadNextPage]
     */
    private var hasNext: Boolean = true
    /**
     * Public getter for [hasNext], this property expose what value [hasNext] holds.
     */
    val isPagingEnabled get() = hasNext

    /**
     * Indicates whether there is ongoing [onLoadNextPage] process that hasn't been finished.
     */
    var isBusy : Boolean = false
        private set

    init {
        hasNext = enablePaging
    }

    /**
     * Set flag that no more page will be loaded when scrolled to bottom
     */
    fun stop() {
        hasNext = false
        notifyItemRemoved(dataset.size)
    }

    /**
     * Reenable paging, allow page to be loaded when scrolled to bottom
     */
    fun reset() {
        hasNext = true
        dataset.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataset.size + (if (hasNext) 1 else 0)

    /**
     * Usage:
     * ```
     *      override fun getItemViewType(position: Int) =
     *          when (position) {
     *              // Choose between
     *              LoadingViewHolder.LAYOUT_ID -> super.getItemViewType(position)
     *              // Or
     *              default -> super.getItemViewType(position)
     *          }
     * ```
     * Or using if-else equivalent
     */
    @CallSuper
    override fun getItemViewType(position: Int): Int = LoadingViewHolder.LAYOUT_ID

    /**
     * Usage:
     * ```
     *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
     *          when (viewType) {
     *              // Choose between
     *              LoadingViewHolder.LAYOUT_ID -> super.onCreateViewHolder(parent, viewType)
     *              // Or
     *              default -> super.onCreateViewHolder(parent, viewType)
     *          }
     * ```
     * Or using if-else equivalent
     */
    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        LoadingViewHolder(parent)

    /**
     * Calling can be from constructor, directly attach to [host]:
     * ```
     *      Adapter(args).attachTo(recyclerView)
     * ```
     * or if usage is isolated in PagingFragment, like this:
     * ```
     *      adapter = Adapter(args).apply { attachTo(recyclerView) }
     * ```
     */
    fun attachTo(host: RecyclerView) {
        host.adapter = this
        host.clearOnScrollListeners()
        host.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : OnScrollListener() {
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

    /**
     * Callback function. Must be loaded on load next page success.
     */
    fun onNextPageLoaded() {
        isBusy = false
    }

    interface OnPaging {
        fun onLoadNext()
    }
}