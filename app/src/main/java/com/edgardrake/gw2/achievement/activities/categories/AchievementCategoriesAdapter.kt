package com.edgardrake.gw2.achievement.activities.categories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.utilities.GlideApp
import com.edgardrake.gw2.achievement.widgets.LoadingViewHolder
import kotlinx.android.synthetic.main.grid_achievement.view.*

class AchievementCategoriesAdapter(val dataset: List<AchievementCategory>,
                                   val onItemClicked: (Int, AchievementCategory) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isStopLoading = false
        private set

    override fun getItemCount() = dataset.size + if (isStopLoading) 0 else 1

    override fun getItemViewType(position: Int) =
        if (position == dataset.size)
            R.layout.grid_loading_view_holder
        else
            R.layout.grid_achievement


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == R.layout.grid_achievement)
            AchievementCategoriesHolder(parent)
        else
            LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AchievementCategoriesHolder) {
            val category = dataset[holder.adapterPosition]
            holder.title = category.name
            holder.setIcon(category.icon)
            holder.itemView.setOnClickListener { onItemClicked(holder.adapterPosition, category) }
        }
    }

    fun stopLoading() {
        notifyItemRemoved(itemCount)
        isStopLoading = true
    }

    fun resetLoading() {
        isStopLoading = false
    }

    class AchievementCategoriesHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grid_achievement, parent, false)) {

        var title: String
            get() = itemView.mAchievementTitle.text.toString()
            set(value) { itemView.mAchievementTitle.text = value }

        fun setIcon(url: String) {
            GlideApp.with(itemView)
                .load(url)
                .into(itemView.mAchievementIcon)
        }
    }
}