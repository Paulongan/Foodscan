package fr.isen.foodscan

import android.app.Activity;
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import java.time.temporal.ValueRange

class ButtonScanActivity : AppCompatActivity() {


    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttonscan)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("scannedid")
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 5)

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val key = myRef.push().key ?: ""
                myRef.child(key).setValue(it.text)
                val builder = AlertDialog.Builder(this@ButtonScanActivity)
                builder.setTitle("Bravo")
                builder.setMessage("Vous avez scanné le code : ${it.text}")
                builder.setPositiveButton("Fermer"){dialog, which ->
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
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

}