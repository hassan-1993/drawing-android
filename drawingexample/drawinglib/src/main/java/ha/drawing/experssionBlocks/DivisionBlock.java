package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class DivisionBlock extends Block {

    public BlockContainer numerator, denominator;

    public float divX, divY, strokeHeight;

    public DivisionBlock() {
        this(TokenID.DIVISION);
    }

    protected DivisionBlock(TokenID tokenID) {
        super(tokenID, "");
        this.numerator = new BlockContainer();
        this.denominator = new BlockContainer();
        this.numerator.setParent(this);
        this.denominator.setParent(this);
    }


    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        numerator.draw(c, paint, offsetX + x, offsetY + y);
        denominator.draw(c, paint, offsetX + x, offsetY + y);

        //paint.setStrokeWidth(strokeHeight);
        c.drawRect(divX + offsetX + x, divY + offsetY + y, divX + width + offsetX + x, divY + strokeHeight + offsetY + y, paint);

    }

    @Override
    public float getBaseLine() {
        return divY + strokeHeight / 2;
    }

    @Override
    public String show() {
        return "((" + numerator.show() + ')' + '/' + '(' + denominator.show() + "))";
    }


    public BlockContainer getNumerators() {
        return numerator;
    }

    public BlockContainer getDenominators() {
        return denominator;
    }


    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

        float scale = textSize / setting.DefaultTextSize;
        lineHeight = scale * setting.TEXT_SPACING;


        this.strokeHeight = setting.DivisonHeight * scale * 1.2f;
        this.numerator.build(setting, paint, textSize);
        this.denominator.build(setting, paint, textSize);

        float shift = setting.block_Division_Margin * scale;

        this.divY = this.numerator.height + shift;
        this.denominator.y = this.divY + shift + this.strokeHeight;
        this.height = this.denominator.height + this.denominator.y;


        this.divX = 0;
        this.width = this.numerator.width < this.denominator.width ? this.denominator.width : this.numerator.width;
        this.width *= setting.DivisonWidthFactor;//add extra width factor
        this.width += setting.DivisionOffsetWidth * scale * 2;//add extra division width default offset

        numerator.x = this.width / 2 - numerator.width / 2;
        denominator.x = this.width / 2 - denominator.width / 2;
    }




}
