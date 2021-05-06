package br.com.glrmeslp.github.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var score: Int = 0
    private lateinit var clickMeButton: Button
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView

    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_TIME = "TIME_LEFT_TIME"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"onCreate called. Score is: $score")

        initializationButtonAndTextViews()
        resetGame()
        clickMeButton.setOnClickListener {
            incrementScore()
        }

        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_TIME)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_TIME,timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG,"onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy called.")
    }

    private fun initializationButtonAndTextViews() {
        clickMeButton = findViewById(R.id.buttonClickMe)
        gameScoreTextView = findViewById(R.id.tv_your_score)
        timeLeftTextView = findViewById(R.id.tv_time_left)
    }

    private fun configureCountDownTimer(countDown: Long) {
        countDownTimer = object : CountDownTimer(countDown, countDownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }
        }
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        score += 1
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        score = 0
        setTextView(initialCountDown)
        configureCountDownTimer(initialCountDown)
        gameStarted = false
    }

    private fun restoreGame() {
        setTextView(timeLeftOnTimer)
        configureCountDownTimer(timeLeftOnTimer)
        startGame()
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun setTextView(countDown: Long){
        gameScoreTextView.text = getString(R.string.yourScore, score)

        val timeLeft = countDown / 1000
        timeLeftTextView.text = getString(R.string.yourScore,timeLeft)
    }

}
