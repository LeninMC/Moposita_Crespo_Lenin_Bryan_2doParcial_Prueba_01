package com.nadershamma.apps.eventhandlers;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nadershamma.apps.androidfunwithflags.MCLBMainActivityFragment;
import com.nadershamma.apps.androidfunwithflags.R;
import com.nadershamma.apps.androidfunwithflags.MCLBResultsDialogFragment;
import com.nadershamma.apps.lifecyclehelpers.MCLBQuizViewModel;

public class MCLBGuessButtonListener implements OnClickListener {
    private MCLBMainActivityFragment mainActivityFragment;
    private Handler handler;

    public MCLBGuessButtonListener(MCLBMainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
        this.handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        String answer = this.mainActivityFragment.getQuizViewModel().getCorrectCountryName();
        this.mainActivityFragment.getQuizViewModel().setTotalGuesses(1);

        if (guess.equals(answer)) {
            this.mainActivityFragment.getQuizViewModel().setCorrectAnswers(1);
            this.mainActivityFragment.getAnswerTextView().setText(answer + "!");
            this.mainActivityFragment.getAnswerTextView().setTextColor(
                    this.mainActivityFragment.getResources().getColor(R.color.correct_answer));

            this.mainActivityFragment.disableButtons();

            if (this.mainActivityFragment.getQuizViewModel().getCorrectAnswers()
                    == MCLBQuizViewModel.getFlagsInQuiz()) {
                MCLBResultsDialogFragment quizResults = new MCLBResultsDialogFragment();
                quizResults.setCancelable(false);
                try {
                    quizResults.show(this.mainActivityFragment.getChildFragmentManager(), "Quiz Results");
                } catch (NullPointerException e) {
                    Log.e(MCLBQuizViewModel.getTag(),
                            "GuessButtonListener: this.mainActivityFragment.getFragmentManager() " +
                                    "returned null",
                            e);
                }
            } else {
                this.handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainActivityFragment.animate(true);
                            }
                        }, 2000);
            }
        } else {
            this.mainActivityFragment.incorrectAnswerAnimation();
            guessButton.setEnabled(false);
        }
    }
}