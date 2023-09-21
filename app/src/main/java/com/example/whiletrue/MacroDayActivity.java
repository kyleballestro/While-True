package com.example.whiletrue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MacroDayActivity extends AppCompatActivity {

    private TextView dateText, totalCaloriesNum, totalProteinNum, totalCarbsNum, totalFatsNum;
    private ImageButton addNewFoodBtn;
    private HashMap<String, ArrayList<FoodItem>> breakfastMap;
    private HashMap<String, ArrayList<FoodItem>> lunchMap;
    private HashMap<String, ArrayList<FoodItem>> dinnerMap;
    private HashMap<String, ArrayList<FoodItem>> snacksMap;
    private String date;
    private FoodRecViewAdapter breakfastAdapter;
    private FoodRecViewAdapter lunchAdapter;
    private FoodRecViewAdapter dinnerAdapter;
    private FoodRecViewAdapter snacksAdapter;
    private SharedPreferences sp;
    private int totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFats = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_day);

        // Set the appbar title and home button listener
        TextView activityTitle = findViewById(R.id.activityTitle);
        activityTitle.setText("Daily Macros");

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MacroDayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sp = getSharedPreferences("maps", Context.MODE_PRIVATE);

        // Get the JSON strings from sharedpreferences
        String bMapjsonString = sp.getString("breakfastMap", null);
        String lMapjsonString = sp.getString("lunchMap", null);
        String dMapjsonString = sp.getString("dinnerMap", null);
        String sMapjsonString = sp.getString("snacksMap", null);

        // Convert the JSON strings to hashmaps
        Gson gson = new GsonBuilder().registerTypeAdapter(FoodItem.class, new FoodItemTypeAdapter()).create();
        breakfastMap = gson.fromJson(bMapjsonString, new TypeToken<HashMap<String, ArrayList<FoodItem>>>() {}.getType());
        lunchMap = gson.fromJson(lMapjsonString, new TypeToken<HashMap<String, ArrayList<FoodItem>>>() {}.getType());
        dinnerMap = gson.fromJson(dMapjsonString, new TypeToken<HashMap<String, ArrayList<FoodItem>>>() {}.getType());
        snacksMap = gson.fromJson(sMapjsonString, new TypeToken<HashMap<String, ArrayList<FoodItem>>>() {}.getType());

        if (breakfastMap == null){
            breakfastMap = new HashMap<>();
        }
        if (lunchMap == null){
            lunchMap = new HashMap<>();
        }
        if (dinnerMap == null){
            dinnerMap = new HashMap<>();
        }
        if (snacksMap == null){
            snacksMap = new HashMap<>();
        }

        // RESET EVERYTHING (keep this commented out unless desired)
