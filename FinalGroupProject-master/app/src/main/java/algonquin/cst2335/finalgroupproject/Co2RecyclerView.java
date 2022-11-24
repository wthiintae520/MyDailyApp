package algonquin.cst2335.finalgroupproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Co2RecyclerView extends AppCompatActivity {

    Co2Fragment fragmentSH;//fragment object

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co2_fragment);

        fragmentSH = new Co2Fragment();

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();//FragmentTransaction can add(), replace() or remove() a fragment

        fTransaction.add(R.id.FragmentCo2, fragmentSH);
        fTransaction.commit();
    }

    public void onClick(View v) {
        Toast.makeText(Co2RecyclerView.this,"Choose an item to check the result", Toast.LENGTH_LONG).show();
    }

    public void userClickedMessage(Co2Activity.SearchHistory searchHistory, int position) {

        Co2FragmentDetails fragmentD = new Co2FragmentDetails(searchHistory, position);

        getSupportFragmentManager().beginTransaction().add(R.id.FragmentCo2, fragmentD).commit();//phone load a second fragment over top of the list
    }

    public void notifyMessageDeleted(Co2Activity.SearchHistory chosenSearchHistory, int chosenPosition) {
        fragmentSH.notifyMessageDeleted(chosenSearchHistory, chosenPosition);
    }
}