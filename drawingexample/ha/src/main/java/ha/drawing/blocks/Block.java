package ha.drawing.blocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassan on 10/1/2017.
 */

public abstract class Block {


    public float x,y,width,height;
    public TokenID tokenID;

    abstract float getBaseLine();
    abstract String show();
    abstract void drawer(Canvas c, Paint paint, float offsetX, float offsetY);
    abstract void builder(Paint paint, float textSize);
    abstract ArrayList<Block> getChildren();


    protected void draw(Canvas c, Paint paint, float offsetX, float offsetY){

    }

    public void draw(Canvas c,Paint paint){

    }

    public void build(Paint paint, float textSize){

    }
}
