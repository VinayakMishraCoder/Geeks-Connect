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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.codal.Daos.userDao
import com.example.codal.databinding.ActivityLandingPageBinding
import com.example.codal.models.userMod
import com.example.codal.sortedLists.CodeChefList
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.AccessController.getContext

class landingPage : AppCompatActivity(),IPostAdapter  {

    lateinit var binding: ActivityLandingPageBinding
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var currDao: userDao
    lateinit var adapt: firebaseAdpter

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
                R.id.logout_menu -> {
//                    val credential = EmailAuthProvider
//                        .getCredential(Firebase.auth.currentUser?.email.toString(), Firebase.auth.currentUser?.)

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

        // RECYCLERVIEW STUFF
//        val ulist = mutableListOf(
////            userMod("vinayak@huntumail.com","vinayak","https://cdn.icon-icons.com/icons2/1378/PNG/512/avatardefault_92824.png","23","Open For Teaming Up!!"),
////            userMod("vinayak@huntumail.com","Buntu","data:image/png;base64,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","10","Unavailable")
//        )
//        val adatper = customAdapter(this,ulist)
//        binding.customRv.adapter = adatper
//        binding.customRv.layoutManager = LinearLayoutManager(this)


        // FIREBASE RECYCLERVIEW
        val query = currDao.collect.orderBy("likes", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<userMod>().setQuery(query, userMod::class.java).build()

        adapt = firebaseAdpter(recyclerViewOptions, this)

        binding.customRv.adapter = adapt
        binding.customRv.layoutManager = LinearLayoutManager(this)
    }


    override fun onStart() {
        super.onStart()
        adapt.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapt.stopListening()
    }
    // NAVIGATION DRAWER SETUP
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item)
    }

    override fun onLikeClicked(usm: userMod) {
        val userPageIntent = Intent(this, userDescription::class.java)
        userPageIntent.putExtra("getDesignation",usm.designation)
        userPageIntent.putExtra("getDescription",usm.udescription)
        userPageIntent.putExtra("getEmail",usm.uid)
        userPageIntent.putExtra("getName",usm.name)
        userPageIntent.putExtra("getImg",usm.imgUrl)
//        userPageIntent.putExtra("getdata",usm.designation)
        startActivity(userPageIntent)
    }


}
