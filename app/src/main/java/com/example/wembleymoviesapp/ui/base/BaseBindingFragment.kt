package com.example.wembleymoviesapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseBindingFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /** ViewBinding **/
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract fun getViewBinding(): VB

    /** ViewModel **/
    protected abstract fun isSameViewModelAsActivity(): Boolean
    protected abstract fun getViewModelClass(): Class<VM>
    protected open val viewModel by lazy {
        if (isSameViewModelAsActivity()) {
            ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(getViewModelClass())
        } else {
            ViewModelProvider(this).get(getViewModelClass())
        }
    }

    /** Base functions **/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    /**
     * The adapters and some variables are initialized here when the view is created.
     */
    protected abstract fun setupView()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}