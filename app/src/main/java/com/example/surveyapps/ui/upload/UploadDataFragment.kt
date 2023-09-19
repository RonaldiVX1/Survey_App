package com.example.surveyapps.ui.upload


import android.content.Intent

import android.graphics.BitmapFactory

import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import androidx.navigation.Navigation

import androidx.navigation.fragment.findNavController

import com.example.surveyapps.R
import com.example.surveyapps.data.fasilitas_ekonomi.FasilitasEkonomiModel
import com.example.surveyapps.data.fasilitas_publik.FasilitasUmumModel
import com.example.surveyapps.data.industri_kecil.IndustriKecilModel
import com.example.surveyapps.data.perusahaan.PerusahaanModel
import com.example.surveyapps.data.tempat_tinggal.TempatTinggalModel
import com.example.surveyapps.data.umkm.UmkmModel
import com.example.surveyapps.databinding.FragmentUploadDataBinding
import com.example.surveyapps.ui.others.Result
import com.example.surveyapps.utils.uriToFile


import kotlinx.coroutines.launch
import java.io.File


class UploadDataFragment : Fragment() {

    private var _binding: FragmentUploadDataBinding? = null
    private val binding get() = _binding
    private var jenisBangunan: String? = null

    private var getFile: File? = null
    private var getUri: Uri? = null

//    private var location: Location? = null
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private var lat: Double? = null
//    private var lon: Double? = null



