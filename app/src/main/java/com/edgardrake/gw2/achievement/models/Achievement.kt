package com.edgardrake.gw2.achievement.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Achievement(
    val id: Int,
    val name: String,
    val icon: String? = null,
    val description: String?,
    val requirement: String,
    val locked_text: String?,
    val type: AchievementType,
    val flags: List<Flag>,
    val tiers: List<@RawValue Tier>? = null,
    val prerequisites: List<Int>? = null,
    val bits: List<@RawValue Bit>? = null,
    val point_cap: Int? = null
    ): Parcelable {

    enum class AchievementType {
        @SerializedName("Default") DEFAULT,
        @SerializedName("ItemSet") ITEM_SET
    }

    enum class Flag {
        @SerializedName("Pvp") PVP,
        @SerializedName("CategoryDisplay") DISPLAY,
        @SerializedName("MoveToTop") MOVE_TO_TOP,
        @SerializedName("IgnoreNearlyComplete") IGNORE_NEARLY_COMPLETE,
        @SerializedName("Repeatable") REPEATABLE,
        @SerializedName("Hidden") HIDDEN,
        @SerializedName("RequiresUnlock") REQUIRE_UNLOCK,
        @SerializedName("Daily") DAILY,
        @SerializedName("Weekly") WEEKLY,
        @SerializedName("Monthly") MONTHLY,
        @SerializedName("Permanent") PERMANENT
    }

    data class Tier(val count: Int, val points: Int)

    enum class RewardType {
        @SerializedName("Text") TEXT,
        @SerializedName("Item") ITEM,
        @SerializedName("Minipet") MINI_PET,
        @SerializedName("Skin") SKIN
    }

    data class Bit(val id: Int,
                   val type: RewardType,
                   val text: String? = null)
}