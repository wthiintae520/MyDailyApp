package algonquin.cst2335.finalgroupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Co2FragmentDetails extends Fragment {

    Co2Activity.SearchHistory chosenSearchHistory;
    int chosenPosition;

    public Co2FragmentDetails(Co2Activity.SearchHistory searchHistory, int position) {
        chosenSearchHistory = searchHistory;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//inflate the layout

        View co2FragmentD = inflater.inflate(R.layout.activity_co2_fragment_layout, container, false);

        TextView textViewCarBrandF = co2FragmentD.findViewById(R.id.TextViewCarBrandF);
        TextView textViewCarTypeF = co2FragmentD.findViewById(R.id.TextViewCarTypeF);
        TextView textViewDistanceF = co2FragmentD.findViewById(R.id.TextViewDistanceF);
        TextView textViewCrbon_g = co2FragmentD.findViewById(R.id.TextViewCarbon_g);
        TextView textViewCrbon_lb = co2FragmentD.findViewById(R.id.TextViewCarbon_lb);
        TextView textViewCrbon_kg = co2FragmentD.findViewById(R.id.TextViewCarbon_kg);

        textViewCarBrandF.setText("Car brand: " + chosenSearchHistory.getCarBrandH());
        textViewCarTypeF.setText("Car type: " + chosenSearchHistory.getCarTypeH());
        textViewDistanceF.setText("Distance: " + chosenSearchHistory.getDistanceH() + " km");
        textViewCrbon_g.setText("Estimated carbon emission: " + chosenSearchHistory.getCarbon_g() + " g");
        textViewCrbon_lb.setText("/ " + chosenSearchHistory.getCarbon_lb() + " lb");
        textViewCrbon_kg.setText("/ " + chosenSearchHistory.getCarbon_kg() + " kg");

        Button buttonClose = co2FragmentD.findViewById(R.id.ButtonClose);
        Button buttonDelete = co2FragmentD.findViewById(R.id.ButtonDelete);

        buttonClose.setOnClickListener( closeClicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();//remove the detailsFragment
        });

        buttonDelete.setOnClickListener( deleteClicked -> {
            Co2RecyclerView parentActivity = (Co2RecyclerView)getContext();
            parentActivity.notifyMessageDeleted(chosenSearchHistory, chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();//remove the detailsFragment
        });

        return co2FragmentD;
    }
}