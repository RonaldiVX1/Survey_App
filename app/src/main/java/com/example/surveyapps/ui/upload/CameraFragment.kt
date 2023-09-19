package com.example.surveyapps.ui.upload

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.surveyapps.databinding.FragmentCameraBinding
import com.example.surveyapps.utils.createFile


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCamera()

        binding?.captureImage?.setOnClickListener{takePhoto()}
        binding?.switchCamera?.setOnClickListener{
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selected_image")?.observe(viewLifecycleOwner) { data ->
            // Do something with the data.
        }




    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val bundle = Bundle()
                    bundle.putBoolean("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    bundle.putParcelable("selected_image", savedUri)
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("bundle_image", bundle)
                    findNavController().popBackStack()
                }
            }
        )
    }


    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    requireActivity(),
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun hideSystemUI(state: Boolean) {
        @Suppress("DEPRECATION")
        if (state) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                requireActivity().window.clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                )
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI(true)
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        hideSystemUI(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}