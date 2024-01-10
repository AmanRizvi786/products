package com.example.projectfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Reg : Fragment() {

    private lateinit var editTextProductName: EditText
    private lateinit var editTextProductPrice: EditText
    private lateinit var editTextProductDescription: EditText
    private lateinit var imageViewProduct: ImageView
    private lateinit var buttonUpload: Button
    private lateinit var buttonSelect: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rowView = inflater.inflate(R.layout.fragment_reg, container, false)

        editTextProductName = rowView.findViewById(R.id.editTextProductName)
        editTextProductPrice = rowView.findViewById(R.id.editTextProductPrice)
        editTextProductDescription = rowView.findViewById(R.id.editTextProductDescription)
        imageViewProduct = rowView.findViewById(R.id.imageViewProduct)
        buttonSelect = rowView.findViewById(R.id.buttonSelect)
        buttonUpload = rowView.findViewById(R.id.buttonUpload)
        progressBar = rowView.findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()


        buttonSelect.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 101)
        }

        buttonUpload.setOnClickListener {
            val productName = editTextProductName.text.toString().trim()
            val productPrice = editTextProductPrice.text.toString().trim()
            val productDescription = editTextProductDescription.text.toString().trim()

            if (productName.isNotEmpty() && productPrice.isNotEmpty() && productDescription.isNotEmpty() && selectedImageUri != null) {
                progressBar.visibility = View.VISIBLE

                val productInfo = hashMapOf(
                    "Product Name" to productName,
                    "Product Price" to productPrice,
                    "Product Description" to productDescription,
                )
                // Upload the image to Firebase Storage
                val imageRef =
                    FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}.jpg")
                val uploadTask = imageRef.putFile(selectedImageUri!!)

                uploadTask.addOnSuccessListener {
                    // Image uploaded successfully, get the download URL
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Add image URL to Firestore along with other product details
                        productInfo["Product Image"] = uri.toString()

                        // Add a new document with a generated ID
                        FirebaseFirestore.getInstance().collection("users")
                            .add(productInfo)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(
                                    requireContext(),
                                    "DocumentSnapshot added with ID: ${documentReference.id}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error adding document: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fill in all fields and select an image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return rowView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            selectedImageUri = data.data
            // Set the image to the ImageView (imageViewProduct)
            imageViewProduct.setImageURI(selectedImageUri)
        }
    }
}
