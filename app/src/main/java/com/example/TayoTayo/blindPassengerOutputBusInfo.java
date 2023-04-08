package com.example.TayoTayo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class blindPassengerOutputBusInfo extends Activity {

    TextView bresultStationName, bresultBusName, bresultArrMsg, bresultRouteType, bresultReside;

    private String BresultBusNum, BrequestUrl, BresultStationNum;

    ArrayList<Item> BresultList = null;
    Item BresultBus = null;

    private TextToSpeech mTts = null;
    private int mQueue = TextToSpeech.QUEUE_ADD;

    boolean B = false;
    public Handler mHandler = new Handler();

    private DatabaseReference bmDatabase;

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

    private Runnable mMyTask_speak = new Runnable() {
        @Override
        public void run() {
            if(B) {
                String arrStr = bresultArrMsg.getText().toString();
                String resideStr = bresultReside.getText().toString();
                if(arrStr.contains("막차")) {
                    int index = arrStr.indexOf("후");
                    int len = arrStr.length();
                    StartSpeak("이번 버스는 막차입니다. 현재 " + arrStr.substring(index + 2, len - 1) + "에 위치하고 있으며, " + arrStr.substring(0, index + 1) + "도착 예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 우측상단을 클릭해주시고, 탑승을 완료하셨다면 화면의 좌측상단을 클릭해주세요.");
                }
                else if(arrStr.contains("후")) {
                    int index = arrStr.indexOf("[");
                    int len = arrStr.length();
                    StartSpeak(arrStr.substring(index + 1, len - 1) + "에 위치하고 있으며, " + arrStr.substring(0, index) + "도착 예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 우측상단을 클릭해주시고, 탑승을 완료하셨다면 화면의 좌측상단을 클릭해주세요.");
                }
                else if(arrStr.equals("대기")) {
                    StartSpeak(arrStr + "상태입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 우측상단을 클릭해주시고, 탑승을 완료하셨다면 화면의 좌측상단을 클릭해주세요.");
                }
                else if(arrStr.contains("곧 도착")) {
                    StartSpeak(arrStr + "예정입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 우측상단을 클릭해주시고, 탑승을 완료하셨다면 화면의 좌측상단을 클릭해주세요.");
                }
                else if(arrStr.contains("차고지")) {
                    int len = arrStr.length();
                    StartSpeak(arrStr.substring(1, len - 1) + "상태입니다. 혼잡도는 " + resideStr + "로 예상됩니다. 버스 정보의 새로고침을 원하시면 화면의 우측상단을 클릭해주시고, 탑승을 완료하셨다면 화면의 좌측상단을 클릭해주세요.");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindpassenger_outputbusinfo);

        Intent bresultintent = getIntent();

        bresultStationName =(TextView)findViewById(R.id.tv_stNm_b);
        bresultBusName = (TextView)findViewById(R.id.tv_rtNm_b);
        bresultArrMsg = (TextView)findViewById(R.id.tv_arrmsg1_b);
        bresultRouteType = (TextView)findViewById(R.id.tv_routeType_b);
        bresultReside = (TextView)findViewById(R.id.tv_reride_Num1_b);

        BresultBusNum = bresultintent.getStringExtra("bResultBusNum");
        BrequestUrl = bresultintent.getStringExtra("requestUrl");
        BresultStationNum = bresultintent.getStringExtra("bResultStationNum");

        blindPassengerOutputBusInfo.MyAsyncTask bmyResultAsyncTask = new blindPassengerOutputBusInfo.MyAsyncTask();
        bmyResultAsyncTask.execute();

        bmDatabase = FirebaseDatabase.getInstance().getReference();

        StartSpeak("입력하신 버스의 탑승예정이 확정되었습니다. ");

        B = true;
        mHandler.postDelayed(mMyTask_speak, 2000);

        Button refresh = (Button) findViewById(R.id.refresh_b);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blindPassengerOutputBusInfo.MyAsyncTask myResultAsyncTask = new blindPassengerOutputBusInfo.MyAsyncTask();
                myResultAsyncTask.execute();
                StartSpeak("버스 정보가 업데이트 되었습니다. ");
                mHandler.postDelayed(mMyTask_speak, 3000);
            }
        });

        Button bSuccess = (Button) findViewById(R.id.finish_b);
        bSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewPassenger(BresultBusNum, BresultStationNum);
                finish();
            }
        });
    }

    private void writeNewPassenger(String busNum, String stationId) {
        bmDatabase.child(busNum).child(stationId).child("blind").setValue("0")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(blindPassengerOutputBusInfo.this, "탑승이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    protected class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                boolean b_stNm = false;
                boolean b_rtNm = false;
                boolean b_arrmsg1 = false;
                boolean b_reride_Num1 = false;
                boolean b_routeType = false;
                boolean b_mkTm = false;

                URL url = new URL(BrequestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            BresultList = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList") && BresultBus != null) {
                                BresultList.add(BresultBus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("itemList")) {
                                BresultBus = new Item();
                            }
                            if (parser.getName().equals("stNm")) b_stNm = true;
                            if (parser.getName().equals("rtNm")) b_rtNm = true;
                            if (parser.getName().equals("arrmsg1")) b_arrmsg1 = true;
                            if (parser.getName().equals("reride_Num1")) b_reride_Num1 = true;
                            if (parser.getName().equals("routeType")) b_routeType = true;
                            if (parser.getName().equals("mkTm")) b_mkTm = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (b_stNm) {
                                BresultBus.setStNm(parser.getText());
                                b_stNm = false;
                            } else if (b_rtNm) {
                                BresultBus.setRtNm(parser.getText());
                                b_rtNm = false;
                            } else if (b_arrmsg1) {
                                BresultBus.setArrmsg1(parser.getText());
                                b_arrmsg1 = false;
                            } else if (b_reride_Num1) {
                                if (parser.getText().toString().equals("0"))
                                    BresultBus.setReride_Num1("데이터 없음");
                                else if (parser.getText().toString().equals("3"))
                                    BresultBus.setReride_Num1("여유");
                                else if (parser.getText().toString().equals("4"))
                                    BresultBus.setReride_Num1("보통");
                                else if (parser.getText().toString().equals("5"))
                                    BresultBus.setReride_Num1("혼잡");
                                b_reride_Num1 = false;
                            } else if (b_routeType) {
                                if (parser.getText().toString().equals("3"))
                                    BresultBus.setRouteType("간선");
                                else if (parser.getText().toString().equals("4"))
                                    BresultBus.setRouteType("지선");
                                b_routeType = false;
                            } else if (b_mkTm) {
                                BresultBus.setMkTm(parser.getText());
                                b_mkTm = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i = 0; i < BresultList.size(); i++) {
                if(BresultList.get(i).rtNm.equals(BresultBusNum)) {
                    bresultStationName.setText(BresultList.get(i).stNm);
                    bresultBusName.setText(BresultList.get(i).rtNm);
                    bresultArrMsg.setText(BresultList.get(i).arrmsg1);
                    bresultRouteType.setText(BresultList.get(i).routeType);
                    bresultReside.setText(BresultList.get(i).reride_Num1);
                }
            }
        }
    }
}