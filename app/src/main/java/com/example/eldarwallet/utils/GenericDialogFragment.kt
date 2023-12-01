package com.example.eldarwallet.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.eldarwallet.databinding.FragmentCustomDialogBinding

class GenericDialogFragment : DialogFragment() {

    private val binding by lazy { FragmentCustomDialogBinding.inflate(layoutInflater) }
    var onClickAccept: (() -> Unit?)? = null
    var onClickCancel: (() -> Unit?)? = null

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_BTN_POSITIVE = "showBtnPositive"
        const val KEY_TEXT_POSITIVE = "textBtnPositive"
        const val KEY_BTN_NEGATIVE = "showBtnNegative"
        const val KEY_TEXT_NEGATIVE = "textBtnNegative"

        fun createInstance(
            title: String,
            description: String,
            showBtnPositive: Boolean = true,
            textBtnPositive: String? = null,
            showBtnNegative: Boolean = false,
            textBtnNegative: String? = null
        ): GenericDialogFragment {
            return GenericDialogFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_DESCRIPTION to description,
                    KEY_BTN_POSITIVE to showBtnPositive,
                    KEY_TEXT_POSITIVE to textBtnPositive,
                    KEY_BTN_NEGATIVE to showBtnNegative,
                    KEY_TEXT_NEGATIVE to textBtnNegative
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        animateEntrance(binding.mContainer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(KEY_TITLE)
        val description = arguments?.getString(KEY_DESCRIPTION)
        val showBtnPositive = arguments?.getBoolean(KEY_BTN_POSITIVE)
        val textBtnPositive = arguments?.getString(KEY_TEXT_POSITIVE)
        val showBtnNegative = arguments?.getBoolean(KEY_BTN_NEGATIVE)
        val textBtnNegative = arguments?.getString(KEY_TEXT_NEGATIVE)

        with(binding) {
            mTitle.text = title
            mDescription.text = description
            btnPositive.text = textBtnPositive
            btnNegative.text = textBtnNegative
            btnPositive.isVisible = showBtnPositive == true
            btnNegative.isVisible = showBtnNegative == true

            btnPositive.setOnClickListener {
                onClickAccept?.invoke()
                dismiss()
            }

            btnNegative.setOnClickListener {
                onClickCancel?.invoke()
                dismiss()
            }
        }
    }

    private fun animateEntrance(container: View) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                container.translationY = container.height.toFloat()
                container.alpha = 0f
                container.animate().translationY(0f).alpha(1f).setInterpolator(JellyInterpolator())
                    .setDuration(500).start()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

}