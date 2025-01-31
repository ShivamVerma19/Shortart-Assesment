package com.example.shortartassesment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shortartassesment.Adapter.PostAdapter
import com.example.shortartassesment.Models.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.dynamicLinks

class HomeActivity : AppCompatActivity() {
    private lateinit var uploadBtn : Button
    private lateinit var rcv : RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var list : ArrayList<Post>
    private lateinit var logoutBtn : Button
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


        uploadBtn = findViewById(R.id.uploadBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        rcv = findViewById(R.id.rcv)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.show()

        list = ArrayList<Post>()
        adapter = PostAdapter(this,list)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(this)


        getData()

        uploadBtn.setOnClickListener {
            val intent = Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        handleDynamicLink()

    }

    private fun getData() {


        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference()

        databaseReference.child("Uploads").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(postSnapshot in snapshot.children){
                    val post = postSnapshot.getValue(Post::class.java)
                    list.add(post!!)
                }

                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                val deepLink: Uri? = pendingDynamicLinkData?.link
                if (deepLink != null) {
                    val imageUrl = deepLink.getQueryParameter("image")
                    val imageId = deepLink.getQueryParameter("id")
                    openImageActivity(imageUrl, imageId)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch dynamic link", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openImageActivity(imageUrl: String?, imageId: String?) {
        val intent = Intent(this, ImageDetailActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("imageId", imageId)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exits the app completely
    }

}