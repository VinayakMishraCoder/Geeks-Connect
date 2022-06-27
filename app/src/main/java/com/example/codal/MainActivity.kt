package com.example.codal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codal.Daos.userDao
import com.example.codal.databinding.ActivityMainBinding
import com.example.codal.models.userMod
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    // HEADER UTILITY
    lateinit var nameUser: String
    lateinit var profPhoto: String
    lateinit var xid: String
    lateinit var currDao: userDao
    // GOOGLE-SIGN-IN UTILITY
    lateinit var gsc: GoogleSignInClient
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityMainBinding
    val RC_SIGN_IN: Int = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        currDao = userDao()
        auth = Firebase.auth
        val um = "160788346664-l6amhbcu2cd5nq85nldoemi2kgprihqo.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(um)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)

//        val db = Firebase.firestore

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }
    override fun onStart() {
        super.onStart()

        // NAV DRAWER UPDATE
        val currentUser = auth.currentUser
        xid = currentUser?.email.toString()
        profPhoto= currentUser?.photoUrl.toString()
        nameUser = currentUser?.displayName.toString()
        updateUI(currentUser,xid,profPhoto,nameUser)
    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d("signvin", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        }
        catch (e: ApiException) {
            Log.w("signvin", "signInResult:failed code=" + e.statusCode)

            // MAKE TOASTS
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        //  binding.signInButton.visibility = View.GONE
        //  progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val at = auth.signInWithCredential(credential).await()
            val firebaseUser = at.user

            // HEADER UI STUFF
            xid = at.user?.email.toString()
            profPhoto = at.user?.photoUrl.toString()
            nameUser = at.user?.displayName.toString()
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser,xid,profPhoto,nameUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?, xid: String, profPhoto: String, nameUser: String) {
        if(firebaseUser != null) {
            currDao.checkIfAddOrUpdate(xid)
            val landingPageIntent = Intent(this, landingPage::class.java)
            landingPageIntent.putExtra("userName",nameUser)
            landingPageIntent.putExtra("profilePhoto",profPhoto)
            landingPageIntent.putExtra("userEmail",xid)
            startActivity(landingPageIntent)
            finish()
        }
        else {
            // binding.signInButton.visibility = View.VISIBLE
            // MAKE TOASTS
        }
    }
}





