package com.example.codal

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.codal.Daos.userDao
import com.example.codal.databinding.ActivityUserDescriptionBinding

class userDescription : AppCompatActivity() {
    lateinit var binding: ActivityUserDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val currDao: userDao = userDao()
        binding.currDesignation.text = intent.extras?.get("getDesignation").toString()
        binding.descriptionPara.text = intent.extras?.get("getDescription").toString()
        binding.useremail.text = intent.extras?.get("getEmail").toString()
        binding.uname.text = intent.extras?.get("getName").toString()
        val emailid : String = intent.extras?.get("getEmail").toString()
        val nm = intent.extras?.get("getName").toString()
        Log.d("recc", "onCreate: $emailid")
        val subj: String = "Hey!! Search me by my email ID $emailid on Cod-AL.\n Wanted a team partner for Group contests/hackathons,etc. \n Can we discuss further and share details $nm ?  "
        Glide.with(this)
            .load(intent.extras!!.get("getImg")) // the uri you got from Firebase
            .circleCrop()
            .into(binding.userImg); //Your imageView variable

        binding.sendEmail.setOnClickListener {
            val uriText = "mailto:$emailid" +
                    "?subject=" + "Coders-Alliance" +
                    "&body=" + subj
            val uri = Uri.parse(uriText)
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = uri
            if(sendIntent.resolveActivity(packageManager) != null) {
                Toast.makeText(this,"Directing",Toast.LENGTH_SHORT).show()
                startActivity(Intent.createChooser(sendIntent, "Send Email").addFlags(FLAG_ACTIVITY_NEW_TASK))
            } else {
                Toast.makeText(this,"Some Error Occurred or Required App not installed!!",Toast.LENGTH_SHORT).show()
            }

        }
//        binding.likeDescription.setOnClickListener {
//            currDao.updateLikes(emailid)
//        }

    }
}