function roll(values, id) {
    var result = values[Math.floor(Math.random()*values.length)];
    document.getElementById(id).innerHTML = result;
}