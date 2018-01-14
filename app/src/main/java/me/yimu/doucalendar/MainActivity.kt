package me.yimu.doucalendar

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        view_pager.adapter = MyPagerAdapter(supportFragmentManager)
        view_pager.currentItem = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1
    }

    class MyPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int {
            val year = Calendar.getInstance().get(Calendar.YEAR) + 1970
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 366
            }
            return 365
        }

        override fun getItem(position: Int): Fragment {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_YEAR, position + 1)
            return DayFragment.newInstance(SimpleDateFormat("M月d日", Locale.CHINA).format(cal.time))
        }

    }

}
