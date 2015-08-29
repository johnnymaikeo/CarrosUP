package br.com.up.carrosup.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import br.com.up.carrosup.R;

public class SobreActivity extends BaseActivity {
    private static final String URL_SOBRE = "http://www.livroandroid.com.br/sobre.htm";
    private WebView webview;
    private ProgressBar progress;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sobre);

        setupToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.webview);
        progress = (ProgressBar) findViewById(R.id.progress);
        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        // Carrega a página
        setupWebViewClient();
        webview.loadUrl(URL_SOBRE);
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview.reload();
            }
        };
    }

    private void setupWebViewClient() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webview, String url, Bitmap favicon) {
                super.onPageStarted(webview, url, favicon);
                // Liga o progress
                progress.setVisibility(View.VISIBLE);
                // Termina a animação do Swipe To Refresh
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onPageFinished(WebView webview, String url) {
                // Desliga o progress
                progress.setVisibility(View.INVISIBLE);
                // Termina a animação do Swipe To Refresh
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
