package com.example.codal

import android.annotation.SuppressLint
import android.content.Context
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

class customAdapter(private var context: Context,var users: List<userMod>) : RecyclerView.Adapter<customAdapter.customViewHolder>(){
    inner class customViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo_hold: ImageView = itemView.findViewById(R.id.profilePhoto)
        val approval_hold: TextView = itemView.findViewById(R.id.textView)
        val designation_hold: TextView = itemView.findViewById(R.id.designation)
        val user_name_hold: TextView = itemView.findViewById(R.id.user_item_name)
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
            Glide.with(context)
                .load(users[position].imgUrl) // the uri you got from Firebase
                .circleCrop()
                .into(photo_hold); //Your imageView variable

            var temp : String = users[position].likes.toString()
            approval_hold.text = "$temp: Approvals"

            designation_hold.text = users[position].designation

            user_name_hold.text = users[position].name
        }
    }
}