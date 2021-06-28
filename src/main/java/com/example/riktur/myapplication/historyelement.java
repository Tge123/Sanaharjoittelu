package com.example.riktur.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class historyelement implements Parcelable, Serializable {
    private Date kysymisPaiva;

    /*
    * kysType pitää kirjaa siitä millä tavalla sanaa kysyttiin
    * 0=suomesta englanniksi valinta
    * 1=englannista suomeksi valinta
    * 2=suomesta englanniksi kirjoitus
    * 3=englannista suomeksi kirjoitus
    * */
    private Integer kysType;

    /*
    *vastasikoOikein tallentaa tiedon miten vastattiin
    * 0=ei vastannut oikein
    * 1=vastasi oikein
    * 2=skippasi
    * */

    private Integer vastasikoOikein;



    public  historyelement(Date kysymishetki, Integer kysymistype, Integer vastasikooikein){
        kysymisPaiva=kysymishetki;
        kysType= kysymistype;
        vastasikoOikein=vastasikooikein;
    }


    public Date getKysymisPaiva() {
        return kysymisPaiva;
    }


    public Integer getVastasikoOikein() {
        return vastasikoOikein;
    }

    public Integer getKysType() {
        return kysType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.kysymisPaiva != null ? this.kysymisPaiva.getTime() : -1);
        dest.writeValue(this.kysType);
        dest.writeValue(this.vastasikoOikein);
    }

    protected historyelement(Parcel in) {
        long tmpKysymisPaiva = in.readLong();
        this.kysymisPaiva = tmpKysymisPaiva == -1 ? null : new Date(tmpKysymisPaiva);
        this.kysType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.vastasikoOikein = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<historyelement> CREATOR = new Parcelable.Creator<historyelement>() {
        @Override
        public historyelement createFromParcel(Parcel source) {
            return new historyelement(source);
        }

        @Override
        public historyelement[] newArray(int size) {
            return new historyelement[size];
        }
    };
}
