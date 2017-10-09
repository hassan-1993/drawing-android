package ha.drawing.experssionBlocks;


import com.example.scanner.TokenID;

/**
 * Created by hassan on 1/21/2017.
 */
public class DerivativeBlock extends DivisionBlock {

    char variable;


    public DerivativeBlock(char variable) {
        this.variable=variable;
        this.numerators.addChild(new TextBlock("d"));

        this.denominators.addChild(new TextBlock("d"){

        });
        if(variable=='$'){
            this.denominators.addChild(new EmptyBlock());
        }else{
            this.denominators.addChild(new TextBlock(variable+""));
        }


    }

    @Override
    public BlockID getId() {
        return BlockID.DERIVE;
    }

    @Override
    public String show() {

        return "derive(" + this.denominators.getChild(1).show() + ",";
    }


}
