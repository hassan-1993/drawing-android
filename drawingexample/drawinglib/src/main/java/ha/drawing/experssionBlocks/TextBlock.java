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

    private String text;

    public float textsize;


    private float proportion;
    private float left;


    //RectF textBlockRect;

    public TextBlock(String text){
        super(text);
        this.text=text;
    }

    @Override
    public TokenID getId() {
        return TokenID.TEXT;
    }


    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setTextSize(textsize);
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
    protected void builder(Setting setting, Paint paint, float textSize) {
        this.textsize=textSize;
        Rect bound=getBound(paint,textSize,text);
        this.height=setting.Rect_Height* textsize / setting.DefaultTextSize;
        this.left=bound.left;
        this.width=getBound2(setting,textSize,text);
        proportion = -setting.maxbottom * textsize / setting.DefaultTextSize+this.height;

}


    public float getTextsize(){
        return this.textsize;
    }
}
