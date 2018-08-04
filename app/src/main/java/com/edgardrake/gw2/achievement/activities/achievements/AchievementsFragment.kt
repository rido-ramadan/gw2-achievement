package com.edgardrake.gw2.achievement.activities.achievements


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.detail.AchievementDetailActivity
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.utilities.GlideApp
import com.edgardrake.multipurpose.base.PagingFragment
import com.edgardrake.multipurpose.https.HTTPError
import com.edgardrake.multipurpose.utilities.flatten
import com.edgardrake.multipurpose.utilities.getInt
import com.edgardrake.multipurpose.utilities.setLookupSize
import com.edgardrake.multipurpose.utilities.toast
import com.edgardrake.multipurpose.views.recycler.LoadingViewHolder
import kotlinx.android.synthetic.main.fragment_achievement_categories.*
import okhttp3.Headers
import okhttp3.ResponseBody

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AchievementsFragment : PagingFragment() {

    private lateinit var category: AchievementCategory
    override val dataset = mutableListOf<Achievement>()

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
        adapter = AchievementsAdapter(dataset, category.icon, actionViewAchievement,
            { GET_Achievements() }, isPagingEnabled)
            .apply {
                attachTo(gridDataset)
                //if (!achievements.isEmpty()) stop()
            }
        gridDataset.layoutManager?.let {
            if (it is GridLayoutManager) {
                it.setLookupSize { position ->
                    when (gridDataset.adapter?.getItemViewType(position)) {
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

    private val actionViewAchievement: (pos: Int, achievement: Achievement) -> Unit =
        { _, achievement ->
            if (TextUtils.isEmpty(achievement.icon)) achievement.icon = category.icon
            AchievementDetailActivity.startThisActivity(hostActivity, achievement)
        }

    private fun GET_Achievements() {
        val callback: (List<Achievement>, Headers) -> Unit =
            { result: List<Achievement>, _: Headers ->
                setAchievements(result)

                currentPage++
                if (currentPage >= 1) {
                    adapterStop()
                }
            }
        val onHTTPError: HTTPError? = { code: Int, message: String, _: ResponseBody? ->
            toast(String.format("%d: %s", code, message))
            adapter.apply {
                adapterStop()
            }
        }
        httpClient.call(GuildWars2API.getService()
            .GET_Achievements(category.achievements.flatten()), callback, onHTTPError)
    }

    private fun setAchievements(source: List<Achievement>) {
        val insertionPoint = dataset.size
        dataset.addAll(source)
        adapter.apply {
            onNextPageLoaded()
            notifyDataSetChanged()
            // it.notifyItemRangeInserted(insertionPoint, source.size)
        }
    }

    companion object {
        private const val CATEGORY = "category"

        @JvmStatic
        fun newInstance(category: AchievementCategory) =
            AchievementsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }
}
