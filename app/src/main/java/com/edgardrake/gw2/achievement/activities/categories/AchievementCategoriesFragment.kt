package com.edgardrake.gw2.achievement.activities.categories

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.achievements.AchievementsActivity
import com.edgardrake.gw2.achievement.activities.achievements.AchievementsFragment
import com.edgardrake.gw2.achievement.activities.groups.AchievementGroupAdapter
import com.edgardrake.gw2.achievement.activities.groups.AchievementGroupsActivity
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseFragment
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.utilities.Logger
import com.edgardrake.gw2.achievement.utilities.setLookupSize
import kotlinx.android.synthetic.main.fragment_achievement_categories.*
import okhttp3.Headers

/**
 * A placeholder fragment containing a simple view.
 */
private const val ACHIEVEMENT_GROUP = "data"

class AchievementCategoriesFragment : BaseFragment() {

    private lateinit var group: AchievementGroup
    private var categories = ArrayList<AchievementCategory>()
    private var isCalling = false

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
        val onItemClick = { _: Int, data: AchievementCategory ->
            if (getHostActivity() is AchievementGroupsActivity) {
                getHostActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.achievementCategoryDetail, AchievementsFragment.newInstance(data))
                    .commitNow()
            } else {
                AchievementsActivity.startThisActivity(getHostActivity(), data)
            }
        }
        gridDataset.setHasFixedSize(true)
        gridDataset.adapter = AchievementCategoriesAdapter(categories, onItemClick)
        gridDataset.layoutManager?.let {
            if (it is GridLayoutManager) {
                it.setLookupSize { position ->
                    when (gridDataset.adapter?.getItemViewType(position)) {
                        R.layout.grid_loading_view_holder ->
                            requireContext().resources.getInteger(R.integer.grid_column)
                        else -> 1
                    }
                }
            }
        }
        gridDataset.addOnScrollListener(onScrollListener)

        // Set up SwipeRefreshLayout
        refreshContainer.setOnRefreshListener {
            refreshContainer.isRefreshing = false
            categories.clear()
            gridDataset.adapter?.let {
                it as AchievementCategoriesAdapter
                it.resetLoading()
                it.notifyDataSetChanged()
            }
            GET_AchievementCategories()
        }
    }

    private fun GET_AchievementCategories() {
        val callback: (List<AchievementCategory>, Headers) -> Unit =
            {result: List<AchievementCategory>, _: Headers ->
                setAchievementCategories(result)
                isCalling = false
        }
        isCalling = true
        httpCall(GuildWars2API.getService()
            .GET_AchievementCategories(group.flattenCategories(), 0), callback)
    }

    private fun setAchievementCategories(source: List<AchievementCategory>) {
        val insertionPoint = categories.size
        categories.addAll(source)
        gridDataset.adapter?.let {
            if (it is AchievementCategoriesAdapter) {
                // it.notifyItemRangeInserted(insertionPoint, source.size)
                it.notifyDataSetChanged()
                it.stopLoading()
            }
        }
        gridDataset.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.adapter?.let {
                val adapter = it as AchievementCategoriesAdapter
                recyclerView.layoutManager?.let {
                    it as LinearLayoutManager
                    if (!adapter.isStopLoading && !isCalling &&
                        it.itemCount <= it.findFirstVisibleItemPosition() + it.childCount) {
                        recyclerView.removeOnScrollListener(this)
                        GET_AchievementCategories()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(group: AchievementGroup): AchievementCategoriesFragment =
            AchievementCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ACHIEVEMENT_GROUP, group)
                }
            }
    }
}
