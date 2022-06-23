package com.example.codal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codal.Daos.userDao
import com.example.codal.models.userMod
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.toObject

class firebaseAdpter(options: FirestoreRecyclerOptions<userMod>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<userMod, firebaseAdpter.customViewHolder>(
    options
) {
    inner class customViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo_hold: ImageView = itemView.findViewById(R.id.profilePhoto)
        val approval_hold: TextView = itemView.findViewById(R.id.textView)
        val designation_hold: TextView = itemView.findViewById(R.id.designation)
        val user_name_hold: TextView = itemView.findViewById(R.id.user_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customViewHolder {
        val viewHolder =  customViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
        viewHolder.itemView.setOnClickListener{
            snapshots.getSnapshot(viewHolder.adapterPosition).toObject<userMod>()
                ?.let { it1 -> listener.onLikeClicked(it1) }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: customViewHolder, position: Int, model: userMod) {
        holder.apply {
            Glide.with(holder.photo_hold.context)
                .load(model.imgUrl) // the uri you got from Firebase
                .circleCrop()
                .into(photo_hold); //Your imageView variable

            var temp : String = model.likes
            if(temp != "null") approval_hold.text = "$temp: Approvals"
            else approval_hold.text = "0: Approvals"
            designation_hold.text = model.designation
            user_name_hold.text = model.name
            Log.d("firerec", "onBindViewHolder: ${user_name_hold.text}")
        }

    }
}

interface IPostAdapter {
    fun onLikeClicked(usm: userMod)
}
