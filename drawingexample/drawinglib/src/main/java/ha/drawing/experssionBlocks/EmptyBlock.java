package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

/**
 * Created by hassan on 1/7/2017.
 */
public class EmptyBlock extends Block {
    float strokeWidth;
    public EmptyBlock() {
        super(TokenID.T_EMPTY, "");
    }

    @Override
    public Cursor onTouch(float touchX, float touchY) {
        touchX-=x;
        touchY-=y;
        return touched(TouchManager.isLeftSide(touchX,this));
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setStyle(Paint.Style.STROKE);
        c.drawRect(offsetX+x,offsetY+y,offsetX+x+width,offsetY+y+height,paint);

    }

    @Override
    public float getBaseLine() {
        return height/2;
    }

    @Override
    public String show() {
        return "#";
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        this.width=setting.TEXT_SPACING*2*scale+setting.Rect_Min_Width*scale;
        this.height=setting.Rect_Height*scale;
        this.strokeWidth=setting.Defualtstroke*scale;


    }

    @Override
    public Cursor touched(boolean left) {
        return new Cursor(this,true);
    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {

        if(caller==this){
            return parent.moveLeft(true,this);
        }else{
            return touched(true);
        }
    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==this){
            return parent.moveRight(false,this);
        }else{
            return touched(true);
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if(caller==this){
            /*means delete and let parent handle removing it*/
            return parent.delete(true,this,true);
        }else{
            return touched(true);
        }
    }
}
