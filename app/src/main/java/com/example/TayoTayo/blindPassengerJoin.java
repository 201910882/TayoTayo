package com.example.TayoTayo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class blindPassengerJoin extends Activity{

    EditText bname, bresidentNumber, brank;

    private TextToSpeech mTts = null;
    private int mQueue = TextToSpeech.QUEUE_ADD;

    ArrayList<String> text;

    String bjoinName=null, bjoinResiNum=null, bjoinRank=null;

    public Handler mHandler = new Handler();

    private static final int NAME_CODE = 1000;
    private static final int NAME_OK_CODE = 1001;
    private static final int NUM_CODE = 2000;
    private static final int NUMOK_CODE = 2001;
    private static final int RANK_CODE = 3000;
    private static final int RANKOK_CODE = 3001;

    String _name, _resinum, _rank;

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

    private Runnable mMyTask_name = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, NAME_CODE);
        }
    };

    private Runnable mMyTask_nameOK = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, NAME_OK_CODE);
        }
    };

    private Runnable mMyTask_num = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, NUM_CODE);
        }
    };

    private Runnable mMyTask_numOK = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, NUMOK_CODE);
        }
    };

    private Runnable mMyTask_rank = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, RANK_CODE);
        }
    };

    private Runnable mMyTask_rankOK = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, RANKOK_CODE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindpassenger_join);

        bname = (EditText)findViewById(R.id.name_b);
        bresidentNumber = (EditText)findViewById(R.id.idnum_b);
        brank = (EditText)findViewById(R.id.rank_b);

        SharedPreferences bauto = getSharedPreferences("bauto", Activity.MODE_PRIVATE);
        bjoinName = bauto.getString("binputName",null);
        bjoinResiNum = bauto.getString("binputResiNum",null);
        bjoinRank = bauto.getString("binputRank",null);

        if(bjoinName !=null && bjoinResiNum != null && bjoinRank != null) {
            if(     (bjoinName.equals("김가영") && bjoinResiNum.equals("9901012123456") && bjoinRank.equals("중증")) ||
                    (bjoinName.equals("이나연") && bjoinResiNum.equals("8506062123451") && bjoinRank.equals("경증")) ||
                    (bjoinName.equals("최가현") && bjoinResiNum.equals("6708082123453") && bjoinRank.equals("중증")) ){
                Intent intent = new Intent(getApplicationContext(), blindPassengerInputBusInfo.class);
                intent.putExtra("blindName", bjoinName);
                startActivity(intent);
                finish();
            }
        }
        else if(bjoinName == null && bjoinResiNum == null && bjoinRank == null){
            StartSpeak("장애인등록증에 적혀있는 정보를 입력하는 화면입니다. 먼저 이름을 말해주세요.");
            mHandler.postDelayed(mMyTask_name, 7000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == NAME_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                _name = text.get(0);
                speak("이름이 " + text.get(0) + "이시면 네, 아니라면 아니오 라고 말씀해주세요", 0);
                mHandler.postDelayed(mMyTask_nameOK, 5000);
            }
            else if (requestCode == NAME_OK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).toString().contains("네") || text.get(0).toString().contains("예")) {
                    bname.setText(_name);
                    speak("주민번호를 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_num, 2500);
                }
                else if(text.get(0).toString().contains("아니")) {
                    speak("이름을 다시 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_name, 2200);
                }
                else {
                    speak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.", 0);
                    mHandler.postDelayed(mMyTask_nameOK, 5000);
                }
            }
            else if (requestCode == NUM_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                _resinum = text.get(0).replaceAll(" ", "");
                speak("주민번호가 ", 0);
                for(int i = 0; i < text.get(0).length(); i++) {
                    speak(Character.toString(text.get(0).charAt(i)), 0);
                }
                speak("이시면 네, 아니라면 아니오 라고 말씀해주세요", 0);
                mHandler.postDelayed(mMyTask_numOK, 10000);
            }
            else if (requestCode == NUMOK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).toString().contains("네") || text.get(0).toString().contains("예")) {
                    bresidentNumber.setText(_resinum);
                    speak("장애정도를 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_rank, 2500);
                }
                else if(text.get(0).toString().contains("아니")) {
                    speak("주민번호를 다시 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_num, 2500);
                }
                else {
                    speak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.", 0);
                    mHandler.postDelayed(mMyTask_numOK, 10000);
                }
            }
            else if (requestCode == RANK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).contains("1") || text.get(0).contains("2") || text.get(0).contains("3"))
                    _rank = "중증";
                else if(text.get(0).contains("4") || text.get(0).contains("5") || text.get(0).contains("6"))
                    _rank = "경증";
                else
                    _rank = text.get(0);
                speak("장애정도가 " + text.get(0) + "이시면 네, 아니라면 아니오 라고 말씀해주세요", 0);
                mHandler.postDelayed(mMyTask_rankOK, 6000);
            }
            else if (requestCode == RANKOK_CODE) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).toString().contains("네") || text.get(0).toString().contains("예")) {
                    brank.setText(_rank);
                    if( (_name.equals("김가영") && _resinum.equals("9901012123456") && _rank.equals("중증")) ||
                        (_name.equals("이나연") && _resinum.equals("8506062123451") && _rank.equals("경증")) ||
                        (_name.equals("최가현") && _resinum.equals("6708082123453") && _rank.equals("중증")) ) {
                        SharedPreferences bauto = getSharedPreferences("bauto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor bautoLogin = bauto.edit();
                        bautoLogin.putString("binputName", _name);
                        bautoLogin.putString("binputResiNum", _resinum);
                        bautoLogin.putString("binputRank", _rank);
                        bautoLogin.commit();

                        Intent intent = new Intent(getApplicationContext(), blindPassengerInputBusInfo.class);
                        intent.putExtra("blindName", bjoinName);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        speak("존재하지 않는 정보로 앱을 종료합니다.", 0);
                        mHandler.postDelayed(mMyTask_exit, 3000);
                    }
                }
                else if(text.get(0).toString().contains("아니")) {
                    speak("장애정도를 다시 말씀해주세요", 0);
                    mHandler.postDelayed(mMyTask_rank, 2500);
                }
                else {
                    speak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.", 0);
                    mHandler.postDelayed(mMyTask_rankOK, 6000);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}