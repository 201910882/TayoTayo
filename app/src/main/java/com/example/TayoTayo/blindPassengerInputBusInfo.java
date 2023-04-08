package com.example.TayoTayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class blindPassengerInputBusInfo extends Activity {

    private TextToSpeech mTts = null;
    private int mQueue = TextToSpeech.QUEUE_ADD;

    ArrayList<String> text;

    EditText busName, stationName;

    String _bus, _station;

    private static final int BUS_CODE = 1000;
    private static final int BUS_OK_CODE = 1001;
    private static final int STATION_CODE = 2000;
    private static final int STATION_OK_CODE = 2001;

    public Handler mHandler = new Handler();

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

    private Runnable mMyTask_bus = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, BUS_CODE);
        }
    };

    private Runnable mMyTask_busOK = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, BUS_OK_CODE);
        }
    };

    private Runnable mMyTask_station = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, STATION_CODE);
        }
    };

    private Runnable mMyTask_stationOK = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, STATION_OK_CODE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindpassenger_inputbusinfo);

        busName = (EditText)findViewById(R.id.src_bnum_b);
        stationName = (EditText)findViewById(R.id.src_stNm_b);

        busName.setText("");
        stationName.setText("");

        Intent intent = getIntent();
        StartSpeak(intent.getExtras().getString("blindName") + "님 환영합니다. 먼저 탑승할 버스 번호를 말해주세요.");
        mHandler.postDelayed(mMyTask_bus, 5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == BUS_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                _bus = text.get(0).replaceAll(" ", "");
                _bus = _bus.toUpperCase(Locale.ROOT);
                speak("버스번호가 ", 0);
                for(int i = 0; i < text.get(0).length(); i++) {
                    speak(Character.toString(text.get(0).charAt(i)), 0);
                }
                speak("이시면 네, 아니라면 아니오 라고 말씀해주세요", 0);
                mHandler.postDelayed(mMyTask_busOK, 7000);
            }
            else if (requestCode == BUS_OK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).toString().contains("네") || text.get(0).toString().contains("예")) {
                    busName.setText(_bus);
                    speak("탑승할 정류장의 번호를 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_station, 3500);
                }
                else if(text.get(0).toString().contains("아니")) {
                    speak("버스 번호를 다시 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_bus, 2200);
                }
                else {
                    speak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.", 0);
                    mHandler.postDelayed(mMyTask_busOK, 6000);
                }
            }
            else if (requestCode == STATION_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                _station = text.get(0).replaceAll(" ", "");
                speak("정류장번호가 ", 0);
                for(int i = 0; i < text.get(0).length(); i++) {
                    speak(Character.toString(text.get(0).charAt(i)), 0);
                }
                speak("이시면 네, 아니라면 아니오 라고 말씀해주세요", 0);
                mHandler.postDelayed(mMyTask_stationOK, 7000);
            }
            else if (requestCode == STATION_OK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).toString().contains("네") || text.get(0).toString().contains("예")) {
                    if(_station.length() == 4)
                        _station = "0".concat(_station);
                    stationName.setText(_station);
                    String resultSN = stationName.getText().toString();
                    String resultBN = busName.getText().toString();
                    if(resultSN.equals("01133")) {
                        if( resultBN.equals("153") || resultBN.equals("1020") || resultBN.equals("1711") || resultBN.equals("7022") || resultBN.equals("7212") || resultBN.equals("7730") || resultBN   .equals("110B국민대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01134")) {
                        if( resultBN.equals("153") || resultBN.equals("7730") || resultBN.equals("110B국민대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01143")) {
                        if( resultBN.equals("153") || resultBN.equals("1020") || resultBN.equals("7730") || resultBN.equals("110A고려대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01144")) {
                        if( resultBN.equals("153") || resultBN.equals("1020") || resultBN.equals("1711") || resultBN.equals("7022") || resultBN.equals("7212") || resultBN.equals("7730") || resultBN.equals("110A고려대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01142") || resultSN.equals("01287")) {
                        if( resultBN.equals("1020") || resultBN.equals("1711") || resultBN.equals("7022") || resultBN.equals("7212") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01135")) {
                        if( resultBN.equals("7016") || resultBN.equals("7018") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(resultSN.equals("01286")) {
                        if( resultBN.equals("7016") || resultBN.equals("7018") || resultBN.equals("8002") )
                            nextPage();
                        else
                            setClear();
                    }
                    else
                        setClear();
                }
                else if(text.get(0).toString().contains("아니")) {
                    speak("정류장번호를 다시 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_station, 2500);
                }
                else {
                    speak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.", 0);
                    mHandler.postDelayed(mMyTask_stationOK, 6000);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setClear() {
        StartSpeak("버스번호 혹은 정류장 번호가 잘못되었습니다. 탑승할 버스 번호를 다시 말해주세요.");
        mHandler.postDelayed(mMyTask_bus, 7000);
        busName.setText("");
        stationName.setText("");
    }

    public void nextPage() {
        Intent intent = new Intent(getApplicationContext(), blindPassengerSearchBusInfo.class);
        intent.putExtra("bbusNum", busName.getText().toString());
        intent.putExtra("bstationNum", stationName.getText().toString());
        finish();
        startActivity(intent);
    }
}