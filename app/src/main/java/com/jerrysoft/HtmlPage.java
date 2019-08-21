package com.jerrysoft;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URL;

public class HtmlPage extends AppCompatActivity {
    private WebView webViewPage;
    private  String pageTitle;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_page);
        webViewPage = (WebView) findViewById(R.id.webView1);
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapseBar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle("Idoma Voice News");
        /* getActionBar().setDisplayHomeAsUpEnabled(true); */


        String webPage = getIntent().getStringExtra("htmlPage");

        //url page title
         pageTitle = getIntent().getStringExtra("htmlTitle");
        //webview settings
        WebSettings webSetting = webViewPage.getSettings();

        webSetting.setJavaScriptEnabled(true);
        webViewPage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewPage.getSettings().setSupportZoom(true);
        webViewPage.getSettings().setLoadsImagesAutomatically(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        webViewPage.getSettings().setAppCacheEnabled(true);
        webViewPage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSetting.setDomStorageEnabled(true); //it store web data at client side
        //***********************************************************************************************//
        webViewPage.setWebViewClient(new NewWebViewClient());
        webViewPage.loadUrl(webPage);


    }

    private class NewWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            collapsingToolbarLayout.setTitle(webViewPage.getTitle());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (url != null && url.startsWith("whatsapp://")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                return true;

            }
            if (url != null && url.startsWith("twitter://")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                return true;

            }
            if (url != null && url.contains("facebook://")) {

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(facebookIntent);
                Toast.makeText(HtmlPage.this, "Past is sucessful", Toast.LENGTH_SHORT).show();


                return true;

            }
            if (url != null && url.startsWith("tel:")) {
                // Intent callIntent = new Intent();
                view.getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(url)));
                return true;
            }

            try {
                if (url != null && url.startsWith("pinterest://")) {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return true;

                }
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(HtmlPage.this, "Pinterst client is not installed,", Toast.LENGTH_SHORT).show();
            }
            if (url.startsWith("smsto:")) {


                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;

            }

            try {
                // final Uri uri = request.getUrl()
                if (url.startsWith("mailto:") || url.startsWith("smsto:")) {

                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                    return true;

                }
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(HtmlPage.this, "There is no Email Client Installed", Toast.LENGTH_SHORT).show();
            }



            //return true;
            return false;

        }
    }

}
