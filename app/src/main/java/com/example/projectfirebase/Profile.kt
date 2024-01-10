package com.example.projectfirebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Profile : Fragment() {

    private lateinit var userEmailTextView: TextView
    private lateinit var userProfileImageView: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        userEmailTextView = rootView.findViewById(R.id.userEmailTextView)
        userProfileImageView = rootView.findViewById(R.id.userProfileImageView)

        // Get the current user
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        // Display the user's email
        userEmailTextView.text = currentUser?.email

        // Display the user's profile image
        loadProfileImage()

        return rootView
    }

    private fun loadProfileImage() {
        val signInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (signInAccount != null && signInAccount.photoUrl != null) {
            Glide.with(this)
                .load(signInAccount.photoUrl)
                .into(userProfileImageView)
        } else {
            // Handle the case when the user is not signed in with Google or no profile image available
            // You can use a default image or provide an error placeholder
            Glide.with(this)
                .load(R.drawable.default_profile_image)
                .into(userProfileImageView)
        }
    }
}
