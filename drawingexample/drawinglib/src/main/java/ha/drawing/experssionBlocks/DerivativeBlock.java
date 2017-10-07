package me.math.com.math.drawing.experssionBlocks;


import me.math.com.math.cas.expression.parser.scanner.TokenID;
import me.math.com.math.drawing.TouchManager;
import me.math.com.math.drawing.cursorblock.Cursor;
import me.math.com.math.settings.LogManager;

/**
 * Created by hassan on 1/21/2017.
 */
public class DerivativeBlock extends DivisionBlock {

    char variable;


    public DerivativeBlock(char variable) {
        super(TokenID.T_DERIVE);
        this.variable=variable;
        this.numerators.addChild(new TextBlock("d"));

        this.denominators.addChild(new TextBlock("d"){
            @Override
            public Cursor touched(boolean left) {
                /*special case for derivative block ,if touched means add empty block next to it
                  or if there is already block after
                 * it than point cursor to it */
                if(denominators.getChildCount()>1){
                    return denominators.getChild(1).touched(true);
                }
                EmptyBlock emptyBlock=new EmptyBlock();
                denominators.addChild(emptyBlock);
                return emptyBlock.touched(true);
            }


            @Override
            public Cursor delete(boolean isLeft, Block caller, boolean delete) {
                /*we can not delete the block in a derivative ,so instead it act as move left outside the derivative block*/
                return DerivativeBlock.this.touched(true);
            }

            @Override
            public Cursor moveLeft(boolean isLeft, Block caller) {
                return DerivativeBlock.this.touched(true);
            }
        });
        if(variable=='$'){
            this.denominators.addChild(new EmptyBlock());
        }else{
            this.denominators.addChild(new TextBlock(variable+""));
        }


    }


    @Override
    public Cursor delete(boolean isLeft, Block caller, boolean delete) {
        return parent.delete(false,this,true);
    }

    @Override
    public String show() {

        return "derive(" + this.denominators.getChild(1).show() + ",";
    }

    @Override
    public Cursor onTouch(float touchX, float touchY) {
        touchX-=x;
        touchY-=y;

        LogManager.d("touch Derivative Block"," touchX is " + touchX    + " x is " + x  + " width is " + width);
        /*special case for derivative */
        if(TouchManager.isLeftSide(touchX,this)){
            LogManager.d("touch Derivative Block"," touch true");
            return touched(true);
        }else{
            LogManager.d("touch Derivative Block"," touch false");
            return next().touched(false); /*there is a always a left bracket after the derivativeBlock*/
        }

    }


    @Override
    public Cursor moveRight(boolean isLeft, Block caller) {
        if(caller==denominators){
            /*there always a left bracket after the derivative*/
            return this.next().touched(false);
        }else {
            if (isLeft) {
                return   touched(true);
            } else {
              //  return denominators.getChild(1).touched(true);
                return next().touched(false);
            }
        }
    }

    @Override
    public Cursor moveLeft(boolean isLeft, Block caller) {
        if(caller==numerators){
            return  this.touched(true);
        }else {
            if (isLeft) {
                return denominators.getLastChild().touched(false);
            } else {
                return touched(false);
            }
        }
    }

    @Override
    public Cursor touched(boolean left) {
        if(left){
            return super.touched(true);
        }else{
            /**/
         //   return denominators.getLastChild().touched(false);
            return super.touched(true);
        }
    }
}
