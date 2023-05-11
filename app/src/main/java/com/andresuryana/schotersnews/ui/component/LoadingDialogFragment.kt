package com.andresuryana.schotersnews.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.andresuryana.schootersnews.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoadingDialogBinding.inflate(layoutInflater)
        binding.loadingSpinner.apply {
            isCancelable = false
        }
        return binding.root
    }
}