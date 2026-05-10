
class Layer {
    row = 0;
    column = 0;
}

class Util {

    /**
     * @param {number} rows
     * @param {number} columns 
     * @param {number} cyclicRotations
     */
    constructor(rows, columns, cyclicRotations) {
        this.source = new Layer();
        this.rotated = new Layer();

        this.cyclicRotations = cyclicRotations;
        this.rotatedMatrix = Array.from(new Array(rows), () => new Array(columns).fill(0));
    }
}

let util;

/**
 * @param {number[][]} inputMatrix
 * @param {number} cyclicRotations
 * @return {number[][]}
 */
var rotateGrid = function (inputMatrix, cyclicRotations) {
    const rows = inputMatrix.length;
    const columns = inputMatrix[0].length;
    util = new Util(rows, columns, cyclicRotations);

    const limit = Math.min(Math.floor(rows / 2), Math.floor(columns / 2));
    let startSource = 0;
    let shrinkSide = 0;

    while (startSource < limit) {
        rotateLayer(startSource, rows - shrinkSide, columns - shrinkSide, inputMatrix);
        ++startSource;
        shrinkSide += 2;
    }
    return util.rotatedMatrix;
};

/**
 * @param {number} startSourceInLayer
 * @param {number} rowsInLayer 
 * @param {number} columnsInLayer
 * @param {number[][]} inputMatrix 
 * @return {void}
 */
function rotateLayer(startSourceInLayer, rowsInLayer, columnsInLayer, inputMatrix) {
    let numberOfElementsInLayer = 2 * rowsInLayer + 2 * (columnsInLayer - 2);
    let rotationsInLayer = util.cyclicRotations % (numberOfElementsInLayer);

    util.source.row = startSourceInLayer;
    util.source.column = startSourceInLayer;
    placeRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer);

    while (numberOfElementsInLayer > 0) {
        updatePosition(util.source, startSourceInLayer, rowsInLayer, columnsInLayer);
        updatePosition(util.rotated, startSourceInLayer, rowsInLayer, columnsInLayer);
        util.rotatedMatrix[util.rotated.row][util.rotated.column] = inputMatrix[util.source.row][util.source.column];
        --numberOfElementsInLayer;
    }
}

/**
 * @param {number} startSourceInLayer
 * @param {number} rotationsInLayer 
 * @param {number} rowsInLayer 
 * @param {number} columnsInLayer
 * @return {void}
 */
function placeRotatedAtStartForNextLayer(startSourceInLayer, rotationsInLayer, rowsInLayer, columnsInLayer) {
    // next start position is on top side
    if (rotationsInLayer >= 2 * rowsInLayer + columnsInLayer - 2) {
        util.rotated.row = startSourceInLayer;
        util.rotated.column = startSourceInLayer + columnsInLayer - 1 - (rotationsInLayer + 1 - (2 * rowsInLayer + columnsInLayer - 2));
        return;
    }

    // next start position is on right side
    if (rotationsInLayer >= rowsInLayer + columnsInLayer - 1) {
        util.rotated.row = startSourceInLayer + rowsInLayer - 1 - (rotationsInLayer + 1 - (rowsInLayer + columnsInLayer - 1));
        util.rotated.column = startSourceInLayer + columnsInLayer - 1;
        return;
    }

    // next start position is on bottom side
    if (rotationsInLayer >= rowsInLayer) {
        util.rotated.row = startSourceInLayer + rowsInLayer - 1;
        util.rotated.column = startSourceInLayer + ((rotationsInLayer + 1 - rowsInLayer));
        return;
    }

    // next start position is on left side
    util.rotated.row = startSourceInLayer + rotationsInLayer;
    util.rotated.column = startSourceInLayer;
}

/**
 * @param {Layer} layer
 * @param {number} startSourceInLayer 
 * @param {number} rowsInLayer 
 * @param {number} columnsInLayer
 * @return {void}
 */
function updatePosition(layer, startSourceInLayer, rowsInLayer, columnsInLayer) {
    // position before update is on left side
    if (layer.row < startSourceInLayer + rowsInLayer - 1 && layer.column === startSourceInLayer) {
        ++layer.row;
        return;
    }

    // position before update is on bottom side
    if (layer.row === startSourceInLayer + rowsInLayer - 1 && layer.column < startSourceInLayer + columnsInLayer - 1) {
        ++layer.column;
        return;
    }

    // position before update is on right side
    if (layer.row > startSourceInLayer && layer.column === startSourceInLayer + columnsInLayer - 1) {
        --layer.row;
        return;
    }

    // position before update is on top side
    if (layer.row === startSourceInLayer && layer.column > startSourceInLayer) {
        --layer.column;
    }
}
