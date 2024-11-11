package com.alexp.composion.domain.usecases

import com.alexp.composion.domain.entity.GameSettings
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val gameRepository: GameRepository
)
{

    operator fun invoke(level: Level) : GameSettings
    {
        return gameRepository.getGameSettings(level)

    }

}