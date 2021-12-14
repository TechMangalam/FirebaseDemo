package com.bitaam.firebasedemo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        onClickActivities(logoutBtn)

        setProfile()

    }

    @SuppressLint("SetTextI18n")
    private fun setProfile() {

        val profileNameTv = findViewById<TextView>(R.id.profileNameTv)
        val profileEmailTv = findViewById<TextView>(R.id.emailProfileTv)
        val profileImgView = findViewById<ImageView>(R.id.profileImageView)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            val personPhoto = acct.photoUrl
            profileNameTv.text = personName
            profileEmailTv.text = personEmail
            if (personPhoto != null) Picasso.get().load(personPhoto).into(profileImgView)
        }
    }

    private fun onClickActivities(logoutBtn: Button) {

        logoutBtn.setOnClickListener(View.OnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        })

    }
}