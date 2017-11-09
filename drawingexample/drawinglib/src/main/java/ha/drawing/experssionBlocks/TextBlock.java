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

    public TextBlock(String text){
        this.text = text;
    }

    @Override
    public BlockID getId() {
        return BlockID.TEXT;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        paint.setTextSize(textSize);
        c.drawText(text, x, y+height, paint);
    }

    @Override
    public float getBaseLineHeight() {
        return height/2;
    }

    @Override
    public String show() {
        return text;
    }

    @Override
    protected void measure(Setting setting, float textSize) {
        float height = setting.RECT_HEIGHT * textSize / setting.DefaultTextSize;
        float width = getBound2(setting, textSize, text);
        proportion = -setting.maxbottom * textSize / setting.DefaultTextSize + this.height;

        //
        this.textSize = textSize;
        setMeasurement(width, height);
    }

    public float getTextSize(){
        return this.textSize;
    }

    protected float getBound2(Setting setting,float textSize,String seq){
        return setting.getValue(seq)*setting.scaleTextSize(textSize);
    }
}
