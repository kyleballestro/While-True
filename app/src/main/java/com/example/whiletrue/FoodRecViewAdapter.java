package com.example.whiletrue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodRecViewAdapter extends RecyclerView.Adapter<FoodRecViewAdapter.ViewHolder>{

    private ArrayList<FoodItem> foods = new ArrayList<FoodItem>();
    private TextView totalCaloriesNum, totalProteinNum, totalCarbsNum, totalFatsNum;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the food name
        if (foods.get(position) != null){
            // Capitalize and set the food name
            String foodName = foods.get(position).getName();
            String foodNameCap = foodName.substring(0, 1).toUpperCase() + foodName.substring(1);
            holder.foodNameTxt.setText(foodNameCap);

            // Set the whole string for the macros
            String calories = foods.get(position).getCalories();
            String protein = foods.get(position).getProtein();
            String carbs = foods.get(position).getCarbs();
            String fats = foods.get(position).getFats();
            String macros = calories + " calories, " + protein + " g protein, " + carbs + " g carbs, " + fats + " g fats";
            holder.foodMacrosTxt.setText(macros);
        }

        // Set up the dialog for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String foodNameCap = foods.get(pos).getName().substring(0, 1).toUpperCase() + foods.get(pos).getName().substring(1);
                builder.setTitle("Delete " + foodNameCap + " from " + foods.get(pos).getMeal() + "?");
                // This is technically the setPositiveButton, but it has to be labeled as the negative button
                // in the code so that the button appears on the left side in the app
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Remove the food
                        foods.remove(pos);
                        notifyItemRemoved(pos);
                        notifyDataSetChanged();
                    }
                });
                // Like the previous comment, this is technically the setNegativeButton, it is just labeled
                // this way in the code so that the "No" option appears on the right side in the app
                builder.setPositiveButton("No", null);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (foods != null) {
            return foods.size();
        }
        else{
            return 0;
        }
    }

    public void setFoods(ArrayList<FoodItem> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    public void setContext(Context context){
        this.context = context;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foodNameTxt, foodMacrosTxt;
        private ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTxt = itemView.findViewById(R.id.foodNameTxt);
            foodMacrosTxt = itemView.findViewById(R.id.foodMacrosTxt);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

}
