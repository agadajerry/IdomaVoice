package com.jerrysoft;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class IdomaVoiceHome extends AppCompatActivity {
    public WebView webView;
    //Context context;
    private SwipeRefreshLayout swipe;
    //checkBoxes
    private CheckBox clearCheck;

    //  private  AlertDialog alertDialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_idoma_voice_home);
        webView = (WebView) findViewById(R.id.webView);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipLayout1);


        //webview settings
        WebSettings webSetting = webView.getSettings();

        webSetting.setJavaScriptEnabled(true);

        //webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//load this when they is not internet connection from storage
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //***********************************************************************
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
// webView.getSettings().setAppCacheEnabled(true);


        //webSetting.setDomStorageEnabled(true); //it store web data at client side
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        webView.loadUrl("http://www.idomavoice.com");
        loadCache();//load history
       checkInternetConnection();//check network connection
        //check internet connection before loading url
       /* if (checkInternetConnection()) {

            swipe.setRefreshing(true); i will implement webpage to refresh it self using
            new Timer().shedule(new TimerTask(){...})

        }*/


//**********************************************************************************************************

        webView.setWebViewClient(new MyWebViewClient());


        //url is to loaded first

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reLoadWeb();


            }
        });

    }//end of  onCreate method


    //**********************************************************************************************************




    //**********************************************************************************************

    //back button click notification for exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
          this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click again to Exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            public void run(){
                doubleBackToExitPressedOnce = false;
            }
        },2000);



    }
    //**********************************************************************************************

    public class MyWebViewClient extends WebViewClient {

//page start


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                swipe.setRefreshing(true);



        }

        //stop refreshing when page finished loading
        public void onPageFinished(WebView view, String url) {
            //setting the title of the web page on the title bar
            //IdomaVoiceHome.this.setTitle(webView.getTitle());
            swipe.setRefreshing(false);


        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String discription, String failUrl) {

            Toast toast = Toast.makeText(getBaseContext(), "Check your internet connection", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();


        }

        //**********************************************************************************************

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.contains("http://www.idomavoice.com/")) {
                Intent intent = new Intent(IdomaVoiceHome.this, HtmlPage.class);
                intent.putExtra("htmlPage", Uri.parse(url).toString());
                intent.putExtra("htmlTitle", webView.getTitle());
                startActivity(intent);
                return true;
            }
            return false;
        }


    }//end of web view client class
    //**********************************************************************************************************

    //method that handle web refreshing
    public void reLoadWeb() {


        webView.loadUrl("http://www.idomavoice.com");
        swipe.setRefreshing(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item selection event handling
        switch (item.getItemId()) {

            case R.id.refresh:
                reLoadWeb();
                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, " download Idoma Voice news App");
                shareIntent.putExtra(Intent.EXTRA_TEXT, " Download idoma news App Via: http://www.idomavoice.com/download-app");
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.whatsapp");

                startActivity(shareIntent);
                break;
            case R.id.exit:
                // exit dialog notification
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Idoma Voice news App");
                alertDialog.setMessage("Do You Want To Exit ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();//recalling the class back
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.create();
                alertDialog.show();
                break;
            //**********************************************************************************************
            case R.id.settings:

                final Dialog settingDialog = new Dialog(this);
                settingDialog.setContentView(R.layout.activity_idoma_voice_setting);

                clearCheck = (CheckBox) settingDialog.findViewById(R.id.enableClear);
                Button btn = (Button) settingDialog.findViewById(R.id.textListener);
                settingDialog.setTitle("Setting");


                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (clearCheck.isChecked() == true) {
                            webView.clearCache(true);
                            Toast.makeText(IdomaVoiceHome.this, "History cleared", Toast.LENGTH_LONG).show();
                            //  swipe.setRefreshing(true);

                        }
                        settingDialog.dismiss();
                    }

                });

                settingDialog.show();


        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            IdomaVoiceHome.this.setTitle("Idoma Voice news Paper");
            Toast.makeText(IdomaVoiceHome.this, "Your are currently Offline", Toast.LENGTH_LONG).show();


            return false;
        } else {

            return true;
        }

    }

    public void loadCache() {
        File dir = getCacheDir();
        if (!dir.exists()) {
            dir.mkdir();
        }
        webView.getSettings().setAppCachePath(dir.getPath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

}

