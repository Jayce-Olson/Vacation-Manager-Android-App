package com.example.androidapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;

// This class is for binding vacation data to the view. It acts as a middle man between the database and the view(UI).
// It is slightly more complex than it needed to be for this project because I decided to use a recycler view instead of
// just a list view. (I free up and re use views as the user scrolls through the list of vacations -- although since this
// is a school assessment that will probably never have over five vacations, this is somewhat overkill -- I may just
// modify this and turn it into my own personal notes app in the future though)

public class VacationAdapter extends ListAdapter<VacationEntity, VacationAdapter.VacationViewHolder> {

    /*
    The listener is stored and changed depending on the current view. For example, the main activity view
    will have a listener for the "add vacation" button, while the addEditVacation activity will have a listener
    for the "save" button.
     */
    private OnItemClickListener listener;


    public VacationAdapter() {
        // This calls a class within the parent a passes through a lambda that holds the logic
        // to determine what data in the view needs to be updated. The lambda is held and defined
        // in the vacationEntity class. This is a bit overkill for a school Assessment as this app
        // Will probably never work with over five vacations Entities at a time, but for good practice
        // and because if this was an actual app, this is what I should do.
        super(VacationEntity.anonymousDiffUtil); // The super class will use the override methods within anonymousDiffUtil
    }



    @NonNull
    @Override
    /* onCreateViewHolder is called when the recycler view needs a new view holder to represent an item.*/
    /* Documentation for below: https://developer.android.com/develop/ui/views/layout/recyclerview#java */
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        * Below, this is very important for understanding how the list is displayed on the screen.
        * If you look inside of res/layout/ you will see a vacation_item.xml, this is the layout for
        * each item in the list. There is no activity file for this. At the time of writing this,
        * vacation_item is only mentioned once, and that is here. Inside of activity_main.xml you will
        * see <androidx.recyclerview.widget.RecyclerView for a recycle viewer. The code below is what is
        * executed when this RecyclerView wants to create a new view/item within it. This is where
        * vacation_item comes into play. Vacation_item is what holds the layout for the newly created
        * items/views within RecyclerView(which is within activity_main.xml). Personally this helped a LOT
        * with understanding how my xml files connected and how the views/items were displayed.
        *
        * I listed a link for the documentation of the code below for more explaining on it. (in order to save space)
        * */
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_item, parent, false); /* vacation_item is the layout used for the creation of the new item*/
        return new VacationViewHolder(itemView);
    }

    @Override
    /* Below is onBindViewHolder, it is called by RecyclerView when a ViewHolder needs to be updated.
    *  When called, the parameters for the viewHolder, of type holder, and the position of the next item
    *  In the list to bound (as the user scrolls, the old view goes off screen, and the same holder with
    *  that views comes onto the screen with the next item in the list, based off of position). Below
    *  the next entity to be bound is set and retrieved with getItem and the position of the item, and
    *  then directly afterwards the bind method is used to bind the vacation entities data to the view.
    *
    * The RecyclerView is able to know the position within the data set (and the data set itself) from
    * when the list of vacations in the database was passed through adapter.submitList() inside the main
    * MainActivity.java file - on line 128 at the time of writing this.
    * */
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        VacationEntity vacation = getItem(position); // Gets the next vacation according to position
        holder.bind(vacation); // This calls a method defined below that just does {field}.setText(vacation.get{field})
    }

    /*
    * VacationViewHolder is a custom ViewHolder class. A view holder holds views and provides
    * fields and methods for the views it holds. Below is basically a class for the vacation
    * views. It will hold the vacation fields I want to display, as well as methods to interact
    * with the views. Since I using recycler view, the views inside of here will be re-used/re-
    * assigned as the user scrolls.
    *
    *
    * */
    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewHotel;
        private final TextView textViewStartDate;
        private final TextView textViewEndDate;
        public VacationViewHolder(View itemView) {
            /* Below, itemView needs to be passed to the parent class so that RecyclerView.ViewHolder knows (has a reference to)
             * which view it is handling. Without it methods (that rely on view) would return errors as the view it is working with
             * would be null. If the constructor was never coded in, it would be likely that the view
             * would have to be passed as a parameter everytime a method (that relies on view) is called,
             * rather than just being passed through the constructor once with super.
             * */
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewHotel = itemView.findViewById(R.id.textViewHotel);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);


// Listens for clicks on the individual items in the list. Will be uncommented after debugging/testing
//            itemView.setOnClickListener(view -> { // onClick listener that will go on each item view in the list.
//                if (listener != null) { // Without this there could be a null pointer exception if somehow this got ran without a listener for the click
//                    listener.onItemClick(getItem(getBindingAdapterPosition()));
//                }
//            });

        }

        public void bind(VacationEntity vacation) {
            textViewTitle.setText(vacation.getTitle());
            textViewHotel.setText(vacation.getHotel());
            textViewStartDate.setText(vacation.getStartDate());
            textViewEndDate.setText(vacation.getEndDate());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(VacationEntity vacation);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

