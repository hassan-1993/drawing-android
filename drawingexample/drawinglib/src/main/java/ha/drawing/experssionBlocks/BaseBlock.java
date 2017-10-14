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
        this.log=new TextBlock(log);
        this.base=new BlockContainer();
        this.base.setParent(this);
    }

    @Override
    public BlockID getId() {
        return BlockID.BASE;
    }

    @Override
    public String show() {
        String equation="";
        equation+=this.log.show()+"("+this.base.show()+",";
        return equation;
    }

    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        this.log.build(setting,paint,textSize);
        textSize=textSize*setting.BaseScaleProportion;
        float scale= (textSize)/setting.DefaultTextSize;
        this.base.build(setting,paint,textSize);

        this.base.x=this.log.width+setting.BaseOffsetWidth*scale;
        this.base.y=log.height+setting.BaseOffsetHeight*scale;

        this.width=this.base.x+this.base.width;
        this.height=this.base.height+this.base.y;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        log.draw(c,paint,offsetX+x,offsetY+y);
        base.draw(c,paint,offsetX+x,offsetY+y);
    }

    @Override
    public float getBaseLine() {
        return this.log.height*0.66f;
    }

    public BlockContainer getBase(){
        return base;
    }


}
