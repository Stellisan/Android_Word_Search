package com.b105.tamilpuzzle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GridArrayAdapter extends ArrayAdapter {

    ArrayList<String> PuzzleLetters;
    boolean GridViewCheck;

    @SuppressWarnings("unchecked")
    public GridArrayAdapter(@NonNull Context context, int textViewResourceId, @NonNull List objects) {
        super(context, textViewResourceId, objects);
        PuzzleLetters = (ArrayList<String>)objects;
        if(textViewResourceId == R.layout.puzzle_cell_layout)
            GridViewCheck = true;
        else if(textViewResourceId == R.layout.word_check)
            GridViewCheck = false;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView l;

        if(GridViewCheck) {
            v = inflater.inflate(R.layout.puzzle_cell_layout, null);
            l = (TextView) v.findViewById(R.id.Letter_Cell);
        }
        else {
            v = inflater.inflate(R.layout.word_check, null);
            l = (TextView) v.findViewById(R.id.word_checker_text);
        }

        l.setText(PuzzleLetters.get(position));
        return v;
    }
}
