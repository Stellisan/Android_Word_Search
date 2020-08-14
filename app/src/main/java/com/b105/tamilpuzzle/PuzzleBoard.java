package com.b105.tamilpuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.GridView;

import java.text.CharacterIterator;
import java.util.ArrayList;

import static java.lang.Math.abs;

// Custom grid view
public class PuzzleBoard extends GridView {

    class Line{
        float x1,y1,x2,y2;
        Paint line_style;

        Line()
        {
            x1 = -1;
            y1 = -1;
            x2 = -1;
            y2 = -1;
        }

        Line(float x, float y)
        {
            x1 = x;
            y1 = y;
            x2 = x;
            y2 = y;
        }

        void set_paint(Paint p)
        {
            this.line_style = p;
        }
    }

    ArrayList<Integer> s;
    String p;
    Line selected;
    ArrayList<Line> all_selected;
    int length,oldx,oldy;
    int first_position;

    int[] Colors = {Color.RED,Color.GREEN,Color.GRAY,Color.CYAN,Color.MAGENTA,Color.YELLOW};
    int line_color;

    // Enumerator for the direction off the line
    enum Direction{
        vertical,
        horizontal,
        cross
    }
    Direction d;

    // Constructor
    public PuzzleBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        s = new ArrayList<Integer>();
        p = new String();
        first_position = -1;
        d = Direction.horizontal;
        length = 0;
        line_color = 0;
        oldx = 0;
        oldy = 0;
        all_selected = new ArrayList<Line>();
    }

    /*
    * Overriding the function onTouchEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int)ev.getX();
        int y = (int)ev.getY();
        float dx = 0,dy = 0,diff = 0;
        int position = this.pointToPosition(x,y);
        View Child = this.getChildAt(position);

        // If the touch event goes out of boundary
        if(Child == null)
            return false;

        super.onTouchEvent(ev);

        // Switch based on the events
        switch(ev.getActionMasked()){
            // Action when the finger touches the screen
            case MotionEvent.ACTION_DOWN:
                s.add(position);
                length++;
                oldx = x;
                oldy = y;
                selected = new Line(Child.getX() + Child.getWidth()/2,Child.getY() + Child.getHeight()/2);
                first_position = position;
                selected.set_paint(this.get_line_style());
                return true;

                // Called when moving the finger
            case MotionEvent.ACTION_MOVE:
                if(length > 0 && s.get(length-1) != position)
                {
                    s.add(position);
                    length++;

                    // Slope calculation
                    dx = x - oldx;
                    dy = y - oldy;
                    oldx = x;
                    oldy = y;

                    if(dx == 0)
                        dx = 1;

                    // Calculate the tan of the angle of the slope
                    diff = abs(dy)/abs(dx);

                    // Find Direction of the touch movement
                    if(diff <= 0.404)
                        d = Direction.horizontal;
                    else if(diff <= 2.35)
                        d = Direction.cross;
                    else if(diff == dy || diff > 2.35)
                        d = Direction.vertical;

                    // Validate the position and redraw the canvas
                    ValidateDirection(position,first_position);
                }
                return true;

                // When the finger taken off the screen
            case MotionEvent.ACTION_UP:
                // Check if the current child is already inserted into the arraylist
                if(length > 0 && s.get(length-1) != position)
                {
                    s.add(position);
                    length++;
                }

                // Validate the position and redraw the canvas
                ValidateDirection(position,first_position);

                // Store the already drawn lines for the canvas
                all_selected.add(selected);


                for(int u : s)
                    p += String.valueOf(this.getItemAtPosition(u));

                // Clearing all the global variables
                this.clear();
                return true;

            default: // Invalid actions
                return false;
        }
    }

    /*
    * Function Name: Clear
    * Description: Clears and defaults all the global variables
    * Parameters: None
    * Return: Void
    */
    public void clear()
    {
        s.clear();
        p = "";
        oldx = 0;
        oldy = 0;
        length = 0;
        first_position = 0;
        d = Direction.horizontal;
    }

    /*
    * Overrides the default onDraw function
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(first_position == -1)
            return;

        canvas.drawLine(selected.x1,selected.y1,selected.x2,selected.y2,selected.line_style);

        for(Line t : all_selected)
            canvas.drawLine(t.x1,t.y1,t.x2,t.y2,t.line_style);
    }

    /*
     * Function Name: ValidateDirection
     * Description: Checks if the cells are selected correctly and redraws the view.
     * Parameters:
     *          int cposition - current position
     *          int pposition - previous position
     * Return: Void
     */
    void ValidateDirection(int cposition,int pposition)
    {
        // Get the number of columns from resource
        int Number_cols = getContext().getResources().getInteger(R.integer.columns);

        // Calculate the rows and columns relative to the gridview
        int CRow = cposition / Number_cols;
        int CColumn = cposition % Number_cols;
        int PRow = pposition / Number_cols;
        int PColumn = pposition % Number_cols;

        // the second position
        int FinalPosition = -1;

        // Variable for the child view in the second position
        View Child = this.getChildAt(0);

        if(d == Direction.cross && abs(PRow - CRow) == abs(PColumn - CColumn)){
            FinalPosition = cposition;
            Child = this.getChildAt(FinalPosition);
        } else if(d == Direction.horizontal)
        {
            FinalPosition += PRow * Number_cols;
            FinalPosition += CColumn+1;
            Child = this.getChildAt(FinalPosition);
        } else if(d == Direction.vertical)
        {
            FinalPosition += CRow * Number_cols;
            FinalPosition += PColumn+1;
            Child = this.getChildAt(FinalPosition);
        } else {
            return;
        }

        // Check for valid position
        if(FinalPosition != -1)
        {
            selected.x2 = Child.getX() + Child.getWidth()/2;
            selected.y2 = Child.getY() + Child.getHeight()/2;
            // to redraw the view
            this.invalidate();
            return;
        }
    }

    /*
     * Function Name: get_line_style
     * Description: Creates a new paint variable with certain styles
     * Parameters: None
     * Return: Paint
     */
    Paint get_line_style()
    {
        Paint select_line = new Paint();
        select_line.setAlpha(15);
        select_line.setColor(Colors[line_color++%6]);
        select_line.setStrokeCap(Paint.Cap.ROUND);
        select_line.setStrokeWidth(100);
        select_line.setStyle(Paint.Style.FILL);
        return select_line;
    }
}
