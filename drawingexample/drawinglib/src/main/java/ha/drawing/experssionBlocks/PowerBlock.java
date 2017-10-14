package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/28/2016.
 */
public class PowerBlock extends Block {

    private BlockContainer powerBlock;
    private int powerHeightOffset=0;

    public PowerBlock() {
        powerBlock=new BlockContainer();
        powerBlock.setParent(this);
    }

    @Override
    public BlockID getId() {
        return BlockID.POWER;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        powerBlock.draw(c,paint,x+offsetX,y+offsetY);
        Paint border=new Paint();
        border.setStrokeWidth(4);
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.MAGENTA);
        border.setColor(Color.GRAY);
        border.setColor(Color.BLACK);
    }

    @Override
    public float getBaseLine() {
        /*if same as the baseline before it ,it will remain at zero origin with respect to before it
        * so we add to this zero the inside powerblock height so it becomes lower than zero by this power block height*/
        return before().getBaseLine()+powerBlock.height+powerHeightOffset;
    }

    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        float newtextsize = setting.PowerScaleProportion * textSize;
        powerBlock.measure(setting, paint, newtextsize);
        float scale = textSize / setting.DefaultTextSize;
        powerHeightOffset= (int) (setting.PowerHeight * scale);
        this.height = powerBlock.height+powerHeightOffset+before().height;
        this.width = powerBlock.width+powerBlock.x;
    }

    @Override
    protected void layout(Setting setting, Paint paint, float textSize, float x, float y) {
        super.layout(setting, paint, textSize, x, y);

        float scale = textSize / setting.DefaultTextSize;
        powerBlock.layout(setting, paint, textSize, scale * setting.PowerWidth, 0);
    }

    @Override
    public String show() {
        return "^"+ "(" + powerBlock.show() + ")";
    }

    public BlockContainer getPower(){
        return powerBlock;
    }
}

