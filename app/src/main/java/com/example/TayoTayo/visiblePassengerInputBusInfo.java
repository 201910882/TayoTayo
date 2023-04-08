package com.example.TayoTayo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class visiblePassengerInputBusInfo extends Activity{

    EditText busNum, stationNum;
    String busNumber, stationNumber, nbKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiblepassenger_inputbusinfo);

        busNum = (EditText)findViewById(R.id.src_bnum_nb);
        stationNum = (EditText)findViewById(R.id.src_stNm_nb);

        busNum.setText("");
        stationNum.setText("");

        Intent resultintent = getIntent();
        nbKind = resultintent.getStringExtra("nbkind");

        Button search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    busNumber = busNum.getText().toString().toUpperCase().replace(" ", "");
                    if(stationNum.getText().toString().length() == 4)
                        stationNumber = "0".concat(stationNum.getText().toString());
                    else
                        stationNumber = stationNum.getText().toString();

                    if(stationNumber.equals("01133")) {
                        if( busNumber.equals("153") || busNumber.equals("1020") || busNumber.equals("1711") || busNumber.equals("7022") || busNumber.equals("7212") || busNumber.equals("7730") || busNumber.equals("110B국민대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01134")) {
                        if( busNumber.equals("153") || busNumber.equals("7730") || busNumber.equals("110B국민대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01143")) {
                        if( busNumber.equals("153") || busNumber.equals("1020") || busNumber.equals("7730") || busNumber.equals("110A고려대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01144")) {
                        if( busNumber.equals("153") || busNumber.equals("1020") || busNumber.equals("1711") || busNumber.equals("7022") || busNumber.equals("7212") || busNumber.equals("7730") || busNumber.equals("110A고려대") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01142") || stationNumber.equals("01287")) {
                        if( busNumber.equals("1020") || busNumber.equals("1711") || busNumber.equals("7022") || busNumber.equals("7212") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01135")) {
                        if( busNumber.equals("7016") || busNumber.equals("7018") )
                            nextPage();
                        else
                            setClear();
                    }
                    else if(stationNumber.equals("01286")) {
                        if( busNumber.equals("7016") || busNumber.equals("7018") || busNumber.equals("8002") )
                            nextPage();
                        else
                            setClear();
                    }
                    else
                        setClear();
                }
                else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결이 원활하지 않습니다. 연결 후, 재시도 해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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

    public void setClear() {
        Toast.makeText(visiblePassengerInputBusInfo.this, "버스번호 혹은 정류장 번호가 잘못되었습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
        busNum.setText("");
        stationNum.setText("");
    }

    public void nextPage() {
        Intent intent = new Intent(getApplicationContext(), visiblePassengerSearchBusInfo.class);
        intent.putExtra("busNum", busNumber);
        intent.putExtra("stationNum", stationNumber);
        intent.putExtra("nbkind", nbKind);
        startActivity(intent);
        finish();
    }
}