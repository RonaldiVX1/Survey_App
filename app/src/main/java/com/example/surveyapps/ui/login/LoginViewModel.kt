package com.example.surveyapps.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        context: Context
    ) {

        try {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Login Sucess.",
                        Toast.LENGTH_SHORT,

                    ).show()
                }


                Result.success(task)
            }.addOnFailureListener {
                exception ->
                Toast.makeText(
                    context,
                    exception.toString(),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Login Failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}