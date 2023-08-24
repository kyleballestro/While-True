package com.example.whiletrue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

// This is the adapter for the Options list in the WorkoutDetailsActivity when the kebab icon is pressed
public class OptionsListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] itemTexts;
    private final int[] itemImages;
    private ArrayList<Exercise> exercises;
    private AlertDialog dialog;

    public OptionsListAdapter(Context context, String[] itemTexts, int[] itemImages, ArrayList<Exercise> exercises, AlertDialog dialog) {
        super(context, R.layout.options_item, itemTexts);
        this.context = context;
        this.itemTexts = itemTexts;
        this.itemImages = itemImages;
        this.exercises = exercises;
        this.dialog = dialog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.options_item, parent, false);

        // Set the text and image for each option
        ImageView itemImageView = itemView.findViewById(R.id.optionIcon);
        TextView itemTextView = itemView.findViewById(R.id.optionNameTxt);

        itemImageView.setImageResource(itemImages[position]);
        itemTextView.setText(itemTexts[position]);

        return itemView;
    }
}