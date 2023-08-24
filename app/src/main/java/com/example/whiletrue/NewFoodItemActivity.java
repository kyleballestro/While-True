package com.example.whiletrue;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewFoodItemActivity extends AppCompatActivity {

    private Button confirmBtn, cancelBtn;
    private EditText foodNameEditText, caloriesEditText, proteinEditText, carbsEditText, fatsEditText;
    private TextView warningTxt;
    private boolean allNumeric = true;
    private final String[] meals = {"Breakfast", "Lunch", "Dinner", "Snacks"};
    private Spinner mealSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        confirmBtn = findViewById(R.id.confirmBtn);
        foodNameEditText = findViewById(R.id.foodNameEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        proteinEditText = findViewById(R.id.proteinEditText);
        carbsEditText = findViewById(R.id.carbsEditText);
        fatsEditText = findViewById(R.id.fatsEditText);
        warningTxt = findViewById(R.id.warningTxt);
        cancelBtn = findViewById(R.id.cancelBtn);
        mealSpinner = findViewById(R.id.mealSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.meal_spinner_item, meals);
        mealSpinner.setAdapter(adapter);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");

        // Set the appbar title and home button listener
        TextView activityTitle = findViewById(R.id.activityTitle);
        activityTitle.setText("New Food");

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewFoodItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Listener for confirm button
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure that validateData() returns true, meaning the data entered was appropriate
                if (validateData()){
                    // Create new food item object
                    String meal = mealSpinner.getSelectedItem().toString();
                    String name = foodNameEditText.getText().toString();
                    String calories = caloriesEditText.getText().toString();
                    String protein = proteinEditText.getText().toString();
                    String carbs = carbsEditText.getText().toString();
                    String fats = fatsEditText.getText().toString();
                    FoodItem food = new FoodItem(name, meal, calories, protein, carbs, fats);

                    // Create intent back to MacroDayActivity. Add the new food item object as an extra.
                    Intent intent = new Intent(NewFoodItemActivity.this, MacroDayActivity.class);
                    intent.putExtra("foodItem", food);
                    intent.putExtra("date", date);
                    startActivity(intent);
                    finish();
                }
                else{
                    // If any numeric values were not numeric, throw error
                    if (allNumeric == false){
                        warningTxt.setText("Please ensure that macronutrients are numeric values");
                        warningTxt.setVisibility(View.VISIBLE);
                    }
                    // Otherwise, at least one field is still empty, throw error
                    else{
                        warningTxt.setText("Please ensure that all of the fields have been filled");
                        warningTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Listener for cancel button. Just returns to MacroDayActivity.
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outgoingIntent = new Intent(NewFoodItemActivity.this, MacroDayActivity.class);
                outgoingIntent.putExtra("date", date);
                startActivity(outgoingIntent);
            }
        });
    }

    // This method validates the data. It makes sure a meal is selected, all numeric fields are numeric, and all fields are filled in.
    private boolean validateData(){
        boolean valid = false;
        // Check that an option in spinner is selected
        if (mealSpinner.getSelectedItem() != null){
            valid = true;
        }
        else{
            valid = false;
        }

        // Make sure each editText is filled out
        if (!TextUtils.isEmpty(foodNameEditText.getText().toString()) &&
                !TextUtils.isEmpty(caloriesEditText.getText().toString()) &&
                !TextUtils.isEmpty(proteinEditText.getText().toString()) &&
                !TextUtils.isEmpty(carbsEditText.getText().toString()) &&
                !TextUtils.isEmpty(fatsEditText.getText().toString())){
            valid = true;
        }
        else{
            valid = false;
        }

        // Make sure each numeric value is actually numeric
        allNumeric = true;
        String calories = caloriesEditText.getText().toString();
        String protein = proteinEditText.getText().toString();
        String carbs = carbsEditText.getText().toString();
        String fats = fatsEditText.getText().toString();
        if (isNumeric(calories) == false || isNumeric(protein) == false || isNumeric(carbs) == false || isNumeric(fats) == false){
            valid = false;
            allNumeric = false;
        }
        return valid;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}