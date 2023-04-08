package com.example.TayoTayo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class blindPassengerSearchBusInfo extends AppCompatActivity {

    TextView BStationName, BBusNum, BArrMsg, BReside;

    public String BdataKey = "XAmXR4DKEwPXzhnJLGjnMFa63utc%2FfojlyedYbjjwl%2FoGsESjjfDDk4k48LgMIMYQIu%2Fcap%2BDBXSRkqDWHW7qA%3D%3D";
    private String BrequestUrl;

    public String BstId;

    public String Binput;
    public String BbusNumInput;

    ArrayList<Item> Blist = null;
    Item Bbus = null;

    private static final int REQUEST_CODE = 1234;
    private TextToSpeech mTts = null;
    private int mQueue = TextToSpeech.QUEUE_ADD;
    ArrayList<String> text;

    boolean B = false;

    public Handler mHandler = new Handler();

    public String[][] stringArray = { {"100000048", "01143"},
            {"100000039", "01134"},
            {"100000189", "01287"},
            {"100000040", "01135"},
            {"100000188", "01286"},
            {"100000047", "01142"},
            {"100000038", "01133"},
            {"100000049", "01144"}};

    private DatabaseReference mDatabase;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private Runnable mMyTask_init = new Runnable() {
        @Override
        public void run() {
            if(B) {
                StartSpeak("검색하신 버스 정류장의 이름은 " + Bbus.getStNm() + " 이고, 버스 번호는 " + BbusNumInput + "번 입니다. 탑승예정 버스는 ");
            }
        }
    };


    private Runnable mMyTask_Answer = new Runnable() {
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

    private Runnable mMyTask_End = new Runnable() {
        @Override
        public void run() {
            if(B) {
                finish();
                Intent intent = new Intent(getApplicationContext(), blindPassengerInputBusInfo.class);
                startActivity(intent);
            }
        }
    };

    private Runnable mMyTask_speak = new Runnable() {
        @Override
        public void run() {
            if(B) {
                String arrStr = BArrMsg.getText().toString();
                String resideStr = BReside.getText().toString();
                if(arrStr.contains("막차")) {
                    int index = arrStr.indexOf("후");
                    int len = arrStr.length();
                    StartSpeak("이번 버스는 막차입니다. 현재 " + arrStr.substring(index + 2, len - 1) + "에 위치하고 있으며, " + arrStr.substring(0, index + 1) + "도착 예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 상단을 클릭해주세요.");
                    mHandler.postDelayed(mMyTask_Answer, 27000);
                }
                else if(arrStr.contains("후")) {
                    int index = arrStr.indexOf("[");
                    int len = arrStr.length();
                    StartSpeak(arrStr.substring(index + 1, len - 1) + "에 위치하고 있으며, " + arrStr.substring(0, index) + "도착 예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 이 버스를 타시려면 음성 안내가 종료된 후 네 라고 말씀해주시고, 다른 버스를 검색하시려면 아니오 라고 말씀해주세요.");
                    mHandler.postDelayed(mMyTask_Answer, 26000);
                }
                else if(arrStr.equals("대기")) {
                    StartSpeak(arrStr + "상태입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 이 버스를 타시려면 음성 안내가 종료된 후 네 라고 말씀해주시고, 다른 버스를 검색하시려면 아니오 라고 말씀해주세요.");
                    mHandler.postDelayed(mMyTask_Answer, 20000);
                }
                else if(arrStr.contains("곧 도착")) {
                    StartSpeak(arrStr + "예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 이 버스를 타시려면 음성 안내가 종료된 후 네 라고 말씀해주시고, 다른 버스를 검색하시려면 아니오 라고 말씀해주세요.");
                    mHandler.postDelayed(mMyTask_Answer, 20000);
                }
                else if(arrStr.contains("차고지")) {
                    int len = arrStr.length();
                    StartSpeak(arrStr.substring(1, len - 1) + "상태입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 이 버스를 타시려면 음성 안내가 종료된 후 네 라고 말씀해주시고, 다른 버스를 검색하시려면 아니오 라고 말씀해주세요.");
                    mHandler.postDelayed(mMyTask_Answer, 25000);
                }
                else if(arrStr.contains("종료")) {
                    StartSpeak("탑승을 원하시는 버스는 현재" + arrStr + "되었습니다. 다른 버스나 교통편을 이용해주세요. 음성안내 종료 후 검색화면으로 다시 이동합니다.");
                    mHandler.postDelayed(mMyTask_End, 15000);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blindpassenger_searchbusinfo);

        Intent Bintent = getIntent();
        Binput = Bintent.getStringExtra("bstationNum");
        BbusNumInput = Bintent.getStringExtra("bbusNum");

        BStationName = (TextView)findViewById(R.id.result_st_b);
        BBusNum = (TextView)findViewById((R.id.result_bnum_b));
        BArrMsg = (TextView)findViewById(R.id.result_arrmsg_b);
        BReside = (TextView)findViewById(R.id.result_reride_b);

        blindPassengerSearchBusInfo.MyAsyncTask BmyAsyncTask = new blindPassengerSearchBusInfo.MyAsyncTask();
        BmyAsyncTask.execute();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        B = true;
        mHandler.postDelayed(mMyTask_init, 1000);
        mHandler.postDelayed(mMyTask_speak, 10000);
    }

    protected class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < stringArray.length; i++) {
                if (Binput.equals(stringArray[i][1])) {
                    BstId = stringArray[i][0];
                    break;
                }
            }

            BrequestUrl = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?serviceKey=" + BdataKey + "&stId=" + BstId;
            try {
                boolean Bb_stNm = false;
                boolean Bb_rtNm = false;
                boolean Bb_arrmsg1 = false;
                boolean Bb_reride_Num1 = false;
                boolean Bb_routeType = false;
                boolean Bb_mkTm = false;


                URL Burl = new URL(BrequestUrl);
                InputStream Bis = Burl.openStream();
                XmlPullParserFactory Bfactory = XmlPullParserFactory.newInstance();
                XmlPullParser Bparser = Bfactory.newPullParser();
                Bparser.setInput(new InputStreamReader(Bis, "UTF-8"));

                int BeventType = Bparser.getEventType();

                while (BeventType != XmlPullParser.END_DOCUMENT) {
                    switch (BeventType) {
                        case XmlPullParser.START_DOCUMENT:
                            Blist = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (Bparser.getName().equals("itemList") && Bbus != null) {
                                Blist.add(Bbus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (Bparser.getName().equals("itemList")) {
                                Bbus = new Item();
                            }
                            if (Bparser.getName().equals("stNm")) Bb_stNm = true;
                            if (Bparser.getName().equals("rtNm")) Bb_rtNm = true;
                            if (Bparser.getName().equals("arrmsg1")) Bb_arrmsg1 = true;
                            if (Bparser.getName().equals("reride_Num1")) Bb_reride_Num1 = true;
                            if (Bparser.getName().equals("routeType")) Bb_routeType = true;
                            if (Bparser.getName().equals("mkTm")) Bb_mkTm = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (Bb_stNm) {
                                Bbus.setStNm(Bparser.getText());
                                Bb_stNm = false;
                            } else if (Bb_rtNm) {
                                Bbus.setRtNm(Bparser.getText());
                                Bb_rtNm = false;
                            } else if (Bb_arrmsg1) {
                                Bbus.setArrmsg1(Bparser.getText());
                                Bb_arrmsg1 = false;
                            } else if (Bb_reride_Num1) {
                                if (Bparser.getText().toString().equals("0"))
                                    Bbus.setReride_Num1("데이터 없음");
                                else if (Bparser.getText().toString().equals("3"))
                                    Bbus.setReride_Num1("여유");
                                else if (Bparser.getText().toString().equals("4"))
                                    Bbus.setReride_Num1("보통");
                                else if (Bparser.getText().toString().equals("5"))
                                    Bbus.setReride_Num1("혼잡");
                                Bb_reride_Num1 = false;
                            } else if (Bb_routeType) {
                                if (Bparser.getText().toString().equals("3"))
                                    Bbus.setRouteType("간선");
                                else if (Bparser.getText().toString().equals("4"))
                                    Bbus.setRouteType("지선");
                                Bb_routeType = false;
                            } else if (Bb_mkTm) {
                                Bbus.setMkTm(Bparser.getText());
                                Bb_mkTm = false;
                            }
                            break;
                    }
                    BeventType = Bparser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i = 0; i < Blist.size(); i++) {
                if(Blist.get(i).rtNm.equals(BbusNumInput)) {
                    BStationName.setText(Blist.get(i).stNm);
                    BBusNum.setText(Blist.get(i).rtNm);
                    BArrMsg.setText(Blist.get(i).arrmsg1);
                    BReside.setText(Blist.get(i).reride_Num1);
                }
            }
        }
    }

    private void writeNewPassenger(String busNum, String stationId) {
        mDatabase.child(busNum).child(stationId).child("blind").setValue("1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(blindPassengerSearchBusInfo.this, "탑승이 확정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(blindPassengerSearchBusInfo.this, "탑승이 확정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(text.toString().contains("네") || text.toString().contains("예")) {
                writeNewPassenger(BbusNumInput, Binput);

                Intent intent = new Intent(getApplicationContext(), blindPassengerOutputBusInfo.class);
                intent.putExtra("bResultBusNum", BbusNumInput);
                intent.putExtra("requestUrl", BrequestUrl);
                intent.putExtra("bResultStationNum", Binput);
                startActivity(intent);
                finish();
            }
            else if(text.toString().contains("아니")) {
                finish();
                Intent intent = new Intent(getApplicationContext(), blindPassengerInputBusInfo.class);
                startActivity(intent);
            }
            else {
                StartSpeak("잘못된 입력입니다. 네 혹은 아니오라고 다시 말씀해주세요.");
                mHandler.postDelayed(mMyTask_Answer, 6000);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}