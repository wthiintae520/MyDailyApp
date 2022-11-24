package algonquin.cst2335.finalgroupproject;

import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CovidActivity extends AppCompatActivity {

    Button searchButton;
    String dateInput;
    EditText edit;
    RecyclerView myRecycler;
    MyAdapter adapter;
    ArrayList<Search> searchList = new ArrayList<Search>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_layout);

        searchButton = findViewById(R.id.searchButton);
        myRecycler = findViewById(R.id.recyclerView);
        edit = findViewById(R.id.editTextDate);


        searchList.add(new Search(dateInput));

        searchButton.setOnClickListener(click -> {
            String dateInput = edit.getText().toString();
            Date timeNow = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDateandTime = sdf.format(timeNow); // this will convert Date to string

            Search searchText = new Search(dateInput);
            searchList.add(searchText);
            edit.setText("");

            Toast.makeText(CovidActivity.this, "Entry saved", Toast.LENGTH_LONG).show();

            adapter.notifyItemInserted(searchList.size() - 1);
        });

        adapter = new MyAdapter();
        myRecycler.setAdapter(adapter);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //RecyclerView to present the results of the search in a list.
        //The query should be “https://api.covid19tracker.ca/reports?after=” + the date entered in YYYY-MM-DD format
        //Selecting one of the items from the list must show detailed info about the item

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            //Load a new row from the layout file
            LayoutInflater inflater = getLayoutInflater();
            viewType = 1;
            View thisRow;
                //layoutID = R.layout.sent_message;
                thisRow = inflater.inflate(R.layout.search, parent, false);
            return new MyViewHolder(thisRow);
        }

        //initializes a Row at position
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //What message objet is at position
            Search thisRow = searchList.get(position);

            //MyViewHolder has date textView
            holder.searchView.setText(thisRow.getDateInput());
        }

        @Override
        public int getItemCount() {
            return searchList.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView searchView;

        public MyViewHolder(View itemView) {
            super(itemView);
            searchView = itemView.findViewById(R.id.searchDate);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                Search removedSearch = searchList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(CovidActivity.this);

                builder.setMessage("Do you want to delete the search: " + searchView.getText() + " ?");
                builder.setTitle("Question:");
                builder.setNegativeButton("No", (dialog, cl) -> {});
                builder.setNegativeButton("Yes", (dialog, cl) -> {
                    //deleting the search

                    searchList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(searchView, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk ->{
                                searchList.add(position, removedSearch);
                            }) .show();
                }).create().show();
            });
        }
    }

    public class Search{

        String dateInput;

        public Search(String dataInput){
            this.dateInput = dataInput;
        }

        public String getDateInput(){
            return dateInput;
        }
    }
}




