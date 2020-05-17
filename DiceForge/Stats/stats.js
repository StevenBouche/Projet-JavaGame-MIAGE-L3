let data;

const loadJSONGet = (callback) => {
    console.log(window.location.pathname);
    let xobj = new XMLHttpRequest();
    xobj.overrideMimeType("application/json");
    xobj.open('GET', 'statistique.json', true);
    xobj.onreadystatechange = () => {
        if (xobj.readyState === 4 && xobj.status === 200)  callback(xobj.responseText);
    };
    xobj.send(null);
};

/*const loadJS = () => {
    let src = document.getElementById('stat');
    console.log(src);
    let x = src.;
    console.log(x);
};*/

function printJsonInBody(id){
    console.log(id)
    let key;
    for(let elem in data) if(data[elem].id == id ) key = elem;
    console.log(key)
    if(key == null) return;
    let dataGame = data[key];

    console.log(document);
    document.getElementById('view-json').innerHTML = "";
    let conf = {
       // hoverPreviewEnabled: true,
        hoverPreviewArrayCount: 100,
        hoverPreviewFieldCount: 5,
        animateOpen: true,
        animateClose: true,
    };

    const formatter = new JSONFormatter(dataGame,5,conf);
    document.getElementById('view-json').appendChild(formatter.render());
}

function printJsonInBodyEvent(event){
    console.log(event)
    printJsonInBody(event.target.value);
}

function initView() {
    let select = document.getElementById('game-select');
    let opt;
    for(let elem in data){
        console.log(data[elem].id);
        opt = document.createElement('option');
        opt.appendChild( document.createTextNode('Game'+data[elem].id));
        opt.value = data[elem].id;
        select.appendChild(opt);
    }
    if(data[0] != null) printJsonInBody(data[0].id);
    select.addEventListener("change",printJsonInBodyEvent);
}

let setData = (json) => {
    console.log(json);
    data = JSON.parse(json);
    initView();
};

function init() {
    loadJSONGet(setData);
}

window.onload=init();