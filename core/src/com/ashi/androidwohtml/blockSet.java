package com.ashi.androidwohtml;

/**
 * Created by Andy on 2018-02-24.
 */

public class blockSet {
    private int leftMostBlock, rightMostBlock;

    public blockSet(int leftMostBlock, int rightMostBlock)
    {
        this.leftMostBlock = leftMostBlock;
        this.rightMostBlock = rightMostBlock;
    }

    public int getLeftMostBlock()
    {
        return leftMostBlock;
    }

    public int getRightMostBlock()
    {
        return rightMostBlock;
    }

    public void moveBlock(direction dir)
    {
        if(dir == direction.LEFT)
        {
            leftMostBlock--;
            rightMostBlock--;
        }
        else
        {
            leftMostBlock++;
            rightMostBlock++;

        }

    }
}
