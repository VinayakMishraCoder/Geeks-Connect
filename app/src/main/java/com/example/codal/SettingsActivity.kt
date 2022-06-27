package com.example.codal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codal.Daos.userDao
import com.example.codal.databinding.ActivitySettingsBinding
import com.example.codal.models.userMod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var currDao: userDao
    lateinit var auth: FirebaseAuth
    lateinit var userModObj: userMod
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        currDao = userDao()
        auth = Firebase.auth
        val user = auth.currentUser

        currDao.checkIfAddOrUpdate(user?.email.toString())
        Log.d("recc", "onCreate: $user?.email.toString()")

        binding.button.setOnClickListener {
            if((currDao.checkif == "add")) {
                val emailID: String = user?.email.toString()
                val desc: String = binding.descriptionText.text.toString()
                val chef: String = binding.cchefText.text.toString()
                val force: String = binding.cforcesText.text.toString()
                val profUrl: String = user?.photoUrl.toString()
                val likes: String = "0"
                var numHacks: String = binding.hackText.text.toString()
                var designation:String
                val numQues: String = binding.quesText.text.toString()

                if(binding.checkBox.isChecked) designation = "Open for teaming Up!!"
                else designation = "Unavailable for Teaming Up"

                userModObj = userMod(emailID, user?.displayName.toString(), profUrl, likes, designation, chef, force, numHacks, desc,numQues)
                Log.d("Firelog", userModObj.toString())
                currDao.insert(userModObj)
            }
            else {
                val emailID: String = user?.email.toString()
                val desc: String = binding.descriptionText.text.toString()
                val chef: String = binding.cchefText.text.toString()
                val force: String = binding.cforcesText.text.toString()
                val profUrl: String = user?.photoUrl.toString()
                val numQues: String = binding.quesText.text.toString()
                // TODO : retrieve likes from getObj later on
                val likes: String = "0"
                var numHacks: String = binding.hackText.text.toString()
                var designation:String

                if(binding.checkBox.isChecked) designation = "Open for teaming Up!!"
                else designation = "Unavailable for Teaming Up"

                userModObj = userMod(emailID, user?.displayName.toString(), profUrl, likes, designation, chef, force, numHacks, desc,numQues)
                Log.d("Firelogupset", userModObj.toString())
                currDao.update(userModObj)

            }
//            val landingPageIntent = Intent(this, landingPage::class.java)
//            startActivity(landingPageIntent)
        }

        // INSERTION


    }
}