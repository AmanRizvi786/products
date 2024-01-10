package com.example.projectfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Dashboard : AppCompatActivity() {
    private lateinit var logoutButton:ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mAuth=Firebase.auth

        logoutButton=findViewById(R.id.logoutButton)

        val bottomNavigation=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        loadFragment(Home())
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.home->loadFragment(Home())
                R.id.reg->loadFragment(Reg())
                R.id.profile->loadFragment(Profile())

            }
            true
        }
        mAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            signOut()
        }
    }
    private fun signOut() {
        mAuth.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()
    }
}