package com.example.surveyapps.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapps.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RegisterViewModel::class.java)

        binding?.buttonRegister?.setOnClickListener {
            val name = _binding?.edName?.text.toString()
            val email = _binding?.edEmail?.text.toString()
            val password = _binding?.edPassword?.text.toString()

            if (password.length < 8) {
                Toast.makeText(requireContext(), "password must be 8 character", Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.createUserWithEmailAndPassword(email,password, requireContext())
            }
        }
    }


}