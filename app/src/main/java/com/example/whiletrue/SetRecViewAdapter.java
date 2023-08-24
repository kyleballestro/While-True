package com.example.whiletrue;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This is the adapter for the RecyclerView that shows the sets of each exercise in the WorkoutDetailsActivity
public class SetRecViewAdapter extends RecyclerView.Adapter<SetRecViewAdapter.ViewHolder>{

    private ArrayList<Set> sets = new ArrayList<Set>();
    private Context context;

    public SetRecViewAdapter(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item, parent, false);
        SetRecViewAdapter.ViewHolder holder = new SetRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetRecViewAdapter.ViewHolder holder, int position) {
        // Set the text for each EditText
        int pos = holder.getAdapterPosition();
        Set set = sets.get(pos);
        String weight = set.getWeight();
        String reps = set.getReps();
        String notes = set.getNotes();
        holder.setText.setText(String.valueOf(pos + 1));
        holder.weightEditText.setText(weight);
        holder.repsEditText.setText(reps);
        holder.notesEditText.setText(notes);
    }

    @Override
    public int getItemCount() {
        if (sets != null){
            return sets.size();
        }
        else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView setText;
        private EditText weightEditText, repsEditText, notesEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setText = itemView.findViewById(R.id.setText);
            weightEditText = itemView.findViewById(R.id.weightEditText);
            repsEditText = itemView.findViewById(R.id.repsEditText);
            notesEditText = itemView.findViewById(R.id.notesEditText);

            // Listeners for each EditText (weight, reps, and notes)
            weightEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (weightEditText.hasFocus()) {
                            Set set = sets.get(pos);
                            set.setWeight(s.toString());
                        }
                    }
                }
            });

            repsEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (repsEditText.hasFocus()) {
                            Set set = sets.get(pos);
                            set.setReps(s.toString());
                        }
                    }
                }
            });

            notesEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (notesEditText.hasFocus()) {
                            Set set = sets.get(pos);
                            set.setNotes(s.toString());
                        }
                    }
                }
            });
        }
    }
}