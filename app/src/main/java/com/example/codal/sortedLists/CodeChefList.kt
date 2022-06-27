package com.example.codal.sortedLists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codal.R
import com.example.codal.adapterOnClick
import com.example.codal.customAdapter
import com.example.codal.databinding.ActivityCodeChefListBinding
import com.example.codal.databinding.ActivityLandingPageBinding
import com.example.codal.models.userMod
import com.example.codal.userDescription
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CodeChefList : AppCompatActivity(),adapterOnClick {
    lateinit var binding: ActivityCodeChefListBinding
    var objList: MutableList<userMod> = emptyList<userMod>().toMutableList()
    lateinit var db: FirebaseFirestore
    lateinit var collect: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeChefListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        db = Firebase.firestore
        collect = db.collection("userMods")
        collect
            .orderBy("cchef_rating", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var temp: userMod = document.toObject<userMod>()
                    if (temp != null) {
                        objList.add(temp)
                    }
                }
                val recView = binding.cchefList
                recView.layoutManager = LinearLayoutManager(this)
                val adpt = customAdapter(objList,this)
                recView.adapter = adpt
                Log.d("objlist ", objList.toString())

            }
            .addOnFailureListener { exception ->
//                Log.d("errorMsg", "Error getting documents: ", exception)
                    Toast.makeText(this,"Some Error Occurred!!",Toast.LENGTH_SHORT)
            }
    }

    override fun onMoved(usm: userMod) {
        val userPageIntent = Intent(this, userDescription::class.java)
        userPageIntent.putExtra("getDesignation",usm.designation)
        userPageIntent.putExtra("getDescription",usm.udescription)
        userPageIntent.putExtra("getEmail",usm.uid)
        userPageIntent.putExtra("getName",usm.name)
        userPageIntent.putExtra("getImg",usm.imgUrl)
        startActivity(userPageIntent)
    }
}