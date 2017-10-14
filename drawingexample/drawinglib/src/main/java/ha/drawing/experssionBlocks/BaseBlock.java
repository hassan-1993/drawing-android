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

    public BaseBlock(String log) {
        this.log = new TextBlock(log);
        this.base = new BlockContainer();

        this.base.setParent(this);
        this.log.setParent(this);
    }

    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        // Measure the log and base
        log.measure(setting,paint,textSize);
        base.measure(setting,paint,textSize);

        // Set width and height
        this.width = log.width + base.width;
        this.height = base.height + base.y;
    }

    @Override
    protected void layout(Setting setting, Paint paint, float textSize, float x, float y) {
        super.layout(setting, paint, textSize, x, y);

        textSize = textSize*setting.BaseScaleProportion;
        float scale = (textSize)/setting.DefaultTextSize;

        // Calculate base position
        float baseX = log.width + setting.BaseOffsetWidth*scale;
        float baseY = log.height + setting.BaseOffsetHeight*scale;

        // Set base position
        base.layout(setting, paint, textSize, baseX, baseY);
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        log.draw(c, paint, offsetX+x, offsetY+y);
        base.draw(c, paint, offsetX+x, offsetY+y);
    }

    @Override
    public float getBaseLine() {
        return this.log.height*0.66f;
    }

    @Override
    public BlockID getId() {
        return BlockID.BASE;
    }

    @Override
    public String show() {
        return log.show()+"("+base.show()+")";
    }

    public BlockContainer getBase(){
        return base;
    }
}
