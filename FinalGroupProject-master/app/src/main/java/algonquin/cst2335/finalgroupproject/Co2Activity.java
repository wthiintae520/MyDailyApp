package algonquin.cst2335.finalgroupproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Co2Activity extends AppCompatActivity {

    static Button buttonSearch;

    static ArrayList<SearchHistory> searchHistories;

    static SQLiteDatabase sqliteDatabase;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co2);

        Spinner spinnerCarBrand = findViewById(R.id.SpinnerCarBrand);
        Spinner spinnerCarType = findViewById(R.id.SpinnerCarType);
        EditText editTextDistance = findViewById(R.id.EditTextDistance);
        ImageView imageViewEraser = findViewById(R.id.ImageViewEraser);
        buttonSearch = findViewById(R.id.ButtonSearch);
        Button buttonHistory = findViewById(R.id.ButtonHistory);

        searchHistories = new ArrayList<SearchHistory>();//save all the car types here

        Co2Database database = new Co2Database( this );//database object
        sqliteDatabase = database.getWritableDatabase();

        Cursor results = sqliteDatabase.rawQuery("SELECT * FROM " + Co2Database.TABLE_NAME + ";", null);
        //column indexes
        int indexOfColumnID = results.getColumnIndex("_id");
        int indexOfColumnCarBrand = results.getColumnIndex(Co2Database.columnCarBrand);
        int indexOfColumnCarType = results.getColumnIndex(Co2Database.columnCarType);
        int indexOfColumnDistance = results.getColumnIndex(Co2Database.columnDistance);
        int indexOfColumnCarbon_g = results.getColumnIndex(Co2Database.columnCarbon_g);
        int indexOfColumnCarbon_lb = results.getColumnIndex(Co2Database.columnCarbon_lb);
        int indexOfColumnCarbon_kg = results.getColumnIndex(Co2Database.columnCarbon_kg);
        while(results.moveToNext()) {
            //get the value of the raw
            long id = results.getInt(indexOfColumnID);
            String carBrand = results.getString(indexOfColumnCarBrand);
            String carType = results.getString(indexOfColumnCarType);
            String distance = results.getString(indexOfColumnDistance);
            String carbon_g = results.getString(indexOfColumnCarbon_g);
            String carbon_lb = results.getString(indexOfColumnCarbon_lb);
            String carbon_kg = results.getString(indexOfColumnCarbon_kg);
            searchHistories.add(new SearchHistory(id, carBrand, carType, distance, carbon_g, carbon_lb, carbon_kg));
        }

        Executor newThread1 = Executors.newSingleThreadExecutor();//create a second thread
        newThread1.execute(() -> {

            //This runs in a separate thread
            try {
                String stringVMakes = "https://www.carboninterface.com/api/v1/vehicle_makes";
                URL urlVMakes = new URL(stringVMakes);//creates a URL object and I pass in the URL of the server I want to connect to as a String.
                HttpURLConnection connectionVMakes = (HttpURLConnection) urlVMakes.openConnection();//connects to the server
                connectionVMakes.addRequestProperty("Authorization", "Bearer wlGgBItQrve5sWjmcSOHUg");//my API KEY = wlGgBItQrve5sWjmcSOHUg
                connectionVMakes.addRequestProperty("Content-Type", "application/json");
                InputStream inVMakes = new BufferedInputStream(connectionVMakes.getInputStream());//waits for a response from the server //The incoming data is represented as an InputStream
                String textVMakes = (new BufferedReader(new InputStreamReader(inVMakes, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));//convert the inputStream from the server into a Java String object //lines(): converts it to the lines of text //collect(): combine the different lines of text into one long string
                JSONArray VMakes = new JSONArray(textVMakes);//converts the String text to JSON Array.

                ArrayList<String> carBrands = new ArrayList<String>();//save all the car brands here

                for (int i = 0; i < VMakes.length(); i++) {
                    JSONObject positionVMakes = VMakes.getJSONObject(i);
                    JSONObject dataVMakes = positionVMakes.getJSONObject("data");
                    JSONObject attributesVMakes = dataVMakes.getJSONObject("attributes");
                    String nameVMakes = attributesVMakes.getString("name");
                    carBrands.add(nameVMakes);
                }

                ArrayAdapter<String> adapterCarBrands = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, carBrands);//set the spinners adapter from the ArrayList<String> carBrands

                runOnUiThread(() -> {//call these functions on the main GUI thread because setText(), setVisibility(), setImageBitmap() can only be called from the main GUI thread
                    spinnerCarBrand.setAdapter(adapterCarBrands);
                });

                spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        Executor newThread2 = Executors.newSingleThreadExecutor();//create a second thread
                        newThread2.execute(() -> {
                            try {
                                JSONObject positionSelected = VMakes.getJSONObject((int)id);
                                JSONObject dataSelected = positionSelected.getJSONObject("data");
                                JSONObject attributesSelected = dataSelected.getJSONObject("attributes");
                                String nameSelected = attributesSelected.getString("name");
                                String idSelected1 = dataSelected.getString("id");

                                String stringVModels = "https://www.carboninterface.com/api/v1/vehicle_makes/" + idSelected1 + "/vehicle_models";
                                URL urlVModels = new URL(stringVModels);//creates a URL object and I pass in the URL of the server I want to connect to as a String.
                                HttpURLConnection connectionVModels = (HttpURLConnection) urlVModels.openConnection();//connects to the server
                                connectionVModels.addRequestProperty("Authorization", "Bearer wlGgBItQrve5sWjmcSOHUg");//my API KEY = wlGgBItQrve5sWjmcSOHUg
                                connectionVModels.addRequestProperty("Content-Type", "application/json");
                                InputStream inVModels = new BufferedInputStream(connectionVModels.getInputStream());//waits for a response from the server //The incoming data is represented as an InputStream
                                String textVModels = (new BufferedReader(new InputStreamReader(inVModels, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));//convert the inputStream from the server into a Java String object //lines(): converts it to the lines of text //collect(): combine the different lines of text into one long string
                                JSONArray VModels = new JSONArray(textVModels);//converts the String text to JSON Array.

                                ArrayList<String> carTypes = new ArrayList<String>();//save all the car types here

                                for (int i = 0; i < VModels.length(); i++) {
                                    JSONObject positionVModels = VModels.getJSONObject(i);
                                    JSONObject dataVModels = positionVModels.getJSONObject("data");
                                    JSONObject attributesVModels = dataVModels.getJSONObject("attributes");
                                    String nameVModels = attributesVModels.getString("name");
                                    carTypes.add(nameVModels);
                                }

                                ArrayAdapter<String> adapterCarTypes = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, carTypes);//set the spinners adapter from the ArrayList<String> carTypes

                                runOnUiThread(() -> {//call these functions on the main GUI thread because setText(), setVisibility(), setImageBitmap() can only be called from the main GUI thread
                                    spinnerCarType.setAdapter(adapterCarTypes);
                                });

                                spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        Executor newThread3 = Executors.newSingleThreadExecutor();//create a second thread
                                        newThread3.execute(() -> {
                                            try {
                                                JSONObject positionSelected2 = VModels.getJSONObject((int)id);
                                                JSONObject dataSelected2 = positionSelected2.getJSONObject("data");
                                                JSONObject attributesSelected2 = dataSelected2.getJSONObject("attributes");
                                                String nameSelected2 = attributesSelected2.getString("name");
                                                String idSelected2 = dataSelected2.getString("id");

                                                buttonSearch.setOnClickListener((click) -> {
                                                    Executor newThread4 = Executors.newSingleThreadExecutor();//create a second thread
                                                    newThread4.execute(() -> {
                                                        try {
                                                            String distance = editTextDistance.getText().toString();

                                                            String stringEst = "https://www.carboninterface.com/api/v1/estimates";
                                                            URL urlEst = new URL(stringEst);//creates a URL object and I pass in the URL of the server I want to connect to as a String.
                                                            HttpURLConnection connectionEst = (HttpURLConnection) urlEst.openConnection();//connects to the server
                                                            connectionEst.setRequestProperty("Authorization", "Bearer wlGgBItQrve5sWjmcSOHUg");//my API KEY = wlGgBItQrve5sWjmcSOHUg
                                                            connectionEst.setRequestProperty("Content-Type", "application/json");
                                                            connectionEst.setRequestMethod("POST");
                                                            String data = "{\"type\":\"vehicle\", \"distance_unit\": \"km\", \"distance_value\": " + distance + ", \"vehicle_model_id\": \"" + idSelected2 + "\" }";
                                                            OutputStream outEst = connectionEst.getOutputStream();
                                                            OutputStreamWriter outWriterEst = new OutputStreamWriter(outEst, "UTF-8");
                                                            outWriterEst.write(data);
                                                            outWriterEst.flush();
                                                            outWriterEst.close();
                                                            outEst.close();
                                                            InputStream inEst = new BufferedInputStream(connectionEst.getInputStream());//waits for a response from the server //The incoming data is represented as an InputStream
                                                            String textEst = (new BufferedReader(new InputStreamReader(inEst, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));//convert the inputStream from the server into a Java String object //lines(): converts it to the lines of text //collect(): combine the different lines of text into one long string
                                                            JSONObject Est = new JSONObject(textEst);//converts the String text to JSON Array.

                                                            JSONObject dataEst = Est.getJSONObject("data");
                                                            JSONObject attributesEst = dataEst.getJSONObject("attributes");

                                                            String carbon_g = String.valueOf(attributesEst.getInt("carbon_g"));
                                                            String carbon_lb = String.valueOf(attributesEst.getDouble("carbon_lb"));
                                                            String carbon_kg = String.valueOf(attributesEst.getDouble("carbon_kg"));

                                                            SearchHistory thisSearch = new SearchHistory(nameSelected, nameSelected2, distance, carbon_g, carbon_lb, carbon_kg);
                                                            searchHistories.add(thisSearch);

                                                            ContentValues newRow = new ContentValues();//row object
                                                            newRow.put(Co2Database.columnCarBrand, thisSearch.getCarBrandH());
                                                            newRow.put(Co2Database.columnCarType, thisSearch.getCarTypeH());
                                                            newRow.put(Co2Database.columnDistance, thisSearch.getDistanceH());
                                                            newRow.put(Co2Database.columnCarbon_g, thisSearch.getCarbon_g());
                                                            newRow.put(Co2Database.columnCarbon_lb, thisSearch.getCarbon_lb());
                                                            newRow.put(Co2Database.columnCarbon_kg, thisSearch.getCarbon_kg());
                                                            long newID = sqliteDatabase.insert(Co2Database.TABLE_NAME, Co2Database.columnCarBrand, newRow);
                                                            thisSearch.setID(newID);

                                                            runOnUiThread(() -> {
                                                            editTextDistance.setText("");//clear the EditText
                                                            });

                                                            startActivity(new Intent(Co2Activity.this, Co2RecyclerView.class));
                                                        } catch (IOException | JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    });
                                                });
                                            } catch(JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                    }
                                });
                            } catch(JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            } catch(IOException | JSONException ioe) {//IOException: there is some error in transmitting and receiving
                Log.e("Connection error:", ioe.getMessage());
            }
        });

//        imageViewEraser.setOnClickListener((click) -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());//alert dialog
//            builder.setTitle("Caution");
//            builder.setMessage("Do you want to clear the distance?");
//            builder.setNegativeButton("NO",(dialog, clik) -> {//if select no: nothing happen
//            });
//            builder.setPositiveButton("YES",(dialog, clik) -> {//if select yes: delete the message
//                editTextDistance.setText("");//clear the EditText
//            } );
//            builder.create().show();
//        });

        buttonHistory.setOnClickListener((click) -> {
            Snackbar.make(Co2Activity.buttonSearch, "Going to History page...", Snackbar.LENGTH_LONG).setAction("YES", cl -> {
                startActivity(new Intent(Co2Activity.this, Co2RecyclerView.class));
            })
            .show();
        });
    }

    public void onClick(View v) {
        Toast.makeText(Co2Activity.this,"Select a car brand to start", Toast.LENGTH_LONG).show();
    }

    protected class SearchHistory {
        long id;
        String carBrandH;
        String carTypeH;
        String distanceH;
        String carbon_g;
        String carbon_lb;
        String carbon_kg;

        public SearchHistory(long id, String carBrandH, String carTypeH, String distanceH, String carbon_g, String carbon_lb, String carbon_kg) {
            setID(id);
            this.carBrandH = carBrandH;
            this.carTypeH = carTypeH;
            this.distanceH = distanceH;
            this.carbon_g = carbon_g;
            this.carbon_lb = carbon_lb;
            this.carbon_kg = carbon_kg;
        }

        public SearchHistory(String carBrandH, String carTypeH, String distanceH, String carbon_g, String carbon_lb, String carbon_kg) {
            this.carBrandH = carBrandH;
            this.carTypeH = carTypeH;
            this.distanceH = distanceH;
            this.carbon_g = carbon_g;
            this.carbon_lb = carbon_lb;
            this.carbon_kg = carbon_kg;
        }

        public void setID(long l) {
            id = l;
        }

        public long getID() {
            return id;
        }

        public String getCarBrandH() {
            return carBrandH;
        }

        public String getCarTypeH() {
            return carTypeH;
        }

        public String getDistanceH() {
            return distanceH;
        }

        public String getCarbon_g() {
            return carbon_g;
        }

        public String getCarbon_lb() {
            return carbon_lb;
        }

        public String getCarbon_kg() {
            return carbon_kg;
        }
    }
}