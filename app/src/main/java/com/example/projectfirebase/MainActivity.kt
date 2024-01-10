package com.example.projectfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    private lateinit var edtEmail:EditText
    private lateinit var edtPass:EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var phoneButton: Button
    private lateinit var googleButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmail=findViewById(R.id.emailField1)
        edtPass=findViewById(R.id.passwordField1)
        loginButton=findViewById(R.id.btnLogin)
        signupButton=findViewById(R.id.btnSignup)
        phoneButton=findViewById(R.id.Phone)
        googleButton=findViewById(R.id.googleButton)

        mAuth= Firebase.auth

        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // If a user is already signed in, navigate to the Dashboard
            startActivity(Intent(this, Dashboard::class.java))
            finish() // Finish the current activity to prevent going back to the login screen
        }
        mAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleButton = findViewById(R.id.googleButton)
        googleButton.setOnClickListener {
            signInWithGoogle()
        }


        loginButton.setOnClickListener {
            val email=edtEmail.text.toString()
            val pass=edtPass.text.toString()
            if(edtEmail.text.isNotEmpty() && edtPass.text.isNotEmpty()){
                login(email,pass)
            }
            else{
                Toast.makeText(this, "Enter Email and Password", Toast.LENGTH_SHORT).show()
            }
        }
        signupButton.setOnClickListener {
            startActivity(Intent(this,Register::class.java))

        }
        phoneButton.setOnClickListener {
                startActivity(Intent(this,PhoneAuthActivity::class.java))
        }
    }

    private fun login(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,Dashboard::class.java))
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Some error in input", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            val task = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (task != null) {
                if (task.isSuccess) {
                    // Google Sign-In was successful, authenticate with Firebase
                    val account = task?.signInAccount
                    firebaseAuthWithGoogle(account!!)
                } else {
                    // Google Sign-In failed
                    Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(this, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}