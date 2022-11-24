package algonquin.cst2335.finalgroupproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * OwlbotActivity page
 * User sees this when clicking on Owlbot button on MainActivity
 * Author: Yusuf Ahmed
 */
public class OwlbotActivity extends AppCompatActivity {
    /**
     * String for user to enter
     */
    String textStr;
    String serverURL = "https://owlbot.info/api/v4/dictionary/";
    MyAdapter adapter;
    ArrayList<SearchList> searches = new ArrayList<>();

    /**
     * creating toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.owlbot_activity_toolbar, menu);
        return true;
    }

    /**
     * Make Toolbar interactive with other menues
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hide_info:
                startActivity(new Intent(OwlbotActivity.this, MainActivity.class));
                return true;

            case R.id.covidMenu:
                startActivity(new Intent(OwlbotActivity.this, CovidActivity.class));
                return true;

            case R.id.carbonMenu:
                startActivity(new Intent(OwlbotActivity.this, Co2Activity.class));
                return true;

            case R.id.helpMenu:
                AlertDialog instructions = new AlertDialog.Builder(OwlbotActivity.this)
                        .setTitle("Instructions")
                        .setMessage("1. Type word in the textbox and hit the search button for definitions" + "\n"
                                + "2. Use both Navigation Bar (on the left) and Toolbar (on the right) to access the" +
                                "other menues")
                        .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owlbot_layout);

        RecyclerView owlbotList = findViewById(R.id.myRecyclerView);
        Button searchTerm = findViewById(R.id.searchButton);
        EditText searchText = findViewById(R.id.searchText);

        owlbotList.setAdapter(adapter);
        /**
         * Created layout for the Owlbot Dictionary to
         */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        /**
         * Created Navigation Bar for the Owlbot Dictionary to
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**
         * Make Navigation Bar interactive with other activities
         */
        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.hide_info:
                    startActivity(new Intent(OwlbotActivity.this, MainActivity.class));
                    return true;

                case R.id.covidMenu:
                    startActivity(new Intent(OwlbotActivity.this, CovidActivity.class));
                    return true;

                case R.id.carbonMenu:
                    startActivity(new Intent(OwlbotActivity.this, Co2Activity.class));
                    return true;
            }
            return super.onOptionsItemSelected(item);
        });



        /**
         * SharedPreferences Object
         */
        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        /**
         * Instructions Alert
         */
            AlertDialog instructions = new AlertDialog.Builder(OwlbotActivity.this)
                    .setTitle("Instructions")
                    .setMessage("1. Type word in the textbox and hit the search button for definitions" + "\n"
                    + "2. Use both Navigation Bar (on the left) and Toolbar (on the right) to access the" +
                            "other menues")
                    .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        /**
         * SearchTerm button clicked by user to save entry
         */
        searchTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textStr = searchText.getText().toString();

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Search Term", textStr);

                editor.apply();

                /**
                 * Loading Alert
                 */
                AlertDialog loading = new AlertDialog.Builder(OwlbotActivity.this)
                        .setTitle("Loading")
                        .setMessage("Please Wait for definitions to load")
                        .setView(new ProgressBar(OwlbotActivity.this))
                        .show();

                /**
                 * Connect to the server
                 */
                instructions.hide();
                String searchTerm = searchText.getText().toString();
                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute( () -> {

                    try {

                        String fullUrl = serverURL+ URLEncoder.encode(textStr, "UTF-8");


                        URL url = new URL(fullUrl);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestProperty("Authorization", "token e19d032b74f770e0091653e6a3644e657bb362de");
                    //    serverURL = "https://owlbot.info/api/v4/dictionary/"
                      //          + URLEncoder.encode(searchTerm, "UTF-8")
                        //        + "&appid=e19d032b74f770e0091653e6a3644e657bb362de";

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                        String text = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)
                                )).lines()
                                    .collect(Collectors.joining("\n"));

                        JSONObject theDefinition = new JSONObject( text );
                        JSONArray definitionsArray = theDefinition.getJSONArray("definitions");


                        for(int i = 0; i < definitionsArray.length(); i++) {
                            JSONObject objA1 = definitionsArray.getJSONObject(i);
                            String type = objA1.getString("type");
                            String definition = objA1.getString("definition");
                            String example = objA1.getString("example");
                            Log.i("type", type);
                            Log.i("definition", definition);
                            Log.i("example", example);

                            runOnUiThread( (  )  -> {
                            });
                        }

                        JSONObject pronunciationWord = theDefinition.getJSONObject("pronunciation");
                        String pronunciation = pronunciationWord.getString("value");
                        JSONObject definitionWord = theDefinition.getJSONObject("word");
                        String word = definitionWord.getString("value");

                        runOnUiThread( (  ) -> {
                        });



                        int i = 0;

                        }
                    catch (IOException | JSONException ioe) {
                        Log.e(" Connection Error:", ioe.getMessage());
                    }
                });

                /**
                 * Toast notification for saved entry
                 */
                //Toast Notification for saving entry
                Toast.makeText(OwlbotActivity.this, "entry saved", Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * MyAdapter class
     */
    public class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    /**
     * Message Class to get MessageTyped
     */
    public class Message{
        String messageTyped;

        public Message(String messageTyped) {
            this.messageTyped = messageTyped;
        }

        public String getMessageTyped() {
            return messageTyped;
        }
    }

    public class SearchList {
    }
}
