package com.alexp.composion.domain.repository

import com.alexp.composion.domain.entity.GameSettings
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings

}