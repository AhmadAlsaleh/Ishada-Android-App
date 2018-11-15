package com.crazy_iter.ishada.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Adapters.UsersAdapter
import com.crazy_iter.ishada.MainActivity
import com.crazy_iter.ishada.Models.TaskModel
import com.crazy_iter.ishada.Models.UserModel
import com.crazy_iter.ishada.R
import com.crazy_iter.ishada.StaticMethods
import com.crazy_iter.ishada.StaticVars
import kotlinx.android.synthetic.main.dialog_select_users.*
import org.json.JSONArray
import org.json.JSONObject

class SelectUsersDialog(var activity: MainActivity, context: Context, var task: TaskModel): Dialog(context) {

    var isDone = false
    private var users = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_users)
        setCanceledOnTouchOutside(false)

        selectUsersTB.title = task.title

        selectUsersRV.setHasFixedSize(true)
        selectUsersRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        getData()

        usersDoneBTN.setOnClickListener {
            if (usersPB.visibility == View.GONE) {
                isDone = true
                assignUsers()
            }
        }
        usersCancelBTN.setOnClickListener {
            isDone = false
            dismiss()
        }
    }

    private fun assignUsers() {
        usersDoneBTN.visibility = View.GONE
        usersCancelBTN.visibility = View.GONE
        userDonePB.visibility = View.VISIBLE

        val json = JSONObject()
        json.put("taskID", task.id)

        val usersArray = JSONArray()
        for (u in users) {
            if (u.selected) {
                val uObject = JSONObject()
                uObject.put("userID", u.id)
                usersArray.put(uObject)
            }
        }
        json.put("users", usersArray)
        Log.e("ass object", json.toString())

        val queue = Volley.newRequestQueue(context)
        val assRequest = JsonObjectRequest(Request.Method.POST, StaticVars.ASSIGN_TASK, json, {
            queue.cancelAll("ass")
            Log.e("ass", it.toString())
            StaticMethods.setTaskUser(json)
            activity.recreate()
            dismiss()
        }, {
            queue.cancelAll("ass")
            Log.e("ass error", it.toString())
            assignUsers()
        })
        assRequest.tag = "ass"
        assRequest.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(assRequest)
    }

    private fun getData() {
        val queue = Volley.newRequestQueue(context)
        val usersRequest = JsonArrayRequest(StaticVars.USERS_BASE, {
            queue.cancelAll("users")
            Log.e("users", it.toString())
            users = ArrayList()
            for (i in 0 until it.length()) {
                val user = it.getJSONObject(i)

                val userModel = UserModel(
                        user.getString("_id"), user.getString("username"),
                        user.getString("fullName"), user.getString("email"),
                        user.getString("phone"), user.getString("departmentID"))
                for (userID in task.taskUser) {
                    if (userID == userModel.id) {
                        userModel.selected = true
                    }
                }
                users.add(userModel)
            }
            setData()
        }, {
            queue.cancelAll("users")
            Log.e("users error", it.toString())
            getData()
        })
        usersRequest.tag = "users"
        usersRequest.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(usersRequest)
    }

    private fun setData() {
        try {
            selectUsersRV.visibility = View.VISIBLE
            usersPB.visibility = View.GONE
            selectUsersRV.adapter = UsersAdapter(context, users)
        } catch (err: Exception) {}
    }
}