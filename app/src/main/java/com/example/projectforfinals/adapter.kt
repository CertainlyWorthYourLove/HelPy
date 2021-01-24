package com.example.projectforfinals

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.examplerow.view.*
import java.nio.file.DirectoryStream
import java.util.*
import java.util.logging.Filter

class adapter(val mutableList: MutableList<post>): RecyclerView.Adapter<adapter.viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.examplerow, parent, false)
        return viewholder(itemview)
    }
    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.onbind()
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
    inner class viewholder(itemview : View) : RecyclerView.ViewHolder(itemview){
        fun onbind(){
            val postimgurl = mutableList[adapterPosition].imgurl
            val postfullname = mutableList[adapterPosition].fullname
            val posttopic = mutableList[adapterPosition].topic
            val posttext = mutableList[adapterPosition].text
            itemView.fullnametextview.text = postfullname
            itemView.topictextview.text = posttopic
            itemView.problemtextview.text = posttext
            Picasso.get().load(postimgurl).into(itemView.imageviewpost)
        }
        init {
            itemview.setOnClickListener {
                val position = adapterPosition
                val key = mutableList[position].key
                val intent2 = Intent(itemview.context, MainActivity5::class.java)
                intent2.putExtra("keyforposts", key)
                intent2.putExtra("topic", mutableList[position].topic)
                intent2.putExtra("text", mutableList[position].text)
                intent2.putExtra("imgurl", mutableList[position].imgurl)
                itemview.context.startActivity(intent2)
            }
        }
    }

}