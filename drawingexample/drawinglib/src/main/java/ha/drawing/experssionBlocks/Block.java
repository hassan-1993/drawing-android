package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public abstract class Block {

    public float x;
    public float y;
    private float width;
    private float height;

    private float marginLeft;
    private float marginTop;
    private float marginRight;
    private float marginBottom;

    public void setMargin(float margin){
        this.marginLeft = margin;
        this.marginTop = margin;
        this.marginRight = margin;
        this.marginBottom = margin;
    }

    public void setMarginLeft(float margin){
        marginLeft = margin;
    }

    public void setMarginTop(float margin){
        marginTop = margin;
    }

    public void setMarginRight(float margin){
        marginRight = margin;
    }

    public void setMarginBottom(float margin){
        marginBottom = margin;
    }

    public float getMarginLeft(){
        return marginLeft;
    }

    public float getMarginTop(){
        return marginTop;
    }

    public float getMarginRight(){
        return marginRight;
    }

    public float getMarginBottom(){
        return marginBottom;
    }

    private Integer color = null;
    protected float lineHeight = 10;
    private int strokeWidth;

    boolean destroyed = false;

    // if rebuild=false means block is built no need to build it again
    boolean rebuild = true;

    public Block parent;

    private float baseLine;

    protected Block(){
    }

    public abstract BlockID getId();
    protected abstract void onDraw(Canvas c, Paint paint, float x, float y, float offsetX, float offsetY);
    protected abstract void measure(Setting setting, float textSize);
    public abstract float measureBaseLine();

    public final float getBaseLineHeight(){
        return measureBaseLine() + marginTop;
    }

    public abstract String show();

    protected final void layout(Setting setting, float textSize, float x, float y) {
        this.x = x;
        this.y = y;
        onLayout(setting, textSize, x + marginLeft, y + marginTop);
    }

    public float getWidth(){
        return marginLeft + width + marginRight;
    }

    public float getHeight(){
        return marginTop + height + marginBottom;
    }

    public float getInnerHeight(){
        return height;
    }

    public float getInnerWidth(){
        return width;
    }

    protected void setWidth(float width){
        this.width = width;
    }

    protected void setHeight(float height){
        this.height = height;
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
        onDraw(c,paint, x+ marginLeft, y+ marginTop, offsetX, offsetY);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tempColor);
//        drawBox(c, new Paint());
//        drawAntiBaseLine(c, new Paint());
    }

    public void drawBox(Canvas c, Paint paint){
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        // Draw base line.
        paint.setColor(Color.GREEN);
        c.drawLine(x+ marginLeft, y+getBaseLineHeight(), x+ marginLeft +width, y+getBaseLineHeight(), paint);

        // Draw Box
        paint.setColor(Color.RED);
        c.drawRect(x, y, x+ marginLeft +width+ marginRight, y+ marginTop +height+ marginBottom, paint);

        // Draw Inner Box
        paint.setColor(Color.BLUE);
        c.drawRect(x+ marginLeft, y+ marginTop, x+ marginLeft +width, y+ marginTop +height, paint);
    }

    public void build(Setting setting, float textSize){
        this.strokeWidth= (int) (setting.Defualtstroke*textSize/setting.DefaultTextSize);
        measure(setting, textSize);
        layout(setting, textSize, x+ marginLeft, y+ marginTop);
    }

    public void setBaseLine(float baseLine){
        this.baseLine = baseLine;
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

    public ArrayList<Block> getBlocksInBetween(Block block0, Block block1){
        int i0 = block0.index() < block1.index() ? block0.index() : block1.index();
        int i1 = block0.index() > block1.index() ? block0.index() : block1.index();
        ArrayList<Block> blockList = new ArrayList<>();

        //
        for(int i = i0+1; i < i1; i++){
            Block block = getParent().getChild(i);
            blockList.add(block);
        }

        //
        return blockList;
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
