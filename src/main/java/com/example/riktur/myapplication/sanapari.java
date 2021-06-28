package com.example.riktur.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class sanapari implements Parcelable, Serializable {

   private String sanaSuomeksi;
    private String sanaEnglanniksi;

    private Integer monestikkoKysytty;

    private Date milloinViimeksiKysytty;

    private Long milloinKysytaanSeuraavanKerran;

    //Tieto siitä onko käyttäjä itse lisännyt sanan vai onko sana haettu valmiista listasta
    private boolean onkoItseLisatty;


    //ArrayList johon tallennetaan sanan kysymispäivämäärä ja tieto siitä onko käyttäjä vastannut oikein vai väärin
    private ArrayList<historyelement> vastausHistoria;




    public  sanapari(String suomeksi,String englanniksi, Boolean lisattyitse){
        setSanaSuomeksi(suomeksi);
        setSanaEnglanniksi(englanniksi);
        setMonestikkoKysytty(0);
        setMilloinViimeksiKysytty(null);
        setMilloinKysytaanSeuraavanKerran(null);
        setOnkoItseLisatty(lisattyitse);

        //Määritetään ArrayList
        setVastausHistoria(new  ArrayList<historyelement>());

    }


    /*
    * Päivitetään monestikko kysytty laskuria
    * päivitetään viimeisin kysymyspäivä
    * lisätään vastauksen tiedot historialogiin
    * */
    private void paivitaTilastot(Date kysymishetki, Integer kysymistyyppi, Integer vastasikomiten){

        setMonestikkoKysytty(getMonestikkoKysytty() + 1);
        setMilloinViimeksiKysytty(new Date());

        historyelement element = new historyelement(kysymishetki,kysymistyyppi,vastasikomiten);
        getVastausHistoria().add(element);

        laskeSeuraavaKysymyspaiva();
    }

    private void laskeSeuraavaKysymyspaiva() {

        //TODO Toteuta funkio joka laskee sanaparille seuraavan kysymyspäivän


        //Jos sana kysytään ensimmäisen kerran ja sana ei ole itse lisätty niin käyttäjä todennäköisesti osaa sanan

        //Jos sana on itse lisätty ja se löytyy myös 5000listasta niin sanaa ei suoraan viedä osattujen osioon

        //Jos vastaus on oikein niin kasvata aikaa seuraavaan kysymykseen

        //jos vastaus on väärin/skipattu niin lyhennä kysymisväliä


    }


    public String getSanaSuomeksi() {
        return sanaSuomeksi;
    }

    public void setSanaSuomeksi(String sanaSuomeksi) {
        this.sanaSuomeksi = sanaSuomeksi;
    }

    public String getSanaEnglanniksi() {
        return sanaEnglanniksi;
    }

    public void setSanaEnglanniksi(String sanaEnglanniksi) {
        this.sanaEnglanniksi = sanaEnglanniksi;
    }

    public Integer getMonestikkoKysytty() {
        return monestikkoKysytty;
    }

    public void setMonestikkoKysytty(Integer monestikkoKysytty) {
        this.monestikkoKysytty = monestikkoKysytty;
    }

    public Date getMilloinViimeksiKysytty() {
        return milloinViimeksiKysytty;
    }

    public void setMilloinViimeksiKysytty(Date milloinViimeksiKysytty) {
        this.milloinViimeksiKysytty = milloinViimeksiKysytty;
    }

    public Long getMilloinKysytaanSeuraavanKerran() {
        return milloinKysytaanSeuraavanKerran;
    }

    public void setMilloinKysytaanSeuraavanKerran(Long milloinKysytaanSeuraavanKerran) {
        this.milloinKysytaanSeuraavanKerran = milloinKysytaanSeuraavanKerran;
    }

    public boolean isOnkoItseLisatty() {
        return onkoItseLisatty;
    }

    public void setOnkoItseLisatty(boolean onkoItseLisatty) {
        this.onkoItseLisatty = onkoItseLisatty;
    }

    public ArrayList<historyelement> getVastausHistoria() {
        return vastausHistoria;
    }

    public void setVastausHistoria(ArrayList<historyelement> vastausHistoria) {
        this.vastausHistoria = vastausHistoria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sanaSuomeksi);
        dest.writeString(this.sanaEnglanniksi);
        dest.writeValue(this.monestikkoKysytty);
        dest.writeLong(this.milloinViimeksiKysytty != null ? this.milloinViimeksiKysytty.getTime() : -1);
        dest.writeValue(this.milloinKysytaanSeuraavanKerran);
        dest.writeByte(this.onkoItseLisatty ? (byte) 1 : (byte) 0);
        dest.writeList(this.vastausHistoria);
    }

    protected sanapari(Parcel in) {
        this.sanaSuomeksi = in.readString();
        this.sanaEnglanniksi = in.readString();
        this.monestikkoKysytty = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpMilloinViimeksiKysytty = in.readLong();
        this.milloinViimeksiKysytty = tmpMilloinViimeksiKysytty == -1 ? null : new Date(tmpMilloinViimeksiKysytty);
        this.milloinKysytaanSeuraavanKerran = (Long) in.readValue(Long.class.getClassLoader());
        this.onkoItseLisatty = in.readByte() != 0;
        this.vastausHistoria = new ArrayList<historyelement>();
        in.readList(this.vastausHistoria, historyelement.class.getClassLoader());
    }

    public static final Parcelable.Creator<sanapari> CREATOR = new Parcelable.Creator<sanapari>() {
        @Override
        public sanapari createFromParcel(Parcel source) {
            return new sanapari(source);
        }

        @Override
        public sanapari[] newArray(int size) {
            return new sanapari[size];
        }
    };
}
