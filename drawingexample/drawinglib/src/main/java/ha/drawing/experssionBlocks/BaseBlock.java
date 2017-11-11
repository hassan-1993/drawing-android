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
    protected void measure(Setting setting, float textSize) {
        // Measure the log and base
        log.measure(setting, textSize);
        base.measure(setting, textSize);

        // Set width and height
        this.setWidth(log.getWidth() + base.getWidth());
        this.setHeight(base.getHeight() + base.y);
    }

    @Override
    protected void onLayout(Setting setting, float textSize, float x, float y) {
        textSize = textSize*setting.BaseScaleProportion;
        float scale = (textSize)/setting.DefaultTextSize;

        // Calculate base position
        float baseX = log.getWidth() + setting.BaseOffsetWidth*scale;
        float baseY = log.getHeight() + setting.BaseOffsetHeight*scale;

        // Set base position
        base.layout(setting, textSize, baseX, baseY);
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float x, float y, float offsetX, float offsetY) {
        log.draw(c, paint, offsetX, offsetY);
        base.draw(c, paint, offsetX, offsetY);
    }

    @Override
    public float measureBaseLine() {
        return this.log.getHeight()*0.66f;
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
