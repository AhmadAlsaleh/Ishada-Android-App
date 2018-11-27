package com.crazy_iter.ishada

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        setLoader(1)

        Handler().postDelayed({
            val perfs = getSharedPreferences("Ishada", Context.MODE_PRIVATE)
            if (perfs?.getString("userID", "")?.isEmpty()!!) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, MeetingsActivity::class.java))
            }
            finish()
        }, 2000)

    }

    private fun setLoader(current: Int) {

        Handler().postDelayed({

            loadRL1.setBackgroundResource(R.drawable.loading_unselect_point)
            loadRL2.setBackgroundResource(R.drawable.loading_unselect_point)
            loadRL3.setBackgroundResource(R.drawable.loading_unselect_point)
            loadRL4.setBackgroundResource(R.drawable.loading_unselect_point)

            when (current) {
                0 -> loadRL1.setBackgroundResource(R.drawable.loading_select_point)
                1 -> loadRL2.setBackgroundResource(R.drawable.loading_select_point)
                2 -> loadRL3.setBackgroundResource(R.drawable.loading_select_point)
                3 -> loadRL4.setBackgroundResource(R.drawable.loading_select_point)
            }
            if (current == 3) {
                setLoader(0)
            } else {
                setLoader(current + 1)
            }

        }, 250)

    }

}
