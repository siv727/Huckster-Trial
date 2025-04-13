package com.android.huckster.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Shared ViewModel for handling product-related data
class SharedProductViewModel : ViewModel() {
    val refreshProductList = MutableLiveData<Boolean>()
}