$(document).ready(function () {
    function stateChange() {
        if (state === 'startLoop') {
            setTimeout(startLoop, 100);
        } else if (state === 'gameLoop') {
            setTimeout(gameLoop, 3000.0/(30.0+score));
        }
    }

    function startLoop() {
        if (state === 'startLoop') {
            ctx.fillStyle = flashColors[idx++ % 6];
            ctx.fillText("Paina ENTER aloittaaksesi", 50, 200);
            stateChange();
        }
    }

    function addFruit() {
        var x = 0, y = 0;
        fruitCandidate = [x, y];
        while (indexOfNode(fruitCandidate, brickList) > -1 ||
                indexOfNode(fruitCandidate, wormList) > -1 ||
                indexOfNode(fruitCandidate, fruitList) > -1) {
            x = Math.floor(1 + Math.random() * 46);
            y = Math.floor(1 + Math.random() * 46);
            fruitCandidate = [x, y];
        }
        fruitList.push([x, y]);
    }

    function initGame() {
        brickList = [];
        fruitList = [];
        bonusFruitList = [];
        for (var i = 0; i < 48; i++) {
            brickList.push([i, 0]);
            brickList.push([i, 47]);
            if ((i > 0) && (i < 47)) {
                brickList.push([0, i]);
                brickList.push([47, i]);
            }
        }
        wormList = [[24, 24]];
        direction = 'up';
        lengthen = 2;
        score = 0;
        addFruit();
    }

    function indexOfNode(node, list) {
        for (var i = 0; i < list.length; i++) {
            if (node[0] === list[i][0] && node[1] === list[i][1]) {
                return i;
            }
        }
        return -1;
    }

    function gameOver() {
        state = 'startLoop';
    }

    function removeExtraBonusFruits() {
        for (var i = 0; i < bonusFruitList.length; i++) {
            if (indexOfNode(bonusFruitList[i], wormList) === -1) {
                bonusFruitList.splice(i, 1);
            }
        }
    }

    function gameLoop() {
        if (state === 'gameLoop') {
            ctx.fillStyle = "#000000";
            ctx.fillRect(0, 0, 480, 510);
            ctx.fillStyle = "#ff0000";
            for (var i = 0; i < brickList.length; i++) {
                ctx.fillRect(10 * brickList[i][0],
                        10 * brickList[i][1], 10, 10);
            }
            var wormHead = [wormList[wormList.length - 1][0],
                wormList[wormList.length - 1][1]];
            switch (direction) {
                case 'left':
                    wormHead[0]--;
                    break;
                case 'up':
                    wormHead[1]--;
                    break;
                case 'right':
                    wormHead[0]++;
                    break;
                case 'down':
                    wormHead[1]++;
                    break;
            }
            wormList.push(wormHead);
            if (lengthen > 0) {
                lengthen--;
            } else {
                wormList.shift();
            }
            ctx.fillStyle = "#00ff00";
            for (var i = 0; i < wormList.length; i++) {
                ctx.beginPath();
                ctx.arc(10 * wormList[i][0] + 5, 10 * wormList[i][1] + 5,
                        5, 0, 2 * Math.PI);
                ctx.fill();
            }
            ctx.fillStyle = "#007fff";
            for (var i = 0; i < fruitList.length; i++) {
                ctx.beginPath();
                ctx.arc(10 * fruitList[i][0] + 5, 10 * fruitList[i][1] + 5,
                        5, 0, 2 * Math.PI);
                ctx.fill();
            }

            for (var i = 0; i < bonusFruitList.length; i++) {
                ctx.fillStyle = flashColors[(idx++ % 6) + i];
                ctx.beginPath();
                ctx.arc(10 * bonusFruitList[i][0] + 5, 10 * bonusFruitList[i][1] + 5,
                        5, 0, 2 * Math.PI);
                ctx.fill();
            }
            
            ctx.fillStyle = "#ffffff";
            ctx.fillText("Pisteet: "+score, 10, 504);

            if (indexOfNode(wormHead, brickList) > -1) {
                gameOver();
            }

            var bonusFruitIdx = indexOfNode(wormHead, bonusFruitList);
            var wormHeadIdx = indexOfNode(wormHead, wormList);
            if (bonusFruitIdx > -1) {
                addFruit();
                lengthen += 2;
                score++;
                bonusFruitList.splice(bonusFruitIdx, 1);
            } else if ((wormHeadIdx > -1) &&
                    (wormHeadIdx !== (wormList.length - 1))) {
                gameOver();
            }

            var fruitIdx = indexOfNode(wormHead, fruitList);
            if (fruitIdx > -1) {
                addFruit();
                lengthen += 2;
                score++;
                bonusFruitList.push([fruitList[fruitIdx][0],
                    fruitList[fruitIdx][1]]);
                fruitList.splice(fruitIdx, 1);
            }

            removeExtraBonusFruits();
            stateChange();
        }
    }

    $(document).keydown(function (e) {
        $('#kd').html(e.which);
        switch (state) {
            case 'startLoop':
                if (e.which === 13) {
                    $('#game').fadeOut(1000, function () {
                        state = 'x';
                        ctx.fillStyle = "#000000";
                        ctx.fillRect(0, 0, 480, 510);
                        initGame();
                        $('#game').fadeIn(1000, function () {
                            state = 'gameLoop';
                            stateChange();
                        });
                    });
                }
                break;
            case 'gameLoop':
                if (e.which === 37 && direction !== 'right') {
                    direction = 'left';
                } else if (e.which === 38 && direction !== 'down') {
                    direction = 'up';
                } else if (e.which === 39 && direction !== 'left') {
                    direction = 'right';
                } else if (e.which === 40 && direction !== 'up') {
                    direction = 'down';
                }
                break;
        }
    });

    $(document).keyup(function (e) {
        $('#ku').html(e.which);
    });

    var flashColors = ["#ff0000", "#ff7f00", "#ffff00",
        "#00ff00", "#0000ff", "#ff00ff"];
    var cnv = $('#game')[0];
    var ctx = cnv.getContext("2d");
    ctx.font = "25px Monospace";
    var idx = 0;
    var state = 'startLoop';
    var brickList, wormList, fruitList, bonusFruitList, direction, lengthen,
            score=0;
    stateChange();
});