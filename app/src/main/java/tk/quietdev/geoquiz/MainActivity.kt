package tk.quietdev.geoquiz

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import tk.quietdev.geoquiz.databinding.ActivityMainBinding
import androidx.activity.viewModels

// TODO: 03/11/2020
// TODO: Challenge: Graded Quiz
private const val KEY_INDEX = "index"
private const val KEY_ANS_ARRAY = "answeredArr"
private const val KEY_IS_RIGHT_ARRAY = "isRightArray"
private const val KEY_RIGHT_ANS_COUNT = "rightAnsCount"

class MainActivity : AppCompatActivity() {

    private val version = "V 2"
    private lateinit var binding : ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.apply {
            putInt(KEY_INDEX,  quizViewModel.currentIndex)
            putBooleanArray(KEY_ANS_ARRAY, quizViewModel.answeredQuestionArray)
            putBooleanArray(KEY_IS_RIGHT_ARRAY, quizViewModel.answeredQuestionIsCorrectArray)
            putInt(KEY_RIGHT_ANS_COUNT, quizViewModel.rightAns)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.rightAns = savedInstanceState?.getInt(KEY_RIGHT_ANS_COUNT,0) ?: 0
        if (savedInstanceState != null) {
            quizViewModel.answeredQuestionArray = savedInstanceState.getBooleanArray(KEY_ANS_ARRAY)!!
            quizViewModel.answeredQuestionIsCorrectArray = savedInstanceState.getBooleanArray(KEY_IS_RIGHT_ARRAY)!!
        }
        setContentView(binding.root)
        binding.tvVersion.text = version

        binding.trueButton.setOnClickListener {
            Toast.makeText(this, quizViewModel.checkAnswer(true), Toast.LENGTH_SHORT).show()
        }

        binding.falseButton.setOnClickListener {
            Toast.makeText(this, quizViewModel.checkAnswer(false), Toast.LENGTH_SHORT).show()
        }

        binding.nextButton.setOnClickListener {
            updateView("next")
        }

        binding.prevButton.setOnClickListener {
            updateView("prev")
        }

        binding.questionTextView.setText(quizViewModel.updateQuestion())

    }

    private fun updateView(string: String) {
        binding.questionTextView.setText(quizViewModel.updateIndex(string))
        if (quizViewModel.allComplete()) {
            Toast.makeText(this, "COMPLETE with ${quizViewModel.percentage}%", Toast.LENGTH_SHORT).show()
        }
    }



}