package com.example.eldarwallet.ui.main.payWithCard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.databinding.ActivityScanNfcactivityBinding
import com.example.eldarwallet.ui.main.MainActivity

class ScanNFCActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScanNfcactivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
            startActivity(
                Intent(this@ScanNFCActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}