    private lateinit var viewModel: UploadDataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this.arguments?.getString("jenis_bangunan") != null) {
            jenisBangunan = this.arguments?.getString("jenis_bangunan")
        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        getMyLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadDataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UploadDataViewModel::class.java)

        detailJenisBangunan(jenisBangunan)



        binding?.buttonCamera?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_uploadDataFragment_to_cameraFragment))
        binding?.buttonGallery?.setOnClickListener { startGallery() }
        binding?.imageView?.setImageResource(R.drawable.ic_image)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("bundle_image")
            ?.observe(viewLifecycleOwner) { data ->
                val fileUri = data.get("selected_image")
                if (fileUri != null) {
                    getUri = fileUri as Uri
                    val uri: Uri = fileUri
                    val isBackCamera = data?.get("isBackCamera") as Boolean
                    getFile = uri.toFile()
                    binding?.imageView?.setImageBitmap(BitmapFactory.decodeFile(getFile!!.path))
                }
            }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            getUri = selectedImg
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile

            binding?.imageView?.setImageURI(selectedImg)
        }
    }

    private fun uploadDataToDatabase(data: Any, view: View) {
        if (getUri != null) {
            lifecycleScope.launch {
                viewModel.uploadImage(
                    getUri!!,
                    data,
                    jenisBangunan!!

                )
                viewModel.uiState.collect { result ->
                    when (result) {
                        is Result.Error -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.error,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(
                                requireContext(),
                                R.string.success_upload,
                                Toast.LENGTH_SHORT,
                            ).show()
                            Navigation.findNavController(view).navigate(
                                R.id.action_uploadDataFragment_to_homeFragment,
                            )
                        }
                    }
                }
            }


        } else {
            Toast.makeText(
                requireContext(),
                R.string.warning_upload,
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    //                   UI For Upload Data                         //

    private fun detailJenisBangunan(jenisBangunan: String?) {

        when (jenisBangunan) {
            "fasilitas_umum" -> {
                fasilitasUmum()
            }
            "fasilitas_ekonomi" -> {
                fasilitasEkonomi()
            }
            "perusahaan" -> {
                perusahaan()
            }
            "industri_kecil" -> {
                industriKecil()
            }
            "umkm" -> {
                umkm()
            }
            else -> {
                tempatTinggal()
            }

        }

    }

    private fun fasilitasUmum() {
        val jenisFasilitas = arguments?.getString("selected_fasilitas")
        val namaFasilitas = arguments?.getString("nama_fasilitas")
        val subKategoriFasilitas = arguments?.getString("selected_subkategori_fasilitas")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")

        binding?.firstTitle?.text = getString(R.string.nama_fasilitas)
        binding?.firstSubtitle?.text = namaFasilitas

        binding?.secondTitle?.text = getString(R.string.jenis_fasilitas)
        binding?.secondSubtitle?.text = jenisFasilitas

        binding?.thirdTitle?.text = getString(R.string.subkategori_fasilitas)
        binding?.thirdSubtitle?.text = subKategoriFasilitas

        binding?.fourthTitle?.text = getString(R.string.text_alamat)
        binding?.fourthSubtitle?.text = alamat

        binding?.fifthTitle?.text = getString(R.string.text_lat)
        binding?.fifthSubtitle?.text = lat.toString()

        binding?.sixthTitle?.text = getString(R.string.text_lon)
        binding?.sixthSubtitle?.text = lon.toString()


        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    FasilitasUmumModel(
                        nama = namaFasilitas,
                        jenisFasilitas = jenisFasilitas,
                        subKategori = subKategoriFasilitas,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }
    }

    private fun fasilitasEkonomi() {
        val namaFasilitas = arguments?.getString("nama_fasilitas")
        val subKategoriFasilitas = arguments?.getString("selected_subkategori_fasilitas")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")


        binding?.firstTitle?.text = getString(R.string.nama_fasilitas)
        binding?.firstSubtitle?.text = namaFasilitas

        binding?.secondTitle?.text = getString(R.string.subkategori_fasilitas)
        binding?.secondSubtitle?.text = subKategoriFasilitas

        binding?.thirdTitle?.text = getString(R.string.text_alamat)
        binding?.thirdSubtitle?.text = alamat

        binding?.fourthTitle?.text = getString(R.string.text_lat)
        binding?.fourthSubtitle?.text = lat.toString()

        binding?.fifthTitle?.text = getString(R.string.text_lon)
        binding?.fifthSubtitle?.text = lon.toString()


        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    FasilitasEkonomiModel(
                        nama = namaFasilitas,
                        subKategori = subKategoriFasilitas,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun perusahaan() {

        val namaPerusahaan = arguments?.getString("nama_perusahaan")
        val golonganIndustri = arguments?.getString("selected_golongan_industri")
        val barangYangDihasilkan = arguments?.getString("barang_yang_dihasilkan")
        val bahanBaku = arguments?.getString("bahan_baku")
        val jumlahPekerja = arguments?.getString("jumlah_pekerja")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")


        binding?.firstTitle?.text = getString(R.string.nama_perusahaan)
        binding?.firstSubtitle?.text = namaPerusahaan

        binding?.secondTitle?.text = getString(R.string.text_golongan_pokok_industri)
        binding?.secondSubtitle?.text = golonganIndustri

        binding?.thirdTitle?.text = getString(R.string.text_barang_yang_dihasilkan)
        binding?.thirdSubtitle?.text = barangYangDihasilkan

        binding?.fourthTitle?.text = getString(R.string.bahan_baku)
        binding?.fourthSubtitle?.text = bahanBaku

        binding?.fifthTitle?.text = getString(R.string.text_jumlah_pekerja)
        binding?.fifthSubtitle?.text = jumlahPekerja

        binding?.sixthTitle?.text = getString(R.string.text_alamat)
        binding?.sixthSubtitle?.text = alamat

        binding?.seventhTitle?.text = getString(R.string.text_lat)
        binding?.seventhSubtitle?.text = lat.toString()

        binding?.eighthTitle?.text = getString(R.string.text_lon)
        binding?.eighthSubtitle?.text = lon.toString()

        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    PerusahaanModel(
                        nama = namaPerusahaan,
                        golonganPokokIndustri = golonganIndustri,
                        barangYangDihasilkan = barangYangDihasilkan,
                        bahanBaku = bahanBaku,
                        jumlahPekerja = jumlahPekerja,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun industriKecil() {
        val namaIndustri = arguments?.getString("nama_industri")
        val golonganIndustri = arguments?.getString("selected_golongan_industri")
        val barangYangDihasilkan = arguments?.getString("barang_yang_dihasilkan")
        val bahanBaku = arguments?.getString("bahan_baku")
        val jumlahPekerja = arguments?.getString("jumlah_pekerja")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")


        binding?.firstTitle?.text = getString(R.string.nama_industri)
        binding?.firstSubtitle?.text = namaIndustri

        binding?.secondTitle?.text = getString(R.string.text_golongan_pokok_industri)
        binding?.secondSubtitle?.text = golonganIndustri

        binding?.thirdTitle?.text = getString(R.string.text_barang_yang_dihasilkan)
        binding?.thirdSubtitle?.text = barangYangDihasilkan

        binding?.fourthTitle?.text = getString(R.string.bahan_baku)
        binding?.fourthSubtitle?.text = bahanBaku

        binding?.fifthTitle?.text = getString(R.string.text_jumlah_pekerja)
        binding?.fifthSubtitle?.text = jumlahPekerja

        binding?.sixthTitle?.text = getString(R.string.text_alamat)
        binding?.sixthSubtitle?.text = alamat

        binding?.seventhTitle?.text = getString(R.string.text_lat)
        binding?.seventhSubtitle?.text = lat.toString()

        binding?.eighthTitle?.text = getString(R.string.text_lon)
        binding?.eighthSubtitle?.text = lon.toString()


        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    IndustriKecilModel(
                        nama = namaIndustri,
                        golonganPokokIndustri = golonganIndustri,
                        barangYangDihasilkan = barangYangDihasilkan,
                        bahanBaku = bahanBaku,
                        jumlahPekerja = jumlahPekerja,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }
    }

    private fun umkm() {
        val namaUmkm = arguments?.getString("nama_umkm")
        val jenisUsaha = arguments?.getString("selected_jenis_usaha")
        val jumlahPekerja = arguments?.getString("jumlah_pekerja")
        val keterangan = arguments?.getString("keterangan")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")

        binding?.firstTitle?.text = getString(R.string.nama_umkm)
        binding?.firstSubtitle?.text = namaUmkm

        binding?.secondTitle?.text = getString(R.string.text_jenis_usaha)
        binding?.secondSubtitle?.text = jenisUsaha

        binding?.thirdTitle?.text = getString(R.string.text_jumlah_pekerja)
        binding?.thirdSubtitle?.text = jumlahPekerja

        binding?.fourthTitle?.text = getString(R.string.text_keterangan)
        binding?.fourthSubtitle?.text = keterangan

        binding?.fifthTitle?.text = getString(R.string.text_alamat)
        binding?.fifthSubtitle?.text = alamat

        binding?.sixthTitle?.text = getString(R.string.text_lat)
        binding?.sixthSubtitle?.text = lat.toString()

        binding?.seventhTitle?.text = getString(R.string.text_lon)
        binding?.seventhSubtitle?.text = lon.toString()


        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    UmkmModel(
                        nama = namaUmkm,
                        jenisUsaha = jenisUsaha,
                        jumlahPekerja = jumlahPekerja,
                        keterangan = keterangan,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }
    }

    private fun tempatTinggal() {
        val namaTempatTinggal = arguments?.getString("nama_tempat_tinggal")
        val alamat = arguments?.getString("alamat")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")

        binding?.firstTitle?.text = getString(R.string.text_nama_tempat_tinggal)
        binding?.firstSubtitle?.text = namaTempatTinggal

        binding?.secondTitle?.text = getString(R.string.text_alamat)
        binding?.secondSubtitle?.text = alamat

        binding?.thirdTitle?.text = getString(R.string.text_lat)
        binding?.thirdSubtitle?.text = lat.toString()

        binding?.fourthTitle?.text = getString(R.string.text_lon)
        binding?.fourthSubtitle?.text = lon.toString()





        binding?.buttonUpload?.setOnClickListener { view ->
            try {
                uploadDataToDatabase(
                    TempatTinggalModel(
                        nama = namaTempatTinggal,
                        alamat = alamat,
                        lat = lat,
                        lon = lon,
                    ), view
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.loadingBar?.visibility = View.VISIBLE
        } else {
            binding?.loadingBar?.visibility = View.GONE
        }
    }
}