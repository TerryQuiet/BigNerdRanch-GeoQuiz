package tk.quietdev.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import tk.quietdev.geoquiz.databinding.ActivityMainBinding
import androidx.activity.viewModels

// TODO: 03/11/2020
// TODO: Challenge: Graded Quiz
private const val TAG = "MainActivityTag"
private const val KEY_INDEX = "index"
private const val KEY_ANS_ARRAY = "answeredArr"
private const val KEY_IS_RIGHT_ARRAY = "isRightArray"
private const val KEY_RIGHT_ANS_COUNT = "rightAnsCount"
private const val REQUEST_CODE_CHEAT = 0
private const val KEY_TIMES_CHEATED = "timesCheated"
private const val KEY_CHEATED_ARRAY = "cheatedArray"


class MainActivity : AppCompatActivity() {

    private val version = "FINAL"
    private lateinit var binding : ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.apply {
            putInt(KEY_INDEX,  quizViewModel.currentIndex)
            putBooleanArray(KEY_ANS_ARRAY, quizViewModel.answeredQuestionArray)
            putBooleanArray(KEY_IS_RIGHT_ARRAY, quizViewModel.answeredQuestionIsCorrectArray)
            putBooleanArray(KEY_CHEATED_ARRAY, quizViewModel.answeredQuestionIsCheatedArray)
            putInt(KEY_TIMES_CHEATED, quizViewModel.timesCheated)
            putInt(KEY_RIGHT_ANS_COUNT, quizViewModel.rightAns)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.rightAns = savedInstanceState?.getInt(KEY_RIGHT_ANS_COUNT,0) ?: 0
        quizViewModel.timesCheated = savedInstanceState?.getInt(KEY_TIMES_CHEATED,0) ?: 0
        if (savedInstanceState != null) {
            quizViewModel.answeredQuestionArray = savedInstanceState.getBooleanArray(KEY_ANS_ARRAY)!!
            quizViewModel.answeredQuestionIsCorrectArray = savedInstanceState.getBooleanArray(KEY_IS_RIGHT_ARRAY)!!
            quizViewModel.answeredQuestionIsCheatedArray = savedInstanceState.getBooleanArray(KEY_CHEATED_ARRAY)!!
        }

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

        binding.cheatButton.setOnClickListener {
            if (quizViewModel.timesCheated < 3) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            } else
                Toast.makeText(this, "You cheated to many times", Toast.LENGTH_SHORT).show()
        }

    }

   override fun onActivityResult(requestCode: Int,
                         resultCode: Int,
                         data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if  (data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) == true) {
                quizViewModel.setCheated()
            }
        }
    }

    private fun updateView(string: String) {
      //  Log.d(TAG, "Updating view", Exception())
        binding.questionTextView.setText(quizViewModel.updateIndex(string))
        if (quizViewModel.allComplete()) {
            Toast.makeText(this, "COMPLETE with ${quizViewModel.percentage}%", Toast.LENGTH_SHORT).show()
        }
    }

}