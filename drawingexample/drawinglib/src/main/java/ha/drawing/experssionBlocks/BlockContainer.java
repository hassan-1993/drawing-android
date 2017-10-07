package me.math.com.math.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;


import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.drawing.Setting;
import me.math.com.math.cas.expression.parser.scanner.TokenID;

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
        super(TokenID.T_PARENT, "");
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

    @Override
    public Cursor onTouch(float touchX, float touchY) {
        /*if root==true ,this is the root block check if touching it  before checking which child is touching
        * */
        touchX -= x;
        touchY -= y;

        Block chosen = TouchManager.getNearestBlock(touchX, touchY, this);

        if (chosen != null) return chosen.onTouch(touchX, touchY);

        return null;
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
    public TokenID getId() {
        return super.getId();
    }



    @Override
    protected void builder(Setting setting, Paint paint, float textSize) {

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
                    if(child.before() instanceof BracketBlock && child.getId()==TokenID.T_POWER) {
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
            if (child.getId() == TokenID.T_LEFT_BRACKET || child.getId() == TokenID.T_RIGHT_BRACKET) {
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
                if ((before != null && (before.getId() == TokenID.T_WORD || before.getId() == TokenID.T_PARENT))) {
                    /*this used only in description */
                    currentX += spaceShiftX;
                } else if (child.getId() == TokenID.T_Operator || (before != null && before.getId() == TokenID.T_Operator)) {
                    currentX += shiftOperator;
                } else if (child.getId() != TokenID.T_POWER) {
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
    @Override
    public Cursor touched(boolean left) {
//        /**/
//
//        EmptyBlock emptyBlock=new EmptyBlock();
//
//        if(left){
//         //  Block first=parent.getChildren();
//            parent.addChild(index(),emptyBlock);
//            return new Cursor(emptyBlock,true);
//        }else{
//            parent.addChild(index()+1,emptyBlock);
//            return new Cursor(emptyBlock,true);
//        }
        return null;/*a parent block can never be touched*/
    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {
        Block before = caller.before();

        /*if before is a parent block we move through it */
        if (before instanceof BlockContainer) {
            BlockContainer parentBlock = ((BlockContainer) before);
            return parentBlock.getChild(parentBlock.getChildCount() - 1).touched(false);
        } else {
            if (before == null) {
                if (caller.parent != null && parent != null) {
                    return parent.moveLeft(isLeft, caller.parent);
                }
                /*if null means this is the root block nothing we can do*/
                return null;
            } else {
                return before.moveLeft(isLeft, caller);
            }
        }

    }

    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        Block after = caller.next();

        /*if after is a parent block we move through it */
        if (after instanceof BlockContainer) {
            BlockContainer parentBlock = ((BlockContainer) after);
            return parentBlock.getChild(parentBlock.getChildCount() - 1).touched(false);
        } else {
            if (after == null) {
                if (caller.parent != null && parent != null)
                    return parent.moveRight(isLeft, caller.parent);

                return null; /*this is the last block*/

            } else {

                return after.moveRight(isLeft, caller);
            }
        }

    }

    // FIXME: 1/21/2017 bracket should not be used for
    // FIXME: 1/20/2017 if we press the power block twice and touched the middle block between the three error happen 
    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        /*PS:a parent block must always consist of one child at least or should be removed entirely with its parent*/

        Block before = caller.before();

        if (caller instanceof EmptyBlock) {
            /*means definitely for delete */
            /*try to delete whats before it if any*/
            if (before != null) {
                if (before.parent != null && before.parent.parent instanceof DerivativeBlock) {
                    /*delete the derivative block and the left bracket after it*/
                    Block nextNextParent = before.parent.parent.next().next();
                    Block beforeParent = before.parent.parent.before();
                    Block parentOfParent = before.parent.parent.parent;
                    before.parent.parent.next().remove();
                    before.parent.parent.remove();

                    if (nextNextParent != null) {
                        return nextNextParent.touched(true);
                    } else if (beforeParent != null) {
                        return beforeParent.touched(false);
                    } else {
                         /*add empty block*/
                        EmptyBlock emptyBlock = new EmptyBlock();
                        ((BlockContainer) parentOfParent).addChild(emptyBlock);
                        return new Cursor(emptyBlock, true);
                    }
                }
                Cursor cursorBlock = before.delete(true, caller, false);

            /*check cursor returned if not the same as caller than delete also*/
                if (cursorBlock != null) {
                    if (cursorBlock.getTouchedBlock() == caller) {
                        return cursorBlock;
                    } else {
                        /*if before it is a power block do not delete */
                        if (before.getId() != TokenID.T_POWER) {
                            removeChild(caller.index());
                        }
                        return cursorBlock;
                    }
                }
            }
        }

        if(before!=null  && before.getId()==TokenID.T_BASE){
            return before.moveLeft(false,before);
        }

        /*if true means we should remove the caller block*/
        if (delete) {
            int callerIndex = caller.index();
            boolean removed = false;
            Block next = caller.next();
            /*if after this block is a power block we can not delete the caller,and delete should  act as a moveleft
            * unless the caller to delete is a textblock than we should delete it and replace it with empty block*/
            if (next != null && caller.getId() == TokenID.T_EMPTY && next instanceof PowerBlock) {
                return moveLeft(isLeft, caller);
            }

            /*don't remove the caller if it is the only child and is an empty block*/
            if (!(children.size() == 1 && caller instanceof EmptyBlock) && !(before instanceof DerivativeBlock) && caller.getId()!=TokenID.T_BASE) {
                this.removeChild(callerIndex);
                removed = true;
            } else if (before instanceof DerivativeBlock) {
//                /*if before is derivative block than delete should act as move left twice
//                * */
//                return before.moveLeft(true, before);
                before=before.before();
                //delete it all (bracket with derivative block before it)
                this.removeChild(callerIndex);
                this.removeChild(callerIndex-1);
                callerIndex=callerIndex-1;
                removed=true;
            }else if(caller.getId()==TokenID.T_BASE){
                caller.next().remove();
                caller.remove();
            }

            if (before == null) {

                if (parent == null && children.size() == 0) {
                    /*if no children left and this is the root block so add empty block and return cursor*/
                    EmptyBlock emptyBlock = new EmptyBlock();
                    addChild(emptyBlock);
                    return new Cursor(emptyBlock, true);
                } else if (parent == null && removed) {
                    return next.touched(true);
                }
                /*if no childrens left or caller is the only child and empty block*/
                if (children.size() == 0 || (children.size() == 1 && caller instanceof EmptyBlock)) {
                    if (caller instanceof EmptyBlock) {
                        if (parent != null && parent.parent != null && parent.parent.getId() == TokenID.T_MATRIX) {
                            //means it is a column of block let the matrix block handle that
                            return parent.parent.delete(false, this, true);
                        } else {
                            if (parent == null) return null;
                            /*delete this block (parent of caller)*/
                            return parent.delete(false, this, true);
                        }
                    } else {
                         /*add empty block*/
                        EmptyBlock emptyBlock = new EmptyBlock();
                        addChild(emptyBlock);
                        return new Cursor(emptyBlock, true);
                    }
                } else {
                    /*if it was the first child which we already delete and must be empty block,the delete will now act as move left */
                    if (callerIndex == 0 && removed && caller.getId() == TokenID.T_EMPTY) {
                        return getChild(callerIndex).moveLeft(true, getChild(callerIndex));
                    }
                    /*if not empty */
                    Block after = children.get(callerIndex);
                    return after.touched(true);
                }
            } else {
                if (before.getId() == TokenID.T_FUNCTION) {
                    /*if before block function block ,means current block we deleted is a left bracket so we delete them as well*/
                    return delete(false, before, true);
                }

                return before.touched(false);


            }
        } else {
            /*if caller left side means delete whats before if exist*/
            if (isLeft) {
                if (before == null) {
                    /*if nothing before it than act as a move left*/
                    return caller.moveLeft(isLeft, caller);
                } else {
                    /*if before block exist than call its delete and let it handle it*/
                    return before.delete(isLeft, caller, false);/*the first parameter is what matters ,the last parameter delete isn't important unless calling function delete of parent block ,deletion can only happen in a parentblock*/
                }
            }
            /*if nothing to delete ,delete acts as a move left*/
            return caller.moveLeft(isLeft, caller);
        }

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

    /**
     * build the height and y position of left and bracket ,as well as any power after a right bracket
     * @param blocks
     * @param setting
     * @param textSize
     */
    protected static void buildPowerBracketHeight(BlockContainer blocks, Setting setting, float textSize) {


        //how it works ,once left bracket is found push it to stack ,and continue
        //while continuing ,check the next ,if left bracket addNewEquation to stack ,else if right bracket ,remove left bracket from stack and calculate the height and y position according to whats between them
        ArrayList<Block> stack = new ArrayList<>();

        for (int i = 0; i < blocks.getChildrens().size(); i++) {
            Block b = blocks.getChildrens().get(i);
            if ((b.getId() == TokenID.T_LEFT_BRACKET)) {
                stack.add(b);
            } else if (b instanceof BlockContainer) {
                buildPowerBracketHeight((BlockContainer) b,setting,textSize);
            } else if (b.getId() == TokenID.T_RIGHT_BRACKET) {
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
                        if (check.getId() == TokenID.T_LEFT_BRACKET || check.getId() == TokenID.T_RIGHT_BRACKET) {
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
                            if (check.getId() != TokenID.T_LEFT_BRACKET && check.getId() != TokenID.T_RIGHT_BRACKET && check.getId()!=TokenID.T_POWER) {
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
        if(block.getId()==TokenID.T_RIGHT_BRACKET && block.next()!=null && block.next().getId()==TokenID.T_POWER)
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

            if (V.get(i).getId() == TokenID.T_LEFT_BRACKET) {
                numberOfOpenedBrackets++;
            } else if (V.get(i).getId() == TokenID.T_RIGHT_BRACKET) {
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
