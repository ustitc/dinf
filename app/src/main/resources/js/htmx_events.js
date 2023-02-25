htmx.on('htmx:timeout', function(evt) {
    evt.detail.isError = false;
    alert("Website is a little tired and slow on response. Try again in a few minutes");
});

htmx.on('htmx:sendError', function(evt) {
    evt.detail.isError = false;
    alert("Something bad has happened. Website is feeling sick, give him some time");
});