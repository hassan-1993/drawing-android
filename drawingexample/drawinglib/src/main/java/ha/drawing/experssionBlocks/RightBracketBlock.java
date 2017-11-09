package ha.drawing.experssionBlocks;

import java.util.ArrayList;

import ha.drawing.Setting;

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

    @Override
    protected void onLayout(Setting setting, float textSize, float x, float y) {
        float minHeight = setting.RECT_HEIGHT * textSize / setting.DefaultTextSize;
        float width = getBound2(setting, textSize, text);

        Block leftBracket = getRelatedBracket();

        // Calculate the height by finding the biggest deference in the
        // y position of each block.
        if(leftBracket != null) {
            ArrayList<Block> blocksInBetween = getBlocksInBetween(leftBracket, this);
            float minY = y;
            float maxY = y;
            for (Block block : blocksInBetween) {
                if(block.y < minY)
                    minY = block.y;

                if(block.y + block.height > maxY)
                    maxY = block.y + block.height;
            }

            minHeight = maxY-minY;
            leftBracket.setMeasurement(width, minHeight);
        }

        //
        setMeasurement(width, minHeight);
    }
}
