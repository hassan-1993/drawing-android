package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


//√

/**
 * Created by hassan on 12/26/2016.
 */
public class RadicalBlock extends Block {

    private BlockContainer insideRadical;
    public PointF a, b, c, d;
    private float offsetFromBelow = 0;

    public RadicalBlock() {
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
    public BlockID getId() {
        return BlockID.SQRT;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
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
    protected void measure(Setting setting, Paint paint, float textSize) {
        float scaling = textSize / setting.DefaultTextSize;

        insideRadical.measure(setting, paint, textSize);

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
}
