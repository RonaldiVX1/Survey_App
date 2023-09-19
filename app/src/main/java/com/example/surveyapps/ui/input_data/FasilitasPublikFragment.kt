package com.example.surveyapps.ui.input_data

import android.content.ContentValues.TAG
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
import com.example.surveyapps.databinding.FragmentFasilitasUmumBinding
import com.example.surveyapps.ui.upload.UploadDataFragment


class FasilitasPublikFragment : Fragment() {

    private var _binding: FragmentFasilitasUmumBinding? = null
    private val binding get() = _binding
    private var selectedJenisFasilitas: String? = null
    private var selectedSubKategoriFasilitas: String? = null
    private val uploadDataFragment = UploadDataFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFasilitasUmumBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.textFasilitasUmum?.setTypeface(binding?.textFasilitasUmum?.typeface, Typeface.BOLD)
        spinnerJenisFasilitas()

        binding?.buttonInputData?.setOnClickListener { navigation ->
            if (checkData()) {
                try {
                    sendData()
                    navigation.findNavController().navigate(
                        R.id.action_FasilitasGeneralFragment_to_uploadDataFragment,
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
        if (binding?.edNamaFasilitas?.text.isNullOrEmpty() || binding!!.edAlamat.text.isNullOrEmpty() || binding?.edLat?.text.isNullOrEmpty() || binding?.edLon?.text.isNullOrEmpty()) {
            return false
        } else {
            return true
        }

    }

    private fun sendData(): Bundle {
        val namaFasilitas = binding?.edNamaFasilitas?.text.toString()
        val alamat = binding!!.edAlamat.text.toString()
        val lat = binding?.edLat?.text.toString().toDouble()
        val lon = binding?.edLon?.text.toString().toDouble()

        val bundle: Bundle = Bundle().apply {
            putString("jenis_bangunan", "fasilitas_umum")
            putString("selected_fasilitas", selectedJenisFasilitas)
            putString("nama_fasilitas", namaFasilitas)
            putString("selected_subkategori_fasilitas", selectedSubKategoriFasilitas)
            putString("alamat", alamat)
            putDouble("lat", lat)
            putDouble("lon", lon)

        }

        uploadDataFragment.arguments = bundle
        Log.e(TAG, bundle.toString())
        return bundle
    }


    private fun spinnerJenisFasilitas() {


        val spinnerJenisFasilitas = binding?.spinnerJenisFasilitas
        val jenisFasilitasArray = resources.getStringArray(R.array.jenis_fasilitas_spinner)
        val arrayAdapterJenisFasilitas = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_item,
            jenisFasilitasArray
        )

        spinnerJenisFasilitas?.adapter = arrayAdapterJenisFasilitas
        spinnerJenisFasilitas?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedJenisFasilitas = jenisFasilitasArray[position]
                    spinnerSubKategoriFasilitas(jenisFasilitasArray[position])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    binding?.spinnerSubkategoriFasilitas?.isEnabled == false
                }

            }
    }

    private fun spinnerSubKategoriFasilitas(selectedJenisFasilitas: String) {
        lateinit var jenisSubKategoriFasilitasArray: Array<String>
        when (selectedJenisFasilitas) {
            "Fasilitas Pendidikan" -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.subkategori_fasilitas_pendidikan)

            }
            "Fasilitas Kesehatan" -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.subkategori_fasilitas_kesehatan)

            }
            "Fasilitas Ibadah" -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.subkategori_tempat_ibadah)

            }
            "Fasilitas Olahraga" -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.subkategori_fasilitas_olahraga)

            }
            "Lainnya" -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.subkategori_lainnya)
            }
            else -> {
                jenisSubKategoriFasilitasArray =
                    resources.getStringArray(R.array.pilih_subkategori)
            }
        }

        val spinnerJenisSubKategoriFasilitas = binding?.spinnerSubkategoriFasilitas
        val arrayAdapterJenisSubFasilitas = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_item,
            jenisSubKategoriFasilitasArray
        )

        spinnerJenisSubKategoriFasilitas?.adapter = arrayAdapterJenisSubFasilitas
        spinnerJenisSubKategoriFasilitas?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSubKategoriFasilitas = jenisSubKategoriFasilitasArray[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }


}