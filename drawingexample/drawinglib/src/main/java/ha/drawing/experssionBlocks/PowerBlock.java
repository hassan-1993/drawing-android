package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import me.math.com.math.cas.expression.Expression;
import me.math.com.math.cas.expression.parser.ExpressionParser;
import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

/**
 * Created by hassan on 12/28/2016.
 */
public class PowerBlock extends Block {

    BlockContainer powerBlock;
    int powerHeightOffset=0;

    public PowerBlock() {
        super(TokenID.T_POWER, "^");
        powerBlock=new BlockContainer(TokenID.T_POWER,"");
        powerBlock.setParent(this);
    }

    @Override
    public Cursor onTouch(float touchX, float touchY) {
        touchX-=x;
        touchY-=y;

        float choice1 = TouchManager.getDistance(touchX, touchY, powerBlock);

        float rightSide= TouchManager.getLeftVerticalLine(touchX-width,touchY,this);
//
        /*if touching below the inside power block compare its distance with the left side border and the right border side
        * in case minimum distance is the left side means touching the block before
        * in case minimum distance is the right side means touching the block after
        * */
        if(touchY>this.powerBlock.height+this.powerBlock.y){
            float leftChoice=TouchManager.getLeftVerticalLine(touchX+x,touchY+y,this);
            float rightChoice=TouchManager.getRightVerticalLine(touchX+x,touchY+y,this);

            if(leftChoice<rightChoice && leftChoice<choice1){
                return before().touched(false);
            }else if(rightChoice<leftChoice && rightChoice<choice1){
                return touched(false);
            }

        }

      //  if (choice1 < leftSide && choice1<rightSide) {
            return powerBlock.onTouch(touchX,touchY);
       // }
     //   return this.touched(TouchManager.isLeftSide(touchX,this));

    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        powerBlock.draw(c,paint,x+offsetX,y+offsetY);

        Paint border=new Paint();
        border.setStrokeWidth(4);
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.MAGENTA);

      //  c.drawRect(x,y,x+width,y+height,border);

        border.setColor(Color.GRAY);
       // c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);

        border.setColor(Color.BLACK);


    //  c.drawRect(bottomRightRect.left+x+offsetX,bottomRightRect.top+y+offsetY,bottomRightRect.right+x+offsetX,bottomRightRect.bottom+y+offsetY,border);
    }


    @Override
    public float getBaseLine() {
        /*if same as the baseline before it ,it will remain at zero origin with respect to before it
        * so we add to this zero the inside powerblock height so it becomes lower than zero by this power block height*/
        return before().getBaseLine()+powerBlock.height+powerHeightOffset;
    }

    @Override
    public String show() {
        return "^"+ "(" + powerBlock.show() + ")";
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale = textSize / setting.DefaultTextSize;
        float newtextsize = setting.PowerScaleProportion * textSize;
        powerBlock.build(setting,paint,newtextsize);
        powerBlock.x=scale * setting.PowerWidth;
        powerHeightOffset= (int) (setting.PowerHeight * scale);
        this.height=powerBlock.height+powerHeightOffset+before().height;
        this.width=powerBlock.width+powerBlock.x;


    }

    public BlockContainer getPower(){
        return powerBlock;
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
        if(caller==this.powerBlock){
            return this.touched(true);
        }else{
            if(isLeft){
                return this.powerBlock.getLastChild().touched(false);
            }else{
                return this.touched(false);
            }
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==this.powerBlock){
            return this.touched(false);
        }else{
            if(isLeft){
                return this.touched(true);
            }else{
                return this.powerBlock.getChild(0).touched(true);
            }
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if(caller==this.powerBlock){
            if(this.powerBlock.getChildCount()==1 && this.powerBlock.getChild(0) instanceof EmptyBlock){
                /*means delete the entire power block since no childrens left anymore except empty block*/
                return parent.delete(isLeft,this,true);
            }
            return moveLeft(true,caller);
        }else{
            /*let move left handle it*/
            return moveLeft(isLeft,caller);
        }

    }


    @Override
    public boolean validate() {

        try {
            ExpressionParser.parse(powerBlock.show());
        }catch (Exception e){
            return false;
        }
       return powerBlock.validate();
    }
}

