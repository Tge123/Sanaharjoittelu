package com.example.riktur.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

/**
 * Sanakääntöluokka
 */
public class harjoittele2 extends Activity {

    //TODO Tähän tehty samaan tapaan kuin monivalinnassa, mutta pienillä muutoksilla. Edelleen tarkistus ei toimi oikein...
    ArrayList<sanapari> opeteltavatsanat;
    private TextInputEditText tekstiInputti;

    private Integer kystype;
    private Integer kysyttava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harjoittele2);

        //Luodaan toiminnot painikkeille
        Button button1 = (Button) findViewById(R.id.button8);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText tekstiInputti = (TextInputEditText) findViewById(R.id.vastaus);

                String vastaus = String.valueOf(tekstiInputti.getText());

                if(vastaus.equals("")){
                    Context context = getApplicationContext();
                    CharSequence text = "Kentät eivät saa olla tyhjiä";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                else{

                    boolean tarkistettu = tarkistaSana(vastaus, opeteltavatsanat);
                    String tietovastauksesta = "Valitse vaihtoehto";

                    if (tarkistettu == true) {
                        tietovastauksesta = "Oikein, haluatko jatkaa?";
                        Date currentdate = new Date();
                        historyelement newelement = new historyelement(currentdate,kystype,1);
                        opeteltavatsanat.get(kysyttava).getVastausHistoria().add(newelement);
                    } else {
                        tietovastauksesta = "Väärin, haluatko jatkaa?";
                        Date currentdate = new Date();
                        historyelement newelement = new historyelement(currentdate,kystype,0);
                        opeteltavatsanat.get(kysyttava).getVastausHistoria().add(newelement);
                    }


                    AlertDialog alertDialog = new AlertDialog.Builder(harjoittele2.this)
                            //set icon
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            //set title
                            .setTitle("Tietoa vastauksesta")
                            //set message
                            .setMessage(tietovastauksesta)
                            //set positive button
                            .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                                    returnIntent.putExtra("jatketaanko",true);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            //set negative button
                            .setNegativeButton("Ei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                                    returnIntent.putExtra("jatketaanko",false);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .show();
            }

            }
        });
        //TODO Skippaus samalla tavalla kuin monivalinnassa
        Button button2 = (Button) findViewById(R.id.button9);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(harjoittele2.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setTitle("Skippaus");
                builder.setMessage("Haluatko skipata sanan?");
                builder.setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Date currentdate = new Date();
                        historyelement newelement = new historyelement(currentdate,kystype,2);
                        opeteltavatsanat.get(kysyttava).getVastausHistoria().add(newelement);

                        Intent returnIntent = new Intent();
                        returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                        returnIntent.putExtra("jatketaanko",true);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
                builder.setNegativeButton("Ei", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //set what should happen when negative button is clicked
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog2 = builder.show();

            }
        });
        Button button3 = (Button) findViewById(R.id.button12);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Palataan MainActivityyn ja lähetetään opeteltavien sanojen lista samalla
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        ArrayList<sanapari> vastaanotettu = this.getIntent().getParcelableArrayListExtra("kysyttavienlista");
        opeteltavatsanat =vastaanotettu;
        kysyttava = this.getIntent().getIntExtra("indeksi",0);
        kystype = this.getIntent().getIntExtra("kystype",0);

        if(kystype == 2){
            TextView eka = (TextView) findViewById(R.id.textView9);
            TextView toka = (TextView) findViewById(R.id.textView10);

            eka.setText("Kirjoita seuraava sana englanniksi:");
            toka.setText(opeteltavatsanat.get(kysyttava).getSanaSuomeksi());

        }
        else if(kystype ==3){
            TextView eka = (TextView) findViewById(R.id.textView9);
            TextView toka = (TextView) findViewById(R.id.textView10);

            eka.setText("Kirjoita seuraava sana suomeksi:");
            toka.setText(opeteltavatsanat.get(kysyttava).getSanaEnglanniksi());

        }

    }



    //TODO Tarkistus ei toimi oikein...
    private boolean tarkistaSana(String vastaus, ArrayList<sanapari> opeteltavatsanat) {

        if(kystype==2){
            String oikeavastaus = (opeteltavatsanat.get(kysyttava).getSanaEnglanniksi().trim());
            oikeavastaus.replaceAll("\\s","");
            if (oikeavastaus.equals(vastaus) ) {
                return true;
            }
            return false;
        }

        else if(kystype==3){
            if (opeteltavatsanat.get(kysyttava).getSanaSuomeksi().trim().equals(vastaus) ) {
                return true;
            }
            return false;
        }

        return  false;
    }

}