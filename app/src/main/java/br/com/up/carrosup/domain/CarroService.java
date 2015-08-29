package br.com.up.carrosup.domain;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.up.carrosup.rest.Response;
import br.com.up.carrosup.rest.ResponseWithURL;
import br.com.up.carrosup.rest.Retrofit;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.IOUtils;

public class CarroService {
    private static final String URL_BASE = "http://livrowebservices.com.br/rest/carros";
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroRest";
    public static List<Carro> getCarros(Context context, String tipo) throws IOException {
        String url = URL_BASE + "/tipo/" + tipo;
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Carro> carros = parserJSON(context, json);
        // Ou com Retrofit.
        // List<Carro> carros = Retrofit.getCarroRest().getCarros(tipo);
        return carros;
    }
    private static List<Carro> parserJSON(Context context, String json) throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Carro>>() {}.getType();
        List<Carro> carros = new Gson().fromJson(json, listType);
        return carros;
    }

    public static Response save(Context context,Carro carro) throws IOException {
        // Converte o carro para JSON
        String jsonCarro = new Gson().toJson(carro);
        Log.d(TAG,"> save: " + jsonCarro);
        HttpHelper http = new HttpHelper();
        http.setContentType("application/json; charset=utf-8");
        // HTTP POST
        String json = http.doPost(URL_BASE,jsonCarro.getBytes(),"UTF-8");
        Log.d(TAG,"<< save response: " + json);
        // Response
        Response response = new Gson().fromJson(json, Response.class);
        return response;
    }

    public static boolean delete(Context context, List<Carro> selectedCarros) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        http.setContentType("application/json; charset=utf-8");
        for (Carro c : selectedCarros) {
            // URL para excluir o carro
            String url = URL_BASE + "/" + c.id;
            Log.d(TAG,"Delete carro: " + url);
            // Request HTTP
            String json = http.doDelete(url);
            Log.d(TAG,"JSON delete: " + json);
            // Parser do JSON
            Gson gson = new Gson();
            Response response = gson.fromJson(json, Response.class);
            if(response.isOk()) {
                throw new IOException("Erro ao excluir: " + response.getMsg());
            }
        }
        // A fazer
        return true;
    }

    public static ResponseWithURL postFotoBase64(Context context,File file) throws IOException {
        String url = URL_BASE + "/postFotoBase64";
        Log.d(TAG,"postFotoBase64 File: " + file);
        // Converte para Base64
        byte[] bytes = IOUtils.toBytes(new FileInputStream(file));
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
        // Parâmetros chave=valor
        Map<String,String> params = new HashMap<String,String>();
        params.put("fileName", file.getName());
        params.put("base64", base64);
        // POST
        Log.d(TAG,">> postFotoBase64: " + params);
        HttpHelper http = new HttpHelper();
        http.setContentType("application/x-www-form-urlencoded");
        http.setCharsetToEncode("UTF-8");
        String json = http.doPost(url, params, "UTF-8");
        Log.d(TAG,"<< postFotoBase64: " + json);
        // Parser JSON
        ResponseWithURL response = new Gson().fromJson(json, ResponseWithURL.class);
        Log.d(TAG,"ResponseWithURL: " + response);
        return response;
    }

    public static List<Carro> seachByNome(Context context, String nome) throws IOException {
        String url = URL_BASE + "/nome/" + nome;
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Carro> carros = parserJSON(context, json);
        return carros;
    }
}