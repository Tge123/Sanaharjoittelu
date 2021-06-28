package com.example.riktur.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;

/**
 * Luokka tilastoja varten
 */
public class tilastot extends Activity {


    private Classifier mClassifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tilastot);
        Context context = this;

        ArrayList<sanapari> vastaanotettu = this.getIntent().getParcelableArrayListExtra("osatut");


        TextView tv = (TextView) findViewById(R.id.Sanojaopittu);
        tv.setText("Sanoja opittu "+vastaanotettu.size()+" /5000");
        String[] mobileArray = new String[vastaanotettu.size()];

        for(int i =0;i<vastaanotettu.size();i++){
           String suomeksi= vastaanotettu.get(i).getSanaSuomeksi();
           String englanniksi = vastaanotettu.get(i).getSanaEnglanniksi();

            mobileArray[i] = suomeksi+" - "+englanniksi;
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);
        ListView listView = (ListView) findViewById(R.id.lw);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);

                vastaanotettu.get(position);

                Integer oikeita=0;
                Integer vaaria=0;
                Integer skipattu=0;

                for(int i =0;i<vastaanotettu.get(position).getVastausHistoria().size();i++){

                    if(vastaanotettu.get(position).getVastausHistoria().get(i).getVastasikoOikein()==0){
                        vaaria++;
                    }

                    else if(vastaanotettu.get(position).getVastausHistoria().get(i).getVastasikoOikein()==1){
                        oikeita++;
                    }
                    else{skipattu++;}

                }

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popupview);
                TextView txteka = (TextView)dialog.findViewById(R.id.sp);
                TextView txttoka = (TextView)dialog.findViewById(R.id.monestikko);
                TextView txtkolmas = (TextView)dialog.findViewById(R.id.oikeita);
                TextView txtneljas = (TextView)dialog.findViewById(R.id.vaaria);
                TextView txtviides = (TextView)dialog.findViewById(R.id.skip);

                txteka.setText(vastaanotettu.get(position).getSanaSuomeksi()+" - "+vastaanotettu.get(position).getSanaEnglanniksi());
                txttoka.setText("Sanaparia on kysytty "+ vastaanotettu.get(position).getVastausHistoria().size()+ " kertaa");
                txtkolmas.setText("Oikeita vastauksia on ollut " +oikeita+" kappaletta");
                txtneljas.setText("Vääriä vastauksia on ollut " +vaaria+" kappaletta");
                txtviides.setText("Sana on skipattu " +skipattu+" kertaa");
                dialog.show();



            }
        });




        //Luodaan toiminnot painikkeille
        Button button1 = (Button) findViewById(R.id.button6);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                finish();
            }
        });




        //TODO Pistä ennustukset toimimaan
        //predict();
    }


    private void predict(){
        AssetManager assetManager = getAssets();
        try {
            mClassifier = (Classifier) weka.core.SerializationHelper.read("model.model");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Weka "catch'em all!"
            e.printStackTrace();
        }
        Toast.makeText(this, "Model loaded.", Toast.LENGTH_SHORT).show();

        final Attribute ID= new Attribute("sanapariID");
        final Attribute monestikkokysyttey=new Attribute("monestikkokysyttey");
        final Attribute kauankoedellisesta=new Attribute("kauankoedellisesta");
        final Attribute kysymistyyppi=new Attribute("kysymistyyppi");
        final Attribute kysymispaiva=new Attribute("kysymispaiva");
        final Attribute vastasikooikein=new Attribute("vastasikooikein");



        ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
            {
                add(ID);
                add(monestikkokysyttey);
                add(kauankoedellisesta);
                add(kysymistyyppi);
                add(kysymispaiva);
                add(vastasikooikein);
            }
        };

        Instances dataUnpredicted = new Instances("TestInstances",
                attributeList, 1);




        dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

        Date currentdate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = dateFormat.format(currentdate);


        DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
            {
                setValue(ID, 1);
                setValue(monestikkokysyttey, 3);
                setValue(kauankoedellisesta, 48);
                setValue(kysymistyyppi, 2);
                setValue(kysymispaiva, 3);

            }
        };

        // reference to dataset
        newInstance.setDataset(dataUnpredicted);



        /*
        osw.write("@ATTRIBUTE suomeksi STRING\n");
        osw.write("@ATTRIBUTE englanniksi STRING\n");
        osw.write("@ATTRIBUTE sanapariID NUMERIC\n");
        osw.write("@ATTRIBUTE monestikkokysyttey NUMERIC\n");
        osw.write("@ATTRIBUTE kauankoedellisesta NUMERIC\n");
        osw.write("@ATTRIBUTE kysymistyyppi  NUMERIC\n");
        osw.write("@ATTRIBUTE kysymispaiva   DATE\n"); //\"yyyy-MM-dd HH:mm:ss\"
        osw.write("@ATTRIBUTE vastasikooikein  NUMERIC\n");
        */
    }

    private Instances StringToNominal(Instances dataset, String columnName) throws Exception {
        StringToNominal stringtoNominal = new StringToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = Integer.toString(dataset.classIndex()+2);  //this changes the Situation Type from String Into Nominal
        stringtoNominal.setOptions(options);
        stringtoNominal.setInputFormat(dataset);
        dataset = Filter.useFilter(dataset, stringtoNominal);

        return dataset;
    }




    }
