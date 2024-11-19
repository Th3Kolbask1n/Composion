package com.alexp.composion.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexp.composion.R
import com.alexp.composion.databinding.FragmentGameBinding
import com.alexp.composion.databinding.FragmentGameFinishedBinding
import com.alexp.composion.domain.entity.GameResult
import com.alexp.composion.presentation.ChooseLevelFragment

class GameFinishedFragment : Fragment() {
//    private lateinit var gameResult: GameResult

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding:FragmentGameFinishedBinding? = null
    private val binding : FragmentGameFinishedBinding
        get() = _binding?:throw RuntimeException("FragmentGameBinding is null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        bindViews()
    }

    private fun setupClickListeners()
    {

        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews()
    {
        binding.gameResult = args.gameResult
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    private fun retryGame()
    {
       findNavController().popBackStack()
    }

}
