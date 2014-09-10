var map;
var skiers = [];
var markers = [];
var iterator = 0;
$.ajax({
    dataType: "json",
    url: "http://whitetavros.com/Sandbox/web/app_dev.php/internalApi/skier/getAllPosition",
    success: function(data, textStatus, jqXHR) {
        data.payload.forEach(function(skier) {
            skiers.push(new google.maps.LatLng(parseFloat(skier.coor_X), parseFloat(skier.coor_Y)));
        });
        drop();
    }
});


function initialize() {
    var centerLatlng = new google.maps.LatLng(parseFloat($('#x_coordinate').text()), parseFloat($('#y_coordinate').text()));
    var mapOptions = {
        zoom: 10,
        center: centerLatlng,
        mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    map = new google.maps.Map(document.getElementById('center-map'),
            mapOptions);
}

function drop() {
    for (var i = 0; i < skiers.length; i++) {
        setTimeout(function() {
            addMarker();
        }, i * 200);
    }
}

function addMarker() {
    markers.push(new google.maps.Marker({
        position: skiers[iterator],
        map: map,
        draggable: false,
        animation: google.maps.Animation.DROP
    }));
    iterator++;
}

google.maps.event.addDomListener(window, 'load', initialize);

$(document).ready(function() {

});
