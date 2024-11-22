package com.example.androidapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Activities.ExcursionAddEditActivity;
import com.example.androidapp.DAO.VacationDAO;
import com.example.androidapp.Database.VacationsDatabase;
import com.example.androidapp.Entities.ExcursionEntity;
import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcursionAdapter extends ListAdapter<ExcursionEntity, ExcursionAdapter.ExcursionViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private VacationDAO vacationDAO;

    public ExcursionAdapter(Context context) {
        super(ExcursionEntity.anonymousDiffUtil);
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        ExcursionEntity excursion = getItem(position);
        holder.bind(excursion);
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewExcursionTitle;
        private TextView textViewExcursionDate;

        public ExcursionViewHolder(View itemView) {
            super(itemView);
            textViewExcursionTitle = itemView.findViewById(R.id.excursionTitleTextView);
            textViewExcursionDate = itemView.findViewById(R.id.excursionDateTextView);

            itemView.setOnClickListener(view -> { // onClick listener that will go on each item view in the list.
                onItemClick(getItem(getBindingAdapterPosition()));
            });
        }

        public void bind(ExcursionEntity excursion) {
            textViewExcursionTitle.setText(excursion.getTitle());
            textViewExcursionDate.setText(excursion.getDate());
        }
    }

    private void onItemClick(ExcursionEntity excursion) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // grab the vacations dates (from the vacation id
        executor.execute(() -> {
            vacationDAO = VacationsDatabase.getInstance(context).vacationDao();
            VacationEntity vacation = vacationDAO.getVacationById(excursion.getVacationId());
            Intent intent = new Intent(context, ExcursionAddEditActivity.class);
            if (vacation != null) {
                intent.putExtra("VACATION", vacation);
            }


            /* The reason Excursion implements serializable is for this one line below */
            intent.putExtra("Excursion", excursion);

            // Below is so that the add/edit activity can know which mode it is in
            intent.putExtra("isEditMode", true);
            context.startActivity(intent); // Adapter doesn't have the startActivity method so I need to use context.
        });
        executor.shutdown();
    }

    public interface OnItemClickListener {
        void onItemClick(ExcursionEntity excursion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

