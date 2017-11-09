package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/28/2016.
 */
public class PowerBlock extends Block {

    private BlockContainer powerBlock;

    public PowerBlock() {
        powerBlock = new BlockContainer();
        powerBlock.setParent(this);
    }

    @Override
    public BlockID getId() {
        return BlockID.POWER;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        powerBlock.draw(c, paint, x+offsetX, y+offsetY);
    }

    @Override
    public float getBaseLineHeight() {
        return before().getBaseLineHeight() + powerBlock.getHeight();
    }

    @Override
    protected void measure(Setting setting, float textSize) {
        powerBlock.measure(setting, textSize);
        setMeasurement(powerBlock.getWidth(), powerBlock.getHeight() + before().getHeight());
    }

    @Override
    protected void onLayout(Setting setting, float textSize, float x, float y) {
        powerBlock.layout(setting, textSize, x, y);
    }

    @Override
    public String show() {
        return "^" + "(" + powerBlock.show() + ")";
    }

    public BlockContainer getPower(){
        return powerBlock;
    }
}

