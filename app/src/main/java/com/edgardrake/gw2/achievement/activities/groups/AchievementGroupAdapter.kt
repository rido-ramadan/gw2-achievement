package com.edgardrake.gw2.achievement.activities.groups

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementGroup
import kotlinx.android.synthetic.main.grid_achievement.view.*

class AchievementGroupAdapter(val dataset: List<AchievementGroup>,
                              val onItemClicked: (Int, AchievementGroup) -> Unit):
    RecyclerView.Adapter<AchievementGroupAdapter.AchievementGroupHolder>() {

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementGroupHolder {
        return AchievementGroupHolder(parent)
    }

    override fun onBindViewHolder(holder: AchievementGroupHolder, position: Int) {
        val achievementGroup = dataset[holder.adapterPosition]
        holder.title = achievementGroup.name
        holder.itemView.setOnClickListener {
            onItemClicked(holder.adapterPosition, achievementGroup)
        }
    }

    class AchievementGroupHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
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

