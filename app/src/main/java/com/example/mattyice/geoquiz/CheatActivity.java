package com.example.mattyice.geoquiz;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

//An activity must extend an activity class
public class CheatActivity extends ActionBarActivity {

    //locations to store information for values to be sent back to the called activity
    public static final String EXTRA_ANSWER_IS_TRUE = "com.example.mattyice.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.example.mattyice.geoquiz.answer_shown";
    //This is for local use of information to be stored when changing activities (land to vert)
    public static final String CHEAT = "cheat";
    private boolean mAnswerIsTrue;
    private boolean mIsCheater = false;
    private TextView mAnswerTextView;
    private Button mShowAnswer;

    public void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        //Putting info into the location where it needs to be stored
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        //setting the result to be returned to the original activity (howTheResultWent, infoBeingSentBack)
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat); //What xml file should this be working with

        //Potentially stored info
        if (savedInstanceState != null){
            mIsCheater = savedInstanceState.getBoolean(CHEAT, false);
        }

        if (mIsCheater)
        {
            setAnswerShownResult(true);
        }
        else
        {
            setAnswerShownResult(false);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
        mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
                mIsCheater = true;
            }
        });

    }

    @Override
    //used to keep info when the activity layout from land to verticle
    public void onSaveInstanceState (Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(CHEAT, mIsCheater);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_cheat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
