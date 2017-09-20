package me.yimu.doucalendar

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by linwei on 2017/9/20.
 */
object Utils {

    val sdf = SimpleDateFormat("M月d日", Locale.CHINA)

    fun getFormatDateString(): String {
        return sdf.format(Date())
    }

    fun getCalendarFromFormatDate(dateStr: String): String {
        val date = sdf.parse(dateStr)
        date.year = Date().year
        val format2 = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
        return format2.format(date)
    }
}