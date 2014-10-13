function model() {
    var state=0, high_score=0;
    var score, wormNodes, fruits, bonusFruits;
}

model.prototype.initGame = function() {
    wormNodes=[[8,8]];
    fruits=[];
    bonusFruits=[];
    score=0;
};

function keydown(e) {
    
}

function keyup(e) {
    
}

var flashColors=["#ff0000", "#ff7f00", "#ffff00", "#00ff00", "#0000ff", "#ff00ff"];
var cnv=document.getElementById('game');
var ctx=cnv.getContext("2d");
ctx.fillStyle="#ffffff";
ctx.font = "25px Monospace";
var idx=0;

// Alkuruutu
setInterval(function() {
    ctx.fillStyle=flashColors[idx++%6];
    ctx.fillText("Paina ENTER aloittaaksesi",50,200);
},100);

// Peli