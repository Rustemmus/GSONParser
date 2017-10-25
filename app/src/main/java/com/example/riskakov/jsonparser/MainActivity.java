package com.example.riskakov.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> solatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solatList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long myTimestamp = System.currentTimeMillis()/1000;
            //int intTimestamp = timestamp/1000;
            System.out.println(myTimestamp);
           // System.out.println(strTimestamp.substring(10));

            // Making a request to url and getting response
            String url = "http://api.aladhan.com/timings/" + //1508780673//
                    myTimestamp
                    + "?latitude=43.327316&longitude=76.948206&timezonestring=Asia/Almaty&method=2%20,";
            String jsonStr = sh.makeServiceCall(url);
            System.out.println(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //JSONObject jsonObj = new JSONObject("{\"code\":200,\"status\":\"OK\",\"data\":{\"timings\":{\"Fajr\":\"05:40\",\"Sunrise\":\"07:16\",\"Dhuhr\":\"12:37\",\"Asr\":\"15:31\",\"Sunset\":\"17:57\",\"Maghrib\":\"17:57\",\"Isha\":\"19:27\",\"Imsak\":\"05:30\",\"Midnight\":\"00:36\"},\"date\":{\"readable\":\"23 Oct 2017\",\"timestamp\":\"1508774435\"},\"meta\":{\"latitude\":43.327316,\"longitude\":76.948206,\"timezone\":\"Asia\\/Almaty\",\"method\":{\"id\":3,\"name\":\"Muslim World League\",\"params\":{\"Fajr\":18,\"Isha\":17}},\"latitudeAdjustmentMethod\":\"ANGLE_BASED\",\"midnightMode\":\"STANDARD\",\"school\":\"STANDARD\",\"offset\":{\"Imsak\":0,\"Fajr\":0,\"Sunrise\":0,\"Dhuhr\":0,\"Asr\":0,\"Maghrib\":0,\"Sunset\":0,\"Isha\":0,\"Midnight\":0}}}}");
                    System.out.println(jsonObj);
                    JSONObject data = jsonObj.getJSONObject("data");
                    //JSONObject jsonObj = new JSONObject((jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1))); ;
                    // Getting JSON Array node
                    //JSONArray timings = jsonObj.getJSONArray("timings");

                    //JSONObject response = new JSONObject(result);
                    JSONObject timings = data.getJSONObject("timings");
                    System.out.println(timings);
                    // String Name = json.getString("name");
                    // String Age =json.getString("Age");

                    String fajr = timings.getString("Fajr");
                    String sunrise = timings.getString("Sunrise");
                    String dhuhr = timings.getString("Dhuhr");
                    String asr = timings.getString("Asr");
                    String maghrib = timings.getString("Maghrib");
                    String isha = timings.getString("Isha");

                    HashMap<String, String> solats = new HashMap<>();

                    // adding each child node to HashMap key => value
                    solats.put("fajr", fajr);
                    solats.put("sunrise", sunrise);
                    solats.put("dhuhr", dhuhr);
                    solats.put("asr", asr);
                    solats.put("maghrib", maghrib);
                    solats.put("isha", isha);

                    // adding contact to contact list
                    solatList.add(solats);
                    System.out.println(solatList);


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, solatList, R.layout.list_item, new String[]
                    {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"},
                    new int[]{R.id.fajr, R.id.sunrise, R.id.dhuhr, R.id.asr, R.id.maghrib, R.id.isha});
            lv.setAdapter(adapter);
            System.out.println(adapter);
        }
    }
}