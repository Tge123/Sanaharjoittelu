package com.example.riktur.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Random;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivity;

public class kysymyksenvalinta {
    private ArrayList<sanapari> opeteltavatsanat;
    private Integer kysyttavanSananIndeksi;
    private Integer kysymystyyppi;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public kysymyksenvalinta(ArrayList<sanapari> a, Context c) {

        this.context =c;
        this.opeteltavatsanat=a;
        pick_next();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void pick_next(){
        //Mitä on kysytty vähiten

        int pienin = 999999;

        for (int i = 0; i < opeteltavatsanat.size(); i++) {
            //VAlitaan randomilla kysyttävä sana
            Random rand = new Random();

            setKysyttavanSananIndeksi(rand.nextInt(opeteltavatsanat.size()));

            /*
            opeteltavatsanat.get(i).getVastausHistoria().size();
            if (opeteltavatsanat.get(i).getVastausHistoria().size() <= pienin) {
                pienin = opeteltavatsanat.get(i).getVastausHistoria().size();
                setKysyttavanSananIndeksi(i);
            }*/
        }

        //Millä tavalla on kysytty vähiten
        //Käydään läpi vähiten kysytyn sanan vastaushistoria ja valitaan se kysymystyyppi jota on kysytty vähiten
        ArrayList<Integer> kystypet = new ArrayList<Integer>();
        kystypet.add(0);
        kystypet.add(0);
        kystypet.add(0);
        kystypet.add(0);

        //Asetetaan kysymystyypit taulukkoon
        for(int i = 0; i<opeteltavatsanat.get(getKysyttavanSananIndeksi()).getVastausHistoria().size(); i++){
            Integer type = opeteltavatsanat.get(getKysyttavanSananIndeksi()).getVastausHistoria().get(i).getKysType();
            kystypet.set(type,kystypet.get(type)+1);

        }

       //taulukosta  poimitaan vähiten kysytty tyyppi
        Integer vahiten_kysytty=9999999;
        Integer indx=0;
        for (int i = 0;i<kystypet.size();i++){
            if (kystypet.get(i)<vahiten_kysytty){
                vahiten_kysytty=kystypet.get(i);
                indx=i;
            }
        }

        setKysymystyyppi(indx);
        //palauta kysytävä sanapari ja kysymystyyppi


        if (kysymystyyppi ==0){
            Intent intent = new Intent(this.context, harjoittele1.class);
            intent.putExtra("indeksi",kysyttavanSananIndeksi);
            intent.putExtra("kystype",0);
            intent.putParcelableArrayListExtra("kysyttavienlista", opeteltavatsanat);

            ((Activity) context).startActivityForResult(intent,1);
        }

        else if (kysymystyyppi ==1){
            Intent intent = new Intent(this.context, harjoittele1.class);
            intent.putParcelableArrayListExtra("kysyttavienlista", opeteltavatsanat);
            intent.putExtra("indeksi",kysyttavanSananIndeksi);
            intent.putExtra("kystype",1);

            ((Activity) context).startActivityForResult(intent,1);
        }

        else if (kysymystyyppi ==2){
            Intent intent = new Intent(this.context, harjoittele2.class);
            intent.putParcelableArrayListExtra("kysyttavienlista", opeteltavatsanat);
            intent.putExtra("indeksi",kysyttavanSananIndeksi);
            intent.putExtra("kystype",2);

            ((Activity) context).startActivityForResult(intent,1);
        }

        else if (kysymystyyppi ==3){
            Intent intent = new Intent(this.context, harjoittele2.class);
            intent.putParcelableArrayListExtra("kysyttavienlista", opeteltavatsanat);
            intent.putExtra("indeksi",kysyttavanSananIndeksi);
            intent.putExtra("kystype",3);

            ((Activity) context).startActivityForResult(intent,1);
        }





    }

    public Integer getKysyttavanSananIndeksi() {
        return kysyttavanSananIndeksi;
    }

    private void setKysyttavanSananIndeksi(Integer kysyttavanSananIndeksi) {
        this.kysyttavanSananIndeksi = kysyttavanSananIndeksi;
    }

    public Integer getKysymystyyppi() {
        return kysymystyyppi;
    }

    private void setKysymystyyppi(Integer kysymystyyppi) {
        this.kysymystyyppi = kysymystyyppi;
    }
}



