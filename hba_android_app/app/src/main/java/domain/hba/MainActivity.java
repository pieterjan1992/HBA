package domain.hba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void getToken(View view){
        final Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String date = sharedPref.getString("date", null);
        System.out.println(date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String today = df.format(c.getTime());


        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println("response : "+ jsonResponse);
                    int Messagecode = Integer.parseInt(jsonResponse.optString("MessageCode"));
                    if(Messagecode==200){

                        SharedPreferences sharedPref = context.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        String token = jsonResponse.optString("token");
                        editor.putString("token", token);
                        editor.commit();
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("received token")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("Failed to get token")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                }
                catch (JSONException e) {

                }
            }
        };
        if(!today.equals(date)){
            tokenRequest request = new tokenRequest(responseListener,errorListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(request);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("date", today);
            editor.commit();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setMessage("Already got a token today")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
        }

    }

    public void open(View view){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println("response : "+ jsonResponse);
                    int Messagecode = Integer.parseInt(jsonResponse.optString("MessageCode"));
                    if(Messagecode==200){
                        //do open lock here
                        System.out.println("send 1");
                    }else{
                        //failed
                        System.out.println("lock not open");
                    }
                }
                catch (JSONException e) {

                }
            }
        };
        final Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        validateRequest request = new validateRequest(token,responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }

    public void close(View view){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println("response : "+ jsonResponse);
                    int Messagecode = Integer.parseInt(jsonResponse.optString("MessageCode"));
                    if(Messagecode==200){
                        //do open lock here
                        System.out.println("send 0");
                    }else{
                        //failed
                        System.out.println("lock not open");
                    }
                }
                catch (JSONException e) {

                }
            }
        };
        final Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        validateRequest request = new validateRequest(token,responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }

    public void resetToken(View view){
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("date", null);
        editor.commit();
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
}
