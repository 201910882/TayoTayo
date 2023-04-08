package com.example.TayoTayo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private TextToSpeech mTts = null;
    private int mQueue = TextToSpeech.QUEUE_ADD;

    ArrayList<String> text;

    boolean B = false;

    public void speak(String text, int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(mTts != null)
                mTts.speak(text, mQueue, null, ""+resId);
        }else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ""+resId);
            if(mTts != null)
                mTts.speak(text, mQueue, map);
        }
    }

    private void StartSpeak(final String data) {
        mTts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int initStatus) {
                if (initStatus == TextToSpeech.SUCCESS) {
                    speak(data, 0);
                }
                else if (initStatus == TextToSpeech.ERROR) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTts != null) {
            if(mTts.isSpeaking()) {
                mTts.stop();
            }
        }
    }

    public Handler mHandler = new Handler();

    private Runnable mMyTask = new Runnable() {
        @Override
        public void run() {
            if(B) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_CODE);
            }

        }
    };

    private Runnable mMyTask_exit = new Runnable() {
        @Override
        public void run() {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    };

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartSpeak("타요타요 앱에 오신것을 환영합니다. 시각장애인이시라면 안내가 종료된 후에 네 라고 말해주시고, 비시각장애인이시라면 아래에 해당하는 버튼을 눌러주세요.");

        B = true;
        mHandler.postDelayed(mMyTask, 13000);

        Button busDriver = (Button) findViewById(R.id.select_bus);
        busDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    B = false;
                    onPause();
                    Intent intent = new Intent(getApplicationContext(), busDriverJoin.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결이 원활하지 않습니다. 연결 후, 재시도 해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button passenger = (Button) findViewById(R.id.select_visible);
        passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                B = false;
                onPause();
                Intent intent = new Intent(getApplicationContext(), visiblePassengerJoin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(text.toString().contains("네") || text.toString().contains("예")) {
                if(isConnected()) {
                    Intent intent = new Intent(getApplicationContext(), blindPassengerJoin.class);
                    startActivity(intent);
                }
                else {
                    StartSpeak("인터넷 연결이 원활하지 않아 앱을 종료합니다. 연결 후, 앱을 재실행 해주세요.");
                    mHandler.postDelayed(mMyTask_exit, 7000);
                }
            }
            else {
                StartSpeak("잘못된 입력입니다. 시각장애인이시라면 안내가 종료된 후, 네 라고 다시 말씀해주시고 아니시라면 음성인식 기능을 종료시킨 후, 본인에게 해당하는 버튼을 눌러주세요.");
                mHandler.postDelayed(mMyTask, 14000);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}