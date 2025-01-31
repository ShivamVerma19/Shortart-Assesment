package com.example.shortartassesment.Adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shortartassesment.Models.Post
import com.example.shortartassesment.Models.User
import com.example.shortartassesment.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.dynamiclinks.socialMetaTagParameters
import java.net.URLEncoder

class PostAdapter(val context : Context, val list : ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.item_image)
        val shareBtn: Button = view.findViewById(R.id.btn_share)
        val copyBtn: Button = view.findViewById(R.id.btn_copy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(context).inflate(R.layout.upload_item_layout , parent , false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        Glide.with(context).load(post.url).into(holder.image)

        holder.shareBtn.setOnClickListener {
            post.pictureId?.let { picId ->
                post.url?.let { url ->
                    createDynamicLink(context, picId, url, post.userId!! , false) // Copy
                }
            }
        }

        holder.copyBtn.setOnClickListener {
            post.pictureId?.let { picId ->
                post.url?.let { url ->
                    createDynamicLink(context, picId, url, post.userId!!, true) // Copy
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private fun createDynamicLink(
        context: Context,
        pictureId: String,
        imageUrl: String,
        userId: String,
        isCopy: Boolean
    ) {
        val encodedUrl = URLEncoder.encode(imageUrl, "UTF-8")
        var url : String? = null

        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://shortartassesment.page.link/?image=$encodedUrl&id=$pictureId")
            domainUriPrefix = "https://shortartassesment.page.link"
            androidParameters { }
            socialMetaTagParameters {
                title = "Check out this Image!"
                description = "Click the link to view this image in the app."
                val imageUri = Uri.parse(imageUrl)
                url = imageUri.toString()
            }
        }.addOnSuccessListener { shortDynamicLink ->
            val shortLink = shortDynamicLink.shortLink.toString()
            if (isCopy) {
                copyToClipboard(context, shortLink) // ✅ Copy link to clipboard
            } else {
                shareContent(context, shortLink) // ✅ Share link
            }

            //sendPushNotification(userId ,isCopy)
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to create link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareContent(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this image: $link")
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }


    private fun copyToClipboard(context: Context, link: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Image Link", link)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

//    private fun sendPushNotification(userId: String, isCopy: Boolean) {
//
//
//        FirebaseDatabase.getInstance().getReference("Users").child(userId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(userSnapshot: DataSnapshot) {
//                    val user = userSnapshot.getValue(User::class.java)
//                    user?.fcmToken?.let { token ->
//                        val message = if (isCopy) {
//                            "Your picture was copied!"
//                        } else {
//                            "Your picture was shared!"
//                        }
//                        sendFCMMessage(token, "Content Shared", message)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
//
//
//    }


}