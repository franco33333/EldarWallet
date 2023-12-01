package com.example.eldarwallet.ui.main.scanQR

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityScanResultBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment
import com.example.eldarwallet.utils.formatAsCurrency
import com.google.gson.Gson


class ScanResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScanResultBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)[ScanViewModel::class.java].apply {
            payGeneratedLiveData.observe(this@ScanResultActivity) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.app_name),
                    getString(R.string.payment_completed),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                    startActivity(
                        Intent(this@ScanResultActivity, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                dialog.show(supportFragmentManager, "")
            }
        }
    }

    companion object {
        private const val ARG_NAME = "ARG_NAME"
        fun newIntent(context: Context, name: String) =
            Intent(context, ScanResultActivity::class.java).apply {
                putExtra(ARG_NAME, Gson().toJson(name))
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.containerToolbar.toolbar.title = ""
        binding.containerToolbar.toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(binding.containerToolbar.toolbar)
        binding.containerToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val name = intent.getStringExtra(ARG_NAME)
        binding.tvName.text = name?.replace("\"", "")

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().replace("[$.,]+".toRegex(), "").trim()

                val amountFormatted = formatAsCurrency(text)

                binding.etAmount.removeTextChangedListener(this)
                binding.etAmount.setText(amountFormatted)
                binding.btnPay.isEnabled = amountFormatted.isNotEmpty()
                if (amountFormatted.length <= 15)
                    binding.etAmount.setSelection(amountFormatted.length)
                binding.etAmount.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnPay.setOnClickListener {
            val amount = binding.etAmount.text.toString()

            if (amount.replace("[$.,]+".toRegex(), "").trim().toLong() == 0L) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.wrong_amount),
                    getString(R.string.enter_the_amount_again),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = { dialog.dismiss() }
                dialog.show(supportFragmentManager, "")
            } else if (amount.replace("[$.,]+".toRegex(), "").trim().toLong() >
                AppPreferences.getUser()!!.balance
            ) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.insufficient_balance),
                    getString(R.string.enter_the_amount_again),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = { dialog.dismiss() }
                dialog.show(supportFragmentManager, "")
            } else {
                viewModel.pay(amount.replace("[$.,]+".toRegex(), "").trim().toLong())
            }
        }
    }
}