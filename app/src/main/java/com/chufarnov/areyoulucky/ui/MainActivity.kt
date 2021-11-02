package com.chufarnov.areyoulucky.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chufarnov.areyoulucky.ui.game.GameFragment
import com.chufarnov.areyoulucky.ui.launcher.LauncherFragment
import com.chufarnov.areyoulucky.ui.score.GameScoreFragment
import com.example.areyoulucky.R
import com.example.areyoulucky.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), LauncherFragment.Contract,
    GameFragment.Contract, GameScoreFragment.Contract {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToFragment(LauncherFragment())
    }

    override fun launchNewGame() {
        supportFragmentManager.beginTransaction()
            .replace(binding.containerLayout.id, GameFragment.getInstance(true))
            .addToBackStack(null)
            .commit()
    }

    override fun resumeGame() {
        supportFragmentManager.beginTransaction()
            .replace(binding.containerLayout.id, GameFragment.getInstance(false))
            .addToBackStack(null)
            .commit()
    }

    override fun launchAnotherGame() {
        navigateToFragment(GameFragment())
    }

    override fun toScoreScreen(score: Int) {
        navigateToFragment(GameScoreFragment.getInstance(score))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0) {
            supportFragmentManager.popBackStack()
            navigateToFragment(LauncherFragment())
        } else {
            super.onBackPressed()
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.containerLayout.id, fragment)
            .commit()
    }
}