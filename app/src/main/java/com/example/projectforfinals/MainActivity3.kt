package com.example.projectforfinals
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    val mylist = mutableListOf<post>()
    private val filteredlist = mutableListOf<post>()
    private val ref = FirebaseDatabase.getInstance().getReference("posts")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        buttonadd.setOnClickListener { startActivity(Intent(this, MainActivity4::class.java)) }
        buttonsignout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            overridePendingTransition(R.anim.animation2, R.anim.animation)
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }
        ref.addListenerForSingleValueEvent(getdata)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        searchView.addTextChangedListener {
            search(it)
        }
    }

    private fun search(its: Editable?) {
        if (its.toString() == "") {
            recyclerposts.adapter = adapter(mylist)
        } else {
            filteredlist.clear()
            mylist.forEach {
                if (it.topic.contains(its.toString())) {
                    filteredlist.add(it)
                }
            }
            recyclerposts.adapter = adapter(filteredlist)
        }
    }
    private var getdata = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach {
                val getfullname = it.child("fullname").value.toString()
                val getimgurl = it.child("imgurl").value.toString()
                val gettext = it.child("text").value.toString()
                val gettopic = it.child("topic").value.toString()
                val getkey = it.child("key").value.toString()
                mylist.add(0, post(getkey, getimgurl, getfullname, gettopic, gettext))
            }
            recyclerposts.adapter = adapter(mylist)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }
}