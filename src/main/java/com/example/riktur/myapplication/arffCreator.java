package com.example.riktur.myapplication;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class arffCreator {


    sanapari sanapari;
    ArrayList<sanapari> listattu;

    public arffCreator(ArrayList<com.example.riktur.myapplication.sanapari> listattu) {
        this.listattu = listattu;
    }

    public arffCreator(com.example.riktur.myapplication.sanapari sanapari) {
        this.sanapari = sanapari;
    }

    public void generatefullarff(){

        if(listattu!=null) {
            String suomeksi;
            String englanniksi;
            Integer kysymistyyppi;
            Integer olikooikein;
            Date milloinkysytty;

            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
            directory.mkdirs();

//Now create the file in the above directory and write the contents into it
            File file = new File(directory, "full.arff");
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            try {
                osw.write("@RELATION sp\n");
                osw.write("@ATTRIBUTE suomeksi STRING\n");
                osw.write("@ATTRIBUTE englanniksi STRING\n");
                osw.write("@ATTRIBUTE sanapariID NUMERIC\n");
                osw.write("@ATTRIBUTE monestikkokysyttey NUMERIC\n");
                osw.write("@ATTRIBUTE kauankoedellisesta NUMERIC\n");
                osw.write("@ATTRIBUTE kysymistyyppi  NUMERIC\n");
                osw.write("@ATTRIBUTE kysymispaiva   DATE\n"); //\"yyyy-MM-dd HH:mm:ss\"
                osw.write("@ATTRIBUTE vastasikooikein  NUMERIC\n");

                osw.write("@DATA\n");

                for (int s = 0; s < listattu.size(); s++) {
                    sanapari = listattu.get(s);

                    for (int i = 0; i < sanapari.getVastausHistoria().size(); i++) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String strDate = dateFormat.format(sanapari.getVastausHistoria().get(i).getKysymisPaiva());
                        Long erotus = Long.valueOf(0);

                        if(i!=0){
                           erotus = (sanapari.getVastausHistoria().get(i).getKysymisPaiva().getTime()-sanapari.getVastausHistoria().get(i-1).getKysymisPaiva().getTime())/1000;

                        }

                        osw.write("\""+sanapari.getSanaSuomeksi() + "\"," +
                                "\""+sanapari.getSanaEnglanniksi() + "\"," +
                                s+","+/*ID*/
                                i+","+/*Monestikko kysytty*/
                                erotus+","+
                                sanapari.getVastausHistoria().get(i).getKysType() + "," +
                                strDate + "," +
                                sanapari.getVastausHistoria().get(i).getVastasikoOikein() + "\n");
                    }
                }




                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateARFF(){


        String suomeksi;
        String englanniksi;

        Integer kysymistyyppi;
        Integer olikooikein;
        Date milloinkysytty;





        //This will get the SD Card directory and create a folder named MyFiles in it.
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
        directory.mkdirs();

//Now create the file in the above directory and write the contents into it
        File file = new File(directory, "mysdfile.txt");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        try {
            osw.write("@RELATION sp\n");
            osw.write("@ATTRIBUTE suomeksi STRING\n");
            osw.write("@ATTRIBUTE englanniksi STRING\n");
            osw.write("@ATTRIBUTE kysymistyyppi  NUMERIC\n");
            osw.write("@ATTRIBUTE kysymispaiva  DATE\n");
            osw.write("@ATTRIBUTE vastasikooikein  NUMERIC\n");

            osw.write("@DATA\n");

            for(int i =0;i<sanapari.getVastausHistoria().size();i++){
                osw.write(  sanapari.getSanaSuomeksi()+","+
                                sanapari.getSanaEnglanniksi()+","+
                                sanapari.getVastausHistoria().get(i).getKysType()+","+
                                sanapari.getVastausHistoria().get(i).getKysymisPaiva().toString()+","+
                                sanapari.getVastausHistoria().get(i).getVastasikoOikein()+"\n");

            }

            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }





}
