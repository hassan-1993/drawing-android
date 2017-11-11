package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import ha.drawing.Setting;


/**
 * Created by hassan on 1/7/2017.
 */
public class EmptyBlock extends Block {

    public EmptyBlock() {
    }

    @Override
    public BlockID getId() {
        return BlockID.EMPTY;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float x, float y, float offsetX, float offsetY) {
        paint.setStyle(Paint.Style.STROKE);
        c.drawRect(offsetX+x,offsetY+y,offsetX+x+getWidth(),offsetY+y+getHeight(),paint);
    }

    @Override
    public float measureBaseLine() {
        return getInnerHeight()/2;
    }

    @Override
    public String show() {
        return "#";
    }

    @Override
    protected void measure(Setting setting, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        this.setWidth(setting.TEXT_SPACING*2*scale+setting.Rect_Min_Width*scale);
        this.setHeight(setting.RECT_HEIGHT *scale);
    }
}
