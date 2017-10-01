package ha.drawing.blocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by hassan on 10/1/2017.
 */

public class TextBlock extends Block {


    @Override
    float getBaseLine() {
        return 0;
    }

    @Override
    String show() {
        return null;
    }

    @Override
    void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {

    }

    @Override
    void builder(Paint paint, float textSize) {

    }

    @Override
    ArrayList<Block> getChildren() {
        return null;
    }
}
