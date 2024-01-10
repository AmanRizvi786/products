package com.example.projectfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtVerificationCode: EditText
    private lateinit var btnSendCode: Button
    private lateinit var btnVerifyCode: Button

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtVerificationCode = findViewById(R.id.edtVerificationCode)
        btnSendCode = findViewById(R.id.btnSendCode)
        btnVerifyCode = findViewById(R.id.btnVerifyCode)

        auth = FirebaseAuth.getInstance()

        btnSendCode.setOnClickListener {
            val phoneNumber =  "+91${edtPhoneNumber.text}"
            sendVerificationCode(phoneNumber)
        }

        btnVerifyCode.setOnClickListener {
            val verificationCode = edtVerificationCode.text.toString()
            verifyVerificationCode(verificationCode)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@PhoneAuthActivity, "Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@PhoneAuthActivity.verificationId = verificationId
                    Toast.makeText(this@PhoneAuthActivity, "Code Sent", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Authentication successful, navigate to the next screen
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
