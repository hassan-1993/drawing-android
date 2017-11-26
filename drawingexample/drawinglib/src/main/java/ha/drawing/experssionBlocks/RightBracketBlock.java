package ha.drawing.experssionBlocks;

import com.example.scanner.TokenID;

/**
 * Created by hassan on 11/26/2017.
 */

public class RightBracketBlock extends BracketBlock {

    public RightBracketBlock(String c) {
        super(c, TokenID.RIGHT_BRACKET);
    }

    public RightBracketBlock(){
        super(")",TokenID.RIGHT_BRACKET);
    }
}
