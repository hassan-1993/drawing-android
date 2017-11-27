package ha.drawing;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * Created by houssam on 9/25/2016.
 */
public class Setting {

    /*empty rectangle width is the width of Rect_Empty_Width plus Text_spacing margin times two from left anf right*/

    private boolean editMode=false;
    private  boolean init=false;
    public   float DefaultTextSize=80;

    public  float MAX_TextHeight=0; // the maximum height possible of a number that is between 1,2,3,4,5,6,7,8,9,0
    public  float Rect_Min_Width=0; //the width of block with TokenID T_EMPTY


    public @CalculatedField float TEXT_SPACING=8;
    public @CalculatedField float WORD_SPACING=20; //used between words
    public float RECT_HEIGHT=80; //the height of a block that contain text like 9
    public @CalculatedField float block_Margin_Operator=10; //left and right margin of block containing for  operator * - = or + or  in case  there two division near each other like equation {5/5}{4/4}  we addNewEquation a margin left for first one {5/5}



    public float maxbottom=0; //the maximum bottom of a character which we use to shift all numbers character updward


    /***********************for division*********************************/
    public   float DivisonHeight=0; //height of division since it is drawn as a rectangle with height and width ,stroke width
    public   float DivisonWidthFactor=1f; //if we have 3/4/5 the first division width will be multiply by DivisonWidthFactor and second one will have the width of first one times DivisonWidthFactor
    public  @CalculatedField float DivisionOffsetWidth=30; //extra width addNewEquation to left and right of a division
    public @CalculatedField float block_Division_Margin =10; //division margin bottom and top
    /***********************for division*********************************/

    /********************for bracket***************************/
    public @CalculatedField float max_Bracket_Width=120f; //maximum width of bracket
    public  float Bracket_Width_Fraction=0.27f; //the width of bracket equal=Bracket_Width_Fraction*height of bracket and mus be between maximum width and minimum width
    public @CalculatedField float Min_Bracket_Width=1f; //minimum width of bracket
    /******************************************************************/


    /********************for Power***************************/
    public @CalculatedField float PowerHeight=0;//the power height ,negative value means move closer
    public @CalculatedField float PowerWidth=0;//width of power like for example value=3 and 5^4 means x position of 4 is 3 pixal away from the end of 5
    public  float PowerScaleProportion=0.88f; //how much to scale down the textsize going with each power ,


    /********************for base***************************/
    public @CalculatedField float BaseOffsetHeight=0;//the base height ,negative value means closer to something like log
    public @CalculatedField float BaseOffsetWidth=0;////width of base like for example value=3 and log(4,3) means x position of 4 is 3 pixal away from the end of log
    public   float BaseScaleProportion=0.83f;  //how much to scale down the textview going with each log
    /********************for base***************************/


    public  float Defualtstroke=1.7f; //value is considered in dp
    public  float MinumumDefualtstroke=0; //means 2dp





    /**************for drawing of radical***********/
    public @CalculatedField float maxHeight=10000; //maximum allowed height distance between point b and c to be drawn in the radicalblock
    public @CalculatedField float paddingTop_radical=15; //padding top //for example if value=10 means there will be a padding of 10 from what inside the radical from top
    public @CalculatedField float ExtraRadicalWidth=20; //padding right//for example if value=10 means extra width of radical equal 10
    public @CalculatedField float ExtraRadicalHeight=5; //padding bottom//for example if value=8 means extra radical height will be 8px which means it is the bottom margin of radical
    public @CalculatedField float ExtraPadding_RadicalLeft=10;//padding left //extra padding from left of inside the radical
    public float ExtraPaddingTopFraction=1.02f; //5% more padding

    /**************for drawing of radical*/



    /***************for matrix*******************/
    public @CalculatedField float offsetColX=85;  //offset x between two consecutive cols
    public @CalculatedField float offsetRowY=38;  //offset y between two consecutive rows
    public @CalculatedField float TopBottomOffsetY=20;  //padding y from above and bottom of the matrix
    /***************for matrix******************/




    private Paint paint;


    private  Rect tempBound=new Rect();;
    public Setting(float floatsize){
        floatsize=floatsize*0.7f;
        init(floatsize);
        initPaint(floatsize);
        set();
    }

    public Setting(){
        this(30);
    }

    protected  void set(){

        float maxWidth=0,minWidth=1000;
        //Rect_Empty_Width is for the empty block it is the width of smallest char which is plus extra TEXT_SPACING from left and right
        Paint paint=new Paint();
        paint.setTextSize(DefaultTextSize);
        Rect bound=new Rect();
        paint.getTextBounds("y",0,1,bound);
        this.maxbottom=bound.bottom/2;

        for(int i=0;i<10;i++){
            paint.getTextBounds(i+"",0,1,bound);

            if(maxWidth<bound.width()){
                maxWidth=bound.width();

            }else if(minWidth>bound.width()){
                minWidth=bound.width();
            }
        }

        Rect_Min_Width=minWidth+(maxWidth-minWidth)*0.5f;
    }


    private void initPaint(float floatsize){
        paint=new Paint();
        paint.setTextSize(floatsize);
        paint.setStrokeWidth(Defualtstroke);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
      //  paint.setTypeface(ResourcesCache.getInstance().getDefaultTypeFace());
        paint.setColor(Color.BLACK);
    }

    protected  void init(float textsize){
        if(init)
            return;
        init = true;

        // Scale all the values according to the text size.
        // So we can adjust all the variables:- for example small text size will have smaller block margin and large bigger
        float scale = scaleTextSize(textsize);

        DefaultTextSize = textsize;
        RECT_HEIGHT = textsize*0.95f;

        // TODO: 11/4/2017  remove the comment
        //float px = UnitConverter.dpToPx(Defualtstroke);
        float px=1;
        Defualtstroke=px;
        MinumumDefualtstroke=px*0.5f;
        DivisonHeight=px; //height of division since it is drawn as a rectangle with height and width ,stroke width

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


    HashMap<String,Float>  map=new HashMap<>();



    private float measureText(String seq){
        paint.setTextSize(DefaultTextSize);
        paint.getTextBounds(seq, 0, seq.length(), tempBound);
        return tempBound.width();
    }

    public float getValue(String value){
        if(editMode){
            Float valueWidth= map.get(value);
            if(valueWidth==null){
                map.put(value,measureText(value));
            }
            return map.get(value);
        }

        return measureText(value);
    }

    public Paint getPaint(){
        return paint;
    }


    public float scaleTextSize(float newTextSize){
        return newTextSize/DefaultTextSize;
    }

    public float scale(float value,float textSize){
        return value*textSize/DefaultTextSize;
    }


    public void setEditMode(boolean editMode){
        this.editMode=editMode;
    }

    public float getScale(float newTextSize){
        return DefaultTextSize*newTextSize;
    }
}
