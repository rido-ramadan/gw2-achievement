package com.edgardrake.gw2.achievement.activities.categories

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.utilities.GlideApp
import com.edgardrake.gw2.achievement.widgets.recycler.PagingRecyclerViewAdapter
import kotlinx.android.synthetic.main.grid_achievement.view.*

class AchievementCategoriesAdapter(dataset: MutableList<AchievementCategory>,
                                   val onItemClicked: (Int, AchievementCategory) -> Unit,
                                   onScroll: () -> Unit,
                                   enablePaging: Boolean):
    PagingRecyclerViewAdapter<AchievementCategory>(dataset, onScroll, enablePaging) {

    override fun getItemViewType(position: Int): Int =
        if (position < dataset.size)
            R.layout.grid_achievement
        else
            super.getItemViewType(position)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == R.layout.grid_achievement)
            AchievementCategoriesHolder(parent)
        else
            super.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AchievementCategoriesHolder) {
            val category = dataset[holder.adapterPosition]
            holder.title = category.name
            holder.setIcon(category.icon)
            holder.itemView.setOnClickListener { onItemClicked(holder.adapterPosition, category) }
        }
    }

    class AchievementCategoriesHolder(parent: ViewGroup): ViewHolder(
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