//        resetData();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        dateText = findViewById(R.id.dateText);
        dateText.setText(date);

        RecyclerView breakfastItems = findViewById(R.id.breakfastItems);
        RecyclerView lunchItems = findViewById(R.id.lunchItems);
        RecyclerView dinnerItems = findViewById(R.id.dinnerItems);
        RecyclerView snacksItems = findViewById(R.id.snacksItems);

        breakfastAdapter = new FoodRecViewAdapter();
        lunchAdapter = new FoodRecViewAdapter();
        dinnerAdapter = new FoodRecViewAdapter();
        snacksAdapter = new FoodRecViewAdapter();

        // Get the FoodItem object from the NewFoodItemActivity and place it in the appropriate meal list
        if (intent != null){
            if (intent.hasExtra("foodItem")){
                FoodItem food = (FoodItem) intent.getSerializableExtra("foodItem");
                if (food != null){
                    String meal = food.getMeal();
                    // Breakfast
                    if (meal.equals("Breakfast")){
                        ArrayList<FoodItem> existingArrayList = breakfastMap.get(date);
                        if (existingArrayList == null) {
                            existingArrayList = new ArrayList<>();
                        }
                        existingArrayList.add(food);
                        breakfastMap.put(date, existingArrayList);
                    }
                    // Lunch
                    else if (meal.equals("Lunch")){
                        ArrayList<FoodItem> existingArrayList = lunchMap.get(date);
                        if (existingArrayList == null) {
                            existingArrayList = new ArrayList<>();
                        }
                        existingArrayList.add(food);
                        lunchMap.put(date, existingArrayList);
                    }
                    // Dinner
                    else if (meal.equals("Dinner")){
                        ArrayList<FoodItem> existingArrayList = dinnerMap.get(date);
                        if (existingArrayList == null) {
                            existingArrayList = new ArrayList<>();
                        }
                        existingArrayList.add(food);
                        dinnerMap.put(date, existingArrayList);
                    }
                    // Snacks
                    else if (meal.equals("Snacks")){
                        ArrayList<FoodItem> existingArrayList = snacksMap.get(date);
                        if (existingArrayList == null) {
                            existingArrayList = new ArrayList<>();
                        }
                        existingArrayList.add(food);
                        snacksMap.put(date, existingArrayList);
                    }
                }
            }
        }

        // Setting up the recyclerview adapters
        if (breakfastMap != null){
            breakfastAdapter.setFoods(breakfastMap.get(date));
        }
        if (lunchMap != null){
            lunchAdapter.setFoods(lunchMap.get(date));
        }
        if (dinnerMap != null){
            dinnerAdapter.setFoods(dinnerMap.get(date));
        }
        if (snacksMap != null){
            snacksAdapter.setFoods(snacksMap.get(date));
        }

        breakfastAdapter.setContext(this);
        lunchAdapter.setContext(this);
        dinnerAdapter.setContext(this);
        snacksAdapter.setContext(this);

        breakfastItems.setAdapter(breakfastAdapter);
        lunchItems.setAdapter(lunchAdapter);
        dinnerItems.setAdapter(dinnerAdapter);
        snacksItems.setAdapter(snacksAdapter);

        breakfastItems.setLayoutManager(new LinearLayoutManager(MacroDayActivity.this));
        lunchItems.setLayoutManager(new LinearLayoutManager(MacroDayActivity.this));
        dinnerItems.setLayoutManager(new LinearLayoutManager(MacroDayActivity.this));
        snacksItems.setLayoutManager(new LinearLayoutManager(MacroDayActivity.this));

        // This next section deals with the daily macros box at the top of the screen
        // Adding up all of the values for the total daily macros
        totalCaloriesNum = findViewById(R.id.totalCaloriesNum);
        totalProteinNum = findViewById(R.id.totalProteinNum);
        totalCarbsNum = findViewById(R.id.totalCarbsNum);
        totalFatsNum = findViewById(R.id.totalFatsNum);
        totalCalories = 0;
        totalProtein = 0;
        totalCarbs = 0;
        totalFats = 0;

        // Get the macro for each meal and each type of macro, then add them up
        ArrayList<FoodItem> breakfastItemsAL = breakfastMap.get(date);
        if (breakfastItemsAL != null){
            for (int i = 0; i < breakfastItemsAL.size(); i++){
                String calS = breakfastItemsAL.get(i).getCalories();
                if (Integer.valueOf(calS) != null){
                    int calI = Integer.valueOf(calS).intValue();
                    totalCalories += calI;
                }
                String proS = breakfastItemsAL.get(i).getProtein();
                if (Integer.valueOf(proS) != null){
                    int proI = Integer.valueOf(proS).intValue();
                    totalProtein += proI;
                }
                String carS = breakfastItemsAL.get(i).getCarbs();
                if (Integer.valueOf(carS) != null){
                    int carI = Integer.valueOf(carS).intValue();
                    totalCarbs += carI;
                }
                String fatS = breakfastItemsAL.get(i).getFats();
                if (Integer.valueOf(fatS) != null){
                    int fatI = Integer.valueOf(fatS).intValue();
                    totalFats += fatI;
                }
            }
        }
        ArrayList<FoodItem> lunchItemsAL = lunchMap.get(date);
        if (lunchItemsAL != null){
            for (int i = 0; i < lunchItemsAL.size(); i++){
                String calS = lunchItemsAL.get(i).getCalories();
                if (Integer.valueOf(calS) != null){
                    int calI = Integer.valueOf(calS).intValue();
                    totalCalories += calI;
                }
                String proS = lunchItemsAL.get(i).getProtein();
                if (Integer.valueOf(proS) != null){
                    int proI = Integer.valueOf(proS).intValue();
                    totalProtein += proI;
                }
                String carS = lunchItemsAL.get(i).getCarbs();
                if (Integer.valueOf(carS) != null){
                    int carI = Integer.valueOf(carS).intValue();
                    totalCarbs += carI;
                }
                String fatS = lunchItemsAL.get(i).getFats();
                if (Integer.valueOf(fatS) != null){
                    int fatI = Integer.valueOf(fatS).intValue();
                    totalFats += fatI;
                }
            }
        }
        ArrayList<FoodItem> dinnerItemsAL = dinnerMap.get(date);
        if (dinnerItemsAL != null){
            for (int i = 0; i < dinnerItemsAL.size(); i++){
                String calS = dinnerItemsAL.get(i).getCalories();
                if (Integer.valueOf(calS) != null){
                    int calI = Integer.valueOf(calS).intValue();
                    totalCalories += calI;
                }
                String proS = dinnerItemsAL.get(i).getProtein();
                if (Integer.valueOf(proS) != null){
                    int proI = Integer.valueOf(proS).intValue();
                    totalProtein += proI;
                }
                String carS = dinnerItemsAL.get(i).getCarbs();
                if (Integer.valueOf(carS) != null){
                    int carI = Integer.valueOf(carS).intValue();
                    totalCarbs += carI;
                }
                String fatS = dinnerItemsAL.get(i).getFats();
                if (Integer.valueOf(fatS) != null){
                    int fatI = Integer.valueOf(fatS).intValue();
                    totalFats += fatI;
                }
            }
        }
        ArrayList<FoodItem> snacksItemsAL = snacksMap.get(date);
        if (snacksItemsAL != null){
            for (int i = 0; i < snacksItemsAL.size(); i++){
                String calS = snacksItemsAL.get(i).getCalories();
                if (Integer.valueOf(calS) != null){
                    int calI = Integer.valueOf(calS).intValue();
                    totalCalories += calI;
                }
                String proS = snacksItemsAL.get(i).getProtein();
                if (Integer.valueOf(proS) != null){
                    int proI = Integer.valueOf(proS).intValue();
                    totalProtein += proI;
                }
                String carS = snacksItemsAL.get(i).getCarbs();
                if (Integer.valueOf(carS) != null){
                    int carI = Integer.valueOf(carS).intValue();
                    totalCarbs += carI;
                }
                String fatS = snacksItemsAL.get(i).getFats();
                if (Integer.valueOf(fatS) != null){
                    int fatI = Integer.valueOf(fatS).intValue();
                    totalFats += fatI;
                }
            }
        }

        totalCaloriesNum.setText(String.valueOf(totalCalories) + " cal");
        totalProteinNum.setText(String.valueOf(totalProtein) + " g");
        totalCarbsNum.setText(String.valueOf(totalCarbs) + " g");
        totalFatsNum.setText(String.valueOf(totalFats) + " g");

        // Set up listener to add a new food item. Intent to NewFoodItemActivity.
        addNewFoodBtn = findViewById(R.id.addNewFoodBtn);
        addNewFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outgoingIntent = new Intent(MacroDayActivity.this, NewFoodItemActivity.class);
                outgoingIntent.putExtra("date", date);
                startActivity(outgoingIntent);
            }
        });
    }

    // When back button is pressed, intent to CalendarActivity
    @Override
    public void onBackPressed() {
        if (this.equals(MacroDayActivity.this)) {
            Intent newIntent = new Intent(MacroDayActivity.this, CalendarActivity.class);
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
        Gson gson = new Gson();
        String bMapJson = gson.toJson(breakfastMap);
        String lMapJson = gson.toJson(lunchMap);
        String dMapJson = gson.toJson(dinnerMap);
        String sMapJson = gson.toJson(snacksMap);

        // Store the JSON string in sharedpreferences
        sp.edit().putString("breakfastMap", bMapJson).commit();
        sp.edit().putString("lunchMap", lMapJson).commit();
        sp.edit().putString("dinnerMap", dMapJson).commit();
        sp.edit().putString("snacksMap", sMapJson).commit();
    }

    private void resetData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        breakfastMap = new HashMap<>();
        lunchMap = new HashMap<>();
        dinnerMap = new HashMap<>();
        snacksMap = new HashMap<>();
    }
}

// Testing push
// Test 2