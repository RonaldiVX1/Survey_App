package com.example.surveyapps.ui.question.third_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentThirdQuestion2Binding


class ThirdQuestionFragment_2 : Fragment() {

    private var _binding: FragmentThirdQuestion2Binding? = null
    private val binding get() = _binding
    private lateinit var question: Array<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdQuestion2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        question = resources.getStringArray(R.array.third_question)

        binding?.textQuestion?.text = question[1]
        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_yes) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_thirdQuestionFragment_2_to_industriKecilFragment
                    )
                )
            } else if (checkedId == R.id.radio_no) {
                binding?.button?.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_thirdQuestionFragment_2_to_umkmFragment

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