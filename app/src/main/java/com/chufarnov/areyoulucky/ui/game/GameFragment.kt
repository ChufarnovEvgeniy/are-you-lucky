package com.chufarnov.areyoulucky.ui.game

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chufarnov.areyoulucky.Impls.OPEN_CELL_ABILITY_COST
import com.chufarnov.areyoulucky.Impls.RESTORE_HEART_ABILITY_COST
import com.chufarnov.areyoulucky.domain.entities.AbilityType
import com.chufarnov.areyoulucky.ui.launcher.LauncherFragment
import com.example.areyoulucky.R
import com.example.areyoulucky.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    companion object {
        private const val IS_NEW_GAME = "is new game"

        fun getInstance(isNewGame: Boolean): GameFragment = GameFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_NEW_GAME, isNewGame)
            }
        }
    }

    interface Contract {
        fun toScoreScreen(score: Int)
    }

    private val binding: FragmentGameBinding by viewBinding(FragmentGameBinding::bind)
    private val viewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVariables()

        if (savedInstanceState == null) {
            viewModel.onArgumentsExtracted(arguments?.getBoolean(IS_NEW_GAME) ?: true)
        }

        binding.gameMapView.setOnCellClickListener(viewModel::onCellClicked)

        binding.openCellAbilityLayout.abilityLayout.setOnClickListener {
            viewModel.onAbilityClicked(AbilityType.OPEN_CELL)
        }

        binding.restoreHeartAbilityLayout.abilityLayout.setOnClickListener {
            viewModel.onAbilityClicked(AbilityType.RESTORE_HEALTH)
        }

        viewModel.level.observe(viewLifecycleOwner, {
            binding.levelTextView.text = it.toString()
        })

        viewModel.hearts.observe(viewLifecycleOwner, {
            binding.heartsTextView.text = it.toString()
        })

        viewModel.energy.observe(viewLifecycleOwner, {
            binding.energyTextView.text = it.toString()
        })

        viewModel.moves.observe(viewLifecycleOwner, {
            binding.movesTextView.text = it.toString()
        })

        viewModel.map.observe(viewLifecycleOwner, {
            binding.gameMapView.drawNewLevelMap(it)
        })

        viewModel.openCellEvent.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.gameMapView.openCell(it)
                viewModel.onOpenCellComplete()
            }
        })

        viewModel.isMapClicksEnabled.observe(viewLifecycleOwner, {
            binding.gameMapView.isClicksEnabled = it
            binding.openCellAbilityLayout.abilityLayout.isClickable = it
            binding.restoreHeartAbilityLayout.abilityLayout.isClickable = it
        })

        viewModel.gameFinishEvent.observe(viewLifecycleOwner, {
            if (it != null) {
                (requireActivity() as Contract).toScoreScreen(it)
                viewModel.onGameComplete()
            }
        })
    }

    private fun initVariables() {
        binding.openCellAbilityCostTextView.text =
            resources.getString(R.string.ability_cost, OPEN_CELL_ABILITY_COST)
        binding.restoreHeartCostTextView.text =
            resources.getString(R.string.ability_cost, RESTORE_HEART_ABILITY_COST)
    }

    override fun onStop() {
        viewModel.onViewStop()
        super.onStop()
    }

    override fun onDestroyView() {
        viewModel.onViewDestroy()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireActivity() !is LauncherFragment.Contract) {
            throw IllegalStateException("Activity must implement GameFragment.Contract")
        }
    }
}