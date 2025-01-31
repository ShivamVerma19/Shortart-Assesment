package com.example.shortartassesment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.shortartassesment.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var profileImageUri: Uri
    private lateinit var profileImageView: ImageView
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var edt_email : EditText
    private lateinit var edt_password : EditText
    private lateinit var edt_name : EditText
    private lateinit var signup : Button
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        edt_email = findViewById(R.id.edt_reg_email)
        edt_password = findViewById(R.id.edt_reg_password)
        edt_name = findViewById(R.id.edt_reg_name)
        signup = findViewById(R.id.btn_reg_signup)
        profileImageView = findViewById(R.id.profile_image)
        auth = FirebaseAuth.getInstance()
        // Gallery Intent launcher
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                profileImageUri = data?.data!!
                profileImageView.setImageURI(profileImageUri)
            }
        }

        profileImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(galleryIntent)
        }

        signup.setOnClickListener {
            val name = edt_name.text.toString().trim()
            val email = edt_email.text.toString().trim()
            val password = edt_password.text.toString().trim()

            validateData(name,email,password)
        }

    }

    private fun validateData(name: String, email: String, password: String) {

        if (name.isEmpty()) {
            Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show()

        }
        else if (email.isEmpty()) {
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show()

        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show()

        }
        else if (!::profileImageUri.isInitialized) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show()
        }
        else {
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    uploadImage(name,email)
                }
            }
            .addOnFailureListener { e ->

                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            }
    }

    private fun uploadImage(name: String, email: String) {
        val filename = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("profile_pic/$filename")

        refStorage.putFile(profileImageUri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    storeData(it.toString(),name,email)
                }
            }
            .addOnFailureListener{

                Toast.makeText(this , "Error in storage" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            }
    }

    private fun storeData(url: String, name: String, email: String) {
        database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference()

//        val token = FirebaseMessaging.getInstance().token.result

        databaseReference.child("Users").child(auth.currentUser?.uid!!).setValue(User(name,email,url,auth.currentUser?.uid!!))
            .addOnSuccessListener{
                Toast.makeText(this, "Data Uploaded" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error in uploading data" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }
}