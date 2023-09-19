package com.example.surveyapps.ui.upload

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.surveyapps.data.fasilitas_ekonomi.FasilitasEkonomiModel
import com.example.surveyapps.data.fasilitas_publik.FasilitasUmumModel
import com.example.surveyapps.data.industri_kecil.IndustriKecilModel
import com.example.surveyapps.data.perusahaan.PerusahaanModel
import com.example.surveyapps.data.umkm.UmkmModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.StateFlow
import com.example.surveyapps.ui.others.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class UploadDataViewModel : ViewModel() {

    private val db = Firebase.firestore
    private var _uiState = MutableStateFlow<Result>(Result.Loading)
    val uiState: StateFlow<Result> = _uiState


    @SuppressLint("SuspiciousIndentation")
    fun uploadData(linkImage: String, data: Any, jenisBangungan: String) = viewModelScope.launch {

        when (data) {
            is FasilitasEkonomiModel -> {
                data.image = linkImage

            }
            is FasilitasUmumModel -> {
                data.image = linkImage

            }
            is PerusahaanModel -> {
                data.image = linkImage

            }
            is IndustriKecilModel -> {
                data.image = linkImage

            }
            is UmkmModel -> {
                data.image = linkImage

            }

        }

        db.collection(jenisBangungan)
            .add(data)
            .addOnSuccessListener { documentReference ->
              _uiState.value = Result.Success(documentReference)

                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                _uiState.value = Result.Error(e.toString())
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun uploadImage(imageUri: Uri, data: Any, jenisBangunan: String) {

        _uiState.value = Result.Loading

        val storage = FirebaseStorage.getInstance().reference

        val fileRef = storage.child("images").child(UUID.randomUUID().toString() + ".jpg")
        val result = fileRef.putFile(imageUri)

        result.addOnSuccessListener { taskSnapShot ->
            taskSnapShot.metadata?.reference?.downloadUrl?.addOnCompleteListener { task ->

                uploadData(task.result.toString(), data, jenisBangunan)

            }

            Log.d("ViewModel", "Success!")
        }.addOnFailureListener { error ->
            Log.d("ViewModel", "${error.message}")
        }.addOnCompleteListener {
            Log.d("ViewModel", "Completed!")
        }

    }
}

