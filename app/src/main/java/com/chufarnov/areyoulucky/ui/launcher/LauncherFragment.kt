package com.chufarnov.areyoulucky.ui.launcher

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.areyoulucky.R
import com.example.areyoulucky.databinding.FragmentLauncherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherFragment : Fragment(R.layout.fragment_launcher) {

    interface Contract {
        fun launchNewGame()
        fun resumeGame()
    }

    private val binding: FragmentLauncherBinding by viewBinding(FragmentLauncherBinding::bind)
    private val viewModel: LauncherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startNewImageView.setOnClickListener {
            viewModel.onNewGameClicked()
        }

        binding.resumeGameTextView.setOnClickListener {
            viewModel.onResumeGameClicked()
        }

        viewModel.isGameCached.observe(viewLifecycleOwner, {
            if (it) {
                binding.resumeGameTextView.isVisible = true
            }
        })

        viewModel.onNewGameClickedEvent.observe(viewLifecycleOwner, {
            if (it != null) {
                startNewGame()
                viewModel.onNewGameClickedFinished()
            }
        })

        viewModel.onResumeGameClickedEvent.observe(viewLifecycleOwner, {
            if (it != null) {
                resumeGame()
                viewModel.onResumeGameClickedFinished()
            }
        })
    }

    private fun startNewGame() {
        (requireActivity() as Contract).launchNewGame()
    }

    private fun resumeGame() {
        (requireActivity() as Contract).resumeGame()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireActivity() !is Contract) {
            throw IllegalStateException("Activity must implement LauncherFragment.Contract")
        }
    }
}