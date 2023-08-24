package com.example.whiletrue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This is the adapter for the RecyclerView that shows the exercises in the WorkoutDetailsActivity
public class ExerciseRecViewAdapter extends RecyclerView.Adapter<ExerciseRecViewAdapter.ViewHolder>{

    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private int clearSetsFlag = 0;

    public ExerciseRecViewAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        ExerciseRecViewAdapter.ViewHolder holder = new ExerciseRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseRecViewAdapter.ViewHolder holder, int position) {
        // Get the position, current exercise, set the layout manager for the sets recyclerview, and get other exercise info
        int pos = holder.getAdapterPosition();
        Exercise exercise = exercises.get(pos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.setRecView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(exercise.getSets().size());
        String exerciseName = exercises.get(pos).getName();
        String exerciseNameCap = "";
        if (!exerciseName.equals("")){
            exerciseNameCap = exerciseName.substring(0, 1).toUpperCase() + exerciseName.substring(1);
        }
        holder.exerciseNameTxt.setText(exerciseNameCap);

        // Set up the adapter for the sets recyclerview
        ArrayList<Set> sets = exercise.getSets();
        SetRecViewAdapter setRecViewAdapter = new SetRecViewAdapter(sets);
        if (clearSetsFlag == 1){
            sets.clear();
            setRecViewAdapter.notifyDataSetChanged();
            clearSetsFlag = 0;
        }
        holder.setRecView.setLayoutManager(layoutManager);
        holder.setRecView.setAdapter(setRecViewAdapter);
        holder.setRecView.setRecycledViewPool(viewPool);

        // Handling the Add Set button click
        holder.addSetButton.setOnClickListener(v -> {
            exercise.getSets().add(new Set(exercise.getName(), "", "", ""));
            if (sets.size() == 0){
                setRecViewAdapter.notifyItemInserted(0);
            }
            else {
                setRecViewAdapter.notifyItemInserted(sets.size() - 1);
            }
            System.out.println("After adding set: " );
            for (int i = 0; i < exercises.size(); i++){
                System.out.println("Sets for " + exercises.get(i).getName());
                for (int j = 0; j < exercises.get(i).getSets().size(); j++){
                    System.out.println(exercises.get(i).getSets().get(j).toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises != null){
            return exercises.size();
        }
        else{
            return 0;
        }
    }

    public void setContext(Context context){
        this.context = context;
        notifyDataSetChanged();
    }

    public void setExercises(ArrayList<Exercise> exercises){
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void clearExercises(){
        for (int i = 0; i < exercises.size(); i++){
            exercises.remove(i);
        }
        notifyDataSetChanged();
    }

    public void clearSets(){
        clearSetsFlag = 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView setRecView;
        private EditText exerciseNameTxt;
        private ImageView deleteButton;
        private CardView addSetButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setRecView = itemView.findViewById(R.id.setRecView);
            exerciseNameTxt = itemView.findViewById(R.id.exerciseNameTxt);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            addSetButton = itemView.findViewById(R.id.addSetButton);

            // Save the exercise name entered by the user
            exerciseNameTxt.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Exercise exercise = exercises.get(position);
                        exercise.setName(s.toString());
                    }
                }
            });

            // Set up the dialog for delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String exerciseName;
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        if (!exercises.get(pos).getName().equals("")) {
                            exerciseName = exercises.get(pos).getName().substring(0, 1).toUpperCase() + exercises.get(pos).getName().substring(1);
                        }
                        else{
                            exerciseName = "this exercise";
                        }
                        builder.setTitle("Delete " + exerciseName + "?");
                        // This is technically the setPositiveButton, but it has to be labeled as the negative button
                        // in the code so that the button appears on the left side in the app
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                exercises.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(getAdapterPosition(), exercises.size() - getAdapterPosition());
                                System.out.println("EXERCISES SIZE IN ADAPTER after removing one: " + exercises.size());
                            }
                        });
                        // Like the previous comment, this is technically the setNegativeButton, it is just labeled
                        // this way in the code so that the "No" option appears on the right side in the app
                        builder.setPositiveButton("No", null);
                        builder.create().show();
                    }
                }
            });
        }
    }
}