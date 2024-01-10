package com.example.projectfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Register : AppCompatActivity() {
    private lateinit var emailSignup:EditText
    private lateinit var passwordSignup:EditText
    private lateinit var registerButton: Button
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailSignup=findViewById(R.id.emailSignup)
        passwordSignup=findViewById(R.id.passwordSignup)
        registerButton=findViewById(R.id.registerUserButton)
        mAuth= Firebase.auth

        registerButton.setOnClickListener {
            val email=emailSignup.text.toString()
            val pass=passwordSignup.text.toString()
            if(emailSignup.text.isNotEmpty() && passwordSignup.text.isNotEmpty())
            {
                signUp(email, pass)
            }
            else{
                Toast.makeText(this, "Enter Email and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,Dashboard::class.java))

                    Toast.makeText(this, "Signed Up successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Some error in input", Toast.LENGTH_SHORT).show()
                }
            }
    }
}