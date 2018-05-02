package com.hllabs.linuxnews

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/*
* Data class to hold Article Information
* */
data class NewsArticle(
            var feedUrl:String = "", var siteName:String = "", var title:String = "", var author:String = "",
            var description:String = "", var content:String = "", var imgLink:String = "", var link:String = "",
            var pubDate: Date = Date(), var categories:List<String> = arrayListOf()
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
                Date(),
                arrayListOf())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(feedUrl)
                parcel.writeString(siteName)
                parcel.writeString(title)
                parcel.writeString(author)
                parcel.writeString(description)
                parcel.writeString(content)
                parcel.writeString(imgLink)
                parcel.writeString(link)
                parcel.writeDate(pubDate)
                parcel.writeStringList(categories)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<NewsArticle> {
                override fun createFromParcel(parcel: Parcel): NewsArticle {
                        return NewsArticle(parcel)
                }

                override fun newArray(size: Int): Array<NewsArticle?> {
                        return arrayOfNulls(size)
                }
        }

        fun Parcel.writeDate(date: Date) {
                writeLong(date.time)
        }

        fun Parcel.readDate(): Date {
                val long = readLong()
                return Date(long)
        }


}
