package com.example.fitplan

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickImageViewModel: ViewModel() {

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri : LiveData<Uri> get() = _photoUri

   fun setPhotoURi(uri: Uri){
        _photoUri.value=uri
    }
}