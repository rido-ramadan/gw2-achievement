package com.edgardrake.multipurpose.base

import android.support.v7.widget.RecyclerView.Adapter
import com.edgardrake.multipurpose.views.recycler.PagingRecyclerViewAdapter

/**
 * Created by Rido Ramadan (rido.ramadan@gmail.com) on 04/08/18
 */
abstract class PagingFragment : BaseFragment() {

    /**
     * Locked dataset that will be used in conjunction with [adapter].
     * The adapter must be constructed using this property.
     * ```
     *      dataset = mutableListOf<Model>()
     *      adapter = Adapter(dataset, varargs).apply { attachTo(recyclerView) }
     * ```
     */
    protected abstract val dataset: MutableList<*>
    /**
     * [Adapter] for the recycler view. Already enabled to do paging from the beginning.
     */
    protected lateinit var adapter: PagingRecyclerViewAdapter<*>
    /**
     * Fragment owned flag about enable or disable paging. Used when in reconstruction of [adapter]
     * during [onViewCreated] when pop-back the fragment transaction. Since the value
     * [PagingRecyclerViewAdapter.isPagingEnabled] always restart to default value during
     * reconstruction, the value now is locked by the perpetual fragment's [isPagingEnabled].
     */
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