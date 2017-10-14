package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import ha.drawing.Setting;


/**
 * Created by houssam on 12/26/2016.
 */
public class TextBlock extends Block {


    protected float textSize;


    private float proportion;
    private float left;


    //RectF textBlockRect;

    public TextBlock(String text){
        super(text);
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
        Rect bound=getBound(paint,textSize,text);
        this.height=setting.Rect_Height* this.textSize / setting.DefaultTextSize;
        this.left=bound.left;
        this.width=getBound2(setting,textSize,text);
        proportion = -setting.maxbottom * this.textSize / setting.DefaultTextSize+this.height;

}


    public float getTextSize(){
        return this.textSize;
    }
}
