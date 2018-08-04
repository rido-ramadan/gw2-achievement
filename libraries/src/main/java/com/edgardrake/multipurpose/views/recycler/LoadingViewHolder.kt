package com.edgardrake.multipurpose.views.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.edgardrake.multipurpose.R
import com.edgardrake.multipurpose.utilities.inflate

class LoadingViewHolder(parent: ViewGroup):
    RecyclerView.ViewHolder(parent.inflate(R.layout.grid_loading_view_holder)) {

    companion object {
        @JvmField
        val LAYOUT_ID: Int = R.layout.grid_loading_view_holder
    }
}