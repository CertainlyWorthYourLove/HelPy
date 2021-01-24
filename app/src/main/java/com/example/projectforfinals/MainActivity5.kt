package com.example.projectforfinals

import adaptercomments
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main5.*

class MainActivity5 : AppCompatActivity() {
    private val commentlist = mutableListOf<comments>()
    private val refusers = FirebaseDatabase.getInstance().getReference("users")
    private val refcomments = FirebaseDatabase.getInstance().getReference("comments")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        val topic = intent.getStringExtra("topic")
        val text = intent.getStringExtra("text")
        val imgurl = intent.getStringExtra("imgurl")
        textviewtopic.text = topic
        textviewpost.text = text
        Picasso.get().load(imgurl).into(imageviewpostcomm)
        addcomment.setOnClickListener {
            if(commentedittext.text!!.isNotEmpty())
            {senddata()}else{
                Toast.makeText(this, "Can't add an empty comment", Toast.LENGTH_SHORT).show()
            }
        }
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        refusers.addListenerForSingleValueEvent(getdata)
        refcomments.addValueEventListener(getcomments)

    }
    lateinit var namecom :String
    lateinit var imgurlcom : String
    private var getdata = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach {
                if (it.key.toString() == FirebaseAuth.getInstance().currentUser!!.uid){
                    namecom = if (it.child("usersurname").value.toString() == "incognito"){
                        it.child("username").value.toString()
                    }else{
                        "${it.child("username").value} ${it.child("usersurname").value}"
                    }
                    imgurlcom = it.child("url").value.toString()
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private fun senddata() {
        val key = refcomments.push().key.toString()
        val postkeyres = intent.getStringExtra("keyforposts")!!
        refcomments.child(postkeyres).child(key).setValue(comments(imgurlcom, namecom, commentedittext.text.toString()))
            .addOnSuccessListener {
                Toast.makeText(this, "Comment added!", Toast.LENGTH_SHORT).show()
                commentedittext.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(this, "FAIL : $it", Toast.LENGTH_SHORT).show()
            }
    }
    var getcomments = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            commentlist.clear()
            val postkeyres = intent.getStringExtra("keyforposts")!!
            snapshot.children.forEach {
                if (it.key == postkeyres){
                    it.children.forEach {its : DataSnapshot ->
                        val comname = its.child("namepost").value.toString()
                        val comimg = its.child("imgurl").value.toString()
                        val textcom = its.child("textpost").value.toString()
                        commentlist.add(0, comments(comimg, comname, textcom))
                    }
                }
                recyclercomments.adapter = adaptercomments(commentlist)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }
}