package com.kuliah.medicalife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kuliah.medicalife.data.Product
import com.kuliah.medicalife.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _mainProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val mainProduct: StateFlow<Resource<List<Product>>> = _mainProduct

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _mainProduct.emit(Resource.Loading())
        }

        firestore.collection("Products").get()
            .addOnSuccessListener { result ->
                val mainProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _mainProduct.emit(Resource.Success(mainProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _mainProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}