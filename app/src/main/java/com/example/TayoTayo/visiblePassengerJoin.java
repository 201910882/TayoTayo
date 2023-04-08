package com.example.TayoTayo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class visiblePassengerJoin extends Activity{

    EditText nbname, nbresidentNumber, nbkind, nbrank;
    Button join;
    String nbjoinName=null, nbjoinResiNum=null, nbjoinKind=null, nbjoinRank=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiblepassenger_autologin);

        nbname = (EditText)findViewById(R.id.namenb);
        nbresidentNumber = (EditText)findViewById(R.id.idnum_nb);
        nbkind = (EditText)findViewById(R.id.kind_nb);
        nbrank = (EditText)findViewById(R.id.rank_nb);

        join = (Button)findViewById(R.id.login_finish_nb);

        SharedPreferences nbauto = getSharedPreferences("nbauto", Activity.MODE_PRIVATE);
        nbjoinName = nbauto.getString("nbinputName",null);
        nbjoinResiNum = nbauto.getString("nbinputResiNum",null);
        nbjoinKind = nbauto.getString("nbinputKind",null);
        nbjoinRank = nbauto.getString("nbinputRank",null);

        if(nbjoinName !=null && nbjoinResiNum != null && nbjoinKind != null && nbjoinRank != null) {
            if( (nbjoinName.equals("김나영") && nbjoinResiNum.equals("9802022123457") && nbjoinKind.equals("청각장애") && nbjoinRank.equals("경증")) ||
                (nbjoinName.equals("김다영") && nbjoinResiNum.equals("9503032123458") && nbjoinKind.equals("지적장애") && nbjoinRank.equals("중증")) ||
                (nbjoinName.equals("김라영") && nbjoinResiNum.equals("0204043123459") && nbjoinKind.equals("지체장애") && nbjoinRank.equals("중증")) ||
                (nbjoinName.equals("이가연") && nbjoinResiNum.equals("8905052123450") && nbjoinKind.equals("정신장애") && nbjoinRank.equals("중증")) ||
                (nbjoinName.equals("이다연") && nbjoinResiNum.equals("0007073123452") && nbjoinKind.equals("호흡기장애") && nbjoinRank.equals("중증")) ||
                (nbjoinName.equals("최나현") && nbjoinResiNum.equals("8909092123454") && nbjoinKind.equals("언어장애") && nbjoinRank.equals("중증")) ||
                (nbjoinName.equals("최다현") && nbjoinResiNum.equals("7701011123455") && nbjoinKind.equals("자폐성장애") && nbjoinRank.equals("중증")) ) {
                Toast.makeText(visiblePassengerJoin.this, nbjoinName +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), visiblePassengerInputBusInfo.class);
                intent.putExtra("nbkind", nbjoinKind);
                startActivity(intent);
                finish();
            }
        }
        else if(nbjoinName == null && nbjoinResiNum == null && nbjoinKind == null && nbjoinRank == null){
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (nbname.getText().toString().equals("김나영") && nbresidentNumber.getText().toString().equals("9802022123457") && nbkind.getText().toString().equals("청각장애") && nbrank.getText().toString().equals("경증")) ||
                        (nbname.getText().toString().equals("김다영") && nbresidentNumber.getText().toString().equals("9503032123458") && nbkind.getText().toString().equals("지적장애") && nbrank.getText().toString().equals("중증")) ||
                        (nbname.getText().toString().equals("김라영") && nbresidentNumber.getText().toString().equals("0204043123459") && nbkind.getText().toString().equals("지체장애") && nbrank.getText().toString().equals("중증")) ||
                        (nbname.getText().toString().equals("이가연") && nbresidentNumber.getText().toString().equals("8905052123450") && nbkind.getText().toString().equals("정신장애") && nbrank.getText().toString().equals("중증")) ||
                        (nbname.getText().toString().equals("이다연") && nbresidentNumber.getText().toString().equals("0007073123452") && nbkind.getText().toString().equals("호흡기장애") && nbrank.getText().toString().equals("중증")) ||
                        (nbname.getText().toString().equals("최나현") && nbresidentNumber.getText().toString().equals("8909092123454") && nbkind.getText().toString().equals("언어장애") && nbrank.getText().toString().equals("중증")) ||
                        (nbname.getText().toString().equals("최다현") && nbresidentNumber.getText().toString().equals("7701011123455") && nbkind.getText().toString().equals("자폐성장애") && nbrank.getText().toString().equals("중증")) ) {
                        SharedPreferences nbauto = getSharedPreferences("nbauto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor nbautoLogin = nbauto.edit();
                        nbautoLogin.putString("nbinputName", nbname.getText().toString());
                        nbautoLogin.putString("nbinputResiNum", nbresidentNumber.getText().toString());
                        nbautoLogin.putString("nbinputKind", nbkind.getText().toString());
                        nbautoLogin.putString("nbinputRank", nbrank.getText().toString());
                        nbautoLogin.commit();
                        Toast.makeText(visiblePassengerJoin.this, nbname.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), visiblePassengerInputBusInfo.class);
                        intent.putExtra("nbkind", nbkind.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(visiblePassengerJoin.this, "잘못된 정보입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        nbname.setText("");
                        nbresidentNumber.setText("");
                        nbkind.setText("");
                        nbrank.setText("");
                    }
                }
            });
        }
    }
}