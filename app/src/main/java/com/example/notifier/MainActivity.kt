package com.example.notifier

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth : FirebaseAuth
    lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.new_notification -> {
                val newFragment = Fragment.instantiate(baseContext,NewFragment::class.java!!.name)
                        as NewFragment
                fragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.sent_notifications -> {
                val oldFragment = Fragment.instantiate(baseContext,Old_fragment::class.java!!.name)
                        as Old_fragment
                fragmentManager.beginTransaction().replace(R.id.fragment,oldFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAuth = FirebaseAuth.getInstance()

        val newFragment = Fragment.instantiate(baseContext,NewFragment::class.java!!.name)
                as NewFragment
        fragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()


        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null){

            } else{
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(
                                        Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN)
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.sign_out) {
            AuthUI.getInstance().signOut(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener (mAuthStateListener)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }
}
