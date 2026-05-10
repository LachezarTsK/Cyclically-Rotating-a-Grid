
public class Solution {

    class Layer {
        int row;
        int column;
    }

    private final Layer source = new Layer();
    private final Layer rotated = new Layer();

    private int cyclicRotations;
    private int[][] rotatedMatrix;

    public int[][] rotateGrid(int[][] inputMatrix, int cyclicRotations) {
        int rows = inputMatrix.length;
        int columns = inputMatrix[0].length;

        this.cyclicRotations = cyclicRotations;
        this.rotatedMatrix = new int[rows][columns];

        int limit = Math.min(rows / 2, columns / 2);
        int startSource = 0;
        int shrinkSide = 0;

        while (startSource < limit) {
            rotateLayer(startSource, rows - shrinkSide, columns - shrinkSide, inputMatrix);
            ++startSource;
            shrinkSide += 2;
        }
        return rotatedMatrix;
    }

    private void rotateLayer(int startSourceInLayer, int rowsInLayer, int columnsInLayer, int[][] inputMatrix) {
        int numberOfElementsInLayer = 2 * rowsInLayer + 2 * (columnsInLayer - 2);
        int rotationsInLayer = cyclicRotations % (numberOfElementsInLayer);

        source.row = startSourceInLayer;
        source.column = startSourceInLayer;
        placeRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer);

        while (numberOfElementsInLayer > 0) {
            updatePosition(source, startSourceInLayer, rowsInLayer, columnsInLayer);
            updatePosition(rotated, startSourceInLayer, rowsInLayer, columnsInLayer);
            rotatedMatrix[rotated.row][rotated.column] = inputMatrix[source.row][source.column];
            --numberOfElementsInLayer;
        }
    }

    private void placeRotatedAtStartForNextLayer(int startSourceInLayer, int rotationsInLayer, int rowsInLayer, int columnsInLayer) {
        // next start position is on top side
        if (rotationsInLayer >= 2 * rowsInLayer + columnsInLayer - 2) {
            rotated.row = startSourceInLayer;
            rotated.column = startSourceInLayer + columnsInLayer - 1 - (rotationsInLayer + 1 - (2 * rowsInLayer + columnsInLayer - 2));
            return;
        }

        // next start position is on right side
        if (rotationsInLayer >= rowsInLayer + columnsInLayer - 1) {
            rotated.row = startSourceInLayer + rowsInLayer - 1 - (rotationsInLayer + 1 - (rowsInLayer + columnsInLayer - 1));
            rotated.column = startSourceInLayer + columnsInLayer - 1;
            return;
        }

        // next start position is on bottom side
        if (rotationsInLayer >= rowsInLayer) {
            rotated.row = startSourceInLayer + rowsInLayer - 1;
            rotated.column = startSourceInLayer + ((rotationsInLayer + 1 - rowsInLayer));
            return;
        }

        // next start position is on left side
        rotated.row = startSourceInLayer + rotationsInLayer;
        rotated.column = startSourceInLayer;
    }

    private void updatePosition(Layer layer, int startSourceInLayer, int rowsInLayer, int columnsInLayer) {
        // position before update is on left side
        if (layer.row < startSourceInLayer + rowsInLayer - 1 && layer.column == startSourceInLayer) {
            ++layer.row;
            return;
        }

        // position before update is on bottom side
        if (layer.row == startSourceInLayer + rowsInLayer - 1 && layer.column < startSourceInLayer + columnsInLayer - 1) {
            ++layer.column;
            return;
        }

        // position before update is on right side
        if (layer.row > startSourceInLayer && layer.column == startSourceInLayer + columnsInLayer - 1) {
            --layer.row;
            return;
        }

        // position before update is on top side
        if (layer.row == startSourceInLayer && layer.column > startSourceInLayer) {
            --layer.column;
        }
    }
}
