import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectforfinals.R
import com.example.projectforfinals.comments
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.examplecomment.view.*

class adaptercomments(val commlist : MutableList<comments>) : RecyclerView.Adapter<adaptercomments.viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.examplecomment, parent, false)
        return viewholder(itemview)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.init()
    }

    override fun getItemCount(): Int {
        return commlist.size
    }
    inner class viewholder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun init(){
            val namecomment = commlist[adapterPosition].namepost
            val imgcomment = commlist[adapterPosition].imgurl
            val textcomm = commlist[adapterPosition].textpost
            itemView.namenamecomment.text = namecomment
            itemView.textcomment.text = textcomm
            Picasso.get().load(imgcomment).into(itemView.imageviewcomment)
        }
    }

}