package com.b105.tamilpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PuzzleBoard PuzzleArea;
    GridView WordCheck;
    ArrayList<String> WordsList;
    ArrayList<String> Letters;
    GridGenerator GGen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Letters = new ArrayList<>();
        String[] AllWords = getResources().getStringArray(R.array.puzzle_words);
        WordCheck = (GridView) findViewById(R.id.wordcheck);

        ArrayList<ArrayList<Character>> LettersList = new ArrayList<ArrayList<Character>>();
        int Rows = this.getResources().getInteger(R.integer.rows), Columns = this.getResources().getInteger(R.integer.columns);

        PuzzleArea = (PuzzleBoard) findViewById(R.id.puzzlearea);
        WordsList = new ArrayList<String>();

       for(int WordLen = 0; WordLen < 10; WordLen++)
       {
           String temp = AllWords[(int)(Math.random()*AllWords.length-1)];
           while(temp.length() >= Math.min(Rows,Columns)-1) {
               temp = AllWords[(int)(Math.random()*AllWords.length-1)];
           }
           WordsList.add(temp.toUpperCase());
       }

        GGen = new GridGenerator(WordsList,Rows,Columns);

        LettersList = GGen.GetPuzzleGrid();

        for (ArrayList<Character> w : LettersList) {
            for(char a: w)
                Letters.add(Character.toString(a));
        }

        Log.d("List Count",Integer.toString(LettersList.size()));

        GridArrayAdapter PuzzleAdapter = new GridArrayAdapter(this,R.layout.puzzle_cell_layout,Letters);
        Log.d("List Count",Integer.toString(PuzzleAdapter.getCount()));
        PuzzleArea.setAdapter(PuzzleAdapter);

        GridArrayAdapter WordCheckAdapter = new GridArrayAdapter(this,R.layout.word_check,WordsList);
        WordCheck.setAdapter(WordCheckAdapter);
    }
}