package com.alexp.composion.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexp.composion.R
import com.alexp.composion.databinding.FragmentGameBinding
import com.alexp.composion.domain.entity.GameResult
import com.alexp.composion.domain.entity.GameSettings
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.presentationT20Widget.GameViewModel
import com.alexp.composion.presentation.ChooseLevelFragment

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("GameFragment is null")

    private val viewModelFactory by lazy {
        GameViewModelFactory(
            args.level,
            requireActivity().application
        )
    }
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this, viewModelFactory
        )[GameViewModel::class.java]
    }


    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {

            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListenersToOptions()
        observerViewModel()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observerViewModel() {

        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()

            Log.d("TvOptions", it.options.toString())

            for (i in 0 until tvOptions.size) {
                tvOptions[i].text = it.options[i].toString()
            }

        }

        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }

        viewModel.enoughtCountOfRightAnswers.observe(viewLifecycleOwner)
        {

            binding.tvAnswersProgress.setTextColor(getColorIdByState(it))
        }

        viewModel.enoughtPercentOfRightAnswers.observe(viewLifecycleOwner)
        {
            var color = getColorIdByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)

        }
        viewModel.formattedTime.observe(viewLifecycleOwner)
        {
            binding.tvTimer.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner)
        {
            Log.d("finished", "25")
            launchGameFinishedFragment(it)
        }
        viewModel.progressAnswers.observe(viewLifecycleOwner)
        {
            binding.tvAnswersProgress.text = it

        }
    }

    private fun getColorIdByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun launchGameFinishedFragment(gameResult: GameResult) {

        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment2(gameResult))
    }



}
