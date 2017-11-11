package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


import java.util.ArrayList;

import ha.drawing.Setting;


/**
 * Created by hassan on 1/3/2017.
 */
public abstract class BracketBlock extends TextBlock {

    public BracketBlock(String c){
        super(c);
    }

    @Override
    protected void measure(Setting setting, float textSize) {
        float scale = textSize/setting.DefaultTextSize;

        // PS: If zero means not set already
        if(this.getHeight() == 0)
            this.setHeight(setting.RECT_HEIGHT *scale);

        float c = this.getHeight() * setting.Bracket_Width_Fraction;
        this.setWidth(c < setting.Min_Bracket_Width ? setting.Min_Bracket_Width : c);
        if (this.getWidth() > setting.max_Bracket_Width) {
            this.setWidth(setting.max_Bracket_Width);
        }
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float x, float y, float offsetX, float offsetY) {
        paint.setStrokeWidth(2);

        switch (text.charAt(0)) {
            case ')':
            {
                RectF rectF = new RectF(x, y, x + getWidth(), y + getHeight());
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 270, 180, false, paint);
                break;
            }
            case '(':
            {
                RectF rectF = new RectF(x, y, x + getWidth(), y + getHeight());
                paint.setStyle(Paint.Style.STROKE);
                c.drawArc(rectF, 90, 180, false, paint);
                break;
            }
            case '[':
            {
                c.drawLine(offsetX + x + getWidth() * 0.25f, offsetY + y, offsetX + x + getWidth() * 0.25f, offsetY + y + getHeight(), paint);
                c.drawLine(offsetX + x + getWidth() * 0.25f, offsetY + y, offsetX + x + getWidth() * 0.83f, offsetY + y, paint);
                c.drawLine(offsetX + x + getWidth() * 0.25f, offsetY + y + getHeight(), offsetX + x + getWidth() * 0.83f, offsetY + y + getHeight(), paint);
                break;
            }
            case ']':
            {
                c.drawLine(offsetX + x + getWidth() * 0.75f, offsetY + y, offsetX + x + getWidth() * 0.75f, offsetY + y + getHeight(), paint);
                c.drawLine(offsetX + x + getWidth() * 0.75f, offsetY + y, offsetX + x + getWidth() * 0.17f, offsetY + y, paint);
                c.drawLine(offsetX + x + getWidth() * 0.75f, offsetY + y + getHeight(), offsetX + x + getWidth() * 0.17f, offsetY + y + getHeight(), paint);
                break;
            }
        }
    }

    @Override
    public float measureBaseLine() {
        float baseLine = 0;
        Block leftBracket = getRelatedBracket();
        ArrayList<Block> blocksInBetween = getBlocksInBetween(leftBracket, this);

        //
        for(Block block : blocksInBetween){
            if(block.getBaseLineHeight() > baseLine)
                baseLine = block.getBaseLineHeight();
        }

        //
        return baseLine;
    }

    protected Block getRelatedBracket(){

        int bracketCounter = 1;
        Block relatedBracket = null;
        Block bracketBlock = this;
        if(bracketBlock.getId() == BlockID.RIGHT_BRACKET){
            relatedBracket = bracketBlock;
            while(relatedBracket != null && bracketCounter > 0){
                relatedBracket = relatedBracket.before();

                if(relatedBracket.getId() == BlockID.LEFT_BRACKET)
                    bracketCounter--;
                else if(relatedBracket.getId() == BlockID.RIGHT_BRACKET)
                    bracketCounter++;
            }
        }

        else if(bracketBlock.getId() == BlockID.LEFT_BRACKET){
            relatedBracket = bracketBlock;
            while(relatedBracket != null && bracketCounter > 0){
                relatedBracket = relatedBracket.next();
                if(relatedBracket.getId() == BlockID.LEFT_BRACKET)
                    bracketCounter++;
                else if(relatedBracket.getId() == BlockID.RIGHT_BRACKET)
                    bracketCounter--;
            }
        }

        return relatedBracket;
    }
}
