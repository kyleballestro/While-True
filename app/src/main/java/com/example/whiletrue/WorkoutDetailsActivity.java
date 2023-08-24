package com.example.whiletrue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class WorkoutDetailsActivity extends AppCompatActivity {

    private Workout workout;
    private String date;
    private TextView dateText, title;
    private RecyclerView exerciseRecView;
    private EditText workoutTitleEditText;
    private ImageButton addNewExerciseBtn;
    private ArrayList<Exercise> exercises;
    private ArrayList<Workout> templates;
    private ArrayList<Workout> workouts;
    private SharedPreferences sp;
    private String workoutsJson;
    private String templatesJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        // Set the appbar title and home button listener
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title = findViewById(R.id.title);
        title.setText("Workout Details");

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sp = getSharedPreferences("workoutsAndTemplates", Context.MODE_PRIVATE);

        // Get the JSON strings from sharedpreferences
        workoutsJson = sp.getString("workouts", null);
        templatesJson = sp.getString("templates", null);

        if (workoutsJson != null) {
            Gson gson = new Gson();
            workouts = gson.fromJson(workoutsJson, new TypeToken<ArrayList<Workout>>() {}.getType());
        }
        if (templatesJson != null) {
            Gson gson = new Gson();
            templates = gson.fromJson(templatesJson, new TypeToken<ArrayList<Workout>>() {}.getType());
        }

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        dateText = findViewById(R.id.dateText);
        dateText.setText(date);

        if (exercises == null){
            exercises = new ArrayList<>();
        }
        if (workouts == null){
            workouts = new ArrayList<>();
        }
        if (templates == null){
            templates = new ArrayList<>();
        }

        // RESET EVERYTHING (keep this commented out unless desired)
//        resetData();
//        System.out.println("After resetData: " + workoutsJson);

        // Setting up the recyclerview adapter
        exerciseRecView = findViewById(R.id.exerciseRecView);
        ExerciseRecViewAdapter exerciseRecViewAdapter = new ExerciseRecViewAdapter(exercises);
        exerciseRecViewAdapter.setContext(this);
        exerciseRecView.setAdapter(exerciseRecViewAdapter);
        exerciseRecView.setLayoutManager(new LinearLayoutManager(WorkoutDetailsActivity.this));

        // If the current date has a workout already saved to it, get the exercises and set the title
        for (int i = 0; i < workouts.size(); i++){
            if (workouts.get(i).getDate().equals(date)){
                exercises.clear();
                exerciseRecViewAdapter.notifyDataSetChanged();
                exercises.addAll(workouts.get(i).getExercises());
                exerciseRecViewAdapter.notifyDataSetChanged();
                workoutTitleEditText = findViewById(R.id.workoutTitleEditText);
                workoutTitleEditText.setText(workouts.get(i).getWorkoutName());
            }
        }

        // Listener for add new exercise button
        addNewExerciseBtn = findViewById(R.id.addNewExerciseBtn);
        addNewExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Set> newSetList = new ArrayList<>();
                Exercise newExercise = new Exercise("", newSetList);
                exercises.add(newExercise);
                if (exercises.size() == 0){
                    exerciseRecViewAdapter.notifyItemInserted(0);
                }
                else {
                    exerciseRecViewAdapter.notifyItemInserted(exercises.size() - 1);
                }
            }
        });

        // This section of code deals with the Options list which is shown when the user presses the hamburger icon in the appbar
        ImageView hamburger = findViewById(R.id.hamburger);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use an AlertDialog to bring up the Options list
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutDetailsActivity.this);
                View customView = getLayoutInflater().inflate(R.layout.workout_options_dialog, null);
                builder.setView(customView);
                AlertDialog dialog = builder.create();
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = 300;
                layoutParams.height = 300;
                dialog.getWindow().setAttributes(layoutParams);

                // Populate the Options list with the options and appropriate images. Then set the list adapter.
                ListView optionsListView = customView.findViewById(R.id.optionsListView);
                String[] itemTexts = {"Save as Template", "Apply Template", "Remove Templates", "Clear Workout"};
                int[] itemImages = {R.drawable.ic_save_color, R.drawable.ic_apply_color, R.drawable.ic_remove, R.drawable.ic_clear};
                OptionsListAdapter adapter = new OptionsListAdapter(WorkoutDetailsActivity.this, itemTexts, itemImages, exercises, dialog);
                optionsListView.setAdapter(adapter);

                dialog.show();

                // This section of code deals with the listener for the Options list. For each option, a different action needs to be taken.
                optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemData = (String) optionsListView.getItemAtPosition(position);
                        // If the user clicks the "Save as Template" option, the current workout title and and exercises are to be saved as a template for future use
                        if (itemData.equals("Save as Template")){
                            // If there aren't any exercises in the current workout, throw an error text
                            if (exercises.size() == 0){
                                TextView noExercisesText = dialog.findViewById(R.id.noExercisesText);
                                noExercisesText.setVisibility(View.VISIBLE);
                            }
                            else{
                                // Dismiss the dialog and get the title
                                dialog.dismiss();
                                EditText title = findViewById(R.id.workoutTitleEditText);
                                String workoutDetailsTitle = title.getText().toString();

                                // Create a new AlertDialog that prompts the user to enter a workout title. If there already was one, it is filled out, but the user can change it.
                                AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutDetailsActivity.this);
                                View newCustomView = getLayoutInflater().inflate(R.layout.save_workout, null);
                                builder.setView(newCustomView);
                                AlertDialog saveDialog = builder.create();

                                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                layoutParams.copyFrom(saveDialog.getWindow().getAttributes());
                                layoutParams.width = 300;
                                layoutParams.height = 300;
                                saveDialog.getWindow().setAttributes(layoutParams);

                                EditText workoutNameEditText = newCustomView.findViewById(R.id.workoutNameEditText);
                                String workoutTitle = workoutNameEditText.getText().toString();

                                if (workoutDetailsTitle != null){
                                    workoutNameEditText.setText(workoutDetailsTitle);
                                }

                                // Listener for the save button
                                Button saveWorkoutButton = newCustomView.findViewById(R.id.saveWorkoutButton);
                                saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        boolean filled = false;
                                        // Check that the workoutName isn't blank
                                        EditText workoutNameEditText = newCustomView.findViewById(R.id.workoutNameEditText);
                                        String workoutName = workoutNameEditText.getText().toString();
                                        // If workout name (title) is blank, throw error text and don't save the template
                                        if (workoutName.equals("")){
                                            TextView warningText = newCustomView.findViewById(R.id.warningText);
                                            warningText.setText("Please enter a workout name.");
                                            warningText.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            filled = true;
                                        }

                                        // Check that there are no other workouts in templates that have that same name
                                        boolean same = false;
                                        for (int i = 0; i < templates.size(); i++){
                                            if (templates.get(i).getWorkoutName().equals(workoutName)){
                                                same = true;
                                            }
                                        }
                                        // If there is a workout with the same name, throw error text
                                        if (same == true){
                                            TextView warningText = newCustomView.findViewById(R.id.warningText);
                                            warningText.setText("A workout with that name already exists. Please enter a new workout name.");
                                            warningText.setVisibility(View.VISIBLE);
                                        }

                                        // If data is valid, save workout to templates
                                        if (filled == true && same == false){
                                            String workoutTitle = workoutNameEditText.getText().toString();
                                            ArrayList<Exercise> templateExercises = new ArrayList<>();
                                            for (int i = 0; i < exercises.size(); i++){
                                                ArrayList<Set> templateSets = new ArrayList<>();
                                                for (int j = 0; j < exercises.get(i).getSets().size(); j++){
                                                    String name = exercises.get(i).getSets().get(j).getExerciseName();
                                                    String weight = exercises.get(i).getSets().get(j).getWeight();
                                                    String reps = exercises.get(i).getSets().get(j).getReps();
                                                    String notes = exercises.get(i).getSets().get(j).getNotes();
                                                    Set set = new Set(name, weight, reps, notes);
                                                    templateSets.add(set);
                                                }
                                                Exercise ex = new Exercise(exercises.get(i).getName(), templateSets);
                                                templateExercises.add(ex);
                                            }
//                                            templateExercises.addAll(exercises);
                                            Workout templateWorkout = new Workout("", workoutTitle, templateExercises);
                                            templates.add(templateWorkout);

                                            saveDialog.dismiss();
                                        }
                                    }
                                });

                                // Listener for cancel button. Just returns to workout screen.
                                Button cancelButton = newCustomView.findViewById(R.id.cancelButton);
                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        saveDialog.dismiss();
                                    }
                                });

                                saveDialog.show();
                            }
                        }

                        // If the user clicks the "Apply Template" option, a list of saved templates will appear.
                        // The user chooses one and its name and exercises are applied to the current workout.
                        else if (itemData.equals("Apply Template")){
                            // Dismiss the dialog and create a new AlertBuilder for the templates list
                            dialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutDetailsActivity.this);
                            View templatesCustomView = getLayoutInflater().inflate(R.layout.templates_list, null);
                            builder.setView(templatesCustomView);
                            AlertDialog applyDialog = builder.create();

                            // Get the names of all the saved templates, create the adapter for the ListView
                            ListView templatesListView = templatesCustomView.findViewById(R.id.templatesListView);
                            ArrayList<String> templateNames = new ArrayList<>();
                            for (int i = 0; i < templates.size(); i++){
                                templateNames.add(templates.get(i).getWorkoutName());
                            }
                            String[] names = new String[templateNames.size()];
                            for (int i = 0; i < templateNames.size(); i++){
                                names[i] = templateNames.get(i);
                            }
                            TemplatesListAdapter adapter = new TemplatesListAdapter(WorkoutDetailsActivity.this, names, applyDialog);
                            templatesListView.setAdapter(adapter);

                            // If there are no saved templates, show text alerting the user of that
                            TextView noTemplatesText = templatesCustomView.findViewById(R.id.noTemplatesText);
                            if (names.length == 0){
                                noTemplatesText.setVisibility(View.VISIBLE);
                                Button cancelButton = templatesCustomView.findViewById(R.id.cancelButton);
                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cancelButton.getLayoutParams();
                                params.setMargins(0, 200, 0, 0);
                                cancelButton.setLayoutParams(params);
                            }
                            else{
                                noTemplatesText.setVisibility(View.GONE);
                            }

                            // Listener for which template the user clicks to apply
                            templatesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Once clicked, get the name. Search the templates ArrayList for one that matches.
                                    // Once found, add its exercises to the exercises ArrayList and set the workout title.
                                    String templateName = (String) templatesListView.getItemAtPosition(position);
                                    ArrayList<Exercise> exercisesToApply = new ArrayList<>();

                                    for (int i = 0; i < templates.size(); i++){
                                        if (templates.get(i).getWorkoutName().toString().equals(templateName)){
                                            for (Exercise templateExercise : templates.get(i).getExercises()) {
                                                // Create a new instance of Exercise and add it to the exercisesToApply list
                                                ArrayList<Set> templateExerciseSets = new ArrayList<>();
                                                for (int j = 0; j < templateExercise.getSets().size(); j++){
                                                    String exName = templateExercise.getSets().get(j).getExerciseName();
                                                    String weight = templateExercise.getSets().get(j).getWeight();
                                                    String reps = templateExercise.getSets().get(j).getReps();
                                                    String notes = templateExercise.getSets().get(j).getNotes();
                                                    Set set = new Set(exName, weight, reps, notes);
                                                    templateExerciseSets.add(set);
                                                }
                                                Exercise newExercise = new Exercise(templateExercise.getName(), templateExerciseSets);
                                                exercisesToApply.add(newExercise);
                                            }
                                            workoutTitleEditText = findViewById(R.id.workoutTitleEditText);
                                            workoutTitleEditText.setText(templates.get(i).getWorkoutName());
                                            break;
                                        }
                                    }
                                    exercises.clear();
                                    exerciseRecViewAdapter.notifyDataSetChanged();
                                    exercises.addAll(exercisesToApply);
                                    exerciseRecViewAdapter.notifyDataSetChanged();
                                    applyDialog.dismiss();
                                }
                            });

                            // Listener for cancel button. Just returns to workout screen.
                            Button cancelButton = templatesCustomView.findViewById(R.id.cancelButton);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    applyDialog.dismiss();
                                }
                            });

                            applyDialog.show();
                        }

                        // If the user clicks the "Remove Templates" option, a list of saved templates will appear.
                        // The user chooses one and it's removed from the templates list.
                        else if (itemData.equals("Remove Templates")){
                            // Dismiss the dialog and create a new AlertBuilder for the templates list
                            dialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutDetailsActivity.this);
                            View templatesCustomView = getLayoutInflater().inflate(R.layout.templates_list, null);
                            builder.setView(templatesCustomView);
                            AlertDialog applyDialog = builder.create();

                            // Get the names of all the saved templates, create the adapter for the ListView
                            ListView templatesListView = templatesCustomView.findViewById(R.id.templatesListView);
                            ArrayList<String> templateNames = new ArrayList<>();
                            for (int i = 0; i < templates.size(); i++){
                                templateNames.add(templates.get(i).getWorkoutName());
                            }
                            String[] names = new String[templateNames.size()];
                            for (int i = 0; i < templateNames.size(); i++){
                                names[i] = templateNames.get(i);
                            }
                            TemplatesListAdapter adapter = new TemplatesListAdapter(WorkoutDetailsActivity.this, names, applyDialog);
                            templatesListView.setAdapter(adapter);

                            // If there are no saved templates, show text alerting the user of that
                            TextView noTemplatesText = templatesCustomView.findViewById(R.id.noTemplatesText);
                            if (names.length == 0){
                                noTemplatesText.setVisibility(View.VISIBLE);
                                Button cancelButton = templatesCustomView.findViewById(R.id.cancelButton);
                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cancelButton.getLayoutParams();
                                params.setMargins(0, 200, 0, 0);
                                cancelButton.setLayoutParams(params);
                            }
                            else{
                                noTemplatesText.setVisibility(View.GONE);
                            }

                            // Listener for which template the user clicks to remove
                            templatesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Once clicked, remove it from templates ArrayList
                                    templates.remove(position);
                                    applyDialog.dismiss();
                                }
                            });

                            // Listener for cancel button. Just returns to workout screen.
                            Button cancelButton = templatesCustomView.findViewById(R.id.cancelButton);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    applyDialog.dismiss();
                                }
                            });

                            applyDialog.show();
                        }

                        // If the user clicks the Clear Workout" option, the exercises and workout title will be cleared.
                        else if (itemData.equals("Clear Workout")) {
                            // Dismiss the dialog and make a new one to make the user confirm that they want to clear the workout
                            dialog.dismiss();
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WorkoutDetailsActivity.this);
                            builder.setTitle("Are you sure you want to clear the workout?");
                            // This is technically the setPositiveButton, but it has to be labeled as the negative button
                            // in the code so that the button appears on the left side in the app
                            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    exercises.clear();
                                    EditText workoutTitleEditText = findViewById(R.id.workoutTitleEditText);
                                    workoutTitleEditText.setText("");
                                    exerciseRecViewAdapter.notifyDataSetChanged();
                                    System.out.println("~~~~~~~~~~~ JUST CLEARED ~~~~~~~~~~~");
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
        });
    }

    // When back button is pressed, intent to WorkoutCalendarActivity
    @Override
    public void onBackPressed() {
        if (this.equals(WorkoutDetailsActivity.this)) {
            Intent newIntent = new Intent(WorkoutDetailsActivity.this, WorkoutCalendarActivity.class);
            startActivity(newIntent);
        } else {
            super.onBackPressed();
        }
    }

    // This saves the data whenever the user pauses the activity (goes to a new screen, closes the app, turns off phone, etc)
    @Override
    protected void onPause() {
        super.onPause();

        // Save the current data
        workoutTitleEditText = findViewById(R.id.workoutTitleEditText);
        workout = new Workout(date, workoutTitleEditText.getText().toString(), exercises);

        // Go through workouts, if a workout exists for that date already then get rid of it and add the new one
        for (int i = 0; i < workouts.size(); i++){
            if (workouts.get(i).getDate() == workout.getDate()){
                workouts.remove(i);
            }
        }
        workouts.add(workout);

        Gson gson = new Gson();
        String workoutsJson = gson.toJson(workouts);
        String templatesJson = gson.toJson(templates);

        // Store the JSON string in sharedpreferences
        sp.edit().putString("workouts", workoutsJson).commit();
        sp.edit().putString("templates", templatesJson).commit();
    }

    private void resetData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        workouts = new ArrayList<>();
        templates = new ArrayList<>();
        workoutsJson = "";
        templatesJson = "";
    }
}

/*

TODO:
        100. add a delete set button?

 */