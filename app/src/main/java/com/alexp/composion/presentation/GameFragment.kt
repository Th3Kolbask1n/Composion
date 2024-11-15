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
import com.alexp.composion.R
import com.alexp.composion.databinding.FragmentGameBinding
import com.alexp.composion.domain.entity.GameResult
import com.alexp.composion.domain.entity.GameSettings
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.presentationT20Widget.GameViewModel
import ru.sumin.composition.presentation.ChooseLevelFragment

class GameFragment : Fragment() {

    private var  _binding : FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding?: throw RuntimeException("GameFragment is null")

    private lateinit var level: Level
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[GameViewModel::class.java]
    }



    private val tvOptions by lazy{
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
        _binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startGame(level)
        setClickListenersToOptions()
        observerViewModel()
    }

    private fun setClickListenersToOptions()
    {
        for(tvOption in tvOptions)
        {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }
    private fun observerViewModel()
    {
        
        viewModel.question.observe(viewLifecycleOwner){
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()

            Log.d("TvOptions",it.options.toString())

            for(i in 0 until tvOptions.size) {
                tvOptions[i].text = it.options[i].toString()
            }

        }

        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it,true)
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
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner)
        {
            launchGameFinishedFragment(it)
        }
        viewModel.progressAnswers.observe(viewLifecycleOwner)
        {
             binding.tvAnswersProgress.text = it

        }
    }

    private fun getColorIdByState(goodState:Boolean): Int
    {
        val colorResId = if(goodState)
        {
            android.R.color.holo_green_light
        }
        else{
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(),colorResId)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs()
    {
        requireArguments().getParcelable(KEY_LEVEL,Level::class.java)?.let {
            level = it
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult)
    {
        requireActivity().supportFragmentManager.popBackStack()

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()

    }
    companion object
    {
        private const val KEY_LEVEL = "level"
        fun newIntent(level: Level):GameFragment
        {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL,level)
                }
            }
        }

    }


}
