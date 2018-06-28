package com.edgardrake.gw2.achievement.activities.categories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementCategory
import kotlinx.android.synthetic.main.grid_achievement.view.*

class AchievementCategoriesAdapter(val dataset: List<AchievementCategory>,
                                   val onItemClicked: (Int, AchievementCategory) -> Unit):
    RecyclerView.Adapter<AchievementCategoriesAdapter.AchievementCategoriesHolder>() {

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AchievementCategoriesHolder(parent)

    override fun onBindViewHolder(holder: AchievementCategoriesHolder, position: Int) {
        val category = dataset[holder.adapterPosition]
        holder.title = category.name
        holder.setIcon(category.icon)
        holder.itemView.setOnClickListener { onItemClicked(holder.adapterPosition, category) }
    }

    class AchievementCategoriesHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grid_achievement, parent, false)) {

        var title: String
            get() = itemView.mAchievementTitle.text.toString()
            set(value) { itemView.mAchievementTitle.text = value }

        fun setIcon(url: String) {
            Glide.with(itemView)
                .load(url)
                .into(itemView.mAchievementIcon)
        }
    }
}