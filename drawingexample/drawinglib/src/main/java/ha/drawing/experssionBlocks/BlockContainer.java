package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;



import ha.drawing.Setting;
import ha.drawing.experssionBlocks.util.BracketBuilder;


/**
 * Created by hassan on 12/26/2016.
 */
public class BlockContainer extends Block {

    /*any Block which is not instance of parentBlock and extend Block its parent must be a parentBlock */


    private Block blockHolder;
    public boolean root = false;
    /*the rectF of parentblock are always set from outside the block ,only in case its parent null which means the root block set from inside
    * all other blocks like textBlock and operatorBlock are set from inside*/

    protected List<Block> children = new ArrayList<>();

    public BlockContainer(TokenID tokenID, String text) {
        super(tokenID, text);
    }

    public BlockContainer() {
        super(TokenID.PARENT, "");
    }

    @Override
    public void drawer(Canvas c, Paint paint, float offsetX, float offsetY) {

        for (Block block : children) {
            paint.setStyle(Paint.Style.FILL);
            block.draw(c, paint, offsetX + x, offsetY + y);
        }


    }


    @Override
    public float getBaseLine() {

        return getChild(0).y + getChild(0).getBaseLine();
    }

    @Override
    public String show() {

        String equation = "";
        if (children.size() == 0) {
            return "";
        }

        for (int i = 0; i < getChildrens().size(); i++) {
            Block a = getChildrens().get(i);
            if (a instanceof BaseBlock || a instanceof DerivativeBlock ) {
                List<Block> v = findV(children, i);
                equation += getSpecialEquation(a, findV(children, i));
                i += v.size();
            } else {
                equation += a.show();
            }

        }

        return equation;
    }

    /*handle getting equation for base ,derivative since*/
    private String getSpecialEquation(Block a, List<Block> list) {
        String equation = "";
         /*if base block the output should be something like log(22,12) since in drawing it is log(22) 12 or if same goes in case of derivative*/
        equation += a.show();
//log(x,(x+log(x,(x+y)))        )
        List<Block> v = list;
        for (int j = 0; j < v.size(); j++) {
            if (v.get(j) instanceof BaseBlock || v.get(j) instanceof DerivativeBlock) {
                ArrayList<Block> v1 = findV(list, j);
                equation += getSpecialEquation(v.get(j), v1);
                j += v1.size();
            } else {
                equation += v.get(j).show();
            }
        }
        equation += ")";


        return equation;
    }


    public int size() {
        return children.size();
    }

    public Block getChild(int i) {

        return children.get(i);
    }

    public int getChildCount() {
        return children.size();
    }

