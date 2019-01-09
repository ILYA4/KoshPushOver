package com.example.ilya4.koshpushover.listeners

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.ilya4.koshpushover.BarcodeCaptureActivity
import com.example.ilya4.koshpushover.MainActivity
import com.example.ilya4.koshpushover.R

class QrButtonOnClickListener(private val context: Context) : View.OnClickListener {

    override fun onClick(v: View?) {
        if (v?.id == R.id.qr_camera){
            val intent =  Intent(context, BarcodeCaptureActivity::class.java)
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true)
            intent.putExtra(BarcodeCaptureActivity.UseFlash, true)
            Log.v(TAG, "in method")

            if (context is Activity){
                context.startActivityForResult(intent, MainActivity.RC_BARCODE_CAPTURE)
            }
        }
    }

    companion object {
        private val TAG = "ClickToQrCamera"
    }
}