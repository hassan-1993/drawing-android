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
        /*if same as the baseline before it ,it will remain at zero origin with respect to before it
        * so we add to this zero the inside powerblock height so it becomes lower than zero by this power block height*/
        return before().getBaseLine()+powerBlock.height+powerHeightOffset;
    }

    @Override
    public String show() {
        return "^"+ "(" + powerBlock.show() + ")";
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale = textSize / setting.DefaultTextSize;
        float newtextsize = setting.PowerScaleProportion * textSize;
        powerBlock.build(setting,paint,newtextsize);
        powerBlock.x=scale * setting.PowerWidth;
        powerHeightOffset= (int) (setting.PowerHeight * scale);
        this.height=powerBlock.height+powerHeightOffset+before().height;
        this.width=powerBlock.width+powerBlock.x;


    }

    public BlockContainer getPower(){
        return powerBlock;
    }


}

