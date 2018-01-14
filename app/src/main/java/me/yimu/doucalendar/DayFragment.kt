package me.yimu.doucalendar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.layout_widget_content.*
import kotlinx.android.synthetic.main.rating_bar_layout.*
import me.yimu.doucalendar.model.CalendarModel
import java.util.regex.Pattern

/**
 * Created by linwei on 2018/1/14.
 */

class DayFragment : Fragment() {

    companion object {
        val KEY_DATE = "key_date"

        fun newInstance(date: String): DayFragment {
            val fragment = DayFragment()
            val bundle = Bundle()
            bundle.putString(KEY_DATE, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_day, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val date = arguments.getString(KEY_DATE)
        title.text = date
        val matcher = Pattern.compile("\\d+月(\\d+)日").matcher(date)
        if (matcher.find()) {
            tv_date.text = matcher.group(1)
        }
        val dayModel = CalendarModel.getDayModel(activity, date)
        tv_content.text = dayModel?.content
        if (dayModel != null && dayModel.contributor.isNullOrBlank().not()) {
            tv_author.visibility = View.VISIBLE
            tv_author.text = "——${dayModel.contributor}"
        } else {
            tv_author.visibility = View.GONE
        }
        tv_movie_name.text = dayModel?.suggestion
        if (TextUtils.isEmpty(dayModel?.detailDate)) {
            tv_event.text = dayModel?.event
        } else {
            tv_event.text = "${dayModel?.event} (${dayModel?.detailDate})"
        }

        Picasso.with(activity).load(dayModel?.pic).into(iv_cover)
        list_item.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(dayModel?.url)
            startActivity(intent)
        }
        if (dayModel == null || dayModel.rating == null) {
            return
        }
        var rating = dayModel.rating?.toFloat() ?: 0f
        tv_movie_score.text = rating.toString()
        tv_rating_count.text = "${dayModel.numberOfComments}人评价"

        val stars = listOf(star1, star2, star3, star4, star5)
        stars.forEach {
            if (rating > 2.0) {
                it.setImageResource(R.drawable.rating_star_xsmall_on)
            } else if (rating > 1.0) {
                it.setImageResource(R.drawable.rating_star_xsmall_half)
            } else {
                it.setImageResource(R.drawable.rating_star_xsmall_off)
            }
            rating -= 2f
        }
    }

}
