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

    BlockContainer powerBlock;
    int powerHeightOffset=0;

    public PowerBlock() {
        super(TokenID.POWER, "^");
        powerBlock=new BlockContainer(TokenID.POWER,"");
        powerBlock.setParent(this);
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        powerBlock.draw(c,paint,x+offsetX,y+offsetY);

        Paint border=new Paint();
        border.setStrokeWidth(4);
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.MAGENTA);

      //  c.drawRect(x,y,x+width,y+height,border);

        border.setColor(Color.GRAY);
       // c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);

        border.setColor(Color.BLACK);


    //  c.drawRect(bottomRightRect.left+x+offsetX,bottomRightRect.top+y+offsetY,bottomRightRect.right+x+offsetX,bottomRightRect.bottom+y+offsetY,border);
    }


    @Override
    public float getBaseLine() {
        return before().getBaseLine()+powerBlock.getHeight()+powerHeightOffset;
    }

    @Override
    public String show() {
        return "^"+ "(" + powerBlock.show() + ")";
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        powerBlock.build(setting,paint,setting.PowerScaleProportion * textSize);

        powerBlock.x=setting.scale(setting.PowerWidth,textSize);
        powerHeightOffset= (int) setting.scale(setting.PowerHeight, textSize);

        float height=powerBlock.getHeight()+powerHeightOffset+before().getHeight();
        float width=powerBlock.getWidth()+powerBlock.x;

        setMeasurement(width,height);
    }

    public BlockContainer getPower(){
        return powerBlock;
    }


}

