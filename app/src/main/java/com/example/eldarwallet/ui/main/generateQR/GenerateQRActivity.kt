package com.example.eldarwallet.ui.main.generateQR

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
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
                val imageBytes: ByteArray = it.bytes()

                // Decodifica los bytes en un objeto Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                // Muestra el bitmap en un ImageView
                val imageView: ImageView = binding.ivQR
                imageView.setImageBitmap(bitmap)
            }
            onError.observe(this@GenerateQRActivity) {
                binding.progressBar.gone()
                val dialog = GenericDialogFragment.createInstance(
                    "Ha habido un error",
                    "Pruebe nuevamente mas tarde",
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    startActivity(Intent(this@GenerateQRActivity, MainActivity::class.java)
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
        viewModel.generateQR("${user.name} ${user.surname}")
    }
}