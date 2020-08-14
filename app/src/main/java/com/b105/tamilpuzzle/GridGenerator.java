package com.b105.tamilpuzzle;

/* package whatever; // don't place package name! */

import android.util.Log;
import java.util.*;
import java.lang.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class GridGenerator {//extends Thread {
    ArrayList<ArrayList<Character>> PuzzleList;
    ArrayList<String> Words;
    int Rows, Columns;
    ArrayList<int[]> Directions;
    Boolean GridGenerated;


    GridGenerator(ArrayList<String> Words,int rows, int columns)
    {
        PuzzleList = new ArrayList<ArrayList<Character>>();
        this.Words = Words;
        Rows = rows;
        Columns = columns;
        GridGenerated = false;

        Directions = new ArrayList<int[]>();
        Directions.add(new int[]{0,-1}); // Left
        Directions.add(new int[]{0,1}); // Right
        Directions.add(new int[]{-1,0}); // Up
        Directions.add(new int[]{1,0}); // Down
        Directions.add(new int[]{-1,1}); // Up Right
        Directions.add(new int[]{-1,-1}); // Up Left
        Directions.add(new int[]{1,1}); // Down Right
        Directions.add(new int[]{1,-1}); // Down Left

        this.Words = Words;

        for(int row = 0; row<this.Rows; row++)
        {
            ArrayList<Character> CRow = new ArrayList<Character>();
            for(int column = 0;column<this.Columns;column++)
            {
                CRow.add('.');
            }
            PuzzleList.add(CRow);
        }
    }

    void SetWordsList(ArrayList<String> Words)
    {
        this.Words = Words;
    }

    boolean CheckWord(String Word, int WordLength)
    {
        if(WordLength > this.Rows || WordLength > Math.floor(Math.sqrt(this.Rows*this.Rows*2)))
        {
            Log.d("Error","The length of Word,"+Word+" is greater than the grid");
            return false;
        }

        return true;
    }


    boolean GenerateGrid()
    {

        int[] Position = {};
        int Retry = 0;

            for (int index = 0; index < Words.size(); index++) {
                String Word = Words.get(index);
                int Size = Word.length();

                if (!CheckWord(Word, Size)) {
                    Log.d("Error", "Word length is longer");
                    return false;
                }

                Position = GeneratePosition(Word.length());

                if (!PlaceWord(Word, Directions.get(Position[2]), Size, Position[0], Position[1])) {
                    index--;
                    if (Retry > 70) {
                        Log.d("Error", "Could not place the Word. Maximum number of tries reached");
                        return false;
                    }
                    Retry++;
                    continue;
                }
                Retry = 0;
            }

            for (int row = 0; row < this.Rows; row++) {
                for (int column = 0; column < this.Columns; column++) {
                    if (PuzzleList.get(row).get(column) == '.')
                        PuzzleList.get(row).set(column, (char) ((Math.random() * 26) + 65));
                }
            }

        Log.d("Success","Grid Generated");
        return true;
    }

    int[] GeneratePosition(int Length)
    {
        int[] Position = new int[3];

        int Direction = (int) (Math.random() * (Directions.size()-1));
        switch (Direction)
        {
            case 0: // Filling in left direction
                Position[0] = (int)(Math.random() * (this.Rows - 1));
                Position[1] = (int)(Length - 1 + (Math.random() * (this.Columns-1-Length)));
                break;
            case 1: // Filling in right direction
                Position[0] = (int)(Math.random() * (this.Rows - 1));
                Position[1] = (int)(Math.random() * (this.Columns - Length - 1));
                break;
            case 2: // Filling in up direction
                Position[0] = (int)(Length - 1 + (Math.random() * (this.Rows - 1 -Length)));
                Position[1] = (int)(Math.random() * (this.Columns - 1));
                break;
            case 3: // Filling in down direction
                Position[0] = (int)(Math.random() * (this.Rows - Length - 1));
                Position[1] = (int)(Math.random() * (this.Columns - 1));
                break;
            case 4: // Filling in up right direction
                Position[0] = (int)(Length - 1 + (Math.random() * (this.Rows - 1 -Length)));
                Position[1] = (int)(Math.random() * (this.Columns - Length - 1));
                break;
            case 5: // Filling in up left direction
                Position[0] = (int)(Length - 1 + (Math.random() * (this.Rows - 1 -Length)));
                Position[1] = (int)(Length - 1 + (Math.random() * (this.Columns-1-Length)));
                break;
            case 6: // Filling in down right direction
                Position[0] = (int)(Math.random() * (this.Rows - Length - 1));
                Position[1] = (int)(Math.random() * (this.Columns - Length - 1));
                break;
            case 7: // Filling in down left direction
                Position[0] = (int)(Math.random() * (this.Rows - Length - 1));
                Position[1] = (int)(Length - 1 + (Math.random() * (this.Columns-1-Length)));
        }

        Position[2] = Direction;

        return Position;
    }

    boolean PlaceWord(String Word, int[] Direction, int StringLength, int row, int column)
    {
        int index = 0,ir =0,ic = 0;

        for(;index < StringLength; index++)
        {
            Log.d("Value","Row: "+(row+ir)+", Column: "+(column+ic));
            char Letter = PuzzleList.get(row+ir).get(column+ic);
            if(Letter != '.' && Letter != Word.charAt(index))
                break;
            ir+=Direction[0];
            ic+=Direction[1];
        }

        if(index < StringLength)
        {
            Log.d("Error","Word "+Word+"couldn't be placed");
            GridGenerated = false;
            return false;
        }

        ic = 0;
        ir = 0;
        for(index = 0;index < StringLength; index++)
        {
            char Letter = PuzzleList.get(row+ir).get(column+ic);
            if(Letter == '.')
                PuzzleList.get(row+ir).set(column+ic,Word.charAt(index));
            ir+=Direction[0];
            ic+=Direction[1];
        }

        Log.d("Success","Word Placed in Grid");

        GridGenerated = true;
        return true;
    }

    public ArrayList<ArrayList<Character>> GetPuzzleGrid()
    {
        int tries = 0;
        while (!this.GenerateGrid() && tries < 5)
            tries++;
        return PuzzleList;
    }
}
