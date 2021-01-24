package com.example.projectforfinals

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class MainActivity2 : Activity() {
    private var imgchosen = false
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        profilepicbut.setOnClickListener { choseimage() }
        auth = Firebase.auth
        designforincognito()
        signupbut.setOnClickListener {
            if (namesignup.text!!.isNotEmpty() && sirnamesignup.text!!.isNotEmpty() && agesignup.text!!.isNotEmpty() &&
                mailsignup.text!!.isNotEmpty() && passsignup.text!!.isNotEmpty() && reppasssignup.text!!.isNotEmpty()
            ) {
                if (passsignup.text.toString() == reppasssignup.text.toString()) {
                    if (imgchosen){
                        uploadtofirebase()
                        progressbarsignup.visibility = View.VISIBLE
                    }else{Toast.makeText(this, "Please choose a profile picture", Toast.LENGTH_SHORT).show()}
                } else {
                    Toast.makeText(this, "Passwords doesn't match!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animation4, R.anim.animation3)
    }



    private fun signup() {
        val email = mailsignup.text.toString()
        val password = passsignup.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressbarsignup.visibility = View.GONE
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("success", "createUserWithEmail:success")
                    Toast.makeText(this, "Successful sign up", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animation4, R.anim.animation3)
                    senddata()
                } else {
                    progressbarsignup.visibility = View.GONE
                    // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, task.exception.toString().split(":")[1],
                            Toast.LENGTH_SHORT
                        ).show()
                    Log.w("fail", "createUserWithEmail:failure", task.exception)
                }

                // ...
            }
    }
    private fun designforincognito(){
        switchsignup.setOnClickListener {
            if (switchsignup.isChecked) {
                switchsignup.text = "Incognito  "
                main2.setBackgroundResource(R.mipmap.incognito)
                profilepiccard.visibility = View.GONE
                switchsignup.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                agesignup.visibility = View.GONE
                sirnamesignup.visibility = View.GONE
                sirnamesignup.setText("incognito")
                agesignup.setText("69420")
                namesignup.hint = "Username"
                imgchosen = true
            }
            else{
                main2.setBackgroundResource(R.mipmap.backgroundsignup)
                switchsignup.text = "Public  "
                profilepiccard.visibility = View.VISIBLE
                switchsignup.setTextColor(resources.getColor(android.R.color.white))
                agesignup.visibility = View.VISIBLE
                sirnamesignup.visibility = View.VISIBLE
                sirnamesignup.setText("")
                agesignup.setText("")
                namesignup.hint = "Name"
                imgchosen = false
            }
            Log.d("age", agesignup.text.toString())
        }
    }
    private fun choseimage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }
    private fun senddata(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users")
        val name = namesignup.text.toString()
        val surname = sirnamesignup.text.toString()
        val age = agesignup.text.toString().toInt()
        val email = mailsignup.text.toString()
        val password = passsignup.text.toString()
        val User = user(name, surname, age, email, password, myurl!!)
        ref.child(uid.toString()).setValue(User)
    }
    private var uri:Uri? = null
    private var myurl:String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2 && resultCode== RESULT_OK && data!=null){
            uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val bitmapdrawable = BitmapDrawable(bitmap)
            profilepicbut.setBackgroundDrawable(bitmapdrawable)
            imgchosen = true
        }
    }
    private fun uploadtofirebase() {
            val filename = UUID.randomUUID().toString()
            if (uri == null) {
                myurl="https://firebasestorage.googleapis.com/v0/b/projectforfinals1.appspot.com/o/Untitled%20design%20(15).png?alt=media&token=cb745121-2730-4528-a842-e8013a998d8c"
                signup()
                return}
            val ref = FirebaseStorage.getInstance().getReference("/test/$filename")
            ref.putFile(uri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    myurl = it.toString()
                    signup()
                }
            }
    }
}