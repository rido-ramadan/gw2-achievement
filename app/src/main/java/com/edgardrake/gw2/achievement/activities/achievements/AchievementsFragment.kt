package com.edgardrake.gw2.achievement.activities.achievements


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.library.BaseFragment
import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.models.AchievementCategory
import kotlinx.android.synthetic.main.fragment_achievement_categories.*

private const val CATEGORY = "category"

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AchievementsFragment : BaseFragment() {

    private lateinit var category: AchievementCategory
    private var achievements: List<Achievement> = ArrayList()
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

        Glide.with(requireContext()).load(category.icon).into(achievementIcon)
        achievementDesc.text = category.description
        achievementTitle.text = category.name

        gridDataset.setHasFixedSize(true)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param category Parameter 1.
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
