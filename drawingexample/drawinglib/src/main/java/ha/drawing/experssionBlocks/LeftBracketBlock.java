package ha.drawing.experssionBlocks;

import android.text.InputFilter;

import com.example.scanner.TokenID;

/**
 * Created by hous_ on 10/9/2017.
 */

public class LeftBracketBlock extends BracketBlock {
    public LeftBracketBlock() {
        super("(");
    }

    @Override
    public BlockID getId() {
        return BlockID.LEFT_BRACKET;
    }
}
