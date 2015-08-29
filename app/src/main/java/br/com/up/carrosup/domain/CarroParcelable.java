package br.com.up.carrosup.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class CarroParcelable implements Parcelable {

    public Long id;
    public String tipo;
    public String nome;
    public String desc;
    public String urlFoto;
    public String urlInfo;
    public String urlVideo;
    public String latitude;
    public String longitude;

    public CarroParcelable() {

    }

    public CarroParcelable(Parcel parcel) {
        this.id = parcel.readLong();
        this.tipo = parcel.readString();
        this.nome = parcel.readString();
        this.desc = parcel.readString();
        this.urlFoto = parcel.readString();
        this.urlVideo = parcel.readString();
        this.urlInfo = parcel.readString();
        this.latitude = parcel.readString();
        this.longitude = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.tipo);
        dest.writeString(this.nome);
        dest.writeString(this.desc);
        dest.writeString(this.urlFoto);
        dest.writeString(this.urlVideo);
        dest.writeString(this.urlInfo);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public static final Creator<CarroParcelable> CREATOR = new Creator<CarroParcelable>() {
        @Override
        public CarroParcelable createFromParcel(Parcel source) {
            return new CarroParcelable(source);
        }

        @Override
        public CarroParcelable[] newArray(int size) {
            return new CarroParcelable[size];
        }
    };

    @Override
    public String toString() {
        return "Carro{" + "nome='" + nome + '\'' + ", desc='" + desc + '\'' + '}';
    }
}