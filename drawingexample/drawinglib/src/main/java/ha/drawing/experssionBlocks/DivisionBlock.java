package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;


import com.example.scanner.TokenID;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class DivisionBlock extends Block {

    public BlockContainer numerators,denominators;

    public float divX,divY,strokeHeight;

    public DivisionBlock(){
        this(TokenID.DIVISION);
    }

    protected DivisionBlock(TokenID tokenID){
        super(tokenID,"");
        this.numerators=new BlockContainer();
        this.denominators=new BlockContainer();
        this.numerators.setParent(this);
        this.denominators.setParent(this);
    }



    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {
        numerators.draw(c,paint,offsetX+x,offsetY+y);
        denominators.draw(c,paint,offsetX+x,offsetY+y);

        //paint.setStrokeWidth(strokeHeight);
        c.drawRect(divX+offsetX+x,divY+offsetY+y,divX+width+offsetX+x,divY+strokeHeight+offsetY+y,paint);

    }

    @Override
    public float getBaseLine() {
        return divY+strokeHeight/2;
    }

    @Override
    public String show() {
        return "(("+ numerators.show() + ')' +'/'+ '('+ denominators.show() + "))";
    }


    public BlockContainer getNumerators(){
        return numerators;
    }

    public BlockContainer getDenominators(){
        return denominators;
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

        float scale=textSize / setting.DefaultTextSize;
        lineHeight=scale*setting.TEXT_SPACING;
        /**/
        {
            this.strokeHeight = setting.DivisonHeight * scale*1.2f;
            this.numerators.build(setting, paint, textSize);
            this.denominators.build(setting, paint, textSize);

        }


        {
            float shift = setting.block_Division_Margin * scale;
        /*setting the division line y position*/
            this.divY = this.numerators.height + shift;
            this.denominators.y = this.divY + shift + this.strokeHeight;
            this.height = this.denominators.height + this.denominators.y;


        /*setting width of division block and x positions of numerator,denumerator and the middle line in division*/
            this.divX = 0;
            this.width = this.numerators.width < this.denominators.width ? this.denominators.width : this.numerators.width;
            this.width *= setting.DivisonWidthFactor;//add extra width factor
            this.width += setting.DivisionOffsetWidth * scale * 2;//add extra division width default offset
        /*center both the numerator and denominator in the middle of the division*/
            numerators.x = this.width / 2 - numerators.width / 2;
            denominators.x = this.width / 2 - denominators.width / 2;
            /***********************************************************************/
        }



    }





}
