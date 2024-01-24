package roy.proyekakhir.mosket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

class IntroSlider : AppCompatActivity() {


    private lateinit var mSLideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var btnNext: Button

    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        btnNext = findViewById(R.id.btnNext)


        btnNext.setOnClickListener {
            if (getItem(0) < 2) {
                mSLideViewPager.setCurrentItem(getItem(1), true)
            } else {
                val i = Intent(this@IntroSlider, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }


        mSLideViewPager = findViewById(R.id.slideViewPager)
        mDotLayout = findViewById(R.id.indicator_layout)

        viewPagerAdapter = ViewPagerAdapter(this)

        mSLideViewPager.adapter = viewPagerAdapter

        setUpIndicator(0)
        mSLideViewPager.addOnPageChangeListener(viewListener)
    }

    private fun setUpIndicator(position: Int) {
        dots = Array(3) { TextView(this) }
        mDotLayout.removeAllViews()

        for (i in 0 until dots.size) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226").toString()
            dots[i].textSize = 35f
            dots[i].setTextColor(getColor(R.color.inactive))
            mDotLayout.addView(dots[i])
        }

        dots[position].setTextColor(getColor(R.color.active))
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private fun getItem(i: Int): Int {
        return mSLideViewPager.currentItem + i
    }





}