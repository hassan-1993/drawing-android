package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class DivisionBlock extends Block {

    private float divisionLineHeight;

    protected BlockContainer numerator;
    protected BlockContainer denominator;

    public DivisionBlock(){
        numerator = new BlockContainer();
        denominator = new BlockContainer();

        divisionLineHeight = 2;

        numerator.setParent(this);
        denominator.setParent(this);
    }

    @Override
    protected void measure(Setting setting, float textSize) {
        // Measure the numerator and denominator
        numerator.measure(setting, textSize);
        denominator.measure(setting, textSize);

        // Calculate height
        float height = numerator.height + divisionLineHeight + this.denominator.height;

        // Calculate width based on the maximum width of the numerator and denominator
        float width = this.numerator.width < this.denominator.width ? this.denominator.width : this.numerator.width;

        //
        setMeasurement(width, height);
    }

    @Override
    protected void onLayout(Setting setting, float textSize, float x, float y) {

        // Calculate numerator and denominator position.
        float numCenterX = x + this.width/2 - numerator.width/2;
        float denCenterX = x + this.width/2 - denominator.width/2;
        float denominatorY = y + numerator.height + divisionLineHeight;

        // Set numerator and denominator position.
        numerator.layout(setting, textSize, numCenterX, y);
        denominator.layout(setting, textSize, denCenterX, denominatorY);
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
        numerator.draw(c, paint, offsetX + x, offsetY + y);
        denominator.draw(c, paint, offsetX + x, offsetY + y);

        // Draw the division line
        c.drawRect(
                x,
                y + numerator.height,
                x + width,
                y + numerator.height + divisionLineHeight,
                paint);
    }

    @Override
    public float getBaseLineHeight() {
        return numerator.height + divisionLineHeight/2;
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
