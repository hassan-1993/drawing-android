package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;
import me.math.com.math.settings.LogManager;

/**
 * Created by hassan on 12/26/2016.
 */
public class DivisionBlock extends Block {

    public BlockContainer numerators,denominators;

    public float divX,divY,strokeHeight;

    public DivisionBlock(){
        this(TokenID.T_DIVISION);
    }

    protected DivisionBlock(TokenID tokenID){
        super(tokenID,"");
        this.numerators=new BlockContainer();
        this.denominators=new BlockContainer();
        this.numerators.setParent(this);
        this.denominators.setParent(this);
    }


    @Override
    public Cursor onTouch(float touchX, float touchY) {
        LogManager.d("position"," width and height " + width + "  "  + height);
        LogManager.d("position"," x and y  " + x + "  "  + y);
        LogManager.d("position"," touchx and touchy  " + touchX + "  "  + touchY);
        touchX-=x;
        touchY-=y;
        LogManager.d("position"," AFTER touchx and touchy  " + touchX + "  "  + touchY);
        float numDistance= TouchManager.getDistance(touchX,touchY,numerators);
        float demDistance= TouchManager.getDistance(touchX,touchY,denominators);
        float leftdivisionDistance= TouchManager.getDistance(touchX,touchY,this.x,getBaseLine());
        float rightdivisionDistance= TouchManager.getDistance(touchX,touchY,this.x+this.width,getBaseLine());

        LogManager.d("position"," numerator distance is " + numDistance);
        LogManager.d("position"," denumartor distance is " + demDistance);
        LogManager.d("position"," leftDivision distance is " + leftdivisionDistance);
        LogManager.d("position"," rightDivison distance is " + rightdivisionDistance);

        if(numDistance<demDistance && numDistance<leftdivisionDistance && numDistance<rightdivisionDistance){
            return numerators.onTouch(touchX,touchY);
        }else if(demDistance<numDistance && demDistance<leftdivisionDistance && demDistance<rightdivisionDistance){
            return denominators.onTouch(touchX,touchY);
        }else{
            return  this.touched(TouchManager.isLeftSide(touchX,this));
        }

    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        numerators.draw(c,paint,offsetX+x,offsetY+y);
        denominators.draw(c,paint,offsetX+x,offsetY+y);

        //paint.setStrokeWidth(strokeHeight);
        c.drawRect(divX+offsetX+x,divY+offsetY+y,divX+width+offsetX+x,divY+strokeHeight+offsetY+y,paint);

    }

    @Override
    public float getBaseLine() {

        return divY+strokeHeight/2;
    }

    @Override
    public String show() {
        return "(("+ numerators.show() + ')' +'/'+ '('+ denominators.show() + "))";
    }


    public BlockContainer getNumerators(){
        return numerators;
    }

    public BlockContainer getDenominators(){
        return denominators;
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

        float scale=textSize / setting.DefaultTextSize;
        lineHeight=scale*setting.TEXT_SPACING;
        /**/
        {
            this.strokeHeight = setting.DivisonHeight * scale*1.2f;
            this.numerators.build(setting, paint, textSize);
            this.denominators.build(setting, paint, textSize);

        }


        {
            float shift = setting.block_Division_Margin * scale;
        /*setting the division line y position*/
            this.divY = this.numerators.height + shift;
            this.denominators.y = this.divY + shift + this.strokeHeight;
            this.height = this.denominators.height + this.denominators.y;


        /*setting width of division block and x positions of numerator,denumerator and the middle line in division*/
            this.divX = 0;
            this.width = this.numerators.width < this.denominators.width ? this.denominators.width : this.numerators.width;
            this.width *= setting.DivisonWidthFactor;//add extra width factor
            this.width += setting.DivisionOffsetWidth * scale * 2;//add extra division width default offset
        /*center both the numerator and denominator in the middle of the division*/
            numerators.x = this.width / 2 - numerators.width / 2;
            denominators.x = this.width / 2 - denominators.width / 2;
            /***********************************************************************/
        }



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
        if(caller==denominators){
            return numerators.getLastChild().touched(false);
        }else if(caller==numerators){
            return  this.touched(true);
        }else {
            if (isLeft) {
                return denominators.getLastChild().touched(false);
            } else {
                return touched(false);
            }
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==denominators){
            return this.touched(false);
        }else if(caller==numerators){
            return  denominators.getChild(0).touched(true);
        }else {
            if (isLeft) {
                return   touched(true);
            } else {
                return numerators.getChild(0).touched(true);
            }
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if(caller==numerators){
            /*check if both numerator and denominater contains only one child empty block*/
            /*if so delete the division block and let its parent handle the deletion*/
            if(numerators.getChildCount()==1 && numerators.getChild(0) instanceof EmptyBlock && denominators.getChildCount()==1 && denominators.getChild(0) instanceof EmptyBlock){
                return parent.delete(false,this,true);
            }else{
                /*means move left outside of the division block*/
                return moveLeft(true,numerators);
            }
        }else if(caller==denominators){
            /*means move to the last child in the numerator so let moveleft handle it*/
            return moveLeft(true,denominators);
        }else{
            /*let move left handle it*/
            return moveLeft(isLeft,caller);
        }
    }


    @Override
    public boolean validate() {
        boolean valid=numerators.validate();
        if(!valid){
            return false;
        }

        return denominators.validate();
    }
}
