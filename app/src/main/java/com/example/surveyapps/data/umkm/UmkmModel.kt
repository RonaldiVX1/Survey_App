package com.example.surveyapps.data.umkm

data class UmkmModel(
    var nama : String? = null,
    var image : String? = null,
    var jenisUsaha : String? = null,
    var jumlahPekerja : String? = null,
    var keterangan : String? = null,
    var alamat : String? = null,
    var lat : Double? = null,
    var lon : Double? = null,
)