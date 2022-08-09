package com.example.qrbarcodescanner

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import java.lang.IllegalArgumentException

private const val CAMERA_REQUEST_CODE=101

class second_activity : AppCompatActivity() {

    val broadcastReceiver:BroadcastReceiver=ConnectionReceiver()

    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setupPermission()
        codeScanner()
        findViewById<Button>(R.id.button_for_copy).setOnClickListener() {
            val texti = findViewById<TextView>(R.id.tv_textView).text.toString()
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied to Clipboard", texti)

            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "copied to clipboard", Toast.LENGTH_SHORT).show()

        }
        //

        //

        networkregister()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, findViewById(R.id.scanner_view))

        codeScanner.apply {

            isFlashEnabled=true
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    findViewById<TextView>(R.id.tv_textView).text = it.text

                }
            }
            errorCallback = ErrorCallback {

                runOnUiThread {
                    Log.e("Main", "camera initializtion errror: ${it.message}")
                }

            }

        }

        findViewById<CodeScannerView>(R.id.scanner_view).setOnClickListener {

            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {

        val permission: Int = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "you need to access your CAMERA", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    networkregister()
                }
            }
        }
    }

    fun networkregister(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }
    fun unregisternetwork(){
        try {
            unregisterReceiver(broadcastReceiver)
        }
        catch (e: IllegalArgumentException){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisternetwork()
    }

}