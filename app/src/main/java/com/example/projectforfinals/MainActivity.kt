package com.example.projectforfinals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        signuptextview.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.animation2, R.anim.animation)
        }
        signinbut.setOnClickListener {
            if (emailedittextlogin.text!!.isNotEmpty() && passwordedittextlogin.text!!.isNotEmpty()) {
                login()
            }
        }
        checkuser()
    }

    private fun checkuser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity3::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.animation4, R.anim.animation3)
        }
    }

    private fun login() {
        progressbarlogin.visibility = View.VISIBLE
        val email = emailedittextlogin.text.toString()
        val password = passwordedittextlogin.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressbarlogin.visibility = View.GONE
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("succ", "signInWithEmail:success")
                    intent = Intent(this, MainActivity3::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animation4, R.anim.animation3)
                } else {
                    progressbarlogin.visibility = View.GONE
                    // If sign in fails, display a message to the user.
                    Log.w("fail", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception.toString().split(":")[1],
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
