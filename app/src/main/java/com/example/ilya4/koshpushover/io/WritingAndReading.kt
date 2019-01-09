package com.example.ilya4.koshpushover.io

import android.util.Log
import java.io.*
import java.lang.StringBuilder

class WritingAndReading {
    private val TAG = ".WritingAndReading"

    fun writeToFile(file: File, text: String){
        Log.v(TAG, text)
        try {
            val fw = FileWriter(file, true)
            fw.write("\n"+ text)
            fw.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun readFromFile(file: File): String? {
        try {
            val text = StringBuilder()
            val br = BufferedReader(FileReader(file))

           while (true){
               val line = br.readLine() ?: break
               text.append(line)
               text.append('\n')
           }
            br.close()

            return text.toString()
        }catch (e: IOException){
            Log.e(TAG, e.message)
            e.printStackTrace()
        }
        return null
    }
}