package ha.drawing.experssionBlocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.List;

import ha.drawing.Setting;


/**
 * Created by hassan on 12/26/2016.
 */
public class MatrixBlock extends Block {
    List<BlockContainer> rows = new ArrayList<>();
    boolean centerColumn = true;
    BracketBlock leftBracket, rightBracket;

    private boolean disableBracket = false;



    public MatrixBlock() {
        leftBracket = new LeftBracketBlock();
        rightBracket = new RightBracketBlock();
    }

    /*FIXME: 1/20/2017 :if we add division inside a matrix block of one row and touched near the division block ,an empty block is added automatically to left or right of matrix
     which is wrong, we need to take in consideration the left and right bracket as well
     */


    @Override
    public BlockID getId() {
        return BlockID.MATRIX;
    }

    @Override
    public void onDraw(Canvas c, Paint paint, float offsetX, float offsetY) {

        for (BlockContainer row : rows) {
            row.draw(c, paint, offsetX + x, offsetY + y);
        }

        if (!disableBracket) {
            leftBracket.draw(c, paint, offsetX + x, offsetY + y);
            rightBracket.draw(c, paint, offsetX + x, offsetY + y);
        }
        Paint border = new Paint();
        border.setStrokeWidth(5);
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.BLACK);


        c.drawRect(x + offsetX, y + offsetY, x + offsetX, y + offsetY, border);
    }


    @Override
    public float getBaseLine() {
        return (height) / 2;
    }

    @Override
    public String show() {
        String equation = "matrix(";
        for (BlockContainer row : rows) {
            equation += "[";
            for (Block col : row.getChildren()) {
                equation += col.show() + "&";
            }
            equation = equation.substring(0, equation.length() - 1);
            equation += "]";
        }
        equation += ")";
        return equation;
    }


    public List<BlockContainer> getRows() {
        return rows;
    }

    /*add new row of empty col for each col*/
    public void addRow() {
        BlockContainer row = new BlockContainer();
        for (int i = 0; i < getColCount(); i++) {
            BlockContainer col = new BlockContainer();
            col.addChild(new EmptyBlock());
            row.addChild(col);
        }
        this.rows.add(row);
    }


    public void addRow(BlockContainer row) {
        this.rows.add(row);
    }

    public BlockContainer getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public BlockContainer getCol(int rowIndex, int colIndex) {
        return (BlockContainer) getRow(rowIndex).getChild(colIndex);
    }

    public BlockContainer getLastRow() {
        return rows.get(rows.size() - 1);
    }

    public void addCol(int rowIndex) {
        this.rows.get(rowIndex).addChild(new BlockContainer());
    }


    @Override
    protected void measure(Setting setting, Paint paint, float textSize) {
        if (rows.size() == 1 && rows.get(0).getChildren().size() == 2) {
            int r = 0;
            r++;
        }

        //loop through all rows
        for (BlockContainer row : rows) {
            row.y = 0;
            row.setParent(this);
            //for each column of each row build its block dimension (all of them at beginning will be at origin x and y equal 0
            for (Block col : row.getChildrens()) {
                col.y = 0;
                col.build(setting, paint, textSize);
                col.setParent(row);
            }
        }


        float scale = textSize / setting.DefaultTextSize;
        float shift = scale * setting.TEXT_SPACING;


        /*calculating the y and x positiions of rows and cols ,also the width and height of this*/
        buildVerticalDimensiopn(setting, paint, textSize);
        buildHoriznetalDimensiopn(setting, paint, textSize);
        /**************************************************************************************/


        /*if true means we don't want to drawer brackets*/
        if (!disableBracket) {
            {
        /*building height and width of the left and rightbracket*/
                leftBracket.height = rows.get(rows.size() - 1).y + rows.get(rows.size() - 1).height;
                rightBracket.height = rows.get(rows.size() - 1).y + rows.get(rows.size() - 1).height;
                leftBracket.build(setting, paint, textSize);
                rightBracket.build(setting, paint, textSize);
                /**********************************************************/
            }
        /*reshift the position of rows based on leftbracket */
            for (BlockContainer row : rows) {
                row.x = leftBracket.x + leftBracket.width;
            }
            rightBracket.x = rows.get(0).x + rows.get(0).width;
            this.width = rightBracket.x + rightBracket.width;
        }


//        this.rectF=new RectF(-shift*2,-shift,this.width+shift*2,this.height+shift);
//        for (BlockContainer row : rows) {
//            row.rectF=new RectF(-shift*2,-shift,row.width+shift*2,row.height+shift);
//            for(Block col:row.getChildren()){
//                col.rectF=new RectF(-shift*2,-shift,col.width+shift*2,col.height+shift);
//            }
//        }
    }

    public void setCenterColumn(boolean centerColumn) {
        this.centerColumn = centerColumn;
    }


    private void buildVerticalDimensiopn(Setting setting, Paint paint, float textSize) {


        //adding margin top and bottom
        float Matrix_TopBottom_margin = setting.TopBottomOffsetY;

        float offsetRow = setting.offsetRowY;
        float currentY = Matrix_TopBottom_margin;


        //now shift y positions and set the row height
        for (BlockContainer row : rows) {
            float maxHeight = 0;
            row.y = currentY;
            for (Block col : row.getChildrens()) {
                maxHeight = maxHeight < col.height + col.y ? col.height + col.y : maxHeight;
            }
            row.height = maxHeight;
            currentY += offsetRow + maxHeight;
        }
        //the height of matrix is the y position of last block plus its height
        Block lastrow = rows.get(rows.size() - 1);
        this.height = lastrow.y + lastrow.height;

//        for (BlockContainer row : rows) {
//            for (Block col : row.getChildrens()) {
//                buildPowerBracketHeight(col);
//            }
//        }


        this.height += Matrix_TopBottom_margin;


        /*recenter the y position for each col in every row based on the biggest column height*/
        for (BlockContainer row : rows) {
            float maxHeight = 0;
            for (Block col : row.getChildren()) {
                if (maxHeight < col.height) {
                    maxHeight = col.height;
                }
            }

            for (Block col : row.getChildren()) {
                col.y = maxHeight / 2 - col.height / 2;
            }
        }
    }


    private void buildHoriznetalDimensiopn(Setting setting, Paint paint, float textSize) {

//99  48

        float offsetCol = setting.offsetColX;


        int index = 0;
        float[] maxColWidth = new float[rows.get(0).getChildrens().size()];
        float[] startX = new float[maxColWidth.length];

        //getting the max width of each col
        for (BlockContainer row : rows) {

            for (Block col : row.getChildrens()) {
                if (maxColWidth[index] < col.width) {
                    maxColWidth[index] = col.width;
                    startX[index] = col.x;
                }
                // maxColWidth[index]=maxColWidth[index]<col.width?col.width:maxColWidth[index];
                index++;
            }
            index = 0;
        }


        /****************setting x positions of col*****************/
        for (BlockContainer row : rows) {
            float currentX = 0;
            for (Block col : row.getChildrens()) {
                col.x = currentX;
                currentX += maxColWidth[index];
                index++;
            }
            index = 0;
        }
        for (BlockContainer row : rows) {
            for (Block col : row.getChildrens()) {
                if (centerColumn) //if true means center the column according to max width of all columns
                    col.x = col.x + maxColWidth[index] / 2 - col.width / 2;

                col.x += (index * offsetCol);
                index++;
            }
            row.width = row.getChildrens().get(row.getChildrens().size() - 1).x + row.getChildrens().get(row.getChildrens().size() - 1).width;
            index = 0;

        }
        /****************setting x positions of col*****************/

        float maxWidth = 0;

        // getting maximum row width
        for (Block row : rows) {
            maxWidth = maxWidth < row.width ? row.width : maxWidth;
        }
        for (Block row : rows) {
            row.width = maxWidth;
        }

        this.width = maxWidth;
    }

    /*return the row index if caller is a column*/
    private int checkIfCallerIsColumn(Block caller) {
        for (BlockContainer row : this.rows) {
            if (row.getChildren().indexOf(caller) != -1) {
                return this.rows.indexOf(row);
            }
        }
        return -1;
    }


    public void removeLastRow() {
        removeRow(this.rows.size() - 1);
    }

    private void removeRow(int i) {
        this.rows.remove(i);
    }

    public void disableBrackets() {
        this.disableBracket = true;
    }

    /*add a column in every row */
    public void addColInEveryRow() {
        for (BlockContainer row : this.rows) {
            BlockContainer col = new BlockContainer();
            col.addChild(new EmptyBlock());
            row.addChild(col);
        }
    }

    public int getColCount() {
        return this.rows.get(0).size();
    }

    public void removeCols() {
        for (BlockContainer row : this.rows) {
            row.removeLastChild();
        }
    }

    /*return the column where the child block is in matrix and null if not in any*/
    public BlockContainer getColumnOfBlock(Block child) {
        Block parentCol = child;
        while (parentCol != null) {
            if (parentCol.parent != null && parentCol.parent.parent != null && parentCol.parent.parent instanceof MatrixBlock) {
                return (BlockContainer) parentCol;
            } else {
                parentCol = parentCol.parent;
            }
        }

        return null;

    }

}
