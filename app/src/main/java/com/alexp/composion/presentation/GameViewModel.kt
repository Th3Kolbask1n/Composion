package com.alexp.composion.presentationT20Widget

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexp.composion.R
import com.alexp.composion.data.GameRepositoryImpl
import com.alexp.composion.domain.entity.GameResult
import com.alexp.composion.domain.entity.GameSettings
import com.alexp.composion.domain.entity.Level
import com.alexp.composion.domain.entity.Question
import com.alexp.composion.domain.usecases.GenerateQuestionUseCase
import com.alexp.composion.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level:Level
) : ViewModel() {

    private lateinit var gameSettings: GameSettings
//    private lateinit var level: Level

//    private val context = application
    private val repository = GameRepositoryImpl
    private val ganerateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughtCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughtCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughtCountOfRightAnswers

    private val _enoughtPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughtPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughtPercentOfRightAnswers


    private val _minPercent = MutableLiveData<Int>()
    val minPercent:LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult:LiveData<GameResult>
        get() = _gameResult

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, 1) {
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }
        }

        timer?.start()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()

        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughtCountOfRightAnswers.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughtPercentOfRightAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if(countOfQuestions==0) return 0
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun generateQuestion() {
        _question.value = ganerateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(number: Int) {

        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer

        if (number == rightAnswer)
            countOfRightAnswers++

        countOfQuestions++
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun finishGame() {
        Log.d("Finished","222");
        _gameResult.value = GameResult(
            enoughtCountOfRightAnswers.value == true
                    && enoughtPercentOfRightAnswers.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)

        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60

    }
}