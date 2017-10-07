package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

/**
 * Created by hassan on 12/26/2016.
 */
public class OperatorBlock extends Block {


    public float textSize;
   // private RectF operatorBlockRect;


    public OperatorBlock(String operator){
        super(TokenID.T_Operator,operator);

    }
    @Override
    public Cursor onTouch(float touchX, float touchY) {
        touchX-=x;
        touchY-=y;
        return touched(TouchManager.isLeftSide(touchX,this));
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        float y=this.y+0.1f*textSize;//shift operator down a little bit
        switch (text){
            case "+":  c.drawLine(x+offsetX+width*0.1f,offsetY+y+height/2,x+offsetX+width*0.9f,offsetY+y+height/2,paint);
                c.drawLine(x+offsetX+width/2,offsetY+y+height/2-width*0.4f,x+offsetX+width/2,offsetY+y+height/2+width*0.4f,paint);
                break;
            case "-":  c.drawLine(x+offsetX+width*0.15f,offsetY+y+height/2,x+offsetX+width*0.85f,offsetY+y+height/2,paint);break;
            case "×":case "*":    c.drawLine(x+offsetX+width*0.15f,offsetY+y+height/2-width*0.35f,x+offsetX+width*0.85f,offsetY+y+height/2+width*0.35f,paint);
                c.drawLine(x+offsetX+width*0.85f,offsetY+y+height/2-width*0.35f,x+offsetX+width*0.15f,offsetY+y+height/2+width*0.35f,paint);
                break;
            case "=":
                c.drawLine(x+offsetX+width*0.05f,offsetY+y+height/2-width*0.27f,x+offsetX+width*0.95f,offsetY+y+height/2-width*0.27f,paint);
                c.drawLine(x+offsetX+width*0.05f,offsetY+y+height/2+width*0.27f,x+offsetX+width*0.95f,offsetY+y+height/2+width*0.27f,paint);



        }



    }



    @Override
    public float getBaseLine() {
        return height*0.5f;
    }

    @Override
    public String show() {
        return text.equals("×")?"*":text;
    }




    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        /*use the same string for calculating of dimension so all have same width*/


        float scale=textSize/setting.DefaultTextSize;
        this.height=setting.Rect_Height*scale;
        this.width=getBound2(setting,textSize,"*");
        this.textSize=textSize;
    }

    @Override
    public Cursor touched(boolean left) {
        EmptyBlock emptyBlock=new EmptyBlock();
        if(left){
            Block before=before();
            if(before!=null && (before.getId()==TokenID.T_TEXT|| before.getId()==TokenID.T_EMPTY)){
                return before.touched(false);
            }
            getParent().addChild(index(),emptyBlock);
            return new Cursor(emptyBlock,true);
        }else{
            Block after=next();
            if(after!=null && (after.getId()==TokenID.T_TEXT|| after.getId()==TokenID.T_EMPTY)){
                return after.touched(true);
            }
            getParent().addChild(index()+1,emptyBlock);
            return new Cursor(emptyBlock,true);
        }

    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {
        if(caller==this){
            if(isLeft){
                return parent.moveLeft(true,this);
            }else{
                return touched(true);
            }
        }else{
            /*it doesn't matter if cursor is to left or right since cursor can never be in an operatorBlock*/
            return touched(true);
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==this){
            throw new IllegalArgumentException("cursor can never be in a operatorBlock");
        }else{
            if(!isLeft){
                /*if caller from right side before it */
                return touched(false);
            }else{
                return parent.moveRight(true,this);
            }
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if(caller==this){
             if(isLeft){ /*if left means delete before it*/
                 return parent.delete(isLeft,this,false);
             }else{
                 /*means delete*/
                 return parent.delete(false,this,true);
             }
        }else{
            if(isLeft){
                /*if caller from its left side means delete*/
                return parent.delete(false,this,true);
            }else {
                return touched(false);
            }
        }
    }
}
