package com.example.fitplan.view_models

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickImageViewModel: ViewModel() {

    private val _photoBitmap = MutableLiveData<Bitmap>()
    val photoBitmap : LiveData<Bitmap> get() = _photoBitmap

   fun setPhotoBitmap(bitmap: Bitmap){
       _photoBitmap.value = bitmap
    }
}