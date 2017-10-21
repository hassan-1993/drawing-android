package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public abstract class Block {

    public float x;
    public float y;
    public float width;
    public float height;

    private Integer color = null;
    protected float lineHeight = 10;
    private int strokeWidth;

    boolean destroyed = false;

    // if rebuild=false means block is built no need to build it again
    boolean rebuild = true;

    public Block parent;

    protected Block(){
    }

    public abstract BlockID getId();
    protected abstract void onDraw(Canvas c, Paint paint, float offsetX, float offsetY);
    protected abstract void measure(Setting setting, float textSize);
    public abstract String show();
    public abstract float getBaseLine();

    protected void layout(Setting setting, float textSize, float x, float y) {
        this.x = x;
        this.y = y;
        onLayout(setting, textSize, x, y);
    }

    /**
     * Derived classes with children should override
     * this method and call layout on each of
     * their children.
     *
     * @param x Left position, relative to parent
     * @param y Top position, relative to parent
     */
    protected void onLayout(Setting setting, float textSize, float x, float y){

    }

    public void draw(Canvas c, Paint paint, float offsetX, float offsetY){
        paint.setStrokeWidth(this.strokeWidth);
        int tempColor=paint.getColor();
        if(color!=null){
            paint.setColor(color);
        }
        onDraw(c,paint, offsetX, offsetY);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tempColor);
        drawBox(c, new Paint());
    }

    public void drawBox(Canvas c, Paint paint){
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        c.drawRect(x, y, x+width, y+height, paint);
    }

    public void build(Setting setting, float textSize){
        this.strokeWidth= (int) (setting.Defualtstroke*textSize/setting.DefaultTextSize);
        measure(setting, textSize);
        layout(setting, textSize, x, y);
    }

    public int index(){
        return this.parent==null?-1:getParent().getChildren().indexOf(this);
    }

    public BlockContainer getParent(){return (BlockContainer) parent;}

    public Block next(){
        return index()+1<getParent().size()?getParent().getChild(index()+1):null;
    }

    public Block before(){
        return parent!=null&&index()-1>=0?getParent().getChild(index()-1):null;
    }

    protected void setMeasurement(float width, float height){
        this.width = width;
        this.height = height;
    }

    public boolean validate(){
       return true;
    }

    protected void setParent(Block parent){
        this.parent=parent;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setReBuild(boolean build) {
        this.rebuild = build;
    }

    public boolean isReBuild(){
        return this.rebuild;
    }

    public void setColor(Integer color){
        this.color=color;
    }
}
