package com.edgardrake.gw2.achievement.activities.achievements


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.detail.AchievementDetailActivity
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.multipurpose.https.HTTPError
import com.edgardrake.gw2.achievement.library.BaseFragment
import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.utilities.GlideApp
import com.edgardrake.multipurpose.utilities.flatten
import com.edgardrake.multipurpose.utilities.setLookupSize
import com.edgardrake.multipurpose.utilities.toast
import kotlinx.android.synthetic.main.fragment_achievement_categories.*
import okhttp3.Headers
import okhttp3.ResponseBody

private const val CATEGORY = "category"

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AchievementsFragment : BaseFragment() {

    private lateinit var category: AchievementCategory
    private var achievements = ArrayList<Achievement>()
    private var isCalling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(CATEGORY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(requireContext())
            .load(category.icon)
            .into(achievementIcon)
        achievementTitle.text = category.name
        achievementDesc.text = category.description
        achievementDesc.visibility = if (!TextUtils.isEmpty(category.description))
            View.VISIBLE else View.GONE

        // Set up RecyclerView
        gridDataset.setHasFixedSize(true)
        gridDataset.adapter = AchievementsAdapter(achievements, category.icon, actionViewAchievement)
            .apply {
                if (!achievements.isEmpty()) stopLoading()
            }
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
            achievements.clear()
            gridDataset.adapter?.let {
                it as AchievementsAdapter
                it.resetLoading()
                it.notifyDataSetChanged()
            }
        }
    }

    private val actionViewAchievement: (pos: Int, achievement: Achievement) -> Unit = { _, achievement ->
        if (TextUtils.isEmpty(achievement.icon)) achievement.icon = category.icon
        AchievementDetailActivity.startThisActivity(hostActivity, achievement)
    }

    private fun GET_Achievements() {
        val callback: (List<Achievement>, Headers) -> Unit =
            {result: List<Achievement>, _: Headers ->
                setAchievements(result)
                isCalling = false
            }
        val onHTTPError: HTTPError? = { code: Int, message: String, _: ResponseBody? ->
            toast(String.format("%d: %s", code, message))
            gridDataset.adapter?.let {
                it as AchievementsAdapter
                it.stopLoading()
            }
        }
        isCalling = true
        httpClient.call(GuildWars2API.getService()
            .GET_Achievements(category.achievements.flatten()), callback, onHTTPError)
    }

    private fun setAchievements(source: List<Achievement>) {
        val insertionPoint = achievements.size
        achievements.addAll(source)
        gridDataset.adapter?.let {
            if (it is AchievementsAdapter) {
                // it.notifyItemRangeInserted(insertionPoint, source.size)
                it.notifyDataSetChanged()
                it.stopLoading()
            }
            gridDataset.addOnScrollListener(onScrollListener)
        }

    }

    private val onScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.adapter?.let {
                val adapter = it as AchievementsAdapter
                recyclerView.layoutManager?.let {
                    it as LinearLayoutManager
                    if (!adapter.isStopLoading && !isCalling &&
                        it.itemCount <= it.findFirstVisibleItemPosition() + it.childCount) {
                        recyclerView.removeOnScrollListener(this)
                        GET_Achievements()
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param category The Achievement Category data.
         * @return A new instance of fragment AchievementsFragment.
         */
        @JvmStatic
        fun newInstance(category: AchievementCategory) =
            AchievementsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }
}
