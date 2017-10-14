package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import ha.drawing.Setting;


/**
 * Created by houssam on 12/26/2016.
 */
public class TextBlock extends Block {

    protected String text;
    protected float textSize;


    private float proportion;
    private float left;


    //RectF textBlockRect;

    public TextBlock(String text){
        this.text=text;
    }

    @Override
    public BlockID getId() {
        return BlockID.TEXT;
    }


    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setTextSize(textSize);
        //drawY=y+height;
        c.drawText(text,x+offsetX-left,y+offsetY+proportion,paint);


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
    protected void measure(Setting setting, Paint paint, float textSize) {
        this.textSize=textSize;
        this.height=setting.Rect_Height* this.textSize / setting.DefaultTextSize;
        this.width=getBound2(setting,textSize,text);
        proportion = -setting.maxbottom * this.textSize / setting.DefaultTextSize+this.height;
    }

    @Override
    protected void layout(Setting setting, Paint paint, float textSize, float x, float y) {
        super.layout(setting, paint, textSize, x, y);
        Rect bound=getBound(paint,textSize,text);
        this.left=bound.left;
    }

    public float getTextSize(){
        return this.textSize;
    }

    protected Rect getBound(Paint paint, float textSize,String seq){
        paint.setTextSize(textSize);
        Rect bound=new Rect();

        paint.getTextBounds(seq, 0, seq.length(), bound);
        return bound;
    }


    protected float getBound2(Setting setting,float textSize,String seq){
        return setting.getValue(seq)*setting.scaleTextSize(textSize);
    }
}
