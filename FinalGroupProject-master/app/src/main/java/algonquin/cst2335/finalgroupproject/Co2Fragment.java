package algonquin.cst2335.finalgroupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class Co2Fragment extends Fragment {

    RecyclerView recyclerView;
    Co2Fragment.MyHistoryAdapter myHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//inflate the layout

        View viewRecyclerView = inflater.inflate(R.layout.activity_co2_recyclerview, container, false);

        recyclerView = viewRecyclerView.findViewById(R.id.RecyclerView);
        myHistoryAdapter = new Co2Fragment.MyHistoryAdapter();

        recyclerView.setAdapter(myHistoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return viewRecyclerView;
    }

    private class MyHistoryAdapter extends RecyclerView.Adapter<Co2Fragment.MyRowViews> {//tells the list how to built the items

        @Override
        public Co2Fragment.MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();//load a layout from .xml file
            View loadedRow = inflater.inflate(R.layout.activity_co2_recyclerview_item, parent, false);
            return new Co2Fragment.MyRowViews(loadedRow);//return ViewHolder;
        }

        @Override
        public void onBindViewHolder(Co2Fragment.MyRowViews holder, int position) {

            holder.textViewCarBrandH.setText("Brand: " + Co2Activity.searchHistories.get(position).getCarBrandH());
            holder.textViewCarTypeH.setText("Type: "+ Co2Activity.searchHistories.get(position).getCarTypeH());
            holder.textViewDistanceH.setText(Co2Activity.searchHistories.get(position).getDistanceH() + " km");
        }

        @Override
        public int getItemCount() {
            return Co2Activity.searchHistories.size();
        }
    }

    private class MyRowViews extends RecyclerView.ViewHolder {//represent a row

        TextView textViewCarBrandH;
        TextView textViewCarTypeH;
        TextView textViewDistanceH;

        public MyRowViews(View itemView) {//constructor
            super(itemView);

            textViewCarBrandH = itemView.findViewById(R.id.TextViewCarBrandH);
            textViewCarTypeH = itemView.findViewById(R.id.TextViewCarTypeH);
            textViewDistanceH = itemView.findViewById(R.id.TextViewDistanceH);

            itemView.setOnClickListener(click -> {
                Co2RecyclerView parentActivity = (Co2RecyclerView)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(Co2Activity.searchHistories.get(position), position);
            });
        }
    }

    public void notifyMessageDeleted(Co2Activity.SearchHistory chosenSearchHistory, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//alert dialog
        builder.setTitle("Caution");
        builder.setMessage("Do you want to delete the history?");
        builder.setNegativeButton("Cancel",(dialog, click) -> {//if select cancel: nothing happen
        });
        builder.setPositiveButton("Delete",(dialog, click) -> {//if select delete: delete the message
            Co2Activity.SearchHistory removedHistory = Co2Activity.searchHistories.get(chosenPosition);
            Co2Activity.searchHistories.remove(chosenPosition);//remove from the ArrayList<>
            myHistoryAdapter.notifyItemRemoved(chosenPosition);//notify the adapter that an item was deleted
            Co2Activity.sqliteDatabase.delete(Co2Database.TABLE_NAME, "_id = ?", new String[] {//delete any row from the given table where the row matches the condition// ? = first element in the String array: removedMessage.getID()
                Long.toString(removedHistory.getID())//getID() returns a long, so convert the long to a String
            } );
            Snackbar.make(Co2Activity.buttonSearch, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG).setAction("UNDO", cl -> {
                Co2Activity.searchHistories.add(chosenPosition, removedHistory);
                myHistoryAdapter.notifyItemInserted(chosenPosition);
                Co2Activity.sqliteDatabase.execSQL("INSERT INTO " + Co2Database.TABLE_NAME + " VALUES('" + removedHistory.getID() + "','" +//reinsert the message
                        removedHistory.getCarBrandH() + "','" +
                        removedHistory.getCarTypeH() + "','" +
                        removedHistory.getDistanceH() + "','" +
                        removedHistory.getCarbon_g() + "','" +
                        removedHistory.getCarbon_lb() + "','" +
                        removedHistory.getCarbon_kg() + "');");
            })
            .show();
        } );
        builder.create().show();
    }
}