    public void addChild(Block child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChild(int index, Block child) {
        child.setParent(this);
        children.add(index, child);
    }

    public void addChildrens(List<Block> all) {
        children.addAll(all);
    }

    public void addChildrens(int index, List<Block> all) {
        for (Block child : all) {
            child.setParent(this);
        }
        children.addAll(index, all);
    }

    public List<Block> getChildrens() {
        return children;
    }


    @Override
    public TokenID getId() {
        return super.getId();
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

        {
            /**********building the width and height of all childrens************************/
            for (Block child : children) {
                child.x = child.y = child.height = 0;
                child.setParent(this);
                child.build(setting, paint, textSize);
            }
            /***********************************************************************************/
        }
        calculateYPosition();
        BracketBuilder.buildBracket(this,setting,textSize,paint);
        calculateXPosition(setting,textSize);
        calculateContainerHeight();
        calculateContainerWidth();

    }

    @Override
    public List<Block> getChildren() {
        return children;
    }

    /**
     * shift all blocks to become at y>=0
     * first we find the child block with the smallest y position ,once found if less than 0 than shift all blocks by the y position of this block
     */
    private void shiftAboveZero() {
        float maxY = 0;
        /*get highest block below 0*/
        for (Block child : children) {
            maxY = maxY > child.y ? child.y : maxY;
        }

        //shift all blocks maxY so all become above zero
        for (Block child : children) {
            child.y = child.y - maxY;
        }
    }

    /**
     * calculate height of the block container
     * PS:note that all it will calculate the height from y position zero
     * so all blocks must have positive y position
     *
     */
    private void calculateContainerHeight(){
        shiftAboveZero();
        float maxHeight = 0;
        /*get max height */
        for (Block child : children) {
           float y=child.y>=0?child.y:0;
            maxHeight = maxHeight < child.height + y ? child.height + y : maxHeight;
        }
        this.height = maxHeight;
    }

    public Block getBlockHolder() {
        return blockHolder;
    }

    public void setBlockHolder(Block blockHolder) {
        this.blockHolder = blockHolder;
    }

    public Block getLastChild() {
        return children.get(children.size() - 1);
    }


    public void removeChild(int index) {
        children.get(index).setDestroyed(true);
        this.rebuild = true;
        children.get(index).parent = null;
        children.remove(index);

    }

    public void removeLastChild() {
        removeChild(children.size() - 1);
    }


    //finding the V of a formula in the form of lnV or sinv
    public static ArrayList<Block> findV(List<Block> V, int position) { //position is from where to start search
        int numberOfClosedBrackets = 0;
        int numberOfOpenedBrackets = 0;
        ArrayList<Block> V1 = new ArrayList<>();

        //continue looping until we find the same number of closed and open brackets or no brackers at all
        for (int i = position + 1; i < V.size(); i++) {

            if (V.get(i).getId() == TokenID.LEFT_BRACKET) {
                numberOfOpenedBrackets++;
            } else if (V.get(i).getId() == TokenID.RIGHT_BRACKET) {
                numberOfClosedBrackets++;
            }
            V1.add(V.get(i));

            //ln(x)^3
            if (numberOfClosedBrackets == numberOfOpenedBrackets) { //means found v
                break;
            }
        }//end for
        return V1;
    }

    @Override
    public boolean validate() {
        for(Block block:children){
            boolean valid=block.validate();
            if(!valid){
                return false;
            }
        }

        return true;
    }

    /**
     * calculating the y position for all blocks based on their baseline
     */
    private void calculateYPosition(){
            if (children.size() != 0) {
                children.get(0).y = 0;
                float baseLine = children.get(0).getBaseLine();
                for (int i = 1; i < children.size(); i++) {
                    Block child = children.get(i);
                    float baseline1 = children.get(i).getBaseLine();
                    child.y = baseLine - baseline1;
                }
            }
    }

    /**
     * calculating the x positions of all children
     * warning: always calculate the x position after calculating the height of all brackets
     * @param setting
     * @param textSize
     */
    private void calculateXPosition(Setting setting,float textSize){
            float currentX = 0;
            float shiftX = setting.TEXT_SPACING * setting.scaleTextSize(textSize);
            float spaceShiftX = setting.WORD_SPACING * setting.scaleTextSize(textSize); //used for space between words
            float shiftOperator = setting.block_Margin_Operator * setting.scaleTextSize(textSize);
            for (Block child : children) {
                Block before = child.before();
                if ((before != null && (before.getId() == TokenID.WORD || before.getId() == TokenID.PARENT))) {
                    /*this used only in description */
                    currentX += spaceShiftX;
                } else if (child.getId() == TokenID.Operator || (before != null && before.getId() == TokenID.Operator)) {
                    currentX += shiftOperator;
                } else if (child.getId() != TokenID.POWER) {
                    currentX += shiftX;
                }
                child.x = currentX;
                currentX += child.width;
            }
    }

    /**
     * calculating the width of the entire block (this)
     */
    private void calculateContainerWidth(){
            if (children.size() != 0) {
                Block lastChild = children.get(children.size() - 1);
                this.width = lastChild.width + lastChild.x;
            }
    }
}
