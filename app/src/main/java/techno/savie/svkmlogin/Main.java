package techno.savie.svkmlogin;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import javax.net.ssl.HttpsURLConnection;

public class Main extends AppCompatActivity {
    Button mSendButton;
    EditText mName,mLink;

    public static final String APPNAME = "SVKMlogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gettrust();
        mSendButton = (Button) findViewById(R.id.button);
        mName = (EditText) findViewById(R.id.editText);
        mLink = (EditText) findViewById(R.id.editText2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Just a place holder", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public static void gettrust(){try {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }catch(Exception e){

    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void send(View view){
        String name = mName.getText().toString();
        GetHtmlSource gs = new GetHtmlSource(this);
        gs.execute(mLink.getText().toString(),"573981400"+name,"tae1@ta%p");

    }
}
class GetHtmlSource extends AsyncTask<String,Void,String>{
    String code;
    URL url;
    String method,urs;
    String response = "";
    Context c;
    GetHtmlSource(Context context){
        c = context;
    }
    String username,password,magic;
    HttpURLConnection conn;
    @Override
    protected void onPreExecute(){}
    @Override
    protected String doInBackground(String... params){
       try {
           urs =getFinalURL(params[0]);
           URL test=new URL(urs);
           magic = test.getQuery();
           Log.d(Main.APPNAME,magic+"");
           url = new URL("Http://"+test.getAuthority());
           username = params[1];
           password = params[2];
           method = "POST";
           connect();
           code = response;
       }catch(Exception e){
           Log.d(Main.APPNAME,"Wrong URL ");
           e.printStackTrace();
           code = "FAILED";
       }
        Log.d(Main.APPNAME,code);
        return code;
    }
    @Override
    protected void onPostExecute(String result){
        ((TextView) ((Activity)c).findViewById(R.id.textView3)).setText(result);
        Toast.makeText(c,magic+"  "+urs,Toast.LENGTH_LONG).show();
    }
    private void connect(){
        try{
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod(method);
        conn.setConnectTimeout(100000);
        conn.setReadTimeout(150000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("4tredir","http://www.IAMANONYMOUS.com").appendQueryParameter("magic",magic).appendQueryParameter("username",username).appendQueryParameter("password",password);
        String query = builder.build().getEncodedQuery();
        Log.d(Main.APPNAME,query);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        Log.d(Main.APPNAME, "trying");
        conn.connect();
        Log.d(Main.APPNAME,"Connected");
        int responseCode=conn.getResponseCode();
        Log.d(Main.APPNAME,""+responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }

        }
        else {
            response=" ERROR "+responseCode;

        }}catch(MalformedURLException me) {
            me.printStackTrace();
        }catch (Exception e)
        {
            Log.d(Main.APPNAME,url.toString()+urs.toString());
            e.printStackTrace();

        }

    }
    public String getFinalURL(String url) throws Exception {
        url=url.replace("https", "http");
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setInstanceFollowRedirects(false);
        Log.d(Main.APPNAME,url);
        con.connect();
        con.getInputStream();

        if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectUrl = con.getHeaderField("Location");
            return getFinalURL(redirectUrl);
        }

        Log.d(Main.APPNAME,url);
        return url;
    }
}