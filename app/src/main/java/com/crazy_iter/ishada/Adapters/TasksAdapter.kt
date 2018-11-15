package com.crazy_iter.ishada.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.crazy_iter.ishada.Dialogs.SelectUsersDialog
import com.crazy_iter.ishada.Dialogs.SureDialog
import com.crazy_iter.ishada.MainActivity
import com.crazy_iter.ishada.Models.TaskModel
import com.crazy_iter.ishada.R
import com.crazy_iter.ishada.StaticMethods
import com.crazy_iter.ishada.TaskPreviewActivity

class TasksAdapter(var activity: MainActivity, var context: Context, private var tasks: ArrayList<TaskModel>)
    :RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_task, p0, false))

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = tasks[position]
        holder.taskTitleTV.text = task.title
        holder.taskDateTV.text = task.data

        when (task.status.toLowerCase()) {
            "new" -> holder.taskStatusIV.setImageResource(R.drawable.ic_start_arrow)
            "todo" -> holder.taskStatusIV.setImageResource(R.drawable.ic_hourglass)
            "done" -> holder.taskStatusIV.setImageResource(R.drawable.ic_done)
            "finish" -> holder.taskStatusIV.setImageResource(R.drawable.ic_done_all)
        }

        holder.taskLL.setOnClickListener {
            context.startActivity(Intent(context, TaskPreviewActivity::class.java)
                    .putExtra("task", task))
        }

        // region menu
        val popupMenu = PopupMenu(context, holder.taskMoreIV)
        popupMenu.menuInflater.inflate(R.menu.task_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.taskDone -> {
                    val sure = SureDialog(context,"Sure to set Task as Done?", task.id, "Done")
                    sure.setOnDismissListener {
                        if (sure.isDone) {
                            StaticMethods.changeTaskStatus(task.id, "Done")
                            activity.setTasks()
                        }
                    }
                    sure.show()
                }

                R.id.taskFinish -> {
                    val sure = SureDialog(context,"Sure to set Task as Finished?", task.id, "Finish")
                    sure.setOnDismissListener {
                        if (sure.isDone) {
                            StaticMethods.changeTaskStatus(task.id, "Finish")
                            activity.setTasks()
                        }
                    }
                    sure.show()
                }

                R.id.taskAssignTo -> {
                    SelectUsersDialog(activity, context, task).show()
                }

            }
            true
        }
        holder.taskMoreIV.setOnClickListener {
            popupMenu.show()
        }
        // endregion

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val taskLL = itemView.findViewById<LinearLayout>(R.id.taskLL)!!
        val taskTitleTV = itemView.findViewById<TextView>(R.id.taskTitleTV)!!
        val taskDateTV = itemView.findViewById<TextView>(R.id.taskDateTV)!!
        val taskStatusIV = itemView.findViewById<ImageView>(R.id.taskStatusIV)!!
        val taskMoreIV = itemView.findViewById<ImageView>(R.id.taskMoreIV)!!

    }


}