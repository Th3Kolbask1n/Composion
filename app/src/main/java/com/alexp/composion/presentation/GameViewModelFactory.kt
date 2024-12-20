package com.alexp.composion.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.presentationT20Widget.GameViewModel
import java.lang.Appendable

class GameViewModelFactory(
    private val level:Level,
    private val application: Application
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, level) as T
        } else {
            throw RuntimeException("Class is not GameViewModel")
        }
    }

}