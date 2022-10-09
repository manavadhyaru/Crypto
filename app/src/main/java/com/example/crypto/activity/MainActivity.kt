package com.example.crypto.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.crypto.R
import com.example.crypto.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFCVWithFragments(fragment = HomeFragment(), isNeedToAddOnStack = false)
    }

    // ::: Function to replace fragments in fragment container view..
    fun replaceFCVWithFragments(fragment: Fragment, isNeedToAddOnStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.idFCVMainActivity, fragment)
        if (isNeedToAddOnStack) {
            fragmentTransaction.addToBackStack(fragment::class.toString())
        }
        fragmentTransaction.commit()
    }
}