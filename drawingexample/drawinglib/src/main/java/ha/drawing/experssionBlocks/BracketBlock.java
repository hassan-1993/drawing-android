package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


import com.example.scanner.TokenID;


import ha.drawing.Setting;


/**
 * Created by hassan on 1/3/2017.
 */
public abstract class BracketBlock extends Block {


    public BracketBlock(String c){
        super(c);
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {


        switch (text.charAt(0)) {
            case ')':
            {
                RectF rectF = new RectF(x + offsetX, y + offsetY, x + offsetX + width, y + offsetY + height);
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 270, 180, false, paint);
                break;
            }
            case '(':
            {
                RectF rectF = new RectF(x + offsetX, y + offsetY, x + offsetX + width, y + offsetY + height);
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 90, 180, false, paint);
                break;
            }
            case '[':
            {
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y, offsetX + x + width * 0.25f, offsetY + y + height, paint);
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y, offsetX + x + width * 0.83f, offsetY + y, paint);
                c.drawLine(offsetX + x + width * 0.25f, offsetY + y + height, offsetX + x + width * 0.83f, offsetY + y + height, paint);
                break;
            }

            case ']':
            {
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y, offsetX + x + width * 0.75f, offsetY + y + height, paint);
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y, offsetX + x + width * 0.17f, offsetY + y, paint);
                c.drawLine(offsetX + x + width * 0.75f, offsetY + y + height, offsetX + x + width * 0.17f, offsetY + y + height, paint);
                break;
            }


        }

//        Paint border=new Paint();
//        border.setStrokeWidth(4);
//        border.setStyle(Paint.Style.STROKE);
//        border.setColor(Color.BLUE);
//
////        c.drawRect(rectF.left+x+offsetX,rectF.top+y+offsetY,rectF.right+x+offsetX,rectF.bottom+y+offsetY,border);
//
//        border.setColor(Color.LTGRAY);
//
//        c.drawRect(x+offsetX,y+offsetY,x+offsetX,y+offsetY,border);

    }



    @Override
    public float getBaseLine() {
        return height/2;
    }

    @Override
    public String show() {
        return text;
    }



    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        /*PS:if zero means not set already */
        if(this.height==0)
        this.height=setting.Rect_Height*scale;

        float c = this.height * setting.Bracket_Width_Fraction;
        this.width = c < setting.Min_Bracket_Width ? setting.Min_Bracket_Width : c;
        if (this.width > setting.max_Bracket_Width) {
            this.width = setting.max_Bracket_Width;
        }





    }


}
