package com.edgardrake.gw2.achievement.models

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AchievementCategories(
    val id: Int,
    val name: String,
    val description: String?,
    val order: Int,
    val icon: String,
    val achievements: List<Int>
): Parcelable {
    fun flattenAchievements(): String {
        return TextUtils.join(",", achievements)
    }
}