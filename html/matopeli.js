$(document).ready(function () {
    function stateChange() {
        if (state === 'startLoop') {
            setTimeout(startLoop, 100);
        } else if (state === 'gameLoop') {
            setTimeout(gameLoop, 100);
        }
    }

    function startLoop() {
        if (state === 'startLoop') {
            ctx.fillStyle = flashColors[idx++ % 6];
            ctx.fillText("Paina ENTER aloittaaksesi", 50, 200);
            stateChange();
        }
    }

    function initGame() {
        brickList = [];
        for (var i = 0; i < 48; i++) {
            brickList.push([i, 0]);
            brickList.push([i, 47]);
            if ((i > 0) && (i < 47)) {
                brickList.push([0, i]);
                brickList.push([47, i]);
            }
        }
    }

    function gameLoop() {
        if (state === 'gameLoop') {
            ctx.fillStyle = "#000000";
            ctx.fillRect(0, 0, 480, 510);
            ctx.fillStyle = "#7f0000";
            for (var i = 0; i < brickList.length; i++) {
                ctx.fillRect(10 * brickList[i][0], 10 * brickList[i][1], 10, 10);
            }
            stateChange();
        }
    }

    $(document).keydown(function (e) {
        $('#kd').html(e.which);
        if (e.which === 13) {
            $('#game').fadeOut(1000);
            ctx.fillStyle = "#000000";
            ctx.fillRect(0, 0, 480, 510);
            initGame();
            $('#game').fadeIn(1000);
            state = 'gameLoop';
            stateChange();
        }
    });

    $(document).keyup(function (e) {
        $('#ku').html(e.which);
    });

    var flashColors = ["#ff0000", "#ff7f00", "#ffff00", "#00ff00", "#0000ff", "#ff00ff"];
    var cnv = $('#game')[0];
    var ctx = cnv.getContext("2d");
    ctx.font = "25px Monospace";
    var idx = 0;
    var state = 'startLoop';
    var brickList;
    stateChange();
});