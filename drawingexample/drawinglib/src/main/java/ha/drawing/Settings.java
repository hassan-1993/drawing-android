package ha.drawing;

import android.graphics.Paint;
import android.graphics.Rect;


import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * Created by houssam on 9/25/2016.
 */
public class Settings {

    public float defaultTextSize        = 80;
    public float defaultStroke          = 1.7f; // Value is considered in dp
    public float MinimumDefaultStroke   = 0;    // Means 2dp

    // Empty rectangle width is the width of Rect_Empty_Width plus Text_spacing margin times two from left anf right
    private boolean editMode = false;

    public float MAX_TextHeight = 0;
    public float Rect_Min_Width = 0;
    public float RECT_HEIGHT    = 0;

    //
    // Text
    //
    public @CalculatedField float TEXT_SPACING = 8;
    public @CalculatedField float WORD_SPACING = 20;     // Used between words

    // Left and right margin of block containing an operator '*' '-' '=' '+' or ' ' in case there is two division
    // near each other like equation {5/5}{4/4} we addNewEquation a margin left for first one {5/5}
    public @CalculatedField float block_Margin_Operator = 10;

    public float maxBottom = 0; // The maximum bottom of a character which we use to shift all numbers character upward

    //
    // Division
    //
    public @CalculatedField float DivisionHeight        = 0;  // Height of division since it is drawn as a rectangle with height and width ,stroke width
    public @CalculatedField float DivisionWidthFactor   = 1f; // If we have 3/4/5 the first division width will be multiply by DivisionWidthFactor and second one will have the width of first one times DivisionWidthFactor
    public @CalculatedField float DivisionOffsetWidth   = 30; //extra width addNewEquation to left and right of a division
    public @CalculatedField float block_Division_Margin = 10; //division margin bottom and top

    //
    // Bracket
    //
    public @CalculatedField float max_Bracket_Width         = 120f;     // maximum width of bracket
    public @CalculatedField float Bracket_Width_Fraction    = 0.27f;    // the width of bracket equal=Bracket_Width_Fraction*height of bracket and mus be between maximum width and minimum width
    public @CalculatedField float Min_Bracket_Width         = 1f;       // minimum width of bracket

    //
    // Power
    //
    public @CalculatedField float PowerHeight               = -15;  // The power height ,negative value means move closer
    public @CalculatedField float PowerWidth                = 0;    // Width of power like for example value=3 and 5^4 means x position of 4 is 3 pixal away from the end of 5
    public @CalculatedField float PowerScaleProportion      = 0.88f; // How much to scale down the textsize going with each power ,


    //
    // Base
    //
    public @CalculatedField float BaseOffsetHeight          = 0;        // The base height ,negative value means closer to something like log
    public @CalculatedField float BaseOffsetWidth           = 0;        // Width of base like for example value=3 and log(4,3) means x position of 4 is 3 pixal away from the end of log
    public @CalculatedField float BaseScaleProportion       = 0.83f;    // How much to scale down the textview going with each log

    //
    // Radical
    //
    public @CalculatedField float maxHeight                 = 10000;    // Maximum allowed height distance between point b and c to be drawn in the radicalblock
    public @CalculatedField float paddingTop_radical        = 15;       // For example if value=10 means there will be a padding of 10 from what inside the radical from top
    public @CalculatedField float ExtraRadicalWidth         = 20;       // For example if value=10 means extra width of radical equal 10
    public @CalculatedField float ExtraRadicalHeight        = 5;        // For example if value=8 means extra radical height will be 8px which means it is the bottom margin of radical
    public @CalculatedField float ExtraPadding_RadicalLeft  = 10;       // Extra padding from left of inside the radical
    public @CalculatedField float ExtraPaddingTopFraction   = 10;
    //
    // Matrix
    //
    public @CalculatedField float offsetColX                = 85;       // Offset x between two consecutive cols
    public @CalculatedField float offsetRowY                = 38;       // Offset y between two consecutive rows
    public @CalculatedField float TopBottomOffsetY          = 20;       // Padding y from above and bottom of the matrix

    // ======================================================================
    // Constructors
    // ======================================================================
    public Settings(float textSize){

        // Scale all the values according to the text size.
        // So we can adjust all the variables:- for example small text size will have smaller block margin and large bigger
        float scale = scaleTextSize(textSize);

        // Use reflection to find all the values that need to be scaled.
        for(Field field : getClass().getFields()){
            CalculatedField statusAnnotation = field.getAnnotation(CalculatedField.class);
            if(statusAnnotation != null) {
                try {
                    Float value = field.getFloat(this);
                    value *= scale;
                    field.setFloat(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void set(){
        float maxWidth = 0;
        float minWidth = 1000;

        // Rect_Empty_Width is for the empty block it is the width of smallest char which is plus extra TEXT_SPACING from left and right
        Paint paint = new Paint();
        paint.setTextSize(defaultTextSize);
        Rect bound = new Rect();
        paint.getTextBounds("y", 0, 1, bound);
        this.maxBottom = bound.bottom/2;

        // Find the maximum width and height of the numbers [0 -> 9]
        for(int i=0;i<10;i++){
            paint.getTextBounds(i+"",0,1,bound);

            if(maxWidth<bound.width())
                maxWidth=bound.width();
            else if(minWidth>bound.width())
                minWidth=bound.width();

            MAX_TextHeight = bound.height() > MAX_TextHeight ? bound.height() : MAX_TextHeight;
        }

        Rect_Min_Width = minWidth + (maxWidth-minWidth)*0.5f;
    }

    public float scaleTextSize(float newTextSize){
        return newTextSize/ defaultTextSize;
    }

    public float measureTextWidth(String seq){
        Rect tempBound = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(defaultTextSize);
        paint.getTextBounds(seq, 0, seq.length(), tempBound);
        return tempBound.width();
    }

    public float measureTextHeight(String seq){
        Rect tempBound = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(defaultTextSize);
        paint.getTextBounds(seq, 0, seq.length(), tempBound);
        return tempBound.height();
    }

    private HashMap<String,Float> map = new HashMap<>();
    public float getValue(String value){
        if(editMode){
            Float valueWidth= map.get(value);
            if(valueWidth==null){
                map.put(value, measureTextWidth(value));
            }
            return map.get(value);
        }

        return measureTextWidth(value);
    }



    public int getStrokeWidth(float textSize){return (int) (defaultStroke *textSize/ defaultTextSize);}

    ////////////////////////////////////////////////////////////////////
    private static HashMap<Float, Settings> cache = new HashMap<>();

    public static Settings getSetting(float textSize){
        if(cache.get(textSize) == null)
            cache.put(textSize, new Settings(textSize));

        return cache.get(textSize);
    }
}
