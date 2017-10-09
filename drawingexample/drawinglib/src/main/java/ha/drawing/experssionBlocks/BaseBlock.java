package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


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
        super("");
        this.log=new TextBlock(log);
        this.base=new BlockContainer();
        this.base.setParent(this);
    }

    @Override
    public BlockID getId() {
        return BlockID.BASE;
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


}
