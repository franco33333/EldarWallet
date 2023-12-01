package com.example.eldarwallet.ui.main.scanQR

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.ActivityScanBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment
import com.google.zxing.BarcodeFormat


class ScanActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScanBinding.inflate(layoutInflater) }

    private lateinit var codeScanner: CodeScanner

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (checkPermission()) {
            if (!::codeScanner.isInitialized)
                setupScanner()
        } else {
            requestPermission();
        }
    }

    private fun setupScanner() {
        val scannerView = binding.scannerView
        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE)// list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                startActivity(ScanResultActivity.newIntent(this, it.text))
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                val dialog = GenericDialogFragment.createInstance(
                    "La app no tiene permisos para abrir la cámara",
                    "Habilite los permisos de cámara de la app y pruebe nuevamente",
                    showBtnPositive = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    startActivity(Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                dialog.show(supportFragmentManager, "")
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!::codeScanner.isInitialized)
                    setupScanner()
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {

                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized)
            codeScanner.startPreview()
    }

    override fun onPause() {
        if (::codeScanner.isInitialized)
            codeScanner.releaseResources()
        super.onPause()
    }
}