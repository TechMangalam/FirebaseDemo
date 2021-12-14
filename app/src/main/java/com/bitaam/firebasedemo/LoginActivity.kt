package com.bitaam.firebasedemo

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var progressDialog: ProgressDialog? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(applicationContext)

        mAuth = Firebase.auth


        val googleLoginBtn = findViewById<Button>(R.id.googleSignInBtn)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.oauth_client))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        if (mAuth!!.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        onClickActivities(googleLoginBtn)

    }

    private fun onClickActivities(googleLoginBtn : Button) {
        googleLoginBtn.setOnClickListener(View.OnClickListener {
            progressDialogShow("Signing with google", "getting accounts")
            mGoogleSignInClient?.signOut()
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    progressDialog!!.dismiss()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    progressDialog!!.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Signing failed with google account",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun progressDialogShow(title: String, Msg: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle(title)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage(Msg)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        //progressDialog.setMax(100);
        progressDialog!!.show()
    }

}