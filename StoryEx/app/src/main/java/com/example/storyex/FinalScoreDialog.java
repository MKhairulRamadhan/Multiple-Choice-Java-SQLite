package com.example.storyex;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalScoreDialog {

    private Context mContext;
    private Dialog finalScoreDialog;

    private TextView textViewFinalScore;
    public FinalScoreDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void finalScoreDialog(int correctAns, int wrongAns, int totalSizeofQuiz){

        finalScoreDialog = new Dialog(mContext);
        finalScoreDialog.setContentView(R.layout.final_score_dialog);

        final Button btnFinalScore = (Button) finalScoreDialog.findViewById(R.id.btn_final_score_dialog);

        finalScoreCal(correctAns, wrongAns, totalSizeofQuiz);

        btnFinalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalScoreDialog.dismiss();

                Intent intent = new Intent(mContext, QuizActivity.class);
                mContext.startActivity(intent);
            }
        });

        finalScoreDialog.show();
        finalScoreDialog.setCancelable(false);
        finalScoreDialog.setCanceledOnTouchOutside(false);

    }

    public void finalScoreCal(int correctAns, int wrongAns, int totalSizeofQuiz) {
        int tempScore;
        textViewFinalScore = (TextView) finalScoreDialog.findViewById(R.id.text_final_score);

        if (correctAns == totalSizeofQuiz){
            tempScore = (correctAns * 20) - (wrongAns * 5);
            textViewFinalScore.setText("Final Score: " + String.valueOf(tempScore));

        }else if(wrongAns == totalSizeofQuiz){
            tempScore = 0;
            textViewFinalScore.setText("Final Score; " + String.valueOf(tempScore));

        }else if(correctAns > wrongAns){
            tempScore = (correctAns * 20) - (wrongAns * 5);
            textViewFinalScore.setText("Final Score: " + String.valueOf(tempScore));
        }else if(wrongAns > correctAns){
            tempScore = (correctAns * 20) - (wrongAns * 5);
            textViewFinalScore.setText("Final Score: " + String.valueOf(tempScore));
        }else if(correctAns == wrongAns){
            tempScore = (correctAns * 20) - (wrongAns * 5);
            textViewFinalScore.setText("Final Score: " + String.valueOf(tempScore));
        }
    }

}
