package com.crazy_iter.ishada.MainFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazy_iter.ishada.Adapters.TasksAdapter
import com.crazy_iter.ishada.MainActivity
import com.crazy_iter.ishada.Models.TaskModel
import com.crazy_iter.ishada.R
import com.crazy_iter.ishada.StaticMethods

class FinishFragment : Fragment() {

    private lateinit var finishRV: RecyclerView
    private lateinit var finishNoTV: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_finish, container, false)

        finishRV = view.findViewById(R.id.finishRV)
        finishNoTV = view.findViewById(R.id.finishNoTV)

        finishRV.setHasFixedSize(true)
        finishRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setTasks()

        finishRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    (activity as MainActivity).hideFABs() // hide
                } else if (dy < 0) {
                    (activity as MainActivity).showFABs() // show
                }
            }
        })

        return view
    }

    fun setTasks() {
        val tasks = ArrayList<TaskModel>()
        for (t in StaticMethods.tasks) {
            if (t.status.contains("Finish", true)) {
                tasks.add(t)
            }
        }
        try {
            if (tasks.isEmpty()) {
                finishNoTV.visibility = View.VISIBLE
            } else {
                finishRV.adapter = TasksAdapter(activity as MainActivity, context!!, tasks)
                finishNoTV.visibility = View.GONE
            }
        } catch (err: Exception) {}
    }

}
