package com.example.shortartassesment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var signup : Button
    private lateinit var auth : FirebaseAuth
    private lateinit var edt_login_email : EditText
    private lateinit var edt_login_password : EditText
    private lateinit var login : Button
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser?.uid != null){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        signup = findViewById(R.id.login_signup)
        login = findViewById(R.id.login)

        edt_login_email = findViewById(R.id.edt_login_email)
        edt_login_password = findViewById(R.id.edt_login_password)

        login.setOnClickListener {



            val email = edt_login_email.text.toString().trim()
            val password = edt_login_password.text.toString().trim()

            validateData(email,password)
        }

        signup.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateData(email: String , password: String) {

        if (email.isEmpty()) {
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show()

        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show()

        }
        else {

            loginUser(email, password)
        }

    }
    private fun loginUser(email: String, password: String) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.show()
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Toast.makeText(this,"Login Successfull" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error in Login" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }
}