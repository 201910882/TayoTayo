package com.example.TayoTayo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mainBusDriver extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    String DriverBusNum, DriverNumberplate;

    TextView stn1, stn2, stn3, stn4, stn5, stn6, stn7, stn8,
             knd1, knd2, knd3, knd4, knd5, knd6, knd7, knd8;

    public String[][] numToName = { {"세검정.상명대(세검정초등학교방면)", "01143"},
            {"세검정.상명대(홍지문방면)", "01134"},
            {"상명대입구.석파랑(하림각방면)", "01287"},
            {"상명대입구.석파랑(하림각방면)", "01135"},
            {"상명대입구.세검정교회(상명대정문방면)", "01286"},
            {"상명대입구.세검정교회(세검정초등학교방면)", "01142"},
            {"세검정초등학교(상명대입구.석파랑방면)", "01133"},
            {"세검정초등학교(화정박물관 방면)", "01144"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busdriver_main);

        stn1 = (TextView)findViewById(R.id.stnN1);
        stn2 = (TextView)findViewById(R.id.stnN2);
        stn3 = (TextView)findViewById(R.id.stnN3);
        stn4 = (TextView)findViewById(R.id.stnN4);
        stn5 = (TextView)findViewById(R.id.stnN5);
        stn6 = (TextView)findViewById(R.id.stnN6);
        stn7 = (TextView)findViewById(R.id.stnN7);
        stn8 = (TextView)findViewById(R.id.stnN8);

        knd1 = (TextView)findViewById(R.id.pKnd1);
        knd2 = (TextView)findViewById(R.id.pKnd2);
        knd3 = (TextView)findViewById(R.id.pKnd3);
        knd4 = (TextView)findViewById(R.id.pKnd4);
        knd5 = (TextView)findViewById(R.id.pKnd5);
        knd6 = (TextView)findViewById(R.id.pKnd6);
        knd7 = (TextView)findViewById(R.id.pKnd7);
        knd8 = (TextView)findViewById(R.id.pKnd8);

        Intent intent = getIntent();
        DriverBusNum = intent.getStringExtra("busNum");
        DriverNumberplate = intent.getStringExtra("numberPlate");

        initDatabase("");

        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainBusDriver.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences Dauto = getSharedPreferences("Dauto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor Deditor = Dauto.edit();
                Deditor.clear();
                Deditor.commit();
                Toast.makeText(mainBusDriver.this, "로그아웃되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void setClear(String stn, String knd) {
        if((stn1.getText().equals(stn) && knd1.getText().equals(knd))) {
            stn1.setText("");
            knd1.setText("");
        }
        else if((stn2.getText().equals(stn) && knd2.getText().equals(knd))) {
            stn2.setText("");
            knd2.setText("");
        }
        else if((stn3.getText().equals(stn) && knd3.getText().equals(knd))) {
            stn3.setText("");
            knd3.setText("");
        }
        else if((stn4.getText().equals(stn) && knd4.getText().equals(knd))) {
            stn4.setText("");
            knd4.setText("");
        }
        else if((stn5.getText().equals(stn) && knd5.getText().equals(knd))) {
            stn5.setText("");
            knd5.setText("");
        }
        else if((stn6.getText().equals(stn) && knd6.getText().equals(knd))) {
            stn6.setText("");
            knd6.setText("");
        }
        else if((stn7.getText().equals(stn) && knd7.getText().equals(knd))) {
            stn7.setText("");
            knd7.setText("");
        }
        else if((stn8.getText().equals(stn) && knd8.getText().equals(knd))) {
            stn8.setText("");
            knd8.setText("");
        }
    }

    public void isEmpty(String stn, String knd) {
        if((stn1.getText().equals(""))) {
            stn1.setText(stn);
            knd1.setText(knd);
        }
        else if((stn2.getText().equals(""))) {
            stn2.setText(stn);
            knd2.setText(knd);
        }
        else if((stn3.getText().equals(""))) {
            stn3.setText(stn);
            knd3.setText(knd);
        }
        else if((stn4.getText().equals(""))) {
            stn4.setText(stn);
            knd4.setText(knd);
        }
        else if((stn5.getText().equals(""))) {
            stn5.setText(stn);
            knd5.setText(knd);
        }
        else if((stn6.getText().equals(""))) {
            stn6.setText(stn);
            knd6.setText(knd);
        }
        else if((stn7.getText().equals(""))) {
            stn7.setText(stn);
            knd7.setText(knd);
        }
        else if((stn8.getText().equals(""))) {
            stn8.setText(stn);
            knd8.setText(knd);
        }
    }

    public String stnNumToName(String stnNum) {
        for (int i = 0; i < numToName.length; i++) {
            if((stnNum.equals(numToName[i][1]))) {
                return numToName[i][0];
            }
        }
        return null;
    }

    public String EngToKor(String EngKnd) {
        if(EngKnd.equals("blind"))
            return "시각장애";
        else if(EngKnd.equals("deaf"))
            return "청각장애";
        else if(EngKnd.equals("autism"))
            return "자폐성장애";
        else if(EngKnd.equals("intelligence"))
            return "지적장애";
        else if(EngKnd.equals("raspiratory"))
            return "호흡기장애";
        else if(EngKnd.equals("speech"))
            return "언어장애";
        else if(EngKnd.equals("physical"))
            return "지체장애";
        else if(EngKnd.equals("mental"))
            return "정신장애";
        else
            return null;
    }

    private void initDatabase(String path) {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference(path);

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnaphost, String s) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onChildChanged(DataSnapshot dataSnaphost, String s) {
                for(DataSnapshot messageData : dataSnaphost.getChildren()) {
                    if(dataSnaphost.getKey().equals(DriverBusNum)) {
                        int blindst = messageData.getValue().toString().indexOf("blind");
                        int blinded = blindst + "blind".length() - 1;
                        int blindval = blinded + 2;

                        int deafst = messageData.getValue().toString().indexOf("deaf");
                        int deafed = deafst + "deaf".length() - 1;
                        int deafval = deafed + 2;

                        int autismst = messageData.getValue().toString().indexOf("autism");
                        int autismed = autismst + "autism".length() - 1;
                        int autismval = autismed + 2;

                        int intellst = messageData.getValue().toString().indexOf("intelligence");
                        int intelled = intellst + "intelligence".length() - 1;
                        int intellval = intelled + 2;

                        int menst = messageData.getValue().toString().indexOf("mental");
                        int mened = menst + "mental".length() - 1;
                        int menval = mened + 2;

                        int physt = messageData.getValue().toString().indexOf("physical");
                        int phyed = physt + "physical".length() - 1;
                        int phyval = phyed + 2;

                        int rasst = messageData.getValue().toString().indexOf("raspiratory");
                        int rased = rasst + "raspiratory".length() - 1;
                        int rasval = rased + 2;

                        int speechst = messageData.getValue().toString().indexOf("speech");
                        int speeched = speechst + "speech".length() - 1;
                        int speechval = speeched + 2;

                        if(String.valueOf(messageData.getValue().toString().charAt(blindval)).equals("1")) {    //blind 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(blindst, blinded + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);
                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(blindval)).equals("0")) {    //blind 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(blindst, blinded + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(deafval)).equals("1")) {    //deaf 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(deafst, deafed + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(deafval)).equals("0")) {    //deaf 승객의 값이 0이면String busNum = dataSnaphost.getKey();
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(deafst, deafed + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(autismval)).equals("1")) {    //autism 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(autismst, autismed + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(autismval)).equals("0")) {    //autism 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(autismst, autismed + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(intellval)).equals("1")) {    //intelligence 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(intellst, intelled + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(intellval)).equals("0")) {    //intelligence 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(intellst, intelled + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(menval)).equals("1")) {    //mental 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(menst, mened + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(menval)).equals("0")) {    //mental 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(menst, mened + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(phyval)).equals("1")) {    //physical 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(physt, phyed + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(phyval)).equals("0")) {    //physical 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(physt, phyed + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(rasval)).equals("1")) {    //raspiratory 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(rasst, rased + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(rasval)).equals("0")) {    //raspiratory 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(rasst, rased + 1));

                            setClear(stationName, kind);
                        }

                        if(String.valueOf(messageData.getValue().toString().charAt(speechval)).equals("1")) {    //speech 승객의 값이 1이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(speechst, speeched + 1));

                            setClear(stationName, kind);
                            isEmpty(stationName, kind);

                        }
                        else if(String.valueOf(messageData.getValue().toString().charAt(speechval)).equals("0")) {    //speech 승객의 값이 0이면
                            String stationName = stnNumToName(messageData.getKey());
                            String kind = EngToKor(messageData.getValue().toString().substring(speechst, speeched + 1));

                            setClear(stationName, kind);
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnaphost, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}