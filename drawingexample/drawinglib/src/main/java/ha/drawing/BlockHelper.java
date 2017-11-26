package ha.drawing;

import android.graphics.Paint;

import com.example.expression.Expression;
import com.example.parser.ExpressionBuilder;
import com.example.parser.ExpressionParser;
import com.example.scanner.Token;
import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;

import ha.drawing.experssionBlocks.BaseBlock;
import ha.drawing.experssionBlocks.BlockContainer;
import ha.drawing.experssionBlocks.BracketBlock;
import ha.drawing.experssionBlocks.DerivativeBlock;
import ha.drawing.experssionBlocks.DivisionBlock;
import ha.drawing.experssionBlocks.LeftBracketBlock;
import ha.drawing.experssionBlocks.MatrixBlock;
import ha.drawing.experssionBlocks.OperatorBlock;
import ha.drawing.experssionBlocks.PowerBlock;
import ha.drawing.experssionBlocks.RadicalBlock;
import ha.drawing.experssionBlocks.RightBracketBlock;
import ha.drawing.experssionBlocks.TextBlock;


/**
 * Created by houssam on 12/26/2016.
 */
public class BlockHelper {


    protected Setting setting;


    public BlockHelper() {
        setting = new Setting();
    }

    public BlockHelper(Setting setting) {
        this.setting = setting;
    }

    public BlockHelper(float textSize) {
        this(new Setting(textSize));
    }

    private void buildBlocks(Expression tree, BlockContainer block) {
        TokenID id = tree.getId();



            boolean addBracket = addBracket(tree);

            if (addBracket) {
                block.addChild(new LeftBracketBlock());
            }

            switch (id) {
                case BRACKET:
                    block.addChild(new LeftBracketBlock());
                    buildBlocks(tree.getChild(0),block);
                    block.addChild(new RightBracketBlock());
                    break;
                case LOG:
                    BaseBlock baseBlock = new BaseBlock("log");
                    buildBlocks(tree.getChild(0), baseBlock.getBase());
                    block.addChild(baseBlock);
                    block.addChild(new LeftBracketBlock());
                    buildBlocks(tree.getChild(1), block);
                    block.addChild(new RightBracketBlock());
                    break;
                case ADDITION:
                case SUBTRACTION:
                case MULTIPLICATION:
                case EQUAL:
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        buildBlocks(tree.getChild(i), block);
                        if (i < tree.getChildCount() - 1) {
                            if (tree.getId() != TokenID.MULTIPLICATION || addMultiplication(tree, i)) {
                                OperatorBlock operatorBlock = new OperatorBlock(tree.getSequence());
                                block.addChild(operatorBlock);
                            }
                        }
                    }
                    break;
                case NUMBER:
                case CONSTANT:
                    block.addChild(new TextBlock(tree.getSequence()));
                    break;
                case NEGATIVE:
                case POSITIVE:
                    block.addChild(new OperatorBlock(tree.getSequence()));
                    buildBlocks(tree.getChild(0), block);
                    break;
                case DERIVE:
                    DerivativeBlock derivativeBlock = new DerivativeBlock(tree.getChild(0).getSequence().charAt(0));
                    block.addChild(derivativeBlock);
                    block.addChild(new LeftBracketBlock());
                    buildBlocks(tree.getChild(1), block);
                    block.addChild(new RightBracketBlock());
                    break;
                case DIVISION:
                case FRACTION:
                    DivisionBlock divisionBlock = new DivisionBlock();
                    buildBlocks(tree.getChild(0), divisionBlock.getNumerators());
                    buildBlocks(tree.getChild(1), divisionBlock.getDenominators());
                    block.addChild(divisionBlock);
                    break;
                case FUNCTION:
                    if (tree.getSequence().equals("log")) {
                        baseBlock = new BaseBlock("log");
                        buildBlocks(tree.getChild(0), baseBlock.getBase());
                        block.addChild(baseBlock);
                        block.addChild(new LeftBracketBlock());
                        buildBlocks(tree.getChild(1), block);
                        block.addChild(new RightBracketBlock());
                    } else {
                        block.addChild(new TextBlock(tree.getSequence()));
                        block.addChild(new LeftBracketBlock());
                        buildBlocks(tree.getChild(0), block);
                        block.addChild(new RightBracketBlock());
                    }
                    break;
                case EXPONENTIAL:
                    block.addChild(new TextBlock("e"));
                    break;
                case FACTORIAL:
                    buildBlocks(tree.getChild(0), block);
                    block.addChild(new TextBlock("!"));
                    break;
                case MATRIX:
                    MatrixBlock matrixBlock = new MatrixBlock();
                    for (int r = 0; r < tree.getChildCount(); r++) {
                        BlockContainer row = new BlockContainer();
                        for (int c = 0; c < tree.getChild(r).getChildCount(); c++) {
                            BlockContainer col = new BlockContainer();
                            buildBlocks(tree.getChild(r).getChild(c), col);
                            row.addChild(col);
                        }
                        matrixBlock.addRow(row);
                    }
                    block.addChild(matrixBlock);
                    break;
                case POWER:
                    PowerBlock powerBlock = new PowerBlock();
                    buildBlocks(tree.getChild(1), powerBlock.getPower());
                    buildBlocks(tree.getChild(0), block);
                    block.addChild(powerBlock);
                    break;
                case SQRT:
                    RadicalBlock radicalBlock = new RadicalBlock();
                    buildBlocks(tree.getChild(0), radicalBlock.getInsideRadical());
                    block.addChild(radicalBlock);
                    break;
                case MAT_INVERSE:
                    break;
            }

