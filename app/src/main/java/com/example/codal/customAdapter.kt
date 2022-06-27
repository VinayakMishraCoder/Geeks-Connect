package com.example.codal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codal.models.userMod
import kotlinx.coroutines.NonDisposableHandle.parent

class customAdapter(var users: List<userMod>,val listener: adapterOnClick) : RecyclerView.Adapter<customAdapter.customViewHolder>(){
    inner class customViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo_hold: ImageView = itemView.findViewById(R.id.profilePhoto)
        val approval_hold: TextView = itemView.findViewById(R.id.textView)
        val designation_hold: TextView = itemView.findViewById(R.id.designation)
        val user_name_hold: TextView = itemView.findViewById(R.id.user_item_name)
        val more_desc_hold: TextView = itemView.findViewById(R.id.more_desc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)
        return customViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: customViewHolder, position: Int) {
        holder.apply {
            Glide.with(holder.itemView.context)
                .load(users[position].imgUrl) // the uri you got from Firebase
                .circleCrop()
                .into(photo_hold); //Your imageView variable

            var temp : String = users[position].uid.toString()
            approval_hold.text = "Id - $temp"
            val temp4 = users[position].designation
            designation_hold.text = users[position].designation
            if(temp4 == "Open for teaming Up!!") designation_hold.setTextColor(Color.parseColor("#64DD17"))
            else designation_hold.setTextColor(Color.parseColor("#EF9A9A"))
            user_name_hold.text = users[position].name
            var temp2 = users[position].numHacks
            var temp3 = users[position].cforces_rating
            if(temp2.isEmpty()) temp2 = "0"
            if(temp3.isEmpty()) temp3 = "0"
            more_desc_hold.text = "HackaThons Attended - $temp2, \n" +
                    "CodeForces Rating - $temp3" +
                    "\nCodeChef Rat..."
        }
        holder.itemView.setOnClickListener {
            listener.onMoved(users[position])
        }
    }
}

interface adapterOnClick {
    fun onMoved(usm: userMod)
}