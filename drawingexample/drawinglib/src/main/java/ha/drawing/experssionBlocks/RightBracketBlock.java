package ha.drawing.experssionBlocks;

import com.example.scanner.TokenID;

/**
 * Created by hous_ on 10/9/2017.
 */

public class RightBracketBlock extends BracketBlock {
    public RightBracketBlock() {
        super(")");
    }

    @Override
    public BlockID getId() {
        return BlockID.RIGHT_BRACKET;
    }
}
