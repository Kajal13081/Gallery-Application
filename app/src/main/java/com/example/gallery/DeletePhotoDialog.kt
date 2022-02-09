package com.example.gallery

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog

class DeletePhotoDialog : DialogFragment() {

    companion object {
        fun create(image: String): DeletePhotoDialog {

            return DeletePhotoDialog().apply {
                arguments = bundleOf(
                    "IMAGE" to image
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val image = extraNotNull<String>("IMAGE").value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dismiss()
            ImageUtil.deleteImageR(requireActivity(), image)
        }
        val pair = Pair(
            R.string.delete_image,
            HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_image_x), "wait"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        return MaterialDialog(requireContext())
            .title(pair.first)
            .message(text = pair.second)
            .noAutoDismiss()
            .cornerRadius(16F)
            .negativeButton(R.string.cancel) {
                dismiss()
            }
            .positiveButton(R.string.delete_image) {
                dismiss()
                ImageUtil.deleteImage(requireContext(), image)
            }
    }
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

