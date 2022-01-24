package com.example.wembleymoviesapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseBindingActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity(){

    /** ViewBinding **/
    protected lateinit var binding: VB
    protected abstract fun getViewBinding(): VB

    /** ViewModel **/
    protected val activityViewModel by lazy { ViewModelProvider(this).get(getViewModelClass()) }
    protected abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        setupView()
    }

    /**
     * The adapters and some variables are initialized here when the view is created.
     */
    protected abstract fun setupView()

}