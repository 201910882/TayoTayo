package com.example.TayoTayo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class visiblePassengerOutputBusInfo extends Activity {

    TextView resultStationName, resultArrMsg, resultRouteType, resultReside, resultMkTm, resultBusNumber;

    private String resultBusNum, requestUrl, resultStationNum;

    ArrayList<Item> resultList = null;
    Item resultBus = null;

    public String FBupdateKind;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiblepassenger_outputbusinfo);

        Intent resultintent = getIntent();

        resultStationName = (TextView)findViewById(R.id.tv_stNm_nb);
        resultArrMsg = (TextView)findViewById(R.id.tv_arrmsg1_nb);
        resultRouteType = (TextView)findViewById(R.id.tv_routeType_nb);
        resultReside = (TextView)findViewById(R.id.tv_reride_Num1_nb);
        resultMkTm = (TextView)findViewById(R.id.tv_mkTm_nb);
        resultBusNumber = (TextView)findViewById(R.id.tv_rtNm_nb);

        resultBusNum = resultintent.getStringExtra("ResultBusNum");
        requestUrl = resultintent.getStringExtra("requestUrl");
        FBupdateKind = resultintent.getStringExtra("FBupdateKind");
        resultStationNum = resultintent.getStringExtra("ResultStationNum");

        visiblePassengerOutputBusInfo.MyAsyncTask myResultAsyncTask = new visiblePassengerOutputBusInfo.MyAsyncTask();
        myResultAsyncTask.execute();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button refresh = (Button) findViewById(R.id.refresh_nb);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visiblePassengerOutputBusInfo.MyAsyncTask myResultAsyncTask = new visiblePassengerOutputBusInfo.MyAsyncTask();
                myResultAsyncTask.execute();
            }
        });

        Button success = (Button) findViewById(R.id.finish_nb);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewPassenger(resultBusNum, resultStationNum);
                finish();
            }
        });
    }

    private void writeNewPassenger(String busNum, String stationId) {
        mDatabase.child(busNum).child(stationId).child(FBupdateKind).setValue("0")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(visiblePassengerOutputBusInfo.this, "탑승이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            resultList = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList") && resultBus != null) {
                                resultList.add(resultBus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("itemList")) {
                                resultBus = new Item();
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
                                resultBus.setStNm(parser.getText());
                                b_stNm = false;
                            } else if (b_rtNm) {
                                resultBus.setRtNm(parser.getText());
                                b_rtNm = false;
                            } else if (b_arrmsg1) {
                                resultBus.setArrmsg1(parser.getText());
                                b_arrmsg1 = false;
                            } else if (b_reride_Num1) {
                                if (parser.getText().toString().equals("0"))
                                    resultBus.setReride_Num1("데이터 없음");
                                else if (parser.getText().toString().equals("3"))
                                    resultBus.setReride_Num1("여유");
                                else if (parser.getText().toString().equals("4"))
                                    resultBus.setReride_Num1("보통");
                                else if (parser.getText().toString().equals("5"))
                                    resultBus.setReride_Num1("혼잡");
                                b_reride_Num1 = false;
                            } else if (b_routeType) {
                                if (parser.getText().toString().equals("3"))
                                    resultBus.setRouteType("간선");
                                else if (parser.getText().toString().equals("4"))
                                    resultBus.setRouteType("지선");
                                b_routeType = false;
                            } else if (b_mkTm) {
                                resultBus.setMkTm(parser.getText());
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

            for(int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).rtNm.equals(resultBusNum)) {
                    resultStationName.setText(resultList.get(i).stNm);
                    resultArrMsg.setText(resultList.get(i).arrmsg1);
                    resultRouteType.setText(resultList.get(i).routeType);
                    resultReside.setText(resultList.get(i).reride_Num1);
                    resultMkTm.setText(resultList.get(i).mkTm);
                    resultBusNumber.setText(resultList.get(i).rtNm);
                }
            }
        }
    }
}