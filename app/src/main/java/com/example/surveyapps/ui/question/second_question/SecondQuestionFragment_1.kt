package com.example.surveyapps.ui.question.second_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentSecondQuestion1Binding


class SecondQuestionFragment_1 : Fragment() {


    private var _binding: FragmentSecondQuestion1Binding? = null
    private val binding get() = _binding
    private lateinit var question: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondQuestion1Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        question = resources.getStringArray(R.array.second_question)

        binding?.textQuestion?.text = question[0]
        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_yes) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_secondQuestionFragment_1_to_thirdQuestionFragment_1
                    )
                )
            } else if (checkedId == R.id.radio_no) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_secondQuestionFragment_1_to_FasilitasGeneralFragment

                    )
                )
            } else {
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