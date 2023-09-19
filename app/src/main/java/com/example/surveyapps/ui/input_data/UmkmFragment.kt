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
import com.example.surveyapps.databinding.FragmentUmkmBinding
import com.example.surveyapps.ui.upload.UploadDataFragment


class UmkmFragment : Fragment() {

    private var _binding: FragmentUmkmBinding? = null
    private val binding get() = _binding
    private var selectedJenisUsaha: String? = null
    private val uploadDataFragment = UploadDataFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUmkmBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.textUmkm?.setTypeface(binding?.textUmkm?.typeface, Typeface.BOLD)
        spinnerJenisUsaha()
        binding?.buttonInputData?.setOnClickListener { navigation ->
            if (checkData()) {
                try {
                    sendData()
                    navigation.findNavController()
                        .navigate(R.id.action_umkmFragment_to_uploadDataFragment, sendData())
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
        if (binding?.edNamaUmkm?.text.isNullOrEmpty() || binding?.edAlamat?.text.isNullOrEmpty() || binding?.edJumlahPekerja?.text.isNullOrEmpty() || binding?.edKeterangan?.text.isNullOrEmpty() || binding?.edLat?.text.isNullOrEmpty() || binding?.edLon?.text.isNullOrEmpty()) {
            return false
        } else {
            return true
        }

    }

    private fun sendData(): Bundle {

        val namaUmkm = binding?.edNamaUmkm?.text
        val alamat = binding?.edAlamat?.text
        val jumlahPekerja = binding?.edJumlahPekerja?.text
        val keterangan = binding?.edKeterangan?.text
        val lat = binding?.edLat?.text.toString().toDouble()
        val lon = binding?.edLon?.text.toString().toDouble()

        val bundle: Bundle = Bundle()

        bundle.apply {
            putString("jenis_bangunan", "umkm")
            putString("nama_umkm", namaUmkm.toString())
            putString("selected_jenis_usaha", selectedJenisUsaha)
            putString("jumlah_pekerja", jumlahPekerja.toString())
            putString("keterangan", keterangan.toString())
            putString("alamat", alamat.toString())
            putDouble("lat", lat)
            putDouble("lon", lon)

        }

        uploadDataFragment.arguments = bundle
        Log.e(ContentValues.TAG, bundle.toString())
        return bundle

    }


    private fun spinnerJenisUsaha() {
        val spinnerJenisUsaha = binding?.spinnerJenisUsaha
        val jenisUsaha = resources.getStringArray(R.array.jenis_usaha)
        val arrayAdapterJenisUsaha = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_item,
            jenisUsaha
        )

        spinnerJenisUsaha?.adapter = arrayAdapterJenisUsaha
        spinnerJenisUsaha?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedJenisUsaha = jenisUsaha[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e(ContentValues.TAG, "select Sub kategori")
                }

            }
    }
}