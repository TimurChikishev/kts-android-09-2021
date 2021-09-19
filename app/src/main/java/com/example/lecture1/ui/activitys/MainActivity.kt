package com.example.lecture1.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lecture1.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("onCreate MainActivity")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop MainActivity")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy MainActivity")
    }
}