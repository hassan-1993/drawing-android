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
        super(TokenID.Operator,operator);

    }
 
    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        float y=this.y+0.1f*textSize;//shift operator down a little bit
        switch (text){
            case "+":  c.drawLine(x+offsetX+getWidth()*0.1f,offsetY+y+getHeight()/2,x+offsetX+getWidth()*0.9f,offsetY+y+getHeight()/2,paint);
                c.drawLine(x+offsetX+getWidth()/2,offsetY+y+getHeight()/2-getWidth()*0.4f,x+offsetX+getWidth()/2,offsetY+y+getHeight()/2+getWidth()*0.4f,paint);
                break;
            case "-":  c.drawLine(x+offsetX+getWidth()*0.15f,offsetY+y+getHeight()/2,x+offsetX+getWidth()*0.85f,offsetY+y+getHeight()/2,paint);break;
            case "×":case "*":    c.drawLine(x+offsetX+getWidth()*0.15f,offsetY+y+getHeight()/2-getWidth()*0.35f,x+offsetX+getWidth()*0.85f,offsetY+y+getHeight()/2+getWidth()*0.35f,paint);
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
