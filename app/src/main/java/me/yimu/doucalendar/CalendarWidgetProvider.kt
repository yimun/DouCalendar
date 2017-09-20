package me.yimu.doucalendar

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import me.yimu.doucalendar.model.CalendarModel
import me.yimu.doucalendar.model.DayModel


/**
 * Created by linwei on 2017/9/20.
 */
class CalendarWidgetProvider : AppWidgetProvider() {

    val TAG = "CalendarWidgetProvider"

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate")
        val N = appWidgetIds?.size ?: 0

        for (i in 0..N - 1) {
            val appWidgetId = appWidgetIds?.get(i)
            val views = RemoteViews(context?.packageName, R.layout.appwidget_provider_layout)
            context?.let {
                val dayModel = CalendarModel.getDayModel(context, Utils.getFormatDateString())
                views.setTextViewText(R.id.tv_content, dayModel?.content)
                if (dayModel != null && dayModel.contributor.isNullOrBlank().not()) {
                    views.setViewVisibility(R.id.tv_author, View.VISIBLE)
                    views.setTextViewText(R.id.tv_author, dayModel.contributor)
                } else {
                    views.setViewVisibility(R.id.tv_author, View.GONE)
                }
                views.setTextViewText(R.id.tv_movie_name, dayModel?.suggestion)
                val date = Utils.sdf.parse(dayModel?.date)
                views.setTextViewText(R.id.tv_date, date.date.toString())
                views.setTextViewText(R.id.tv_event, "${dayModel?.event} (${dayModel?.detailDate})")

                Picasso.with(context).load(dayModel?.pic)
                        .into(views, R.id.iv_cover, appWidgetIds)
                bindRatingBar(views, dayModel)

                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.`package` = "com.douban.frodo"
                intent.data = Uri.parse(dayModel?.url)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                views.setOnClickPendingIntent(R.id.layout, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetId?.let { appWidgetManager?.updateAppWidget(appWidgetId, views) }
        }
    }

    fun bindRatingBar(views: RemoteViews, dayModel: DayModel?) {
        if (dayModel == null || dayModel.rating == null) {
            return
        }
        var rating = dayModel.rating?.toFloat() ?: 0f
        views.setTextViewText(R.id.tv_movie_score, rating.toString())
        views.setTextViewText(R.id.tv_rating_count, "${dayModel?.numberOfComments}人评价")

        val stars = listOf(R.id.star1, R.id.star2,  R.id.star3, R.id.star4, R.id.star5)
        stars.forEach {
            if (rating > 2.0) {
                views.setImageViewResource(it, R.drawable.rating_star_xsmall_on)
            } else if (rating > 1.0) {
                views.setImageViewResource(it, R.drawable.rating_star_xsmall_half)
            } else {
                views.setImageViewResource(it, R.drawable.rating_star_xsmall_off)
            }
            rating -= 2f
        }

    }

}