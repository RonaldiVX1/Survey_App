package com.example.surveyapps.ui.register

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun createUserWithEmailAndPassword(email: String, password: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Log.d(ContentValues.TAG, "registerWithEmail:success")
                Toast.makeText(
                    context,
                    "Authentication Success.",
                    Toast.LENGTH_SHORT,
                ).show()
                val user = auth.currentUser

            } else {
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                Log.w(ContentValues.TAG, "registerWithEmail:failure", task.exception)

            }
        }
    }
}