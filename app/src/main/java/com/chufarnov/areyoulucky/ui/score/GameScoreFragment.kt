package com.chufarnov.areyoulucky.ui.score

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chufarnov.areyoulucky.ui.launcher.LauncherFragment
import com.example.areyoulucky.R
import com.example.areyoulucky.databinding.FragmentGameScoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameScoreFragment : Fragment(R.layout.fragment_game_score) {

    interface Contract {
        fun launchAnotherGame()
    }

    companion object {
        private const val SCORE_KEY = "score"

        fun getInstance(score: Int) = GameScoreFragment().apply {
            arguments = Bundle().apply {
                putInt(SCORE_KEY, score)
            }
        }
    }

    private val binding: FragmentGameScoreBinding by viewBinding(FragmentGameScoreBinding::bind)
    private var score: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        score = arguments?.getInt(SCORE_KEY)

        score?.let {
            binding.scoreTextView.text = resources.getString(R.string.score, it)
        }

        binding.tryAgainImageView.setOnClickListener {
            (requireActivity() as Contract).launchAnotherGame()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireActivity() !is LauncherFragment.Contract) {
            throw IllegalStateException("Activity must implement GameScoreFragment.Contract")
        }
    }
}