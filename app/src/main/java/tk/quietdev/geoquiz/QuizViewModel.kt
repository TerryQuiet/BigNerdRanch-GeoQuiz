package tk.quietdev.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var percentage = 0
    var rightAns = 0
    var timesCheated = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

     var currentIndex = 0

     var answeredQuestionArray = BooleanArray(questionBank.size)
     var answeredQuestionIsCorrectArray = BooleanArray(questionBank.size)
     var answeredQuestionIsCheatedArray = BooleanArray(questionBank.size)

    override fun onCleared() {
        super.onCleared()
    }


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    /*val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId*/

    fun setCheated () {
        answeredQuestionIsCheatedArray[currentIndex] = true
        timesCheated++
    }


     fun updateIndex(string: String) : Int {

             when (string) {
                 "next" ->  currentIndex = (currentIndex + 1) % questionBank.size
                 "prev" -> {
                     if ( currentIndex == 0 ) {
                         currentIndex = questionBank.size - 1
                     } else currentIndex--
                 }
             }
            return updateQuestion()

    }

     fun updateQuestion() : Int {
         return questionBank[currentIndex].textResId
    }

    fun checkAnswer(userAnswer: Boolean) : Int {
        val correctAnswer = questionBank[currentIndex].answer
        if (answeredQuestionIsCheatedArray[currentIndex])
            return R.string.judgment_toast
       return when (answeredQuestionArray[currentIndex]) {
           true -> R.string.question_answered_toast
           false -> {
               answeredQuestionArray[currentIndex] = true
               return if (userAnswer == correctAnswer) {
                   answeredQuestionIsCorrectArray[currentIndex] = true
                   rightAns++
                   R.string.correct_toast
               } else {
                   R.string.incorrect_toast
               }
           }
       }
    }

    fun allComplete() : Boolean {
        return if (!answeredQuestionArray.contains(false)) {
            percentage = (100 / questionBank.size.toDouble() * rightAns).toInt()
            true
        } else
            false
    }
}