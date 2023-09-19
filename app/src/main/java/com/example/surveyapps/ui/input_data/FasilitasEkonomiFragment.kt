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
import com.example.surveyapps.databinding.FragmentFasilitasEkonomiBinding
import com.example.surveyapps.ui.upload.UploadDataFragment


class FasilitasEkonomiFragment : Fragment() {

    private var _binding: FragmentFasilitasEkonomiBinding? = null
    private val binding get() = _binding

    private val uploadDataFragment = UploadDataFragment()
    private var selectedSubKategoriFasilitasEkonomi: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFasilitasEkonomiBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.textFasilitasEkonomi?.setTypeface(
            binding?.textFasilitasEkonomi?.typeface,
            Typeface.BOLD
        )
        spinnerSubKategoriEkonomi()

        binding?.buttonInputData?.setOnClickListener { navigation ->
            if (checkData()) {
                try {
                    sendData()
                    navigation.findNavController().navigate(
                        R.id.action_fasilitasEkonomiFragment_to_uploadDataFragment,
                        sendData()
                    )
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
        if (binding?.edAlamat?.text.isNullOrEmpty() || binding?.edNamaFasilitas?.text.isNullOrEmpty() || binding?.edLat?.text.isNullOrEmpty() || binding?.edLon?.text.isNullOrEmpty()) {
            return false
        } else {
            return true
        }

    }


    private fun sendData(): Bundle {

        val namaFasilitas = binding?.edNamaFasilitas?.text
        val alamat = binding?.edAlamat?.text
        val lat = binding?.edLat?.text.toString().toDouble()
        val lon = binding?.edLon?.text.toString().toDouble()

        val bundle = Bundle()

        bundle.apply {
            putString("jenis_bangunan", "fasilitas_ekonomi")
            putString("nama_fasilitas", namaFasilitas.toString())
            putString("selected_subkategori_fasilitas", selectedSubKategoriFasilitasEkonomi)
            putString("alamat", alamat.toString())
            putDouble("lat", lat)
            putDouble("lon", lon)

        }

        uploadDataFragment.arguments = bundle
        return bundle

    }


    private fun spinnerSubKategoriEkonomi() {

        val spinnerSubKategoriEkonomi = binding?.spinnerSubkategoriFasilitas
        val jenisSubKategoriEkonomi =
            resources.getStringArray(R.array.subkategori_fasilitas_ekonomi)
        val arrayAdapterSubKategoriEkonomi = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_item,
            jenisSubKategoriEkonomi
        )

        spinnerSubKategoriEkonomi?.adapter = arrayAdapterSubKategoriEkonomi
        spinnerSubKategoriEkonomi?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSubKategoriFasilitasEkonomi = jenisSubKategoriEkonomi[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e(ContentValues.TAG, "select Sub kategori")
                }

            }
    }

}