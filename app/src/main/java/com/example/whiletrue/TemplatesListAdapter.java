package com.example.whiletrue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

// This is the adapter for the ListView that shows the templates. This is used for the Apply Workout and Remove Workouts options.
public class TemplatesListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] names;
    private AlertDialog dialog;

    public TemplatesListAdapter(Context context, String[] names, AlertDialog dialog) {
        super(context, R.layout.template_name, names);
        this.context = context;
        this.names = names;
        this.dialog = dialog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.template_name, parent, false);

        // Set the text of the TextView to the name of the template
        TextView templateNameTxt = itemView.findViewById(R.id.templateNameTxt);
        templateNameTxt.setText(names[position]);

        return itemView;
    }
}