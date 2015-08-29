package br.com.up.carrosup.activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import org.parceler.Parcels;
import java.io.File;
import br.com.up.carrosup.R;
import br.com.up.carrosup.domain.Carro;
import br.com.up.carrosup.fragment.CarroEditFragment;
import br.com.up.carrosup.fragment.CarroFragment;
import br.com.up.carrosup.utils.ImageUtils;
public class CarroActivity extends BaseActivity {
    CollapsingToolbarLayout collapsingToolbar;
    private Carro carro;
    private ImageView appBarImg;
    private ClickHeaderListener clickHeaderListener;
    public interface ClickHeaderListener{
        void onHeaderClicked();
    }
    public void setClickHeaderListener(ClickHeaderListener clickHeaderListener) {
        this.clickHeaderListener = clickHeaderListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Título da CollapsingToolbarLayout
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Header
        appBarImg = (ImageView) findViewById(R.id.appBarImg);
        appBarImg.setOnClickListener(onClickImgHeader());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.header_appbar);
        // Palleta cores
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });
        // Carro dos argumentos
        this.carro = Parcels.unwrap(getIntent().getExtras().getParcelable("carro"));
        final boolean editMode = getIntent().getBooleanExtra("editMode", false);
        // Mostra as informações do carro no header (foto)
        setAppBarInfo(carro);
        // FAB
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Você pode favoritar esse carro...");
            }
        });
        // Adiciona o Fragment com os detalhes carro
        if (savedInstanceState == null) {
            //CarroFragment frag = new CarroFragment();
            CarroFragment frag = editMode ? new CarroEditFragment() : new CarroFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrag, frag, "frag").commit();
        }
    }
    private View.OnClickListener onClickImgHeader() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickHeaderListener != null) {
                    // Delegate para notificar o fragment que teve clique.
                    clickHeaderListener.onHeaderClicked();
                }
            }
        };
    }
    public void setImage(Bitmap bitmap) {
        if(bitmap != null) {
            appBarImg.setImageBitmap(bitmap);
        }
    }
    public void setAppBarInfo(Carro c) {
        if (c != null) {
            String nome = c.nome;
            String url = c.urlFoto;
            collapsingToolbar.setTitle(nome);
            setImage(url);
        } else {
            // Novo Carro
            collapsingToolbar.setTitle(getString(R.string.novo_carro));
        }
    }
    public void setImage(String url) {
        ImageUtils.setImage(this, url, appBarImg);
    }
    public void setImage(File file) {
        ImageUtils.setImage(this, file, appBarImg);
    }
}