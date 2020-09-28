package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviatest.data.AnswerListAsyncResponse;
import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewQuestionText;
    private TextView textViewCountQuestions;
    private Button buttonTrue;
    private Button buttonFalse;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private CardView cardView;

    private int currentQuestionIndex = 0;
    private ArrayList<Question> questionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuestionText = findViewById(R.id.textViewQuestionText);
        textViewCountQuestions = findViewById(R.id.textViewCountQuestions);
        nextButton = findViewById(R.id.imageButtonNext);
        previousButton = findViewById(R.id.imageButtonPrev);
        buttonTrue = findViewById(R.id.buttonTrue);
        buttonFalse = findViewById(R.id.buttonFalse);
        cardView = findViewById(R.id.cardViewQuestion);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);
        buttonFalse.setOnClickListener(this);

        questionArrayList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                updateQuestion();
            }
        });

    }

    private void updateQuestion() {
        textViewQuestionText.setText(questionArrayList.get(currentQuestionIndex).getQuestion().toString());
        textViewCountQuestions.setText(currentQuestionIndex + "/" + (questionArrayList.size() - 1));
    }

    private void changeQuestion(int i) {
        if (i == 1) {
            if (currentQuestionIndex != questionArrayList.size() - 1) {
                currentQuestionIndex++;
            }
        }
        else {
            if (currentQuestionIndex != 0) {
                currentQuestionIndex--;
            }
        }
        updateQuestion();
    }

    private void isTrue(boolean button) {
        boolean answer = questionArrayList.get(currentQuestionIndex).isAnswerTrue();
        if (button == answer) {
            Toast.makeText(this, "You're right", Toast.LENGTH_SHORT).show();
            fadeView();
            changeQuestion(1);
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            shakeAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonPrev:
                changeQuestion(0);
                break;
            case R.id.imageButtonNext:
                changeQuestion(1);
                break;
            case R.id.buttonTrue:
                isTrue(true);
                updateQuestion();
                break;
            case R.id.buttonFalse:
                isTrue(false);
                updateQuestion();
                break;
        }
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardViewQuestion);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
