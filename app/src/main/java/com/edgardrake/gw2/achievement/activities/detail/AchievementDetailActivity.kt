package com.edgardrake.gw2.achievement.activities.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.utilities.GlideApp
import kotlinx.android.synthetic.main.activity_achievement_detail.*

class AchievementDetailActivity : BaseActivity() {

    private lateinit var achievement: Achievement
    private var mName: String
        get() = currentTitle!!
        set(value) {
            currentTitle = value
        }
    private var mRequirement: String
        get() = achievementRequirement.text.toString()
        set(value) { achievementRequirement.text = value }
    private var mDescription: String?
        get() = achievementDesc.text.toString()
        set(value) {
            achievementDesc.text = value
            achievementDesc.visibility = if (TextUtils.isEmpty(value)) View.GONE else View.VISIBLE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_detail)

        // achievement = intent.getSerializableExtra(ACHIEVEMENT) as Achievement
        achievement = intent.getParcelableExtra(ACHIEVEMENT)
        mName = achievement.name
        mDescription = achievement.description
        mRequirement = achievement.requirement
        setIcon(achievement.icon)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun setIcon(url: String?) {
        GlideApp.with(this).load(url).into(achievementIcon)
    }

    companion object {

        private const val ACHIEVEMENT = "achievement"

        @JvmStatic
        fun startThisActivity(context: Context, achievement: Achievement) {
            context.startActivity(Intent(context, AchievementDetailActivity::class.java)
                .putExtra(ACHIEVEMENT, achievement as Parcelable))
        }
    }
}
