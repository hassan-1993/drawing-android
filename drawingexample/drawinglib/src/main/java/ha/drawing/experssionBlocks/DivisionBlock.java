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
    protected void measure(Setting setting, float textSize) {
        // Measure the numerator and denominator
        numerator.measure(setting, textSize);
        denominator.measure(setting, textSize);

        //
        float scale = textSize / setting.DefaultTextSize;
        lineHeight = scale*setting.TEXT_SPACING;
        strokeHeight = 2;

        // Calculate width and height.
        float shift = setting.block_Division_Margin * scale;
        float height = numerator.height + shift + this.denominator.height;
        float width = this.numerator.width < this.denominator.width ? this.denominator.width : this.numerator.width;

        // Add extra width factor
        width *= setting.DivisonWidthFactor;

        // Add extra division width default offset
        width += setting.DivisionOffsetWidth * scale * 2;

        //
        setMeasurement(width, height);
    }

    @Override
    protected void onLayout(Setting setting, float textSize, float x, float y) {

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
        numerator.layout(setting, textSize, numCenterX, 0);
        denominator.layout(setting, textSize, denCenterX, denominatorY);
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
