package com.example.countriesquizapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizPage extends AppCompatActivity {
    TextView questionText;
   // AutoCompleteTextView answerText;
    Spinner answerText ;
    Button next, start;
    int currentIndex ;
    TextView questionNumber ;
    String[] countries = {"Egypt", "USA", "France", "UK"};
    String[] capitals = {"cairo", "ws", "paris", "london"};
    List<String>capitalList;
    byte i, startCount;
    int score;
    MediaPlayer player;
    TextView lastText;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.spinner);
        next = findViewById(R.id.button2);
        start = findViewById(R.id.button);
        lastText = findViewById(R.id.lastScore);
         questionNumber =findViewById(R.id.questionNumber);
        answerText.setEnabled(false);
        next.setEnabled(false);
        String[] allCapitals = getResources().getStringArray(R.array.capitals);
       List<String> temp = Arrays.asList(allCapitals);
       capitalList=new ArrayList<>(temp);
              ArrayAdapter adapter =new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,capitalList);
            //  answerText.setThreshold(1);
              answerText.setAdapter(adapter);
        pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Retrieve the last score from SharedPreferences
        int last = pref.getInt("SCORE", -1);
        if (last != -1) {
            lastText.setText("Your Last Score is " + last);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void start(View view) {
        answerText.setEnabled(true);
        next.setEnabled(true);
        score = 0;
        i = 0;

        questionText.setText("What Is The Capital of " + countries[0]);
       // answerText.setText("");
        startCount++;

        if (startCount > 3) {
            start.setEnabled(false);
            return;
        }
        currentIndex = 1;


        questionNumber.setText("Question " + currentIndex + " of " + countries.length);
    }


    public void next(View view) {
        int selectedItemPosition = answerText.getSelectedItemPosition();

        if (selectedItemPosition == 0) {
            Toast.makeText(this, "Please Choose an answer", Toast.LENGTH_SHORT).show();
            return;
        }


        // Release previous player if playing
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }

        // Check answer
        String answer = answerText.getSelectedItem().toString();
        if (answer.equalsIgnoreCase(capitals[i])) {
            score++;
            player = MediaPlayer.create(this, R.raw.success);
        } else {
            player = MediaPlayer.create(this, R.raw.failure);
        }

        currentIndex++;
        player.start();



        //currentIndex=;
        // Release player after sound completes
        player.setOnCompletionListener(mp -> {
            mp.release();
            player = null;
        });

        i++;
       answerText.setSelection(0);

        // If quiz is not over, show the next question
        if (i < countries.length&& currentIndex<=countries.length) {
            questionNumber.setText("Question " + currentIndex + " of " + countries.length);
            questionText.setText("What Is The Capital of " + countries[i]);

        } else {
            // Show the score in a Toast message only
            Toast.makeText(this, "Your Score Is: " + score, Toast.LENGTH_LONG).show();
            answerText.setEnabled(false);
            next.setEnabled(false);
        }

        Collections.shuffle(capitalList);
        capitalList.remove("Please Select Item") ;
        capitalList.add(0,"Please Select Item ") ;
        answerText.setSelection(0);
    }
}
