package com.lattice.uber

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tedpark.tedpermission.rx2.TedRx2Permission

class UserActivity : AppCompatActivity() {
    private var mDriver: Button? = null
    private var mCustomer: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        mDriver = findViewById<View>(R.id.driver) as Button
        mCustomer = findViewById<View>(R.id.customer) as Button
locationPermission()
        //startService(new Intent(MainActivity.this, onAppKilled.class));
        mDriver!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UserActivity, DriverLoginActivity::class.java)
            startActivity(intent)
            finish()
            return@OnClickListener
        })
        mCustomer!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UserActivity, CustomerLoginActivity::class.java)
            startActivity(intent)
            finish()
            return@OnClickListener
        })
    }



    @SuppressLint("CheckResult")
    private fun locationPermission() {
        TedRx2Permission.with(this)
            .setRationaleTitle(R.string.text_permission_title)
            .setRationaleMessage(R.string.text_permission_message)
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

            )
            .request()
            .subscribe { tedPermissionResult ->
                if (tedPermissionResult.isGranted) {

                } else {
                    Toast.makeText(this@UserActivity, getString(R.string.error_permission), Toast.LENGTH_SHORT).show()
                    //finish()
                }
            }
    }
}