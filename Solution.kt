
import kotlin.math.min

class Solution {

    private data class Layer(var row: Int, var column: Int)

    private val source = Layer(0, 0)
    private val rotated = Layer(0, 0)

    private var cyclicRotations = 0
    private lateinit var rotatedMatrix: Array<IntArray>

    fun rotateGrid(inputMatrix: Array<IntArray>, cyclicRotations: Int): Array<IntArray> {
        val rows = inputMatrix.size
        val columns = inputMatrix[0].size

        this.cyclicRotations = cyclicRotations
        this.rotatedMatrix = Array(rows) { IntArray(columns) }

        val limit = min(rows / 2, columns / 2)
        var startSource = 0
        var shrinkSide = 0

        while (startSource < limit) {
            rotateLayer(startSource, rows - shrinkSide, columns - shrinkSide, inputMatrix)
            ++startSource
            shrinkSide += 2
        }
        return rotatedMatrix
    }

    private fun rotateLayer(startSourceInLayer: Int, rowsInLayer: Int, columnsInLayer: Int, inputMatrix: Array<IntArray>) {
        var numberOfElementsInLayer = 2 * rowsInLayer + 2 * (columnsInLayer - 2)
        val rotationsInLayer = cyclicRotations % (numberOfElementsInLayer)

        source.row = startSourceInLayer
        source.column = startSourceInLayer
        placeRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer)

        while (numberOfElementsInLayer > 0) {
            updatePosition(source, startSourceInLayer, rowsInLayer, columnsInLayer)
            updatePosition(rotated, startSourceInLayer, rowsInLayer, columnsInLayer)
            rotatedMatrix[rotated.row][rotated.column] = inputMatrix[source.row][source.column]
            --numberOfElementsInLayer
        }
    }

    private fun placeRotatedAtStartForNextLayer(startSourceInLayer: Int, rotationsInLayer: Int, rowsInLayer: Int, columnsInLayer: Int) {
        // next start position is on top side
        if (rotationsInLayer >= 2 * rowsInLayer + columnsInLayer - 2) {
            rotated.row = startSourceInLayer
            rotated.column =
                startSourceInLayer + columnsInLayer - 1 - (rotationsInLayer + 1 - (2 * rowsInLayer + columnsInLayer - 2))
            return
        }

        // next start position is on right side
        if (rotationsInLayer >= rowsInLayer + columnsInLayer - 1) {
            rotated.row =
                startSourceInLayer + rowsInLayer - 1 - (rotationsInLayer + 1 - (rowsInLayer + columnsInLayer - 1))
            rotated.column = startSourceInLayer + columnsInLayer - 1
            return
        }

        // next start position is on bottom side
        if (rotationsInLayer >= rowsInLayer) {
            rotated.row = startSourceInLayer + rowsInLayer - 1
            rotated.column = startSourceInLayer + ((rotationsInLayer + 1 - rowsInLayer))
            return
        }

        // next start position is on left side
        rotated.row = startSourceInLayer + rotationsInLayer
        rotated.column = startSourceInLayer
    }

    private fun updatePosition(layer: Layer, startSourceInLayer: Int, rowsInLayer: Int, columnsInLayer: Int) {
        // position before update is on left side
        if (layer.row < startSourceInLayer + rowsInLayer - 1 && layer.column == startSourceInLayer) {
            ++layer.row
            return
        }

        // position before update is on bottom side
        if (layer.row == startSourceInLayer + rowsInLayer - 1 && layer.column < startSourceInLayer + columnsInLayer - 1) {
            ++layer.column
            return
        }

        // position before update is on right side
        if (layer.row > startSourceInLayer && layer.column == startSourceInLayer + columnsInLayer - 1) {
            --layer.row
            return
        }

        // position before update is on top side
        if (layer.row == startSourceInLayer && layer.column > startSourceInLayer) {
            --layer.column
        }
    }
}
