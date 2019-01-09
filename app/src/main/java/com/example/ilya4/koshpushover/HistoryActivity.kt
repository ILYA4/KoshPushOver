package com.example.ilya4.koshpushover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.ilya4.koshpushover.io.WritingAndReading
import java.io.File

class HistoryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "initialize")
        setContentView(R.layout.activity_history)
        val history = findViewById<TextView>(R.id.history_tv)
        val file = File(filesDir, "history.dg")

        val text = WritingAndReading().readFromFile(file)
        history.text = text
    }

    private val TAG = ".HistoryActivity"
}