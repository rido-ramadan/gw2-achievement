package com.edgardrake.gw2.achievement.activities.groups

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.edgardrake.gw2.achievement.R
import com.edgardrake.multipurpose.base.BaseActivity

class AchievementGroupsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_group)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setFragment(AchievementGroupsFragment.newInstance(), null,
            R.id.fragmentContainer, false)
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, AchievementGroupsActivity::class.java))
        }

        const val ACHIEVEMENT_GROUPS = "groups"
    }
}
