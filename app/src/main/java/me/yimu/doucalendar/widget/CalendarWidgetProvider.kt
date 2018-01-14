package me.yimu.doucalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import me.yimu.doucalendar.BuildConfig
import me.yimu.doucalendar.Logger
import me.yimu.doucalendar.R


/**
 * Created by linwei on 2017/9/20.
 */
class CalendarWidgetProvider : AppWidgetProvider() {

    val TAG = "CalendarWidgetProvider"

    companion object {
        val ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK"
        val ACTION_AUTO_UPDATE = BuildConfig.APPLICATION_ID + ".ACTION_AUTO_UPDATE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Logger.d(TAG, "onReceive=${intent?.action} ${intent?.dataString}")
        when (intent?.action) {
            ACTION_CLICK -> {
                val vIntent = Intent()
                vIntent.data = intent?.data
                vIntent.action = Intent.ACTION_VIEW
                context?.startActivity(vIntent)
            }
            AppWidgetManager.ACTION_APPWIDGET_UPDATE,
            ACTION_AUTO_UPDATE -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidget = ComponentName(context?.packageName, javaClass.name)
                val appIds = appWidgetManager.getAppWidgetIds(appWidget)
                onUpdate(context, appWidgetManager, appIds)
            }
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Logger.d(TAG, "onUpdate appWidgetIds=${appWidgetIds?.contentToString()}")
        val N = appWidgetIds?.size ?: 0

        for (i in 0..N - 1) {
            val appWidgetId = appWidgetIds?.get(i)
            val views = RemoteViews(context?.packageName, R.layout.appwidget_provider_layout)

            val intent = Intent(context, CalendarWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            if (appWidgetId != null) {
                views.setRemoteAdapter(R.id.list_view, intent)
            }

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            val clickIntent = Intent(context, CalendarWidgetProvider::class.java)
            clickIntent.action = ACTION_CLICK
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.list_view, toastPendingIntent)

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetId?.let {
                Logger.d(TAG, "start update $appWidgetId")
                appWidgetManager?.updateAppWidget(appWidgetId, views)
                appWidgetManager?.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view)
            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        CalendarWidgetAlarm.startAlarm(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        CalendarWidgetAlarm.stopAlarm(context)
    }

}