package xyz.devdeejay.notespace.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.devdeejay.notespace.R;
import xyz.devdeejay.notespace.utilities.Constants;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        android.webkit.WebView webView = findViewById(R.id.webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra(Constants.URL);
        String title = intent.getStringExtra(Constants.TITLE);

        if (isNetworkAvailable()) {
            webView.loadUrl(url);
        } else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "We need Internet Connectivity For This", Snackbar.LENGTH_INDEFINITE);
            snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white));
            snackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            snackBar.show();
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if ((networkInfo != null) && (networkInfo.isConnected()))
            isAvailable = true;
        return isAvailable;
    }
}
