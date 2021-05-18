package com.lattice.uber

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase


class CustomerLoginActivity : AppCompatActivity() {
    private var mEmail: EditText? = null
    private var mPassword: EditText? = null
    private var mLogin: Button? = null
    private var mRegistration: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)
        mAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val intent = Intent(this, CustomerProfileActivity::class.java)
                startActivity(intent)
                return@AuthStateListener
            }
        }
        mEmail = findViewById<View>(R.id.email) as EditText
        mPassword = findViewById<View>(R.id.password) as EditText
        mLogin = findViewById<View>(R.id.login) as Button
        mRegistration = findViewById<View>(R.id.registration) as Button
        mRegistration!!.setOnClickListener {
            val email = mEmail!!.text.toString()
            val password = mPassword!!.text.toString()
            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@CustomerLoginActivity) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this@CustomerLoginActivity, "sign up error", Toast.LENGTH_SHORT).show()
                } else {
                    val user_id = mAuth!!.currentUser!!.uid
                    val current_user_db = FirebaseDatabase.getInstance().reference.child("Users").child("Customers").child(user_id)
                    current_user_db.setValue(email)
                    Toast.makeText(this@CustomerLoginActivity, "registration successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mLogin!!.setOnClickListener {
            val email = mEmail!!.text.toString()
            val password = mPassword!!.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@CustomerLoginActivity) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this@CustomerLoginActivity, "sign in error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(firebaseAuthListener!!)
    }
}
