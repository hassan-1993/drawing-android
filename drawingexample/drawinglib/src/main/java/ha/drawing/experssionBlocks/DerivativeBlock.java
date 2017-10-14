package ha.drawing.experssionBlocks;


import com.example.scanner.TokenID;

/**
 * Created by hassan on 1/21/2017.
 */
public class DerivativeBlock extends DivisionBlock {

    private char variable;

    public DerivativeBlock(char variable) {
        this.variable=variable;
        this.numerator.addChild(new TextBlock("d"));

        this.denominator.addChild(new TextBlock("d"));
        if(variable=='$'){
            this.denominator.addChild(new EmptyBlock());
        }else{
            this.denominator.addChild(new TextBlock(variable+""));
        }
    }

    @Override
    public BlockID getId() {
        return BlockID.DERIVE;
    }

    @Override
    public String show() {
        return "derive(" + this.denominator.getChild(1).show() + ",";
    }

}
