package com.example.surveyapps.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.surveyapps.R
import com.example.surveyapps.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textSurvey.setTypeface(null, Typeface.BOLD)

        binding.buttonStartSurvey.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_questionFragment)
        )
    }
}