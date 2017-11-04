package ha.drawing.experssionBlocks.util;

import android.graphics.Paint;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.Stack;

import ha.drawing.Setting;
import ha.drawing.experssionBlocks.Block;
import ha.drawing.experssionBlocks.BlockContainer;

/**
 * Created by houssam on 10/14/2017.
 */

public class BracketBuilder {

    /**
     * build the bracket height and width  ,for the left or right bracket or both
     * @param blocks
     * @param setting
     * @param textSize
     */
    public static void buildBracket(BlockContainer blocks, Setting setting, float textSize, Paint paint) {
        ArrayList<ArrayList<Block>> pos = getBrackets(blocks);

        //calculate height for each bracket and power after right bracket
        for (int i = 0; i < pos.size(); i++) {
            ArrayList<Block> br = pos.get(i);
            //if a left and right bracket in it
            if (br.size() == 2) {
                Block leftBracket = br.get(0);
                Block rightBracket = br.get(1);

                if (leftBracket.next() == rightBracket) {
                    //means no block between left and right bracket
                    continue;
                }

                float[] minMax=getMinMaxY(leftBracket,br.get(1));

                float minY =minMax[0];
                float maxY= minMax[1];

                //calculate the height and y of left and right bracket
                leftBracket.height = maxY - minY;
                leftBracket.y = minY;
                br.get(1).y = leftBracket.y;
                br.get(1).height = leftBracket.height;
                //build again the left and right bracket (the width based on the height chosen)
                leftBracket.build(setting, paint, textSize);
                br.get(1).build(setting,paint,textSize);

                /**/
                Block nextBlock=br.get(1).next();
                if(nextBlock!=null && nextBlock.getId()==TokenID.POWER){
                    nextBlock.y=br.get(1).y+br.get(1).getBaseLine()-nextBlock.getBaseLine();
                }
                /**/

                pos.remove(i);
                i--;
            }
        }


        if(pos.size()!=0){
            //if not zero than the rest are either all left brackets or all right brackets
            Block bracket=pos.get(0).get(0);
            boolean isLeftBracket=bracket.getId()==TokenID.LEFT_BRACKET;

            float[] minMax=getMinMaxY(isLeftBracket?bracket:null,isLeftBracket?null:bracket);

            float minY =minMax[0];
            float maxY= minMax[1];

            bracket.height = maxY - minY;
            bracket.y = minY;

            for (ArrayList<Block> current:pos) {
                Block block=current.get(0);
                block.height=bracket.height;
                block.y=bracket.y;
                //build again the left bracket or right bracket (building the width of bracket based on the height chosen)
                block.build(setting,paint,textSize);
            }
        }

        //calculate
    }



    private static float[] getMinMaxY(Block start, Block end) {
        float minY = 0;
        float maxY = 0;

        if (start != null && end != null) {
            //calculate minY and maxY between start and end

            if (start.next() == end) {
                throw new RuntimeException("there most be at least one block between start and end");
            }

            minY = start.y;
            maxY = start.y + start.height;

            while (start != end) {
                maxY = maxY < start.y + start.height ? start.y + start.height : maxY;
                minY = minY > start.y ? start.y : minY;
                start = start.next();
            }

            return new float[]{minY, maxY};

        } else if (start != null) {
            minY = start.y;
            maxY = start.y + start.height;

            while (start != null) {
                maxY = maxY < start.y + start.height ? start.y + start.height : maxY;
                minY = minY > start.y ? start.y : minY;
                start = start.next();
            }

        } else if (end != null) {

            minY = end.y;
            maxY = end.y + end.height;

            while (end != null) {
                maxY = maxY < end.y + end.height ? end.y + end.height : maxY;
                minY = minY > end.y ? end.y : minY;
                end = end.before();
            }


        }

        return new float[]{minY, maxY};
    }


    /**
     * @param blockContainer
     * @return every left bracket block with its right bracket block (in case any left right or left bracket without the other ,just return it alone)
     */
    private static ArrayList<ArrayList<Block>> getBrackets(BlockContainer blockContainer) {
        ArrayList<ArrayList<Block>> pos = new ArrayList<>();

        Stack<Block> stack = new Stack<>();

        for (int i = 0; i < blockContainer.getChildCount(); i++) {
            Block b = blockContainer.getChild(i);
            if ((b.getId() == TokenID.LEFT_BRACKET)) {
                stack.push(b);
            } else if (b.getId() == TokenID.RIGHT_BRACKET) {
                ArrayList<Block> element = new ArrayList<>();
                pos.add(element);
                //remove last bracket from stack if exists
                if (!stack.empty()) {
                    element.add(stack.pop());
                }
                element.add(b);
            }
        }

        while (!stack.empty()){
            ArrayList<Block> element = new ArrayList<>();
            element.add(stack.pop());
            pos.add(element);
        }

        return pos;
    }


}
