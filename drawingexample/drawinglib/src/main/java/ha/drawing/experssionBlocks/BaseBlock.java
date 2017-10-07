package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

/**
 * Created by hassan on 12/26/2016.
 */
public class BaseBlock extends Block {
    private BlockContainer base;
    private TextBlock log;

   // private RectF baseBlockRect;
   // private RectF baseRect;
   // private RectF logRect;

    public BaseBlock(String log) {
        super(TokenID.T_BASE,"");
        this.log=new TextBlock(log);
        this.base=new BlockContainer();
        this.base.setParent(this);
    }



    @Override
    public String show() {
        String equation="";
        equation+=this.log.show()+"("+this.base.show()+",";

        return equation;
    }


    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

        this.log.build(setting,paint,textSize);
        textSize=textSize*setting.BaseScaleProportion;
        float scale= (textSize)/setting.DefaultTextSize;
        this.base.build(setting,paint,textSize);

        this.base.x=this.log.width+setting.BaseOffsetWidth*scale;
        this.base.y=log.height+setting.BaseOffsetHeight*scale;

        this.width=this.base.x+this.base.width;
        this.height=this.base.height+this.base.y;

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

            return getBase().getLastChild().touched(false);
        }
    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {
        if(caller==this.base){
            return touched(true);
        }else {
            if (isLeft) {
                return this.base.getLastChild().touched(false);
            } else {
                return touched(false);
            }
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==this.base){
            /*there is always a bracket after the base*/
            return this.next().touched(false);
        }else {
            if (isLeft) {
                return   touched(true);
            } else {
                return this.base.getChild(0).touched(true);
            }
        }
    }

    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        if(caller==this.base){
            /**/
            if(this.base.getChildCount()==1 && this.base.getChild(0).getId()==TokenID.T_EMPTY){
                return parent.delete(false,this,true);
            }else{
                /*means move left outside of the division block*/
                return moveLeft(true,caller);
            }
        }else{
            /*let move left handle it*/
            return moveLeft(isLeft,caller);
        }
    }


    @Override
    public Cursor onTouch(float touchX, float touchY) {
        /*shift origin to become at (0,0)*/
        touchX-=x;
        touchY-=y;

        float baseDistance= TouchManager.getDistance(touchX,touchY,base);
        float testDistance=TouchManager.getDistance(touchX,touchY,log);

        if(baseDistance<testDistance){
            return base.onTouch(touchX,touchY);
        }else{
            return this.touched(true);
        }

    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        log.draw(c,paint,offsetX+x,offsetY+y);
        base.draw(c,paint,offsetX+x,offsetY+y);


//        Paint border=new Paint();
//        border.setStrokeWidth(6);
//        border.setStyle(Paint.Style.STROKE);
//        border.setColor(Color.BLUE);
//
//
//        c.drawRect(x+offsetX,y+offsetY,x+width+offsetX,y+height+offsetY,border);
//
//        border.setColor(Color.YELLOW);
      //  c.drawRect(baseRect.left+x+offsetX,baseRect.top+y+offsetY,baseRect.right+x+offsetX,baseRect.bottom+y+offsetY,border);
      //  c.drawRect(logRect.left+x+offsetX,logRect.top+y+offsetY,logRect.right+x+offsetX,logRect.bottom+y+offsetY,border);
   //    c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);
        //c.drawRect(logRect.left+x+offsetX,logRect.top+y+offsetY,logRect.right+x+offsetX,logRect.bottom+y+offsetY,border);
        // c.translate(offsetX+x,offsetY+x);
       // c.drawRect(base.rectF.left+x+offsetX,base.rectF.top+y+offsetY,base.rectF.right+x+offsetX,base.rectF.bottom+y+offsetY,border);
       // c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);
     //   c.drawLine(offsetX+x,offsetY+y+getBaseLine(),offsetX+x+500,offsetY+y+getBaseLine(),paint);
    }



    @Override
    public float getBaseLine() {
        return this.log.height*0.66f;
    }


    public BlockContainer getBase(){
        return base;
    }


    @Override
    public boolean validate() {
        return base.validate();
    }
}
