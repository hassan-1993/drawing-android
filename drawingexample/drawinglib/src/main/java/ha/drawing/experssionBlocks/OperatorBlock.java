package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class OperatorBlock extends Block {


    public float textSize;
   // private RectF operatorBlockRect;


    public OperatorBlock(String operator){
        super(operator);

    }

    @Override
    public BlockID getId() {
        return BlockID.Operator;
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        float y=this.y+0.1f*textSize;//shift operator down a little bit
        switch (text){
            case "+":  c.drawLine(x+offsetX+width*0.1f,offsetY+y+height/2,x+offsetX+width*0.9f,offsetY+y+height/2,paint);
                c.drawLine(x+offsetX+width/2,offsetY+y+height/2-width*0.4f,x+offsetX+width/2,offsetY+y+height/2+width*0.4f,paint);
                break;
            case "-":  c.drawLine(x+offsetX+width*0.15f,offsetY+y+height/2,x+offsetX+width*0.85f,offsetY+y+height/2,paint);break;
            case "×":case "*":    c.drawLine(x+offsetX+width*0.15f,offsetY+y+height/2-width*0.35f,x+offsetX+width*0.85f,offsetY+y+height/2+width*0.35f,paint);
                c.drawLine(x+offsetX+width*0.85f,offsetY+y+height/2-width*0.35f,x+offsetX+width*0.15f,offsetY+y+height/2+width*0.35f,paint);
                break;
            case "=":
                c.drawLine(x+offsetX+width*0.05f,offsetY+y+height/2-width*0.27f,x+offsetX+width*0.95f,offsetY+y+height/2-width*0.27f,paint);
                c.drawLine(x+offsetX+width*0.05f,offsetY+y+height/2+width*0.27f,x+offsetX+width*0.95f,offsetY+y+height/2+width*0.27f,paint);



        }



    }



    @Override
    public float getBaseLine() {
        return height*0.5f;
    }

    @Override
    public String show() {
        return text.equals("×")?"*":text;
    }




    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {
        /*use the same string for calculating of dimension so all have same width*/


        float scale=textSize/setting.DefaultTextSize;
        this.height=setting.Rect_Height*scale;
        this.width=getBound2(setting,textSize,"*");
        this.textSize=textSize;
    }


}
