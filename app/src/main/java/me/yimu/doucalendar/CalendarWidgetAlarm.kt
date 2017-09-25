package me.yimu.doucalendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*


/**
 * Created by linwei on 2017/9/25.
 */
class CalendarWidgetAlarm {

    companion object {
        val ALARM_ID = 0
        val INTERVAL_MILLIS = 10 * 1000
        val TAG = "CalendarWidgetAlarm"

        fun startAlarm(context: Context?) {
            Logger.d(TAG, "startAlarm")
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS)
            val alarmIntent = Intent(CalendarWidgetProvider.ACTION_AUTO_UPDATE)
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            // RTC does not wake the device up
            alarmManager.setRepeating(AlarmManager.RTC, calendar.timeInMillis, INTERVAL_MILLIS.toLong(), pendingIntent)
        }

        fun stopAlarm(context: Context?) {
            Logger.d(TAG, "stopAlarm")
            val alarmIntent = Intent(CalendarWidgetProvider.ACTION_AUTO_UPDATE)
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

}