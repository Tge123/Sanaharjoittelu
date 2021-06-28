package com.example.riktur.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

/**
 * Monivalintaluokka
 */

public class harjoittele1 extends Activity {

    ArrayList<sanapari> opeteltavatsanat;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Integer kysyttava = 0;
    private Integer kystype=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harjoittele1);

        radioGroup = (RadioGroup) findViewById(R.id.rg);

        //Luodaan toiminnot painikkeille
        Button button1 = (Button) findViewById(R.id.button7);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                String vastaus = (String) radioButton.getText();

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


                AlertDialog alertDialog = new AlertDialog.Builder(harjoittele1.this)
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
                                //set what would happen when positive button is clicked
                                radioGroup = (RadioGroup) findViewById(R.id.rg);
                                radioGroup.clearCheck();


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
                                //set what should happen when negative button is clicked
                                Intent returnIntent = new Intent();
                                returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                                returnIntent.putExtra("jatketaanko",false);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        })
                        .show();


            }
        });
        Button button2 = (Button) findViewById(R.id.button11);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Toimii, muttei päivitä historiaa
                final AlertDialog.Builder builder = new AlertDialog.Builder(harjoittele1.this);
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
        Button button3 = (Button) findViewById(R.id.button10);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                //Palataan MainActivityyn ja lähetetään opeteltavien sanojen lista samalla
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("palautettu_opeteltavat", opeteltavatsanat);
                setResult(Activity.RESULT_OK, returnIntent);
                //finish();


                setContentView(R.layout.activity_main);
                finish();
            }
        });




        //VAstaanotetaan intent ja tallennetaan opeteltavien sanojen lista
        ArrayList<sanapari> vastaanotettu = this.getIntent().getParcelableArrayListExtra("kysyttavienlista");
        kysyttava = this.getIntent().getIntExtra("indeksi",0);
        kystype = this.getIntent().getIntExtra("kystype",0);
        //kysyttava = opeteltavatsanat.get(sanaindex);

        opeteltavatsanat = vastaanotettu;
        arvovaihtoehdot(kysyttava);


    }



    private boolean tarkistaSana(String vastaus, ArrayList<sanapari> opeteltavatsanat) {


        if(kystype==0){
            if (opeteltavatsanat.get(kysyttava).getSanaEnglanniksi().equals(vastaus) ) {
                return true;
            }
        return false;
        }

        else if(kystype==1){
            if (opeteltavatsanat.get(kysyttava).getSanaSuomeksi().equals(vastaus) ) {
                return true;
            }
            return false;
        }

        return  false;
    }


    //Tässä funktiossa arvotaan "dummy" vaihtoehdot monivalinta harjoittelussa
    private void arvovaihtoehdot(Integer kysytty) {

        //Jos kysytään suomesta-englanniksi
        if(kystype==0){
            TextView kysyttavasana = (TextView) findViewById(R.id.textView8);
            kysyttavasana.setText(opeteltavatsanat.get(kysyttava).getSanaSuomeksi());


            //Arvottaan dummy vaihtoehdot
            Random rand = new Random();
            Integer eka = rand.nextInt(opeteltavatsanat.size());
            Integer toka = rand.nextInt(opeteltavatsanat.size());

            //Jos kaikki ovat erejä niin asetetaan vaihtoehdot näkyviin
            if (eka != toka && eka != kysytty && toka != kysytty) {
                RadioButton ylin = (RadioButton) findViewById(R.id.ve1);
                RadioButton keskin = (RadioButton) findViewById(R.id.ve2);
                RadioButton alin = (RadioButton) findViewById(R.id.ve3);

                ArrayList<Integer> jarjestys = new ArrayList<Integer>();
                jarjestys.add(kysytty);
                jarjestys.add(eka);
                jarjestys.add(toka);

                //Sekoitetaan vaihtoehdot
                Collections.shuffle(jarjestys);


                //Asetetaan vaihtoehdot näkyviin
                ylin.setText(opeteltavatsanat.get(jarjestys.get(0)).getSanaEnglanniksi());
                keskin.setText(opeteltavatsanat.get(jarjestys.get(1)).getSanaEnglanniksi());
                alin.setText(opeteltavatsanat.get(jarjestys.get(2)).getSanaEnglanniksi());
            } else {
                //Jos arvonnassa tuli samoja lukuja niin arvotaan uudelleen
                arvovaihtoehdot(kysytty);
            }

        }


        //Jos kysytäään englannista suomeksi
        else if(kystype==1){
            TextView kysyttavasana = (TextView) findViewById(R.id.textView8);
            kysyttavasana.setText(opeteltavatsanat.get(kysyttava).getSanaEnglanniksi());


            //Arvottaan dummy vaihtoehdot
            Random rand = new Random();
            Integer eka = rand.nextInt(opeteltavatsanat.size());
            Integer toka = rand.nextInt(opeteltavatsanat.size());

            //Jos kaikki ovat erejä niin asetetaan vaihtoehdot näkyviin
            if (eka != toka && eka != kysytty && toka != kysytty) {
                RadioButton ylin = (RadioButton) findViewById(R.id.ve1);
                RadioButton keskin = (RadioButton) findViewById(R.id.ve2);
                RadioButton alin = (RadioButton) findViewById(R.id.ve3);


                ArrayList<Integer> jarjestys = new ArrayList<Integer>();
                jarjestys.add(kysytty);
                jarjestys.add(eka);
                jarjestys.add(toka);

                //Sekoitetaan vaihtoehdot
                Collections.shuffle(jarjestys);


                //Asetetaan vaihtoehdot näkyviin
                ylin.setText(opeteltavatsanat.get(jarjestys.get(0)).getSanaSuomeksi());
                keskin.setText(opeteltavatsanat.get(jarjestys.get(1)).getSanaSuomeksi());
                alin.setText(opeteltavatsanat.get(jarjestys.get(2)).getSanaSuomeksi());
            } else {
                //Jos arvonnassa tuli samoja lukuja niin arvotaan uudelleen
                arvovaihtoehdot(kysytty);
            }


        }


    }
}