package com.example.riktur.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


/**
 * Ohjelman pääluokka, käynistyy ohjelmaa avatessa
 */
public class MainActivity extends Activity {



    public ArrayList<sanapari> osatutsanat;
    public  ArrayList<sanapari> opeteltavatsanat;


    //Mitä tehdään kun ohjelma pysäytettään
    @Override
    protected void onStop() {
        super.onStop();
        //tallenna_muistiin();
    }


    //Mitä tehdään kun ohjelma tuhotaan
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //tallenna_muistiin();
    }


    /*
     *Mitä tehdään kun ohjelma luodaan
     * Oppettelu ja uusien sanojen lisääminen palauttavat opeteltavien sanojen listan,
     * joka käsitelläään tässä ja tallennetaan muistiin.
     *
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Kysytään käyttäjältä lupaa tiedostojen tallentamiseen



        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        setContentView(R.layout.activity_main);


        //Luodaan toiminnot Harjoittele painikkeelle
        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //Jos harjoiteltavien sanojen listaa ei vielä ole tai harjoiteltavia sanoja on alle 10,
                // niin lisätään harjoiteltavia sanoja listalle kutsumalla funktiota hae5000sananlistastasanapari()
                if(opeteltavatsanat==null){
                    Log.d("harjoittelu_start", "opeteltavat=null");
                        hae5000sananlistastasanapari();
                }
                else if(opeteltavatsanat.size()<11){
                    Log.d("harjoittelu_start", "opeteltavat<11");
                        hae5000sananlistastasanapari();
                }


                //Kun listalla on riittävästi sanoja niin käynnistetään kysymysten kysely luomalla uusi kysymyksenvalinta olio
                kysymyksenvalinta kysval = new kysymyksenvalinta(opeteltavatsanat,MainActivity.this);




            }

        });

        //Lisää sanoja painikkeen toiminnot
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, lisaasanoja.class);
                intent.putParcelableArrayListExtra("kysyttavienlista", opeteltavatsanat);

                startActivityForResult(intent,1);

            }
        });

        //Tilastot painikkeen toiminnot
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, tilastot.class));


                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                arffCreator a = new arffCreator(osatutsanat);
                a.generatefullarff();

                Log.d("polku",   MainActivity.this.getFilesDir().getAbsolutePath());

                Intent intent = new Intent(MainActivity.this, tilastot.class);
                intent.putParcelableArrayListExtra("osatut", osatutsanat);
                startActivity(intent);
            }
        });

        //Lopetus painikkeen toiminnot
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //android.os.Process.killProcess(android.os.Process.myPid());
                tallenna_muistiin();
                finish();
                System.exit(0);

            }
        });

        //Luetaan tiedosto
        osatutsanat = readfile("osatut", this);
        opeteltavatsanat = readfile("opeteltavat", this);

        if(osatutsanat==null){
            osatutsanat=new ArrayList<sanapari>();

        }


    }




    //Tämä funktio avaa puhelimen muistiin tallennetut sanalistat
    public ArrayList<sanapari> readfile(String fileName, Context context) {
        ArrayList<sanapari> luettutiedosto = null;

        try {
            FileInputStream fis = context.openFileInput(fileName);
            if (fis != null) {
                ObjectInputStream is = new ObjectInputStream(fis);
                luettutiedosto = (ArrayList<sanapari>) is.readObject();
                is.close();
                fis.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("tiedoston avaus", "Tiedostoa ei löydy" + e.toString());
            return null;
        } catch (IOException e) {
            Log.e("tiedoston avaus", "Tiedostoa ei voida lukea" + e.toString());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e("tiedoston avaus", "Väärä olio" + e.toString());
            e.printStackTrace();
            return null;
        }
        Log.d("Tiedoston avaaminen", "Tiedosto avattiin onnistuneesti");
        return luettutiedosto;
    }


    //Tämä funktio tallentaa parametrina saadun listan puhelimen muistiin
    private void writeToFile(String fileName,Context context, ArrayList<sanapari> tallennettava) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(tallennettava);
            os.close();
            fos.close();
            Log.d("Save", "Tallentaminen onnistui");
        }
        catch (IOException e) {
            Log.e("Exception", "Tiedostoon tallentaminen epäonnistui: " + e.toString());
        }
    }


    //Tämä on "Koonti funktio" jonka avulla voidaan tallentaa kaikki käytettävät sanalistat yhdellä kertaa
    private void tallenna_muistiin(){
        writeToFile("osatut",this, osatutsanat);
        writeToFile("opeteltavat",this, opeteltavatsanat);

    }


    //Tämä funktio avaa 5000-words_kaannetty.xlsx nimisen tiedoston, joka sisältää 5000 kpl englanti-suomi sanapareja
    //Listalta poimitaan summassa sanapareja kunnes opeteltavien listaan on kooltaan 10 sanaparia.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hae5000sananlistastasanapari(){
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(getAssets().open("5000-words_kaannetty.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheetAt(0);

        int i;

        if(opeteltavatsanat==null){
            i=0;
        }
        else{
            i=opeteltavatsanat.size();
        }

        for(;i<11;i++) {


            Random rand = new Random();
            int randomNum = rand.nextInt(4999);

            Row row = sheet.getRow(randomNum);

            String Englanniksi = row.getCell(0).toString();
            String Suomeksi = row.getCell(1).toString();

            sanapari uusisana = new sanapari(Suomeksi, Englanniksi, false);

            if (opeteltavatsanat == null) {
                opeteltavatsanat = new ArrayList<sanapari>();
            }
            opeteltavatsanat.add(uusisana);
            Log.d("sanahaku", "Haettiin sana: " + uusisana.getSanaSuomeksi());
        }
    }



    /*
    * Tässä Funktiossa käsitellään Harjoittele1 ja Harjoittele2 luokkien palauttama tieto.
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Boolean jatketaanko=null;
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<sanapari> vastaanotettu = data.getParcelableArrayListExtra("palautettu_opeteltavat");
                jatketaanko = data.getBooleanExtra("jatketaanko",false);
                Log.d("intent", "Palattiin onnistuneesti : "+ vastaanotettu.size());
                opeteltavatsanat=vastaanotettu;


                //Käydään läpi opeteltavien sanojen lista
                for(int i = 0;i<opeteltavatsanat.size();i++){
                    Integer oikeat =0;

                    //Jos sanan vastaushistoriassa on enemmän kuin 3 merkintää
                    if(opeteltavatsanat.get(i).getVastausHistoria().size()>3){
                        ArrayList<historyelement> hisel = opeteltavatsanat.get(i).getVastausHistoria();
                        for(int e=0;e<hisel.size();e++){
                            if(hisel.get(e).getVastasikoOikein()==1){
                                oikeat++;

                            }


                        }
                        if(oikeat > 3){

                            osatutsanat.add(opeteltavatsanat.get(i));
                            opeteltavatsanat.remove(i);
                        }
                    }


                    /*
                    for(int e =0;e<opeteltavatsanat.get(i).getVastausHistoria().size() ;e++){
                        if(opeteltavatsanat.get(i).getVastausHistoria().size()>3){



                            if(opeteltavatsanat.get(i).getVastausHistoria().get(opeteltavatsanat.get(i).getVastausHistoria().size()-1-e).getVastasikoOikein()==1){
                                oikeat++;

                            }
                        }
                    }
                    */



                }

                if(opeteltavatsanat.size()<10){
                    hae5000sananlistastasanapari();
                }

                tallenna_muistiin();
            }

            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        if(jatketaanko==true){
            kysymyksenvalinta kysval = new kysymyksenvalinta(opeteltavatsanat,MainActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Käyttöoikeuksia ei myönnetty. Ohjelma ei ehkä toimi oikein", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



}