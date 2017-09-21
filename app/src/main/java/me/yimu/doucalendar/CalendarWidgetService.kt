package me.yimu.doucalendar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.squareup.picasso.Picasso
import me.yimu.doucalendar.model.CalendarModel
import me.yimu.doucalendar.model.DayModel
import java.util.*


/**
 * Created by linwei on 2017/9/20.
 */

class CalendarWidgetService : RemoteViewsService() {


    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return CalendarRemoteViewsFactory(this.applicationContext)
    }
}

val ACTION_CLICK = "yimu.me.doucalendar.ACTION_CLICK"

internal class CalendarRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun getCount(): Int {
        return 1
    }

    override fun getViewAt(position: Int): RemoteViews {
        val date = Date()
        date.date -= position
        val dayModel = CalendarModel.getDayModel(mContext, Utils.sdf.format(date))
        val views = RemoteViews(mContext.packageName, R.layout.layout_widget_content)
        views.setTextViewText(R.id.tv_content, dayModel?.content)
        if (dayModel != null && dayModel.contributor.isNullOrBlank().not()) {
            views.setViewVisibility(R.id.tv_author, View.VISIBLE)
            views.setTextViewText(R.id.tv_author, "——${dayModel.contributor}")
        } else {
            views.setViewVisibility(R.id.tv_author, View.GONE)
        }
        views.setTextViewText(R.id.tv_movie_name, dayModel?.suggestion)
        views.setTextViewText(R.id.tv_date, date.date.toString())
        views.setTextViewText(R.id.tv_event, "${dayModel?.event} (${dayModel?.detailDate})")

        // 这里非主线程，只可以同步获取
        // https://stackoverflow.com/questions/24771297/picasso-load-images-to-widget-listview/42437729#42437729
        val bitmap = Picasso.with(mContext).load(dayModel?.pic).get()
        views.setImageViewBitmap(R.id.iv_cover, bitmap)
        bindRatingBar(views, dayModel)

        val intent = Intent()
        intent.action = ACTION_CLICK
        intent.data = Uri.parse(dayModel?.url)
        views.setOnClickFillInIntent(R.id.list_item, intent)
        return views
    }

    fun bindRatingBar(views: RemoteViews, dayModel: DayModel?) {
        if (dayModel == null || dayModel.rating == null) {
            return
        }
        var rating = dayModel.rating?.toFloat() ?: 0f
        views.setTextViewText(R.id.tv_movie_score, rating.toString())
        views.setTextViewText(R.id.tv_rating_count, "${dayModel.numberOfComments}人评价")

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

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }
}
