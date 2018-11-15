package com.crazy_iter.ishada

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.MainFragments.*
import com.crazy_iter.ishada.Models.TaskModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val newFragment = NewFragment()
    private val toDoFragment = TODOFragment()
    private val doneFragment = DoneFragment()
    private val finishFragment = FinishFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainFragmentsAdapter(supportFragmentManager)
        adapter.addFragment(newFragment, "New")
        adapter.addFragment(toDoFragment, "TODO")
        adapter.addFragment(doneFragment, "Done")
        adapter.addFragment(finishFragment, "Finish")
        mainVP.adapter = adapter
        mainTL.setupWithViewPager(mainVP)

        mainSRL.setColorSchemeColors(resources.getColor(R.color.primary))
        mainSRL.isRefreshing = false
        mainSRL.setOnRefreshListener {
            getTasks()
        }

        mainUserSelectedTV.text = getSharedPreferences("Ishada", Context.MODE_PRIVATE)
                .getString("name", "")

        mainAddNewTaskFAB.setOnClickListener {
            startActivity(Intent(this, CreateNewTaskActivity::class.java))
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Sure to Exit?")
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun setTasks() {
        recreate()
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
                    } catch (err: Exception) {
                        users = ArrayList()
                    }
                    val taskModel = TaskModel(
                            task.getString("_id"),
                            task.getString("title"),
                            task.getString("description"),
                            task.getString("status"),
                            task.getString("todoDate"),
                            users)

                    StaticMethods.tasks.add(taskModel)
                }
                newFragment.setTasks()
                toDoFragment.setTasks()
                doneFragment.setTasks()
                finishFragment.setTasks()
                mainSRL.isRefreshing = false
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

    fun hideFABs() {
        StaticMethods.hideRightAnim(this, mainFABsLL)
    }

    fun showFABs() {
        StaticMethods.showRightAnim(this, mainFABsLL)
    }

}
