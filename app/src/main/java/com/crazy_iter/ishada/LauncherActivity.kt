package com.crazy_iter.ishada

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Models.TaskModel
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        setLoader(1)

        Handler().postDelayed({
            getTasks()
        }, 2000)

    }

    private fun getTasks() {
        val queue = Volley.newRequestQueue(this)
        val tasksRequest = JsonArrayRequest(StaticVars.TASKS_BASE, {
            queue.cancelAll("tasks")
            Log.e("tasks", it.toString())
            StaticMethods.tasks = ArrayList<TaskModel>()

            for (i in 0 until it.length()) {
                val task = it.getJSONObject(i)

                var users = ArrayList<String>()
                try {
                    for (j in 0 until task.getJSONArray("taskUser").length()) {
                        users.add(task.getJSONArray("taskUser").getJSONObject(j).getString("userID"))
                    }
                } catch (e: Exception) {
                    Log.e("err", e.message)
                    users = ArrayList()
                }
                val taskModel = TaskModel(task.getString("_id"),
                        task.getString("title"),
                        task.getString("description"),
                        task.getString("status"),
                        task.getString("todoDate"),
                        users)

                StaticMethods.tasks.add(taskModel)
            }

            val perfs = getSharedPreferences("Ishada", Context.MODE_PRIVATE)
            if (perfs?.getString("userID", "")?.isEmpty()!!) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, {
            Log.e("tasks error", it.toString())
            queue.cancelAll("tasks")
            getTasks()
        })
        tasksRequest.tag = "tasks"
        tasksRequest.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(tasksRequest)
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
