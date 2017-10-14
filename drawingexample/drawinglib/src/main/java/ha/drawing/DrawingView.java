package ha.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ha.drawing.experssionBlocks.Block;
import ha.drawing.experssionBlocks.BlockContainer;

/**
 * Created by houssam on 10/1/2017.
 */

public class DrawingView extends View{

    int width=500,height=500;
    BlockContainer block;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        block = new BlockHelper().buildBlocks("2+3");
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        block.draw(canvas, new Paint(), 0, 0);
    }


    /**
     * http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = (int) Math.min(this.width, widthSize);
        } else {
            //Be whatever you want
            width = (int) this.width;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = (int) Math.min(this.height, heightSize);
        } else {
            //Be whatever you want
            height = (int) this.height;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

}
