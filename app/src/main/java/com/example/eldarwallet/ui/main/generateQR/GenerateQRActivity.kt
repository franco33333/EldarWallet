package com.example.eldarwallet.ui.main.generateQR

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityGenerateQractivityBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment
import com.example.eldarwallet.utils.gone
import com.example.eldarwallet.utils.visible

class GenerateQRActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGenerateQractivityBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)[GenerateQRViewModel::class.java].apply {
            qrGeneratedLiveData.observe(this@GenerateQRActivity) {
                binding.progressBar.gone()
                binding.tvTitle.visible()
                binding.ivQR.setImageBitmap(it)
            }
            onError.observe(this@GenerateQRActivity) {
                binding.progressBar.gone()
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.there_was_an_error),
                    getString(R.string.try_again_later),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    startActivity(
                        Intent(this@GenerateQRActivity, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                dialog.show(supportFragmentManager, "")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.progressBar.visible()
        val user = AppPreferences.getUser()!!
        if (user.qr == null) {
            viewModel.generateQR("${user.name} ${user.surname}")
        } else {
            binding.tvTitle.visible()
            binding.ivQR.setImageBitmap(user.qr)
        }
    }
}