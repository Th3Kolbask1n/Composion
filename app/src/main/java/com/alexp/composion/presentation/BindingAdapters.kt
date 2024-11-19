package com.alexp.composion.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.alexp.composion.R
import com.alexp.composion.domain.entity.GameResult


interface OnOptionClickListener{

    fun onOptionClick(option:Int)
}

@BindingAdapter("requiredAnswers")

fun bindRequredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("answerCount")

fun bindAnswerCount(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("requiredAnswersPercent")

fun bindRequiredAnswersPercent(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        count
    )
}

@BindingAdapter("percentRightAnswer")

fun bindPercentRightAnswer(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult)
{
    if (countOfQuestions == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("resultEmoji")

fun bindResultEmoji(imageView: ImageView, winner:Boolean) {
    imageView.setImageResource(getSmileResID(winner))
}

private fun getSmileResID(winner:Boolean):Int{
    return if(winner)
    {
        R.drawable.ic_smile
    }
    else{
        R.drawable.ic_sad
    }
}

@BindingAdapter("enoughCount")

fun bindEnoughCount(textView: TextView, enough:Boolean) {
    textView.setTextColor(getColorIdByState(textView.context, enough))
}

@BindingAdapter("enoughPercent")

fun bindEnoughPercent(progressBar: ProgressBar, enough:Boolean) {
    var color = getColorIdByState(progressBar.context, enough)
    progressBar.progressTintList = ColorStateList.valueOf(color)

}

private fun getColorIdByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")

fun bindNumberAsText(textView: TextView, number:Int)
{
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")

fun bindOnOptionClickListener(textView: TextView,clickListener: OnOptionClickListener)
{
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}