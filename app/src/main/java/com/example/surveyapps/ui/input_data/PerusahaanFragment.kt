package com.example.surveyapps.ui.input_data

import android.content.ContentValues
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentPerusahaanBinding
import com.example.surveyapps.ui.upload.UploadDataFragment


class PerusahaanFragment : Fragment() {

    private var _binding: FragmentPerusahaanBinding? = null
    private val binding get() = _binding
    private var selectedGolonganPokokIndustri: String? = null
    private val uploadDataFragment = UploadDataFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerusahaanBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.textPerusahaan?.setTypeface(binding?.textPerusahaan?.typeface, Typeface.BOLD)
        spinnerGolonganPokokIndustri()
        binding?.buttonInputData?.setOnClickListener { navigation ->
            if (checkData()) {
                try {
                    sendData()
                    navigation.findNavController()
                        .navigate(R.id.action_perusahaanFragment_to_uploadDataFragment, sendData())
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        R.string.warning_data,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.warning_data,
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }
    }

    private fun checkData(): Boolean {
        if (binding?.edNamaPerusahaan?.text.isNullOrEmpty() || binding?.edAlamat?.text.isNullOrEmpty() || binding?.edBarangYangDihasilkan?.text.isNullOrEmpty() || binding?.edBahanBaku?.text.isNullOrEmpty() || binding?.edJumlahPekerja?.text.isNullOrEmpty() || binding?.edLat?.text.isNullOrEmpty() || binding?.edLon?.text.isNullOrEmpty()) {
            return false
        } else {
            return true
        }

    }


    private fun sendData(): Bundle {

        val namaPerusahaan = binding?.edNamaPerusahaan?.text
        val alamat = binding?.edAlamat?.text
        val barangYangDihasilkan = binding?.edBarangYangDihasilkan?.text
        val bahanBaku = binding?.edBahanBaku?.text
        val jumlahPekerja = binding?.edJumlahPekerja?.text
        val lat = binding?.edLat?.text.toString().toDouble()
        val lon = binding?.edLon?.text.toString().toDouble()

        val bundle: Bundle = Bundle()

        bundle.apply {
            putString("jenis_bangunan", "perusahaan")
            putString("nama_perusahaan", namaPerusahaan.toString())
            putString("selected_golongan_industri", selectedGolonganPokokIndustri)
            putString("barang_yang_dihasilkan", barangYangDihasilkan.toString())
            putString("bahan_baku", bahanBaku.toString())
            putString("jumlah_pekerja", jumlahPekerja.toString())
            putString("alamat", alamat.toString())
            putDouble("lat", lat)
            putDouble("lon", lon)

        }
        uploadDataFragment.arguments = bundle
        Log.e(ContentValues.TAG, bundle.toString())
        return bundle
    }


    private fun spinnerGolonganPokokIndustri() {

        val spinnerGolonganPokokIndustri = binding?.spinnerGolonganPokokIndustri
        val jenisGolonganPokokIndustri = resources.getStringArray(R.array.golongan_pokok_industri)
        val arrayAdapterGolonganIndustri = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_item,
            jenisGolonganPokokIndustri
        )

        spinnerGolonganPokokIndustri?.adapter = arrayAdapterGolonganIndustri
        spinnerGolonganPokokIndustri?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedGolonganPokokIndustri = jenisGolonganPokokIndustri[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e(ContentValues.TAG, "select golongan pokok industri")
                }
            }
    }


}