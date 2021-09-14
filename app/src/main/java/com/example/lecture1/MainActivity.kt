package com.example.lecture1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSkip).setOnClickListener {
            Toast.makeText(this, R.string.skip_toast, Toast.LENGTH_SHORT).show()
        }
    }
}