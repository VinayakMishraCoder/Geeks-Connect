package com.example.codal.Daos

import android.util.Log
import com.example.codal.models.userMod
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class userDao {
    val db = Firebase.firestore
    val collect = db.collection("userMods")
    val auth = Firebase.auth
    var checkif = "insert"
    fun checkIfAddOrUpdate(currEmail: String) {
            GlobalScope.launch (Dispatchers.IO){
                if (collect.document(currEmail) == null) checkif = "add"
                else checkif = "update"
            }
    }
    fun insert(obj: userMod?) {
        obj?.let {
            GlobalScope.launch (Dispatchers.IO){
                collect.document(obj.uid).set(it)
            }
        }
    }

    fun update(obj: userMod) {
        val currUser = auth.currentUser
        val emailID = currUser?.email
        obj.let {
            GlobalScope.launch (Dispatchers.IO) {
                val docRef = emailID?.let { it1 -> db.collection("userMods").document(it1) }
                docRef?.get()?.addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<userMod>()
                    collect.document(obj.uid).set(it)
                }
            }
        }
    }
}