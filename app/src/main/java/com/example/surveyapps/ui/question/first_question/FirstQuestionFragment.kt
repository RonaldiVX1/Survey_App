package com.example.surveyapps.ui.question.first_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentQuestionBinding

class FirstQuestionFragment : Fragment() {


    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_yes) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_questionFragment_to_secondQuestionFragment_2,

                        )
                )
            } else if (checkedId == R.id.radio_no) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_questionFragment_to_secondQuestionFragment_1,

                        )
                )
            }else{
                binding?.button?.setOnClickListener{
                    Toast.makeText(
                        requireContext(),
                        R.string.warning_null_answer,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

}