package com.edgardrake.gw2.achievement.activities.achievements

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementCategory

private const val CATEGORY = "category"

class AchievementsActivity : BaseActivity() {

    private lateinit var category: AchievementCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_categories)

        category = intent.getParcelableExtra(CATEGORY)
        currentTitle = category.name

        setFragment(AchievementsFragment.newInstance(category), null,
            R.id.fragmentContainer, false)
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context, category: AchievementCategory) {
            context.startActivity(Intent(context, AchievementsActivity::class.java)
                .putExtra(CATEGORY, category))
        }
    }
}
