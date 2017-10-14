package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class DivisionBlock extends Block {

    private float divisionLineX;
    private float divisionLineY;

    private float strokeHeight;

    protected BlockContainer numerator;
    protected BlockContainer denominator;

    public DivisionBlock(){
        this.numerator = new BlockContainer();
        this.denominator = new BlockContainer();

        this.numerator.setParent(this);
        this.denominator.setParent(this);
    }

    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        // Measure the numerator and denominator
        numerator.measure(setting, paint, textSize);
        denominator.measure(setting, paint, textSize);

        //
        float scale = textSize / setting.DefaultTextSize;
        lineHeight = scale*setting.TEXT_SPACING;
        strokeHeight = 2;

        // Set the width
        this.width = this.numerator.width < this.denominator.width ? this.denominator.width : this.numerator.width;

        // Add extra width factor
        this.width *= setting.DivisonWidthFactor;

        // Add extra division width default offset
        this.width += setting.DivisionOffsetWidth * scale * 2;

        // set the height
        height = numerator.width + this.denominator.height;
    }

    @Override
    protected void layout(Setting setting, Paint paint, float textSize, float x, float y) {
        super.layout(setting, paint, textSize, x, y);

        float scale = textSize / setting.DefaultTextSize;
        float shift = setting.block_Division_Margin * scale;

        // Set division line position.
        this.divisionLineX = 0;
        this.divisionLineY = this.numerator.height + shift;

        // Calculate numerator and denominator position.
        float numCenterX = this.width/2 - numerator.width/2;
        float denCenterX = this.width/2 - denominator.width/2;
        float denominatorY = this.divisionLineY + shift + this.strokeHeight;

        // Set numerator and denominator position.
        numerator.layout(setting, paint, textSize, numCenterX, 0);
        denominator.layout(setting, paint, textSize, denCenterX, denominatorY);
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        numerator.draw(c,paint,offsetX+x,offsetY+y);
        denominator.draw(c,paint,offsetX+x,offsetY+y);

        // Draw the division line
        c.drawRect(
                divisionLineX + offsetX + x,
                divisionLineY + offsetY + y,
                divisionLineX + width + offsetX + x,
                divisionLineY + strokeHeight + offsetY + y,
                paint);
    }

    @Override
    public float getBaseLine() {
        return divisionLineY + strokeHeight/2;
    }

    @Override
    public String show() {
        return "(("+ numerator.show() + ')' +'/'+ '('+ denominator.show() + "))";
    }

    @Override
    public BlockID getId() {
        return BlockID.DIVISION;
    }

    public BlockContainer getNumerators(){
        return numerator;
    }

    public BlockContainer getDenominators(){
        return denominator;
    }
}
