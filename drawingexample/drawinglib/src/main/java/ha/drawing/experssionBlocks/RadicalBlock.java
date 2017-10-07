package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;
//√

/**
 * Created by hassan on 12/26/2016.
 */
public class RadicalBlock extends Block {

    BlockContainer insideRadical;
    public PointF a, b, c, d;
    float offsetFromBelow = 0;


    public RadicalBlock() {
        super(TokenID.T_SQRT, "");
        insideRadical = new BlockContainer();
        insideRadical.setParent(this);

        a = new PointF();
        b = new PointF();
        c = new PointF();
        d = new PointF();
    }


    public BlockContainer getInsideRadical() {
        return insideRadical;
    }


    @Override
    public Cursor onTouch(float touchX, float touchY) {

        //// TODO: 1/11/2017 :in the right side of radical when clicking not exactly error 
        touchX -= x;
        touchY -= y;

        float choice1 = TouchManager.getDistance(touchX, touchY, insideRadical);
        float leftChoice=TouchManager.getLeftVerticalLine(touchX+x,touchY+y,this);
        float rightChoice=TouchManager.getRightVerticalLine(touchX+x,touchY+y,this);

        if(leftChoice<rightChoice && leftChoice<choice1){
            return this.touched(true);
        }else if(rightChoice<leftChoice && rightChoice<choice1){
            return touched(false);
        }


        return insideRadical.onTouch(touchX,touchY);





    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        //√
        paint.setStyle(Paint.Style.FILL);
        //paint.setStrokeWidth(strokeWidth);

        c.drawLine(a.x + offsetX + x, offsetY + a.y + y, offsetX + width + x, offsetY + a.y + y, paint); //A
        c.drawLine(offsetX + a.x + x, offsetY + a.y + y, offsetX + b.x + x, offsetY + b.y + y, paint);//B
        c.drawLine(offsetX + b.x + x, offsetY + b.y + y, offsetX + this.c.x + x, offsetY + this.c.y + y, paint);  //C
        c.drawLine(offsetX + this.c.x + x, offsetY + this.c.y + y, offsetX + d.x + x, offsetY + d.y + y, paint);

        insideRadical.draw(c, paint, x + offsetX, y + offsetY);

    }


    // FIXME: 1/22/2017 the base line of radical is wrong should be with respect to its children
    @Override
    public float getBaseLine() {
        return this.insideRadical.getChild(0).getBaseLine() + this.insideRadical.getChild(0).y + this.insideRadical.y;
    }


    @Override
    public String show() {
        return "(√(" + this.insideRadical.show() + "))";
    }


    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scaling = textSize / setting.DefaultTextSize;

        insideRadical.build(setting, paint, textSize);

        float newHeight = insideRadical.height * setting.ExtraPaddingTopFraction + setting.paddingTop_radical * scaling; //adding extra padding between radical and what inside it
        setHeight(newHeight + setting.ExtraRadicalHeight * scaling, setting.maxHeight);

        insideRadical.y = newHeight - insideRadical.height;


        setWidth(insideRadical.width + setting.ExtraRadicalWidth * scaling + setting.ExtraPadding_RadicalLeft * scaling);
        float shiftX = setting.ExtraPadding_RadicalLeft * scaling;
        insideRadical.x = a.x + shiftX;

    }


    private void setHeight(float height, float maxHeight) {
        a.y = 0;
        b.y = a.y + height;
        offsetFromBelow = height * 0.37f > maxHeight ? maxHeight : height * 0.37f;

        c.y = b.y - offsetFromBelow;
        d.y = c.y;

        this.height = height;

    }

    //always setHeight must be called before
    private void setWidth(float width) {

        a.x = 0;
        float offset = height * 0.2f;
        b.x = a.x - offset;
        c.x = b.x - (b.y - c.y) * 0.25f;
        d.x = c.x - offsetFromBelow * 0.1f;

        a.x -= d.x;
        b.x -= d.x;
        c.x -= d.x;
        this.width = width - d.x;
        d.x -= d.x;


    }


    @Override
    public Cursor touched(boolean left) {
        EmptyBlock emptyBlock = new EmptyBlock();
        if (left) {
            Block before = before();
            if (before != null && (before.getId() == TokenID.T_TEXT || before.getId() == TokenID.T_EMPTY)) {
                return before.touched(false);
            }
            getParent().addChild(index(), emptyBlock);
            return new Cursor(emptyBlock, true);
        } else {
            Block after = next();
            if (after != null && (after.getId() == TokenID.T_TEXT || after.getId() == TokenID.T_EMPTY)) {
                return after.touched(true);
            }
            getParent().addChild(index() + 1, emptyBlock);
            return new Cursor(emptyBlock, true);
        }
    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {
        if (caller == insideRadical) {
            return this.touched(true);
        } else {
            if (isLeft) {
            /*move through radical block*/
                return this.insideRadical.getLastChild().touched(false);
            } else {
                return touched(false);
            }
        }
    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if (caller == insideRadical) {
            return this.touched(false);
        } else {
            if (isLeft) {
            /*move through radical block*/
                return touched(true);
            } else {
                return this.insideRadical.getChild(0).touched(true);
            }
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if (this.insideRadical == caller) {
            /*if contains only one child and is empty block than we should delete all the radical block*/
            if (this.insideRadical.getChildCount() == 1 && this.insideRadical.getChild(0) instanceof EmptyBlock) {
                return parent.delete(true, this, true);
            }
            /*means move outside of it out of radical*/
            return moveLeft(true, this.insideRadical);
        } else {
          /*let move left handle it*/
            return moveLeft(isLeft, caller);
        }
    }


    @Override
    public boolean validate() {
        return insideRadical.validate();
    }
}
