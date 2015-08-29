package br.com.up.carrosup.fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import org.parceler.Parcels;

import java.io.File;

import br.com.up.carrosup.R;
import br.com.up.carrosup.activity.CarroActivity;
import br.com.up.carrosup.domain.Carro;
import br.com.up.carrosup.domain.CarroService;
import br.com.up.carrosup.rest.ResponseWithURL;
import br.com.up.carrosup.utils.BroadcastUtil;
import br.com.up.carrosup.utils.CameraUtil;
import livroandroid.lib.utils.GooglePlayServicesHelper;


public class CarroEditFragment extends CarroFragment implements LocationListener, CarroActivity.ClickHeaderListener {
    // Views que só existem na tela de edição
    protected TextView tLat;
    protected TextView tLng;
    protected RadioGroup tTipo;
    protected TextView tUrlVideo;
    private GooglePlayServicesHelper gps;
    private CameraUtil camera = new CameraUtil();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligar o Google Play Services
        if(carro == null) {
            // Se não existe carro, liga GPS
            gps = new GooglePlayServicesHelper(getContext(), true);
        }
        // Camera
        camera.onCreate(savedInstanceState);
        // Click no header da appbar
        CarroActivity activity = (CarroActivity) getActivity();
        activity.setClickHeaderListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Conecta no Google Play Services
        if(gps != null) {
            gps.onResume(this);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        // Desconecta no Google Play Services
        if(gps != null) {
            gps.onPause();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        camera.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carro_edit, container, false);
        setHasOptionsMenu(true);
        initViews(view);
        return view;
    }
    @Override
    protected void initViews(View view) {
        super.initViews(view);
        // Views que só existem na tela de edição
        tTipo = (RadioGroup) view.findViewById(R.id.radioTipo);
        tUrlVideo = (TextView) view.findViewById(R.id.tUrlVideo);
        tLat = (TextView) view.findViewById(R.id.tLat);
        tLng = (TextView) view.findViewById(R.id.tLng);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_edit_carro, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_salvar) {
            // Salva ou atualiza o carro
            if (carro == null) {
                // Cria um novo carro
                carro = new Carro();
                carro.tipo = "esportivos";
            }
            // Valida campos preenchidos
            boolean formOk = validate(tNome, tDesc);
            if (formOk) {
                // Copia os dados digitadospara o objeto caro
                carro.nome = tNome.getText().toString();
                carro.desc = tDesc.getText().toString();
                carro.latitude = tLat.getText().toString();
                carro.longitude = tLng.getText().toString();
                carro.urlVideo = tUrlVideo.getText().toString();
                carro.tipo = getTipo();
                Log.d(TAG, "Salvar carro tipo: " + carro.tipo);
                // Dispara a thread para salvar
                startTask("salvar", taskSaveCarro());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validate(TextView... array) {
        for (TextView t : array) {
            String s = t.getText().toString();
            if (s == null || s.trim().length() == 0) {
                t.setError(getString(R.string.msg_error_campo_obrigatorio));
                return false;
            }
        }
        return true;
    }
    private BaseTask taskSaveCarro() {
        return new BaseTask() {
            @Override
            public Object execute() throws Exception {
                // Upload da foto
                File file = camera.getFile();
                if (file != null) {
                    ResponseWithURL response = CarroService.postFotoBase64(getContext(), file);
                    if (response != null && response.isOk()) {
                        // Atualiza a foto do carro
                        carro.urlFoto = response.getUrl();
                    }
                }
                // Salva o carro
                CarroService.save(getContext(), carro);
                // Envia mensagem para lista de carros.
                Intent intent = new Intent(BroadcastUtil.ACTION_CARRO_SALVO);
                intent.putExtra("carro", Parcels.wrap(carro));
                BroadcastUtil.broadcast(getContext(), intent);
                getActivity().finish();
                return null;
            }
            @Override
            public void updateView(Object object) {
            }
        };
    }
    // Retorna o tipo em string conforme marcado no RadioGroup
    protected String getTipo() {
        if(tTipo != null) {
            int id = tTipo.getCheckedRadioButtonId();
            switch (id) {
                case R.id.tipoClassico:
                    return "classicos";
                case R.id.tipoEsportivo:
                    return "esportivos";
                case R.id.tipoLuxo:
                    return "luxo";
            }
        }
        return "classicos";
    }

    @Override
    public void onLocationChanged(Location location) {
        // Atualiza os campos lat/lng do formulário
        tLat.setText(String.valueOf(location.getLatitude()));
        tLng.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onHeaderClicked() {
        // Tirar a foto
        // Cria o o arquivo no sdcard
        long ms = System.currentTimeMillis();
        String fileName = String.format("foto_carro_%s_%s.jpg", carro != null ? carro.id : ms, ms);
        Intent intent = camera.open(fileName);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            // Resize da imagem
            Bitmap bitmap = camera.getBitmap(600,600);
            if(bitmap != null) {
                // Salva arquivo neste tamanho
                camera.save(bitmap);
                // Atualiza imagem do Header
                CarroActivity activity = (CarroActivity) getActivity();
                activity.setImage(bitmap);
            }
        }
    }
}