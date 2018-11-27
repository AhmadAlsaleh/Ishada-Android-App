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
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.signInFL, LoadingFragment())
        ft.commit()
        signInFL.visibility = View.GONE

        signInBTN.setOnClickListener {
            if (signInUsernameET.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (signInPasswordET.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (signUpEmailET.text.trim().isEmpty() || !StaticMethods.isEmail(signUpEmailET.text.toString().trim())) {
                Toast.makeText(this, "Check email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val json = JSONObject()
            json.put("username", signInUsernameET.text.trim())
            json.put("password", signInPasswordET.text.trim())
            json.put("email", signUpEmailET.text.trim())
            if (roleAdminRB.isChecked) {
                json.put("departmentID", "admin")
            } else {
                json.put("departmentID", "employee")
            }
            signIn(json)

        }

    }

    private fun signIn(json: JSONObject) {
        signInFL.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val signInRequest = JsonObjectRequest(Request.Method.POST, StaticVars.SIGN_UP, json, {
            queue.cancelAll("in")
            Toast.makeText(this, "Signed up Successfully", Toast.LENGTH_SHORT).show()
            onBackPressed()
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
