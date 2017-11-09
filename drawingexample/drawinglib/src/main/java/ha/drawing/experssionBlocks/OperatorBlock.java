package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class OperatorBlock extends TextBlock {

    public OperatorBlock(String operator){
        super(operator);
    }

    @Override
    public BlockID getId() {
        return BlockID.Operator;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        float y=this.y+0.1f*textSize;//shift operator down a little bit
        paint.setStrokeWidth(2);
        switch (text){
            case "+":
                c.drawLine(x + getWidth()*0.1f, y + getHeight()/2, x + getWidth()*0.9f, y + getHeight()/2, paint);
                c.drawLine(x + getWidth()/2, y + getHeight()/2 - getWidth()*0.4f, x + getWidth()/2, y + getHeight()/2 + getWidth()*0.4f, paint);
                break;
            case "-":
                c.drawLine(x+offsetX+getWidth()*0.15f,offsetY+y+getHeight()/2,x+offsetX+getWidth()*0.85f,offsetY+y+getHeight()/2,paint);break;
            case "×":
            case "*":
                c.drawLine(x+offsetX+getWidth()*0.15f,offsetY+y+getHeight()/2-getWidth()*0.35f,x+offsetX+getWidth()*0.85f,offsetY+y+getHeight()/2+getWidth()*0.35f,paint);
                c.drawLine(x+offsetX+getWidth()*0.85f,offsetY+y+getHeight()/2-getWidth()*0.35f,x+offsetX+getWidth()*0.15f,offsetY+y+getHeight()/2+getWidth()*0.35f,paint);
                break;
            case "=":
                c.drawLine(x+offsetX+getWidth()*0.05f,offsetY+y+getHeight()/2-getWidth()*0.27f,x+offsetX+getWidth()*0.95f,offsetY+y+getHeight()/2-getWidth()*0.27f,paint);
                c.drawLine(x+offsetX+getWidth()*0.05f,offsetY+y+getHeight()/2+getWidth()*0.27f,x+offsetX+getWidth()*0.95f,offsetY+y+getHeight()/2+getWidth()*0.27f,paint);
        }
    }

    @Override
    public String show() {
        return text.equals("×")?"*":text;
    }
}
