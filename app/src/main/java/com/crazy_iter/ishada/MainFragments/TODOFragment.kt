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

class TODOFragment : Fragment() {

    private lateinit var todoRV: RecyclerView
    private lateinit var toDoNoTV: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        todoRV = view.findViewById(R.id.todoRV)
        toDoNoTV = view.findViewById(R.id.toDoNoTV)

        todoRV.setHasFixedSize(true)
        todoRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setTasks()

        todoRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            if (t.status.contains("TODO", true)) {
                tasks.add(t)
            }
        }
        try {
            if (tasks.isEmpty()) {
                toDoNoTV.visibility = View.VISIBLE
            } else {
                todoRV.adapter = TasksAdapter(activity as MainActivity, context!!, tasks)
                toDoNoTV.visibility = View.GONE
            }
        } catch (err: Exception) {}
    }

}
