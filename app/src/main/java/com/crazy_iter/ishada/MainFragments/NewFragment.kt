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

class NewFragment : Fragment() {

    private lateinit var openRV: RecyclerView
    private lateinit var newNoTV: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)

        openRV = view.findViewById(R.id.openRV)
        newNoTV = view.findViewById(R.id.newNoTV)

        openRV.setHasFixedSize(true)
        openRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setTasks()

        openRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            if (t.status.contains("New", true)) {
                tasks.add(t)
            }
        }
        try {
            if (tasks.isEmpty()) {
                newNoTV.visibility = View.VISIBLE
            } else {
                openRV.adapter = TasksAdapter(activity as MainActivity, context!!, tasks)
                newNoTV.visibility = View.GONE
            }
        } catch (err: Exception) {}
    }

}
