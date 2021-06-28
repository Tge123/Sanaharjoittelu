package com.example.riktur.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Luokka sanojen lisäystä varten
 */
public class lisaasanoja extends Activity {
    ArrayList<sanapari> opeteltavatsanat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lisaasanoja);

        ArrayList<sanapari> vastaanotettu = this.getIntent().getParcelableArrayListExtra("kysyttavienlista");
        opeteltavatsanat = vastaanotettu;


        //Lisää painikkeen toiminnallisuudet
        Button button1 = (Button) findViewById(R.id.button5);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText suomeksi = (TextInputEditText)findViewById(R.id.sanasuomeksi_input);
                TextInputEditText englanniksi = (TextInputEditText)findViewById(R.id.sanaenglanniksi_input);

                //Takistetaan että kenttiin on syötetty tekstit
                if(suomeksi.getText().toString().trim().length() == 0 || englanniksi.getText().toString().trim().length() == 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Kentät eivät saa olla tyhjiä";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    //Luodaan uusi sanapari annetuilla arvoilla
                    sanapari uusisanapari = new sanapari(suomeksi.getText().toString(),englanniksi.getText().toString(),true);
                    opeteltavatsanat.add(uusisanapari);
                    suomeksi.setText("");
                    englanniksi.setText("");
                    Context context = getApplicationContext();
                    CharSequence text = "Sana lisätty";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }
        });

        //Palaa valikkoon painikkeen toiminnallisuudet
        Button button2 = (Button) findViewById(R.id.button13);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Palataan MainActivityyn ja lähetetään opeteltavien sanojen lista samalla
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat",opeteltavatsanat);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}