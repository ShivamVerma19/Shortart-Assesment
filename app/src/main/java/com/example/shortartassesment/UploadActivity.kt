package com.example.shortartassesment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shortartassesment.Models.Post
import com.example.shortartassesment.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var uploadButton : Button
    private lateinit var imageUri: Uri
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload)

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.btn_final_upload)

        // Gallery Intent launcher
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data!!
                imageView.setImageURI(imageUri)
            }
        }

        imageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(galleryIntent)
        }

        uploadButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (!::imageUri.isInitialized) {
            Toast.makeText(this, "Please select a image to upload", Toast.LENGTH_SHORT).show()
        }
        else
            uploadImage(imageUri)
    }

    private fun uploadImage(uri: Uri) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.show()
        val filename = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("uploads/$filename")

        refStorage.putFile(uri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    storeData(it.toString())
                }
            }
            .addOnFailureListener{

                Toast.makeText(this , "Error in storage" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            }
    }

    private fun storeData(url: String) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference()
        val auth = FirebaseAuth.getInstance()
        val pictureId = UUID.randomUUID().toString()

        databaseReference.child("Uploads").child(pictureId).setValue(Post(auth.currentUser?.uid,url,pictureId))
            .addOnSuccessListener{
                Toast.makeText(this, "Picture Posted" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error in Posting Picture" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }
}