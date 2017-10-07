package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

/**
 * Created by hassan on 1/3/2017.
 */
public class BracketBlock extends Block {


    public BracketBlock(String c, TokenID tokenID){
        super(tokenID,c);
        if(tokenID!=TokenID.T_LEFT_BRACKET && tokenID!=TokenID.T_RIGHT_BRACKET){
            throw new IllegalArgumentException("must be left or right bracket only");
        }

    }

    @Override
    public Cursor onTouch(float touchX, float touchY) {
        touchX-=x;
        touchY-=y;

        return touched(TouchManager.isLeftSide(touchX,this));
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {


        switch (text.charAt(0)) {
            case ')':
            {
                RectF rectF = new RectF(x + offsetX, y + offsetY, x + offsetX + width, y + offsetY + height);
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 270, 180, false, paint);
                break;
            }
            case '(':
            {
                RectF rectF = new RectF(x + offsetX, y + offsetY, x + offsetX + width, y + offsetY + height);
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 90, 180, false, paint);
                break;
            }
            case '[':
            {
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y, offsetX + x + width * 0.25f, offsetY + y + height, paint);
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y, offsetX + x + width * 0.83f, offsetY + y, paint);
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y + height, offsetX + x + width * 0.83f, offsetY + y + height, paint);
                break;
            }

            case ']':
            {
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y, offsetX + x + width * 0.75f, offsetY + y + height, paint);
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y, offsetX + x + width * 0.17f, offsetY + y, paint);
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y + height, offsetX + x + width * 0.17f, offsetY + y + height, paint);
                break;
            }


        }

//        Paint border=new Paint();
//        border.setStrokeWidth(4);
//        border.setStyle(Paint.Style.STROKE);
//        border.setColor(Color.BLUE);
//
////        c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);
//
//        border.setColor(Color.LTGRAY);
//
//        c.drawRect(x+offsetX,y+offsetY,x+offsetX,y+offsetY,border);

    }



    @Override
    public float getBaseLine() {
        return height/2;
    }

    @Override
    public String show() {
        return text;
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        /*PS:if zero means not set already */
        if(this.height==0)
        this.height=setting.Rect_Height*scale;

        float c = this.height * setting.Bracket_Width_Fraction;
        this.width = c < setting.Min_Bracket_Width ? setting.Min_Bracket_Width : c;
        if (this.width > setting.max_Bracket_Width) {
            this.width = setting.max_Bracket_Width;
        }





    }


    @Override
    public Cursor touched(boolean left) {

        EmptyBlock emptyBlock=new EmptyBlock();
        if(left){
            Block before=before();
            if(before!=null ){
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
        }
        else
        if(isLeft){
            if(before()!=null && before().getId()==TokenID.T_FUNCTION){
                /*if function before it */
                return before().moveLeft(true,this);
            }
           return touched(true);
        }else{
            return touched(false);
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(isLeft){
            /*let parent handle moving to the cursor left*/
            return touched(true);
        }else{
            return touched(false);
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {

        if(caller==this){
            if(isLeft){
                return parent.delete(true,caller, false);
            }else{
                return parent.delete(false,this, true);
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
