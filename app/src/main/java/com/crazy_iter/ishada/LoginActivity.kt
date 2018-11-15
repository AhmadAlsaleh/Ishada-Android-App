package com.crazy_iter.ishada

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.signInFL, LoadingFragment())
        ft.commit()
        signInFL.visibility = View.GONE

        signInBTN.setOnClickListener {
            if (signInUsernameET.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter your username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (signInPasswordET.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val json = JSONObject()
            json.put("username", signInUsernameET.text.trim())
            json.put("password", signInPasswordET.text.trim())
            signIn(json)

        }

    }

    private fun signIn(json: JSONObject) {
        signInFL.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val signInRequest = JsonObjectRequest(Request.Method.POST, StaticVars.SIGN_IN, json, {
            queue.cancelAll("in")
            val perfs = getSharedPreferences("Ishada", Context.MODE_PRIVATE).edit()
            perfs.putString("userID", it.getString("_id"))
            perfs.putString("role", it.getString("departmentID"))
            perfs.putString("name", it.getString("username"))
            perfs.apply()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }, {
            queue.cancelAll("in")
            signInFL.visibility = View.GONE
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
        })
        signInRequest.tag = "in"
        signInRequest.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(signInRequest)
    }

}
