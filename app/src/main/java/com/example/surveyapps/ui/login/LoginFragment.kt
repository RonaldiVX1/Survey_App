package com.example.surveyapps.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.surveyapps.MainActivity
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: LoginViewModel
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(LoginViewModel::class.java)


        checkUserLogIn()



        binding?.buttonRegister?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )

        binding?.buttonLogin?.setOnClickListener {
            val email = _binding?.edEmail?.text.toString()
            val password = _binding?.edPassword?.text.toString()

            if (password.length < 6) {
                Toast.makeText(requireContext(), "password must be 6 character", Toast.LENGTH_LONG)
                    .show()
            } else {
              loginWithEmailAndPassword(email,password)
            }
        }
    }

    private fun checkUserLogIn(){
        if (auth.currentUser !=null){
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ) {

        try {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Login Sucess.",
                        Toast.LENGTH_SHORT,

                        ).show()
                    navigateToHome()
                }
                Result.success(task)
            }.addOnFailureListener {
                    exception ->
                Toast.makeText(
                    requireContext(),
                    exception.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Login Failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}


