package com.example.mattyice.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;

import org.w3c.dom.Text;

//The "Main Class" Of an activity has to extend an activity class and also be included in the manifest
public class QuizActivity extends ActionBarActivity {

    private static final String TAG = "QuizActivity"; //Used in logging to show what method is called in what file (or activity)
    private static final String KEY_INDEX = "index"; //Used to maintain data when switching from horizontal to verticle (which creates a new activity)
    private static final String CHEAT = "cheat"; //Same as above
    private Button mTrueButton; //Variables that will be needed throughout this activity (generally just references to objects in the xml file)
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mApiIdentifier;
    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true),
    };
    private boolean[] mIsCheater = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;

    @TargetApi(11) //This tells java lint that it should be looking at this code as API version 11 code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Creates this activity and calls any saved data in the bundle which is acquired below
        Log.d(TAG, "onCreate(Bundle) called"); //Gives the log the tag info and what happened making this easier to find in debugging
        setContentView(R.layout.activity_quiz); //Sets what xml layout should be run with this java file

        //A condition in which is checks if the running version is compatible with some features
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //ActionBar actionBar = getActionBar();
            //actionBar.setSubtitle("Bodies of Water");
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void onClick(View v) {
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               updateQuestion();
           }
        });
        
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The intent is used to jump from one activity to another. (this, targetClass.class)
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                //The putExtra function adds on info that could be needed in the next activity. (pathOfStoredInfo, value)
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                //startActivity(i) - starts an activity with no expectation of a result value
                //startActivityForResult(i, 0) - starts an activity looking for a result value, the 0 is which activity to return back to then
                startActivityForResult(i, 0);
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if (mCurrentIndex < 0){
                    mCurrentIndex = 0;
                }
                updateQuestion();
            }
        });

        mApiIdentifier = (TextView)findViewById(R.id.api_identifier);
        mApiIdentifier.setText("Api level " + Integer.toString(Build.VERSION.SDK_INT));

        //Checks  to see if there is any saved info that needs to be checked
        if (savedInstanceState != null){
            //This is how to grab info out of the location in which its being stored when the activity changes (location, defaultValue)
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater[mCurrentIndex] = savedInstanceState.getBoolean(CHEAT, false);
        }

        updateQuestion();
    }

    @Override
    //Used to save info when converting from 1 activity to another (such as going from verticle to horizontal)
    public void onSaveInstanceState (Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //How to put info on this bundle to be used (infoLocation, variableToStoreInfo)
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEAT, mIsCheater[mCurrentIndex]);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    //Called when a different activity returns to this activity (WhosRecievingIt, WhosReturningIt, intentWithData)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data == null) {
            return;
        }
        //Getting info from the extra sent from the other activity (locationOfData, defaultValue)
        mIsCheater[mCurrentIndex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId = 0;

        if (mIsCheater[mCurrentIndex]){
            messageResId = R.string.judgment_toast;
        }
        else if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        //A toast is a little message that appears (whatDisplaysMessage, whatMessage, something...)
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
