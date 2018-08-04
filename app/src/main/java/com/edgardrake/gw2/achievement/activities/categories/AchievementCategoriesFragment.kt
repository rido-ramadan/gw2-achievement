package com.edgardrake.gw2.achievement.activities.categories

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.achievements.AchievementsFragment
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.PagingFragment
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.utilities.flatten
import com.edgardrake.gw2.achievement.utilities.getInt
import com.edgardrake.gw2.achievement.utilities.setLookupSize
import com.edgardrake.gw2.achievement.widgets.recycler.LoadingViewHolder
import kotlinx.android.synthetic.main.fragment_achievement_categories.*
import okhttp3.Headers

/**
 * A placeholder fragment containing a simple view.
 */
class AchievementCategoriesFragment : PagingFragment() {

    private lateinit var group: AchievementGroup
    private var categories = ArrayList<AchievementCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(ACHIEVEMENT_GROUP)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_achievement_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achievementIcon.visibility = View.GONE
        achievementDesc.text = group.description
        achievementTitle.text = group.name

        // Set up RecyclerView
        gridDataset.setHasFixedSize(true)

        val onItemClick = { _: Int, data: AchievementCategory -> actionOpenCategory(data) }
        val onScroll = { GET_AchievementCategories() }
        adapter = AchievementCategoriesAdapter(categories, onItemClick, onScroll, isPagingEnabled)
            .apply { attachTo(gridDataset) }
        gridDataset.layoutManager?.let {
            if (it is GridLayoutManager) {
                it.setLookupSize { position ->
                    when (adapter.getItemViewType(position)) {
                        LoadingViewHolder.LAYOUT_ID -> R.integer.grid_column.getInt()
                        else -> 1
                    }
                }
            }
        }

        // Set up SwipeRefreshLayout
        refreshContainer.setOnRefreshListener {
            refreshContainer.isRefreshing = false
            adapterReset()
        }
    }

    private fun GET_AchievementCategories() {
        val callback = { result: List<AchievementCategory>, _: Headers ->
            setAchievementCategories(result)
        }
        httpClient.call(GuildWars2API.getService()
            .GET_AchievementCategories(group.categories.flatten(), 0), callback)
    }

    private fun setAchievementCategories(source: List<AchievementCategory>) {
        val insertionPoint = categories.size
        categories.addAll(source)

        adapter.run {
            onNextPageLoaded()
            adapterStop()
            notifyDataSetChanged()
            // notifyItemRangeInserted(insertionPoint, source.size)
        }
    }

    private fun actionOpenCategory(category: AchievementCategory) {
        if (hostActivity.findViewById<ViewGroup>(R.id.achievementCategoryDetail) != null) {
            hostActivity.setFragment(AchievementsFragment.newInstance(category), "",
                R.id.achievementCategoryDetail, false)
        } else {
            hostActivity.setFragment(AchievementsFragment.newInstance(category), category.name,
                R.id.fragmentContainer, true)
        }
    }

    companion object {
        const val ACHIEVEMENT_GROUP = "data"

        @JvmStatic
        fun newInstance(group: AchievementGroup): AchievementCategoriesFragment =
            AchievementCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ACHIEVEMENT_GROUP, group)
                }
            }
    }
}
