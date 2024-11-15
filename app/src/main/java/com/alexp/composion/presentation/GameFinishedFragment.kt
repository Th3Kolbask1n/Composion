package com.alexp.composion.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.alexp.composion.R
import com.alexp.composion.databinding.FragmentGameBinding
import com.alexp.composion.databinding.FragmentGameFinishedBinding
import com.alexp.composion.domain.entity.GameResult
import ru.sumin.composition.presentation.ChooseLevelFragment

class GameFinishedFragment : Fragment() {
    private lateinit var gameResult: GameResult

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

        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }
    private fun parseArgs()
    {
        requireArguments().getParcelable(KEY_RESULT,GameResult::class.java)?.let {
            gameResult = it
        }
    }

    private fun retryGame()
    {
        requireActivity().supportFragmentManager.popBackStack(ChooseLevelFragment.NAME,0)
    }
    companion object
    {
        const val NAME = "GameResult"
        private const val KEY_RESULT = "result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment
        {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT,gameResult)
                }
            }
        }
    }
}
