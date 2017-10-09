package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 1/7/2017.
 */
public class EmptyBlock extends Block {
    float strokeWidth;
    public EmptyBlock() {
        super("");
    }

    @Override
    public TokenID getId() {
        return TokenID.EMPTY;
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setStyle(Paint.Style.STROKE);
        c.drawRect(offsetX+x,offsetY+y,offsetX+x+width,offsetY+y+height,paint);

    }

    @Override
    public float getBaseLine() {
        return height/2;
    }

    @Override
    public String show() {
        return "#";
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        this.width=setting.TEXT_SPACING*2*scale+setting.Rect_Min_Width*scale;
        this.height=setting.Rect_Height*scale;
        this.strokeWidth=setting.Defualtstroke*scale;


    }


}
