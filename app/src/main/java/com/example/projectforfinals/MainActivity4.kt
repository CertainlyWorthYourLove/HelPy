package com.example.projectforfinals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main4.*

class MainActivity4 : AppCompatActivity() {
    private lateinit var imgurl : String
    private lateinit var fullname : String
    private val usersref = FirebaseDatabase.getInstance().getReference("users")
    private val ref = FirebaseDatabase.getInstance().getReference("posts")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        postnowbichobutton.setOnClickListener {
            if (topicedittext.text!!.isNotEmpty() && problemedittext.text!!.isNotEmpty()){
            senddatatofirebase()}else{
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }
        }
        usersref.addListenerForSingleValueEvent(getdata)
    }

    private var getdata = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach {
                if (it.key == FirebaseAuth.getInstance().currentUser!!.uid){
                    fullname = if(it.child("usersurname").value.toString() != "incognito"){
                        "${it.child("username").value} ${it.child("usersurname").value}"
                    } else{
                        it.child("username").value.toString()
                    }
                    imgurl = it.child("url").value.toString()
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
        }

    }
    private fun senddatatofirebase() {
        val topic = topicedittext.text.toString()
        val problem = problemedittext.text.toString()
        val fullnamepost = fullname
        val urlpost = imgurl
        val key = ref.push().key.toString()
        ref.child(key).setValue(post(key ,urlpost, fullnamepost, topic, problem))
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity3::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to post, Try again", Toast.LENGTH_SHORT).show()
            }
    }
}