            if (addBracket) {
                block.addChild(new RightBracketBlock());
            }


    }


    public BlockContainer buildBlocks(String seq) {
        return buildBlocks(seq, setting.DefaultTextSize);
    }

    public BlockContainer buildBlocks(String seq, float textSize) {
        Expression expression = new ExpressionBuilder().build(seq);
        BlockContainer parentBlock = new BlockContainer();
        buildBlocks(expression, parentBlock);
        parentBlock.root = true;
        parentBlock.build(setting, setting.getPaint(), textSize);
        return parentBlock;
    }






    public Setting getSetting() {
        return setting;
    }

    public Paint getPaint() {
        return setting.getPaint();
    }


    /**
     * @param tree
     * @return whether we should add bracket to block (expression) or not
     */
    private boolean addBracket(Expression tree) {
/*        add bracket if similar to cases like below
          addition       (4+3)^3
                         *(3+3)      (3+3)*
                         -(3+3)
                         4-(4+5)
                         +(3+3)

          multiply       (3*5*6)^5
          division       (4/3)^3
          subtraction    -(3-3)
                         4-(3-3)
                         (3-3)^3
                         *(3-3)       (3-3)*
         negative       *(-3)*    *(-3)
                        (-3)^3
                        2+(-3)
                        -(-3)
                        3-(-3)
                        +(-3)


                        +(-3)*  3+(-3)*
                        3-(-3)* -(-3)*

       positive       same concept as the negative

       power    2^(5^6)
       */

        Expression parent=tree.getParent();

        if(parent==null){
            return false;
        }else{
            int index=tree.getIndex();
            TokenID parentId=parent.getId();
            TokenID treeId=tree.getId();
            boolean leftOfPower=(index==0 && parentId== TokenID.POWER);
            boolean addBracketForAddition=treeId==TokenID.ADDITION && (leftOfPower || parentId==TokenID.MULTIPLICATION || isNegativeOrPositive(parent) || (index==1 && parentId==TokenID.SUBTRACTION));
            boolean addBracketForMultiplication=treeId==TokenID.MULTIPLICATION && leftOfPower;
            boolean addBracketForDivision=(treeId==TokenID.DIVISION  || treeId==TokenID.FRACTION )&& leftOfPower;
            boolean addBracketForSubtraction=treeId==TokenID.SUBTRACTION && (leftOfPower || isNegativeOrPositive(parent) || (parentId==TokenID.SUBTRACTION && index==1) || parentId==TokenID.MULTIPLICATION);

            Expression mainParent=parent.getParent();
            boolean addBracketForNegativeOrPositive=isNegativeOrPositive(tree) &&
                    (((parentId==TokenID.MULTIPLICATION || parentId==TokenID.ADDITION) && (index!=0  || (mainParent!=null && (isNegativeOrPositive(mainParent) || (parent.getIndex()!=0 &&  isAdditionOrSubtraction(mainParent))))))
                                                 || isNegativeOrPositive(parent)  || (parentId==TokenID.SUBTRACTION && index==1) || leftOfPower );

            boolean addBracketForPower=tree.getId()==TokenID.POWER && parentId==TokenID.POWER;
            return addBracketForAddition || addBracketForMultiplication || addBracketForDivision || addBracketForSubtraction || addBracketForNegativeOrPositive || addBracketForPower;
        }
    }


    private boolean isNegativeOrPositive(Expression tree){
        return tree.getId()==TokenID.NEGATIVE || tree.getId()==TokenID.POSITIVE;
    }

    private boolean isAdditionOrSubtraction(Expression tree){
        return tree.getId()==TokenID.SUBTRACTION || tree.getId()==TokenID.ADDITION;
    }



    /**
     * for example 2*x   ,there is no need for * ,it should become 2x
     * for example 3*x*y ,it should become 3xy
     * for example 3*x*x ,keep it the same since x and x are the same
     * for example 3*2*x it should become 3*2x
     * for example 2*sin(x) it should become 2sin(x)
     *
     * @param tree should be the multiplication
     * @return whether we should add a multiplication to block or not
     */
    private boolean addMultiplication(Expression tree, int index) {
        TokenID id1 = getActualId(tree.getChild(index));
        TokenID id2 = getActualId(tree.getChild(index + 1));

        //if like 2*sin(x) or 2*x no need for *
        if (id1 == TokenID.NUMBER && ((id2 == TokenID.CONSTANT || id2 == TokenID.LOG || id2 == TokenID.FUNCTION))) {
            return false;
        }

        //if like x*y no need for * unless both are same like x*x
        if (id1 == TokenID.CONSTANT && id2 == TokenID.CONSTANT && !tree.getChild(index).getSequence().equals(tree.getChild(index + 1).getSequence())) {
            return false;
        }

        if (id1 == TokenID.FUNCTION && id2 == TokenID.FUNCTION) {
            return false;
        }

        //if like 2*x^2 no need for power
        if ((id1 == TokenID.NUMBER || id1 == TokenID.CONSTANT || id1 == TokenID.FUNCTION) && id2 == TokenID.POWER) {
            TokenID id3 = getActualId(tree.getChild(index + 1).getChild(0));
            if (((id3.isFunction() && id3 != TokenID.DERIVE) || id3 == TokenID.CONSTANT))
                return false;
        }

        return true;
    }


    /**
     * get the id of expression
     * in case of the expression is a stepFrom or stepTo  ,the id is their first child
     *
     * @param expression
     * @return
     */
    private TokenID getActualId(Expression expression) {
        Token token = expression.getToken();
        boolean isStepExpression = (token.getTokenID() == TokenID.FUNCTION && (token.getSequence().equals("stepFrom") || token.getSequence().equals("stepTo")));
        return isStepExpression ? getActualId(expression.getChild(0)) : token.getTokenID();
    }

}
