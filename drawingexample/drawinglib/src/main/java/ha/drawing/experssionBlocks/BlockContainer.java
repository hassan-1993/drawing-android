package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;



import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class BlockContainer extends Block {

    /**
     * Any Block which is not instance of the parentBlock and extends Block then its
     * parent must be a parentBlock
     */

    private Block blockHolder;
    public boolean root = false;

    /**
     * The rectF of parentblock are always set from outside the block, only in case its
     * parent null which means the root block set from inside all other blocks like textBlock
     * and operatorBlock are set from inside
     */
    protected List<Block> children = new ArrayList<>();

    public BlockContainer() {

    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {
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

    /**
     * handle getting equation for base, derivative since
     *
     */
    private String getSpecialEquation(Block a, List<Block> list) {
        String equation = "";
        // if base block the output should be something like log(22,12) since in
        // drawing it is log(22) 12 or if same goes in case of derivative
        equation += a.show();

        //log(x,(x+log(x,(x+y))))
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

    public List<Block> getChildren() {
        return children;
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
    public BlockID getId() {
        return BlockID.PARENT;
    }

    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
    {
            /**********building the width and height of all childrens************************/
            this.rebuild = true;
            for (Block child : children) {
                child.x = 0;
                child.y = 0;
                if (!child.isReBuild()) {/*means block is builded no need to rebuild it again*/
                  //  continue;
                } else {
                   // Debug.logBlock("building", child);
                    child.setReBuild(true);
                }
                child.height = 0;
                ;
                child.setParent(this);
                child.build(setting, paint, textSize);
            }
            /***********************************************************************************/
        }


        /******setting the y position for all blocks based on their baseline*/
        {
            if (children.size() != 0) {
                children.get(0).y = 0;
                float baseLine = children.get(0).getBaseLine();
                for (int i = 1; i < children.size(); i++) {
                    Block child = children.get(i);
                    float baseline1 = children.get(i).getBaseLine();
                    if(child.before() instanceof BracketBlock && child.getId()==BlockID.POWER) {
                        /*in case of power and before it is a bracket
                        * the bracket is not build yet so we can not use it to calculate the baseline of power yet*/
                    }else{
                        child.y = baseLine - baseline1;
                    }
                }
            }
            /************************************************************************************/
        }


        shiftAboveZero();

        /*calculate current height since it might be needed in buidling the brackets*/
        calculateContainerHeight();

        buildPowerBracketHeight(this,setting,textSize);
        shiftAboveZero();
        for (Block child : children) {
            if (child.getId() == BlockID.LEFT_BRACKET || child.getId() == BlockID.RIGHT_BRACKET) {
                child.build(setting, paint, textSize);
            }
        }
        //calculate container height again since it might have changed after building bracket ()
        calculateContainerHeight();



        {
            /*******************calculating the x positions of all children*************************/
            float currentX = 0;
            float shiftX = setting.TEXT_SPACING * setting.scaleTextSize(textSize);
            float spaceShiftX = setting.WORD_SPACING * setting.scaleTextSize(textSize); //used for space between words
            float shiftOperator = setting.block_Margin_Operator * setting.scaleTextSize(textSize);
            for (Block child : children) {
                Block before = child.before();
                if ((before != null && (before.getId() == BlockID.WORD || before.getId() == BlockID.PARENT))) {
                    /*this used only in description */
                    currentX += spaceShiftX;
                } else if (child.getId() == BlockID.Operator || (before != null && before.getId() == BlockID.Operator)) {
                    currentX += shiftOperator;
                } else if (child.getId() != BlockID.POWER) {
                    currentX += shiftX;
                }
                child.x = currentX;
                currentX += child.width;
            }
            /***************************************************************************************/
        }

        {
            /*** calculating the width of the entire block (this)**********************************/
            if (children.size() != 0) {
                Block lastChild = children.get(children.size() - 1);
                this.width = lastChild.width + lastChild.x;
            }
            /*************************************************************************************/
        }


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

        float maxHeight = 0;
        /*get max height */
        for (Block child : children) {
           float y=child.y>=0?child.y:0;
            maxHeight = maxHeight < child.height + y ? child.height + y : maxHeight;
        }
        this.height = maxHeight;
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

    /**
     * build the height and y position of left and bracket ,as well as any power after a right bracket
     * @param blocks
     * @param setting
     * @param textSize
     */
    protected static void buildPowerBracketHeight(BlockContainer blocks, Setting setting, float textSize) {

        // How it works? once left bracket is found push it to stack ,and continue
        // while continuing ,check the next ,if left bracket addNewEquation to stack ,else if right bracket ,remove left bracket from stack and calculate the height and y position according to whats between them
        ArrayList<Block> stack = new ArrayList<>();

        for (int i = 0; i < blocks.getChildrens().size(); i++) {
            Block b = blocks.getChildrens().get(i);
            if ((b.getId() == BlockID.LEFT_BRACKET)) {
                stack.add(b);
            } else if (b instanceof BlockContainer) {
                buildPowerBracketHeight((BlockContainer) b,setting,textSize);
            } else if (b.getId() == BlockID.RIGHT_BRACKET) {
                //remove last bracket from stack if exists
                if (stack.size() != 0) {
                    float minY = 999000, maxY = 0;
                    //means we found a left and right bracket
                    //now lets find the minimum y and max y between them
                    int firstindex = stack.get(stack.size() - 1).index();

                    boolean found = false;
                    for (int j = firstindex; j <= i; j++) {
                        Block check = blocks.getChildrens().get(j);
                        //in case of base block the height of bracket only includes the log
                        maxY = maxY < check.y + check.height ? check.y + check.height : maxY;
                        minY = minY > check.y ? check.y : minY;
                        found = true;
                    }
                    Block lastLeftBracket = stack.get(stack.size() - 1);
                    if (found) {
                        //now set the height and y
                        lastLeftBracket.height = maxY - minY;
                        lastLeftBracket.y = minY;
                        b.y = minY;
                        b.height = maxY - minY;
                        stack.remove(stack.size() - 1);

                    } else {
                        lastLeftBracket.height = blocks.height;
                        lastLeftBracket.y = 0;
                        b.height = blocks.height;
                        b.y = 0;
                    }
                    if(isPowerAfterBracket(b)){
                        /*calculate the y position of power block since it depends on the right bracket before it
                         * */
                        b.next().y=b.y+b.getBaseLine()-b.next().getBaseLine();
                    }

                } else {
                    float minY = 999000, maxY = 0;
                    Boolean found = false;
                    //calculate the  height of right bracket with respect to whats before it
                    for (int e = i - 1; e >= 0; e--) {
                        Block check = blocks.getChildrens().get(e);
                        if (check.getId() == BlockID.LEFT_BRACKET || check.getId() == BlockID.RIGHT_BRACKET) {
                            continue;
                        }
                        maxY = maxY < check.y + check.height ? check.y + check.height : maxY;
                        minY = minY > check.y ? check.y : minY;
                        found = true;
                    }
                    if (found) {
                        b.y = minY;
                        b.height = maxY - minY;
                    } else {
                        /*find any block which is not a bracket to calculate the y position of bracket
                        * in case all are bracket or no block than y is 0*/
                        for (int e = i+1; e <blocks.getChildCount(); e++) {
                            Block check = blocks.getChildrens().get(e);
                            if (check.getId() != BlockID.LEFT_BRACKET && check.getId() != BlockID.RIGHT_BRACKET && check.getId()!=BlockID.POWER) {
                               b.y=check.y+check.getBaseLine()-b.getBaseLine();
                                break;
                            }else if(e==blocks.getChildCount()-1){
                                b.y = 0;
                            }
                        }

                        /*if nothing before it than use the default height of block*/
                        b.height = setting.Rect_Height*setting.scaleTextSize(textSize);
                    }
                    if(isPowerAfterBracket(b)){
                            /*calculate the y position of power block since it depends on the right bracket before it
                            * */
                        b.next().y=b.y+b.getBaseLine()-b.next().getBaseLine();
                    }
                }

            }
        }


        //check if there is any left bracket left in stack
        for (Block b : stack) {
            boolean found = false;
            //calculate its height with respect to whats after it
            int index = b.index();
            float maxY = 0, minY = 99999;
            for (int i = index + 1; i < blocks.getChildrens().size(); i++) {
                Block check = blocks.getChildrens().get(i);
                maxY = maxY < check.y + check.height ? check.y + check.height : maxY;
                minY = minY > check.y ? check.y : minY;
                found = true;
            }

            if (found) {
                b.height = maxY - minY;
                b.y = minY;
            }
        }
    }

    /**
     *
     * @param block
     * @return true if block is a right bracket and after it is a power block
     */
    private static boolean isPowerAfterBracket(Block block){
        if(block.getId()==BlockID.RIGHT_BRACKET && block.next()!=null && block.next().getId()==BlockID.POWER)
            return true;
        return false;
    }

    //finding the V of a formula in the form of lnV or sinv
    public static ArrayList<Block> findV(List<Block> V, int position) { //position is from where to start search
        int numberOfClosedBrackets = 0;
        int numberOfOpenedBrackets = 0;
        ArrayList<Block> V1 = new ArrayList<>();

        //continue looping until we find the same number of closed and open brackets or no brackers at all
        for (int i = position + 1; i < V.size(); i++) {

            if (V.get(i).getId() == BlockID.LEFT_BRACKET) {
                numberOfOpenedBrackets++;
            } else if (V.get(i).getId() == BlockID.RIGHT_BRACKET) {
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
}
