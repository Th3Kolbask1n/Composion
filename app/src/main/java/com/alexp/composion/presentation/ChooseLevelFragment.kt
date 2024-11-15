package ru.sumin.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexp.composion.R
import com.alexp.composion.databinding.FragmentChooseLevelBinding
import com.alexp.composion.databinding.FragmentGameBinding
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.presentation.GameFragment

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding : FragmentChooseLevelBinding
        get() = _binding?: throw RuntimeException("FragmentChooseLevelBinding is null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

    }

    fun setListeners() {
        with(binding)
        {


            buttonLevelTest.setOnClickListener {
                launhGameFragment(Level.TEST)
            }
            buttonLevelEasy.setOnClickListener {
                launhGameFragment(Level.EASY)

            }
            buttonLevelNormal.setOnClickListener {
                launhGameFragment(Level.NORMAL)

            }
            buttonLevelHard.setOnClickListener {
                launhGameFragment(Level.HARD)

            }
        }
    }

    private fun launhGameFragment(level: Level)
    {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, GameFragment.newIntent(level))
            .addToBackStack(null)
            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object
    {
        const val NAME = "ChooseLevelFragment"
        fun newInstance():ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }


}
