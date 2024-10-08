package com.jetli.vina.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class QrCodedDecoded(
    val Merchant_Name: String = "",
    val Account_Number: String = "",
    val Store_Name: String = "",
    val Amount: String = "",
)

class PaymentViewModel : ViewModel() {
    // show initial state just like initial state in createSlice of Redux-toolkit
    private val _showModal = MutableStateFlow(false)
    val showModal : StateFlow<Boolean> = _showModal

    private val _amount = MutableStateFlow("")
    val amount : StateFlow<String> = _amount

    private val _qrCodeDetails = MutableStateFlow(QrCodedDecoded())
    val qrCodeDetails: StateFlow<QrCodedDecoded> = _qrCodeDetails

    // set state similar to reducer of createSlice in Redux-Toolkit
    fun setShowModal(show: Boolean){
        _showModal.value = show
    }

    fun setAmount (amount: String){
        _amount.value = amount
    }

    fun setQrCodeDetails(details: QrCodedDecoded){
        _qrCodeDetails.value = details
    }
}