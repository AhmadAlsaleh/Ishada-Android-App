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

class DoneFragment : Fragment() {

    private lateinit var doneRV: RecyclerView
    private lateinit var doneNoTV: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_done, container, false)

        doneRV = view.findViewById(R.id.doneRV)
        doneNoTV = view.findViewById(R.id.doneNoTV)

        doneRV.setHasFixedSize(true)
        doneRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setTasks()

        doneRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            if (t.status.contains("Done", true)) {
                tasks.add(t)
            }
        }
        try {
            if (tasks.isEmpty()) {
                doneNoTV.visibility = View.VISIBLE
            } else {
                doneNoTV.visibility = View.GONE
                doneRV.adapter = TasksAdapter(activity as MainActivity, context!!, tasks)
            }
        } catch (err: Exception) {}
    }

}
