package me.yimu.doucalendar.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by linwei on 2017/9/20.
 */

data class DayModel(

        var weekday: String? = null,
        var date: String? = null,
        @SerializedName("lunar_date")
        var lunarDate: String? = null,
        var event: String? = null,
        @SerializedName("detail_date")
        var detailDate: String? = null,
        var content: String? = null,
        var suggestion: String? = null,
        var url: String? = null,
        var pic: String? = null,
        @SerializedName("number_of_comments")
        var numberOfComments: String? = null,
        var rating: String? = null,
        @SerializedName("event_type")
        var eventType: String? = null,
        @SerializedName("born_person")
        var bornPerson: String? = null,
        @SerializedName("die_person")
        var diePerson: String? = null,
        var supplement: String? = null,
        var contributor: String? = null,
        @SerializedName("profile_url")
        var profileUrl: String? = null,
        var description: String? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(weekday)
        parcel.writeString(date)
        parcel.writeString(lunarDate)
        parcel.writeString(event)
        parcel.writeString(detailDate)
        parcel.writeString(content)
        parcel.writeString(suggestion)
        parcel.writeString(url)
        parcel.writeString(pic)
        parcel.writeString(numberOfComments)
        parcel.writeString(rating)
        parcel.writeString(eventType)
        parcel.writeString(bornPerson)
        parcel.writeString(diePerson)
        parcel.writeString(supplement)
        parcel.writeString(contributor)
        parcel.writeString(profileUrl)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "DayModel(weekday=$weekday, date=$date, lunarDate=$lunarDate, event=$event, detailDate=$detailDate, content=$content, suggestion=$suggestion, url=$url, pic=$pic, numberOfComments=$numberOfComments, rating=$rating, eventType=$eventType, bornPerson=$bornPerson, diePerson=$diePerson, supplement=$supplement, contributor=$contributor, profileUrl=$profileUrl, description=$description)"
    }

    companion object CREATOR : Parcelable.Creator<DayModel> {
        override fun createFromParcel(parcel: Parcel): DayModel {
            return DayModel(parcel)
        }

        override fun newArray(size: Int): Array<DayModel?> {
            return arrayOfNulls(size)
        }
    }


}


