package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public abstract class Block {


    public float x,y,width,height;
    private Integer color=null;
    protected float lineHeight=10;
    private int strokeWidth;
    private boolean bold=false;



    boolean destroyed=false;
    boolean rebuild=true; /*if rebuild=false means block is builded no need to build it again*/

    public Block parent;

    protected Block(){
    }



    public abstract BlockID getId();



    protected abstract void onDraw(Canvas c, Paint paint, float offsetX, float offsetY);
    protected abstract void measure(Setting setting, Paint paint, float textSize);
    public abstract String show();
    public abstract float getBaseLine();

    /**
     * Derived classes with children should override
     * this method and call layout on each of
     * their children.
     *
     * @param x Left position, relative to parent
     * @param y Top position, relative to parent
     */
    protected void layout(Setting setting, Paint paint, float textSize, float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas c, Paint paint, float offsetX, float offsetY){
        paint.setStrokeWidth(this.strokeWidth);
        int tempColor=paint.getColor();
        if(color!=null){
            paint.setColor(color);
        }
        onDraw(c,paint,offsetX,offsetY);
        paint.setStyle(Paint.Style.FILL);/*set back paint to fill in case changed*/
        paint.setColor(tempColor);
    }

    public void build(Setting setting, Paint paint, float textSize){
        this.strokeWidth= (int) (setting.Defualtstroke*textSize/setting.DefaultTextSize);
        measure(setting,paint,textSize);
        layout(setting, paint, textSize, x, y);
    }

    public float getAbsoluteX(){
        float x=0;
        Block b=parent;
        while (b!=null){
            x+=b.x;
            b=b.parent;
        }

        return x+this.x;
    }

    public float getAbsoluteY(){
        float y=0;
        Block b=parent;
        while (b!=null){
            y+=b.y;
            b=b.parent;
        }

        return y+this.y;
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




    public   boolean validate(){
       return true;
    }



    public void remove(){
            getParent().removeChild(index());
    }






    protected void setParent(Block parent){
        this.parent=parent;
    }







    protected static float leftSide(RectF rectF){
        return rectF.width()/2+rectF.left;
    }

    public boolean isDestroyed() {
        return destroyed;
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


    public void resetColor(){
        this.color=null;
    }


    public int getStrokeWidth(){
        return this.strokeWidth;
    }
}
