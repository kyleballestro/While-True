package com.example.whiletrue;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FoodItemTypeAdapter extends TypeAdapter<FoodItem> {

    @Override
    public void write(JsonWriter out, FoodItem value) throws IOException {

    }

    // Reading the JSon back into FoodItem objects
    @Override
    public FoodItem read(JsonReader in) throws IOException {
        String name = null;
        String meal = null;
        String calories = null;
        String protein = null;
        String carbs = null;
        String fats = null;

        in.beginObject();
        while (in.hasNext()) {
            String propertyName = in.nextName();
            switch (propertyName) {
                case "name":
                    name = in.nextString();
                    break;
                case "meal":
                    meal = in.nextString();
                    break;
                case "calories":
                    calories = in.nextString();
                    break;
                case "protein":
                    protein = in.nextString();
                    break;
                case "carbs":
                    carbs = in.nextString();
                    break;
                case "fats":
                    fats = in.nextString();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();

        return new FoodItem(name, meal, calories, protein, carbs, fats);
    }
}