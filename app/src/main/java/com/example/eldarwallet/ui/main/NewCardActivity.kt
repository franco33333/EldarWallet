package com.example.eldarwallet.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityNewCardBinding
import com.example.eldarwallet.utils.GenericDialogFragment
import java.util.Calendar

class NewCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewCardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.toString() == "MM/YY") return

                val formattedText = formatAsDate(s.toString())
                binding.etCardNumber.setText(formattedText)
                binding.etCardNumber.setSelection(formattedText.length)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnAddCard.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val cardNumber = binding.etCardNumber.text.toString()
        val cardName = binding.etCardName.text.toString()
        val expirationDate = binding.etExpirationDate.text.toString()
        val securityCode = binding.etSecurityCode.text.toString()
        val document = binding.etCardDocument.text.toString()

        val user = AppPreferences.getUser()!!

        if (cardNumber.isEmpty() || cardName.isEmpty() || expirationDate.isEmpty() || securityCode.isEmpty() || document.isEmpty()) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.incomplete_fields),
                description = getString(R.string.please_complete_fields),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else if (isExpirationDateValid(expirationDate)) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.expiration_date_invalid),
                description = getString(R.string.please_fix_expiration_date),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else if (!cardName.contains(user.name!!, true) ||
            !cardName.contains(user.surname!!, true)) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.card_name_invalid),
                description = getString(R.string.please_fix_card_name),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.app_name),
                description = getString(R.string.card_added),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            dialog.show(supportFragmentManager, null)
        }
    }

    private fun isExpirationDateValid(expirationDate: String): Boolean {
        if (expirationDate.length!=5)
            return false
        val month = expirationDate.substring(0..1)
        val year = expirationDate.substring(3..4)
        if (year.toInt() < Calendar.getInstance().get(Calendar.YEAR))
            return false
        if (year.toInt() == Calendar.getInstance().get(Calendar.YEAR) &&
            month.toInt() < Calendar.getInstance().get(Calendar.MONTH))
            return false
        return true
    }

    private fun formatAsDate(text: String): String {
        val numbersOnly = text.replace("[^0-9]".toRegex(), "")
        val formattedText = StringBuilder(numbersOnly)

        if (numbersOnly.length >= 3) {
            formattedText.insert(2, "/")
        }

        return formattedText.toString()
    }
}