package br.com.up.carrosup.domain;

@org.parceler.Parcel
public class Carro {

    public Long id;
    public String tipo;
    public String nome;
    public String desc;
    public String urlFoto;
    public String urlInfo;
    public String urlVideo;
    public String latitude;
    public String longitude;

    // Flag para a actiobar de contexto
    public boolean selected;

    public double getLatitude() {
        try {
            if(latitude == null) {
                return 0;
            }
            return Double.parseDouble(latitude);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public double getLongitude() {
        try {
            if(longitude == null) {
                return 0;
            }
            return Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Carro{" + "nome='" + nome + '\'' + ", desc='" + desc + '\'' + '}';
    }
}