package com.example.surveyapps.ui.input_data

import android.content.ContentValues
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentPerusahaanBinding
import com.example.surveyapps.databinding.FragmentTempatTinggalBinding
import com.example.surveyapps.databinding.FragmentUmkmBinding
import com.example.surveyapps.ui.upload.UploadDataFragment


class TempatTinggalFragment : Fragment() {

    private var _binding: FragmentTempatTinggalBinding? = null
    private val binding get() = _binding
    private var selectedGolonganPokokIndustri: String? = null
    private val uploadDataFragment = UploadDataFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTempatTinggalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.textTempatTingal?.setTypeface(binding?.textTempatTingal?.typeface, Typeface.BOLD)
        binding?.buttonInputData?.setOnClickListener { navigation ->
            if (checkData()) {
                try {
                    sendData()
                    navigation.findNavController().navigate(
                        R.id.action_tempatTinggalFragment_to_uploadDataFragment,
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
        if (binding?.edNamaTempatTinggal?.text.isNullOrEmpty() || binding?.edAlamat?.text.isNullOrEmpty() || binding?.edLat?.text.isNullOrEmpty() || binding?.edLon?.text.isNullOrEmpty()) {
            return false
        } else {
            return true
        }
    }

    private fun sendData(): Bundle {

        val namaTempatTinggal = binding?.edNamaTempatTinggal?.text
        val alamat = binding?.edAlamat?.text
        val lat = binding?.edLat?.text.toString().toDouble()
        val lon = binding?.edLon?.text.toString().toDouble()

        val bundle: Bundle = Bundle()

        bundle.apply {
            putString("jenis_bangunan", "tempat_tinggal")
            putString("nama_tempat_tinggal", namaTempatTinggal.toString())
            putString("alamat", alamat.toString())
            putDouble("lat", lat)
            putDouble("lon", lon)

        }

        uploadDataFragment.arguments = bundle
        Log.e(ContentValues.TAG, bundle.toString())
        return bundle

    }

}