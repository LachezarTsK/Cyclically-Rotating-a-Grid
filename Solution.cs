
using System;

public class Solution
{
    class Layer
    {
        public int Row;
        public int Column;
    }

    private readonly Layer source = new();
    private readonly Layer rotated = new();

    private int cyclicRotations;
    private int[][]? rotatedMatrix;
    public int[][] RotateGrid(int[][] inputMatrix, int cyclicRotations)
    {
        int rows = inputMatrix.Length;
        int columns = inputMatrix[0].Length;

        this.cyclicRotations = cyclicRotations;
        rotatedMatrix = new int[rows][];
        for (int row = 0; row < rows; ++row)
        {
            rotatedMatrix[row] = new int[columns];
        }

        int limit = Math.Min(rows / 2, columns / 2);
        int startSource = 0;
        int shrinkSide = 0;

        while (startSource < limit)
        {
            RotateLayer(startSource, rows - shrinkSide, columns - shrinkSide, inputMatrix);
            ++startSource;
            shrinkSide += 2;
        }
        return rotatedMatrix;
    }

    private void RotateLayer(int startSourceInLayer, int rowsInLayer, int columnsInLayer, int[][] inputMatrix)
    {
        int numberOfElementsInLayer = 2 * rowsInLayer + 2 * (columnsInLayer - 2);
        int rotationsInLayer = cyclicRotations % (numberOfElementsInLayer);

        source.Row = startSourceInLayer;
        source.Column = startSourceInLayer;
        PlaceRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer);

        while (numberOfElementsInLayer > 0)
        {
            UpdatePosition(source, startSourceInLayer, rowsInLayer, columnsInLayer);
            UpdatePosition(rotated, startSourceInLayer, rowsInLayer, columnsInLayer);
            rotatedMatrix![rotated.Row][rotated.Column] = inputMatrix[source.Row][source.Column];
            --numberOfElementsInLayer;
        }
    }

    private void PlaceRotatedAtStartForNextLayer(int startSourceInLayer, int rotationsInLayer, int rowsInLayer, int columnsInLayer)
    {
        // next start position is on top side
        if (rotationsInLayer >= 2 * rowsInLayer + columnsInLayer - 2)
        {
            rotated.Row = startSourceInLayer;
            rotated.Column = startSourceInLayer + columnsInLayer - 1 - (rotationsInLayer + 1 - (2 * rowsInLayer + columnsInLayer - 2));
            return;
        }

        // next start position is on right side
        if (rotationsInLayer >= rowsInLayer + columnsInLayer - 1)
        {
            rotated.Row = startSourceInLayer + rowsInLayer - 1 - (rotationsInLayer + 1 - (rowsInLayer + columnsInLayer - 1));
            rotated.Column = startSourceInLayer + columnsInLayer - 1;
            return;
        }

        // next start position is on bottom side
        if (rotationsInLayer >= rowsInLayer)
        {
            rotated.Row = startSourceInLayer + rowsInLayer - 1;
            rotated.Column = startSourceInLayer + ((rotationsInLayer + 1 - rowsInLayer));
            return;
        }

        // next start position is on left side
        rotated.Row = startSourceInLayer + rotationsInLayer;
        rotated.Column = startSourceInLayer;
    }

    private void UpdatePosition(Layer layer, int startSourceInLayer, int rowsInLayer, int columnsInLayer)
    {
        // position before update is on left side
        if (layer.Row < startSourceInLayer + rowsInLayer - 1 && layer.Column == startSourceInLayer)
        {
            ++layer.Row;
            return;
        }

        // position before update is on bottom side
        if (layer.Row == startSourceInLayer + rowsInLayer - 1 && layer.Column < startSourceInLayer + columnsInLayer - 1)
        {
            ++layer.Column;
            return;
        }

        // position before update is on right side
        if (layer.Row > startSourceInLayer && layer.Column == startSourceInLayer + columnsInLayer - 1)
        {
            --layer.Row;
            return;
        }

        // position before update is on top side
        if (layer.Row == startSourceInLayer && layer.Column > startSourceInLayer)
        {
            --layer.Column;
        }
    }
}
