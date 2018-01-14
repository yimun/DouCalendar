package me.yimu.doucalendar.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by linwei on 2017/9/20.
 */
object CalendarModel {

    val TAG = "CalendarModel"

    val allDays: HashMap<String, DayModel> = hashMapOf()

    /**
     * Get all Calendar from json asset
     */
    fun loadFromJson(context: Context) {
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open("moon_data_2018.json")
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            val type = object : TypeToken<List<DayModel>>() {}.type
            val daysList = Gson().fromJson<List<DayModel>>(bufferReader, type)
            allDays.clear()
            daysList.forEach {
                day -> day.date?.let { allDays[it] = day }
            }
        } catch(e: Exception) {
            Log.e(TAG, "read file exception")
        } finally {
            inputStream?.close()
        }
    }

    fun getDayModel(context: Context, date: String): DayModel? {
        if (allDays.isEmpty()) {
            loadFromJson(context)
        }
        return allDays[date]
    }

}