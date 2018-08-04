package com.edgardrake.gw2.achievement.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Achievement(
    val id: Int,
    val name: String,
    var icon: String? = null,
    val description: String?,
    val requirement: String,
    val locked_text: String?,
    val type: AchievementType,
    val flags: List<Flag>?,
    val tiers: List<Tier>? = null,
    val prerequisites: List<Int>? = null,
    val bits: List<Bit>? = null,
    val point_cap: Int? = null
    ): Parcelable, Serializable {

    enum class AchievementType: Serializable, Parcelable {
        @SerializedName("Default") DEFAULT { override fun toString() = "Default" },
        @SerializedName("ItemSet") ITEM_SET { override fun toString() = "ItemSet" };

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(ordinal)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<AchievementType> {
            override fun createFromParcel(parcel: Parcel) = AchievementType.values()[parcel.readInt()]

            override fun newArray(size: Int): Array<AchievementType?> = arrayOfNulls(size)
        }
    }

    enum class Flag: Serializable, Parcelable {
        @SerializedName("Pvp") PVP,
        @SerializedName("CategoryDisplay") DISPLAY,
        @SerializedName("MoveToTop") MOVE_TO_TOP,
        @SerializedName("IgnoreNearlyComplete") IGNORE_NEARLY_COMPLETE,
        @SerializedName("Repeatable") REPEATABLE,
        @SerializedName("Hidden") HIDDEN,
        @SerializedName("RequiresUnlock") REQUIRE_UNLOCK,
        @SerializedName("RepairOnLogin") REPAIR_ON_LOGIN,
        @SerializedName("Daily") DAILY,
        @SerializedName("Weekly") WEEKLY,
        @SerializedName("Monthly") MONTHLY,
        @SerializedName("Permanent") PERMANENT;

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(ordinal)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<Flag> {
            override fun createFromParcel(parcel: Parcel) = Flag.values()[parcel.readInt()]

            override fun newArray(size: Int): Array<Flag?> = arrayOfNulls(size)
        }
    }

    @Parcelize
    data class Tier(val count: Int, val points: Int): Parcelable, Serializable

    enum class RewardType: Serializable, Parcelable {
        @SerializedName("Text") TEXT,
        @SerializedName("Item") ITEM,
        @SerializedName("Minipet") MINI_PET,
        @SerializedName("Skin") SKIN;

        override fun writeToParcel(parcel: Parcel, flags: Int) = parcel.writeInt(ordinal)

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<RewardType> {
            override fun createFromParcel(parcel: Parcel) = RewardType.values()[parcel.readInt()]

            override fun newArray(size: Int): Array<RewardType?> = arrayOfNulls(size)
        }
    }

    @Parcelize
    data class Bit(val id: Int,
                   val type: RewardType?,
                   val text: String? = null): Parcelable, Serializable
}