package com.andresuryana.schotersnews.util

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.andresuryana.schootersnews.R
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {

    fun showSuccessSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.success))
            )
            .show()
    }

    fun showErrorSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.error))
            )
            .show()
    }
}