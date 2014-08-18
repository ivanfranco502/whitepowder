var map;
function initialize() {
    var mapOptions = {
        zoom: 14,
        center: new google.maps.LatLng(parseFloat($('#x_coordinate').text()),
                parseFloat($('#y_coordinate').text())),
        mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    map = new google.maps.Map(document.getElementById('center-map'),
            mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);

$(document).ready(

        );