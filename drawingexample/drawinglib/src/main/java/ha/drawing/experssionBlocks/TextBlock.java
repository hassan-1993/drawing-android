package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scanner.TokenID;

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
        super(TokenID.TEXT,text);
        this.text=text;
    }

    public TextBlock(TokenID tokenID,String text){
        super(tokenID,text);
        this.text=text;
    }


    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setTextSize(textSize);
        //drawY=y+height;
        c.drawText(text,x+offsetX-left,y+offsetY+proportion,paint);



    }



    @Override
    public float getBaseLine() {

        return getHeight()/2;
    }


    @Override
    public String show() {
        return text;
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        this.left=getBound(paint,textSize,text).left;

        float height = setting.RECT_HEIGHT * textSize / setting.DefaultTextSize;
        float width = getBound2(setting, textSize, text);
        proportion = -setting.scale(setting.maxbottom,textSize) + height;

        //
        this.textSize = textSize;
        setMeasurement(width, height);
}




    public float getTextsize(){
        return this.textSize;
    }
}
