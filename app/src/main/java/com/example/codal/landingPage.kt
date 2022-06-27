package com.example.codal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.codal.Daos.userDao
import com.example.codal.databinding.ActivityLandingPageBinding
import com.example.codal.models.userMod
import com.example.codal.sortedLists.CodeChefList
import com.example.codal.sortedLists.CodeForcesList
import com.example.codal.sortedLists.HackaThonList
import com.example.codal.sortedLists.PlatformQuestions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.AccessController.getContext

class landingPage : AppCompatActivity() ,adapterOnClick {

    lateinit var binding: ActivityLandingPageBinding
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var currDao: userDao
    var objList: MutableList<userMod> = emptyList<userMod>().toMutableList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NAVIGATION DRAWER SETUP
        toggle = ActionBarDrawerToggle(this, binding.myDrawer, R.string.open, R.string.close)
        binding.myDrawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currDao = userDao()
        // SETUP NAVIGATION HEADER VALUES
        val head: View = binding.navView.getHeaderView(0) /* Inflate Header from navView */


        val userName: TextView = head.findViewById(R.id.user_name)!!
        userName.text = intent.extras!!.get("userName")!!.toString()!!

        val userEmail: TextView = head.findViewById(R.id.user_email)!!
        userEmail.text = intent.extras!!.get("userEmail")!!.toString()!!

        val prof: ImageView = head.findViewById(R.id.user_photo)
        Glide.with(this)
            .load(intent.extras!!.get("profilePhoto")) // the uri you got from Firebase
            .circleCrop()
            .into(prof); //Your imageView variable


        // NAVIGATION MENU CLICK LISTENERS
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.settings_menu -> {
                    val settingsPageIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsPageIntent)
                }
                R.id.cchef -> {
                    val sortingListsIntent = Intent(this, CodeChefList::class.java)
                    startActivity(sortingListsIntent)
                }
                R.id.cforces -> {
                    val sortingListsIntent = Intent(this, CodeForcesList::class.java)
                    startActivity(sortingListsIntent)
                }
                R.id.hacks -> {
                    val sortingListsIntent = Intent(this, HackaThonList::class.java)
                    startActivity(sortingListsIntent)
                }
                R.id.questions -> {
                    val sortingListsIntent = Intent(this, PlatformQuestions::class.java)
                    startActivity(sortingListsIntent)
                }
                R.id.logout_menu -> {
// Prompt the user to re-provide their sign-in credentials
                    val user = Firebase.auth.currentUser!!
                    Firebase.auth.signOut()
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("dell", "User account deleted.")
                            }
                        }
                    val sgoutListsIntent = Intent(this, MainActivity::class.java)
                    startActivity(sgoutListsIntent)
                }
            }
            return@setNavigationItemSelectedListener true
        }


        // FIREBASE RECYCLERVIEW
        val query = currDao.collect.orderBy("likes", Query.Direction.DESCENDING)
        query
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var temp: userMod = document.toObject<userMod>()
                    if (temp != null) {
                        objList.add(temp)
                    }
                }
                val recView = binding.customRv
                recView.layoutManager = LinearLayoutManager(this)
                val adpt = customAdapter(objList,this)
                recView.adapter = adpt
                Log.d("objlist ", objList.toString())

            }
            .addOnFailureListener { exception ->
//                Log.d("errorMsg", "Error getting documents: ", exception)
                Toast.makeText(this,"Some Error Occurred!!", Toast.LENGTH_SHORT)
            }

    }


//    override fun onStart() {
//        super.onStart()
//        adapt.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapt.stopListening()
//    }
    // NAVIGATION DRAWER SETUP
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item)
    }

//    override fun onLikeClicked(usm: userMod) {
//        val userPageIntent = Intent(this, userDescription::class.java)
//        userPageIntent.putExtra("getDesignation",usm.designation)
//        userPageIntent.putExtra("getDescription",usm.udescription)
//        userPageIntent.putExtra("getEmail",usm.uid)
//        userPageIntent.putExtra("getName",usm.name)
//        userPageIntent.putExtra("getImg",usm.imgUrl)
////        userPageIntent.putExtra("getdata",usm.designation)
//        startActivity(userPageIntent)
//    }

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
