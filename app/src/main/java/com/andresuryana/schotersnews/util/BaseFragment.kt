package com.andresuryana.schotersnews.util

import androidx.fragment.app.Fragment
import com.andresuryana.schotersnews.ui.component.LoadingDialogFragment

open class BaseFragment : Fragment() {

    private var loadingDialog: LoadingDialogFragment? = null

    fun showLoadingDialog(isLoading: Boolean) {
        if (isLoading) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialogFragment()
                loadingDialog?.show(childFragmentManager, LoadingDialogFragment::class.simpleName)
            }
        } else {
            loadingDialog?.dismissAllowingStateLoss()
            loadingDialog = null
        }
    }
}