package ha.drawing;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;


import ha.drawing.experssionBlocks.BaseBlock;
import ha.drawing.experssionBlocks.Block;
import ha.drawing.experssionBlocks.BlockContainer;
import ha.drawing.experssionBlocks.BracketBlock;
import ha.drawing.experssionBlocks.DivisionBlock;
import ha.drawing.experssionBlocks.EmptyBlock;
import ha.drawing.experssionBlocks.LeftBracketBlock;
import ha.drawing.experssionBlocks.MatrixBlock;
import ha.drawing.experssionBlocks.PowerBlock;
import ha.drawing.experssionBlocks.RadicalBlock;
import ha.drawing.experssionBlocks.TextBlock;


/**
 * Created by houssam on 1/15/2017.
 */
public class BlockFactory {

    public static final int MATRIX_INVERSE=0;//for a matrix power inverse
    public static final int MATRIX_TRANSPOSE=1;//for a matrix power tranpose
    public static final int MATRIX_POWER=2;//for a matrix power Empty block
    public static final int TEXT=3;//like 1,2,3... sin cos
    public static final int LEFT_BRACKET=3;//for (


    public static List<Block> addBlock(int blockID){
        switch (blockID){
            case MATRIX_POWER:return addMatrixPowerEmptyBlock(2,2);
            case MATRIX_INVERSE:return addMatrixInverseBlock(2,2);
            case MATRIX_TRANSPOSE:return addMatrixTransposeBlock(2,2);
        }
        return null;
    }

    /*return $/$ block*/
    public static Block emptyDivisionBlock(){
     DivisionBlock divisionBlock=new DivisionBlock();
     divisionBlock.getDenominators().addChild(new EmptyBlock());
     divisionBlock.getNumerators().addChild(new EmptyBlock());
        return divisionBlock;
    }

    //return √$ block
    public static Block emptyRadicalBlock(){
        RadicalBlock radicalBlock=new RadicalBlock();
        radicalBlock.getInsideRadical().addChild(new EmptyBlock());
        return radicalBlock;
    }



    //return √x
    public static Block radicalX(){
        RadicalBlock radicalBlock=new RadicalBlock();
        radicalBlock.getInsideRadical().addChild(new TextBlock("x"));
        return radicalBlock;
    }

    //return x^2
    public static Block xPower2(){
        PowerBlock powerBlock=new PowerBlock();
        powerBlock.getPower().addChild(new TextBlock("2"));
        return powerBlock;
    }


    public static Block baseBlock(String log){
        BaseBlock baseBlock=new BaseBlock(log);
        baseBlock.getBase().addChild(new EmptyBlock());
        return baseBlock;
    }

    public static Block baseBlock(String log,String text){
        BaseBlock baseBlock=new BaseBlock(log);
        baseBlock.getBase().addChild(new TextBlock(text));
        return baseBlock;
    }

    public static Block emptyBlock(){
        return new EmptyBlock();
    }


    public static Block leftBracket() {
        return new LeftBracketBlock();
    }

    public static Block addEmptyPowerBlock(){
        PowerBlock powerBlock=new PowerBlock();
        powerBlock.getPower().addChild(new EmptyBlock());
        return powerBlock;
    }



    public static MatrixBlock addMatrixBlock(int row, int col){
        MatrixBlock matrixBlock=new MatrixBlock();
        for(int i=0;i<row;i++){
            BlockContainer row1=new BlockContainer();
            for(int j=0;j<col;j++){
                BlockContainer column=new BlockContainer();
                row1.addChild(column);
                column.addChild(new EmptyBlock());
            }
            matrixBlock.addRow(row1);

        }
        return matrixBlock;
    }

    //return empty matrix power -1
    public static List<Block> addMatrixInverseBlock(int col, int row){
        List<Block> blocks=new ArrayList<>();
        Block matrixBlock=addMatrixBlock(col,row);
        PowerBlock powerBlock=new PowerBlock();
        powerBlock.getPower().addChild(new TextBlock("-1"));

        blocks.add(matrixBlock);
        blocks.add(powerBlock);
        return blocks;
   }


    //return empty matrix power T transpose
    public static List<Block> addMatrixTransposeBlock(int col, int row){
        List<Block> blocks=new ArrayList<>();
        Block matrixBlock=addMatrixBlock(col,row);
        PowerBlock powerBlock=new PowerBlock();
        powerBlock.getPower().addChild(new TextBlock("T"));

        blocks.add(matrixBlock);
        blocks.add(powerBlock);
        return blocks;
    }


    //return empty matrix power empty block
    public static List<Block> addMatrixPowerEmptyBlock(int col, int row){
        List<Block> blocks=new ArrayList<>();
        Block matrixBlock=addMatrixBlock(col,row);
        PowerBlock powerBlock=new PowerBlock();
        powerBlock.getPower().addChild(new EmptyBlock());

        blocks.add(matrixBlock);
        blocks.add(powerBlock);
        return blocks;

    }


    public static Block textBlock(String text){
        return new TextBlock(text );
    }





}
