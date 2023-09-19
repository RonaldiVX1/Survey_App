package com.example.surveyapps.data.perusahaan

data class PerusahaanModel(
    var nama : String? = null,
    var image : String? = null,
    var golonganPokokIndustri : String? = null,
    var barangYangDihasilkan : String? = null,
    var bahanBaku : String? = null,
    var jumlahPekerja : String? = null,
    var alamat : String? = null,
    var lat : Double? = null,
    var lon : Double? = null,
)
