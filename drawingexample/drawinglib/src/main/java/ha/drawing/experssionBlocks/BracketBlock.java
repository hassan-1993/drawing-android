package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


import com.example.scanner.TokenID;


import ha.drawing.Setting;


/**
 * Created by hassan on 1/3/2017.
 */
public class BracketBlock extends Block {


    public BracketBlock(String c, TokenID tokenID){
        super(tokenID,c);
        if(tokenID!=TokenID.LEFT_BRACKET && tokenID!=TokenID.RIGHT_BRACKET){
            throw new IllegalArgumentException("must be left or right bracket only");
        }

    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {


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
    protected void builder(Setting setting, Paint paint, float textSize) {
        float scale=textSize/setting.DefaultTextSize;
        /*PS:if zero means not set already */
        if(this.height==0)
        this.height=setting.RECT_HEIGHT*scale;

        float c = this.height * setting.Bracket_Width_Fraction;
        this.width = c < setting.Min_Bracket_Width ? setting.Min_Bracket_Width : c;
        if (this.width > setting.max_Bracket_Width) {
            this.width = setting.max_Bracket_Width;
        }





    }


}
