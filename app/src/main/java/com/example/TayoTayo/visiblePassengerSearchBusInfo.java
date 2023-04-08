package com.example.TayoTayo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class visiblePassengerSearchBusInfo<mDatabase> extends AppCompatActivity {

    TextView StationName, BusNum, ArrMsg, Reside;
    String nbKind;

    public String dataKey = "XAmXR4DKEwPXzhnJLGjnMFa63utc%2FfojlyedYbjjwl%2FoGsESjjfDDk4k48LgMIMYQIu%2Fcap%2BDBXSRkqDWHW7qA%3D%3D";
    public String stId;
    public String input;
    private String requestUrl;
    ArrayList<Item> list = null;
    Item bus = null;
    public String busNumInput;
    public String updateKind;

    private Intent intent;

    public String[][] stringArray = { {"100000048", "01143"},
            {"100000039", "01134"},
            {"100000189", "01287"},
            {"100000040", "01135"},
            {"100000188", "01286"},
            {"100000047", "01142"},
            {"100000038", "01133"},
            {"100000049", "01144"}};

    private DatabaseReference mDatabase;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiblepassenger_searchbusinfo);

        StationName = (TextView)findViewById(R.id.result_st_nb);
        BusNum = (TextView)findViewById((R.id.result_bnum_nb));
        ArrMsg = (TextView)findViewById(R.id.result_arrmsg_nb);
        Reside = (TextView)findViewById(R.id.result_reride_nb);

        intent = getIntent();
        input = intent.getStringExtra("stationNum");
        busNumInput = intent.getStringExtra("busNum");
        nbKind = intent.getStringExtra("nbkind");

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button okResult = (Button) findViewById(R.id.ok_signal_nb);
        okResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getAutism = "0";
                String getDeaf = "0";
                String getIntelligence = "0";
                String getMental = "0";
                String getPhysical = "0";
                String getRaspiratory = "0";
                String getSpeech = "0";

                if(nbKind.equals("자폐성장애"))  {
                    getAutism = "1";
                }
                else if(nbKind.equals("청각장애")) {
                    getDeaf = "1";
                }
                else if(nbKind.equals("정신장애")) {
                    getMental = "1";
                }
                else if(nbKind.equals("지적장애"))  {
                    getIntelligence= "1";
                }
                else if(nbKind.equals("호흡기장애"))  {
                    getRaspiratory = "1";
                }
                else if(nbKind.equals("언어장애"))  {
                    getSpeech = "1";
                }
                else if(nbKind.equals("지체장애"))  {
                    getPhysical = "1";
                }

                HashMap result = new HashMap<>();
                result.put("autism", getAutism);
                result.put("deaf", getDeaf);
                result.put("intelligence", getIntelligence);
                result.put("mental", getMental);
                result.put("physical", getPhysical);
                result.put("raspiratory", getRaspiratory);
                result.put("speech", getSpeech);

                writeNewPassenger(busNumInput, input, getAutism, getDeaf, getIntelligence, getMental, getPhysical, getRaspiratory, getSpeech);

                Intent intent = new Intent(getApplicationContext(), visiblePassengerOutputBusInfo.class);
                intent.putExtra("ResultBusNum", busNumInput);
                intent.putExtra("requestUrl", requestUrl);
                intent.putExtra("FBupdateKind", updateKind);
                intent.putExtra("ResultStationNum", input);
                startActivity(intent);
                finish();
            }
        });

        Button backResult = (Button) findViewById(R.id.back_nb);
        backResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), visiblePassengerInputBusInfo.class);
                startActivity(intent);
            }
        });
    }

    @IgnoreExtraProperties
    public class Passenger {

        public String autism, deaf, intelligence, mental, physical, raspiratory, speech;

        public Passenger() {

        }

        public Passenger(String autism, String deaf, String intelligence, String mental, String physical, String raspiratory, String speech) {
            this.autism = autism;
            this.deaf = deaf;
            this.intelligence = intelligence;
            this.mental = mental;
            this.physical = physical;
            this.raspiratory = raspiratory;
            this.speech = speech;
        }

        public String getAutism() {
            return autism;
        }

        public void setAutism(String autism) {
            this.autism = autism;
        }

        public String getDeaf() {
            return deaf;
        }

        public void setDeaf(String daef) {
            this.deaf = deaf;
        }

        public String getIntelligence(String intelligence) {
            return intelligence;
        }

        public void setIntelligence(String intelligence) {
            this.intelligence = intelligence;
        }

        public String getMental() {
            return mental;
        }

        public void setMental(String mental) {
            this.mental = mental;
        }

        public String getPhysical() {
            return physical;
        }

        public void setPhysical(String physical) {
            this.physical = physical;
        }

        public String getRaspiratory() {
            return raspiratory;
        }

        public void setRaspiratory(String raspiratory) {
            this.raspiratory = raspiratory;
        }

        public String getSpeech() {
            return speech;
        }

        public void setSpeech(String speech) {
            this.speech = speech;
        }
    }

    private void writeNewPassenger(String busNum, String stationId, String autism, String deaf, String intelligence, String mental, String physical, String raspiratory, String speech) {
        Passenger passenger = new Passenger(autism, deaf, intelligence, mental, physical, raspiratory, speech);

        String Kind = null;
        if(autism.equals("1")) {
            Kind = "autism";
        }
        else if(deaf.equals("1")) {
            Kind = "deaf";
        }
        else if(intelligence.equals("1")) {
            Kind = "intelligence";
        }
        else if(mental.equals("1")) {
            Kind = "mental";
        }
        else if(physical.equals("1")) {
            Kind = "physical";
        }
        else if(raspiratory.equals("1")) {
            Kind = "raspiratory";
        }
        else if(speech.equals("1")) {
            Kind = "speech";
        }

        updateKind = Kind;

        mDatabase.child(busNum).child(stationId).child(Kind).setValue("1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(visiblePassengerSearchBusInfo.this, "탑승이 확정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(visiblePassengerSearchBusInfo.this, "탑승이 확정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    protected class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < stringArray.length; i++) {
                if (input.equals(stringArray[i][1])) {
                    stId = stringArray[i][0];
                    break;
                }
            }

            requestUrl = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?serviceKey=" + dataKey + "&stId=" + stId;
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
                            list = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList") && bus != null) {
                                list.add(bus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("itemList")) {
                                bus = new Item();
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
                                bus.setStNm(parser.getText());
                                b_stNm = false;
                            } else if (b_rtNm) {
                                bus.setRtNm(parser.getText());
                                b_rtNm = false;
                            } else if (b_arrmsg1) {
                                bus.setArrmsg1(parser.getText());
                                b_arrmsg1 = false;
                            } else if (b_reride_Num1) {
                                if (parser.getText().toString().equals("0"))
                                    bus.setReride_Num1("데이터 없음");
                                else if (parser.getText().toString().equals("3"))
                                    bus.setReride_Num1("여유");
                                else if (parser.getText().toString().equals("4"))
                                    bus.setReride_Num1("보통");
                                else if (parser.getText().toString().equals("5"))
                                    bus.setReride_Num1("혼잡");
                                b_reride_Num1 = false;
                            } else if (b_routeType) {
                                if (parser.getText().toString().equals("3"))
                                    bus.setRouteType("간선");
                                else if (parser.getText().toString().equals("4"))
                                    bus.setRouteType("지선");
                                b_routeType = false;
                            } else if (b_mkTm) {
                                bus.setMkTm(parser.getText());
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

            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).rtNm.equals(busNumInput)) {
                    StationName.setText(list.get(i).stNm);
                    BusNum.setText(list.get(i).rtNm);
                    ArrMsg.setText(list.get(i).arrmsg1);
                    Reside.setText(list.get(i).reride_Num1);
                }
            }
        }
    }
}