
package main

type Layer struct {
    row    int
    column int
}

var source Layer
var rotated Layer

var cyclicRotations int
var rotatedMatrix [][]int

func rotateGrid(inputMatrix [][]int, cyclicRotations_ int) [][]int {
    rows := len(inputMatrix)
    columns := len(inputMatrix[0])

    source = Layer{}
    rotated = Layer{}

    cyclicRotations = cyclicRotations_
    rotatedMatrix = make([][]int, rows)
    for row := range rows {
        rotatedMatrix[row] = make([]int, columns)
    }

    limit := min(rows / 2, columns / 2)
    startSource := 0
    shrinkSide := 0

    for startSource < limit {
        rotateLayer(startSource, rows - shrinkSide, columns - shrinkSide, inputMatrix)
        startSource++
        shrinkSide += 2
    }
    return rotatedMatrix
}

func rotateLayer(startSourceInLayer int, rowsInLayer int, columnsInLayer int, inputMatrix [][]int) {
    numberOfElementsInLayer := 2 * rowsInLayer + 2 * (columnsInLayer - 2)
    rotationsInLayer := cyclicRotations % (numberOfElementsInLayer)

    source.row = startSourceInLayer
    source.column = startSourceInLayer
    placeRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer)

    for numberOfElementsInLayer > 0 {
        updatePosition(&source, startSourceInLayer, rowsInLayer, columnsInLayer)
        updatePosition(&rotated, startSourceInLayer, rowsInLayer, columnsInLayer)
        rotatedMatrix[rotated.row][rotated.column] = inputMatrix[source.row][source.column]
        numberOfElementsInLayer--
    }
}

func placeRotatedAtStartForNextLayer(startSourceInLayer int, rotationsInLayer int, rowsInLayer int, columnsInLayer int) {
    // next start position is on top side
    if rotationsInLayer >= 2 * rowsInLayer + columnsInLayer - 2 {
        rotated.row = startSourceInLayer
        rotated.column = startSourceInLayer + columnsInLayer - 1 - (rotationsInLayer + 1 - (2 * rowsInLayer + columnsInLayer - 2))
        return
    }

    // next start position is on right side
    if rotationsInLayer >= rowsInLayer+columnsInLayer-1 {
        rotated.row = startSourceInLayer + rowsInLayer - 1 - (rotationsInLayer + 1 - (rowsInLayer + columnsInLayer - 1))
        rotated.column = startSourceInLayer + columnsInLayer - 1
        return
    }

    // next start position is on bottom side
    if rotationsInLayer >= rowsInLayer {
        rotated.row = startSourceInLayer + rowsInLayer - 1
        rotated.column = startSourceInLayer + (rotationsInLayer + 1 - rowsInLayer)
        return
    }

    // next start position is on left side
    rotated.row = startSourceInLayer + rotationsInLayer
    rotated.column = startSourceInLayer
}

func updatePosition(layer *Layer, startSourceInLayer int, rowsInLayer int, columnsInLayer int) {
    // position before update is on left side
    if layer.row < startSourceInLayer + rowsInLayer - 1 && layer.column == startSourceInLayer {
        layer.row++
        return
    }

    // position before update is on bottom side
    if layer.row == startSourceInLayer + rowsInLayer - 1 && layer.column < startSourceInLayer + columnsInLayer - 1 {
        layer.column++
        return
    }

    // position before update is on right side
    if layer.row > startSourceInLayer && layer.column == startSourceInLayer + columnsInLayer - 1 {
        layer.row--
        return
    }

    // position before update is on top side
    if layer.row == startSourceInLayer && layer.column > startSourceInLayer {
        layer.column--
    }
}
