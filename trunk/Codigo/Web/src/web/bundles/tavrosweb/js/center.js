var map;
var skiers = [];
var markers = {};
var iterator = 0;
var interval = 2000;

function getMarkers() {

    $.ajax({
        dataType: "json",
        url: "http://whitetavros.com/Sandbox/web/app_dev.php/internalApi/skier/getAll",
        success: function (data, textStatus, jqXHR) {
            data.payload.forEach(function (skier) {
                skiers.push({
                    id: skier.id,
                    name: skier.username,
                    position: new google.maps.LatLng(parseFloat(skier.position.coor_X), parseFloat(skier.position.coor_Y))
                });
            });
            setMarkers(map, skiers);
            skiers = [];
            window.setTimeout(getMarkers, interval);
        }
    });
}

function initialize() {
    getMarkers();
    var centerLatlng = new google.maps.LatLng(parseFloat($('#x_coordinate').text()), parseFloat($('#y_coordinate').text()));
    var mapOptions = {
        zoom: 4,
        center: centerLatlng,
        mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    map = new google.maps.Map(document.getElementById('center-map'),
            mapOptions);

}

function setMarkers(map, locations) {

    var marker, i;

    // REFRESH MARKERS    
    for (var index in markers) {
        var found = 0;
        for (k = 0; k < locations.length; k++) {
            if (index === locations[k].name) {
                found = 1;
                break;
            } else {
                found = 0;
            }
        }
        if (found === 0) {
            markers[index].setMap(null);
            delete markers[index];
        }
    }

    for (i = 0; i < locations.length; i++) {

        if (markers.hasOwnProperty(locations[i].name)) {
            markers[locations[i].name].setPosition(locations[i].position);
        } else {
            var marker = new google.maps.Marker({
                position: locations[i].position,
                map: map,
                draggable: false,
                title: locations[i].name,
                animation: google.maps.Animation.DROP
            });

            marker.metadata = {
                "id": locations[i].id
            };

            var content = "<div style='padding: 5%; width: 80px;'>" + locations[i].id + ". " + locations[i].name + "</div>";
            var infowindow = new google.maps.InfoWindow();

            google.maps.event.addListener(marker, 'click', (function (marker, content, infowindow) {
                return function () {
                    infowindow.setContent(content);
                    infowindow.open(map, marker);

                };
            })(marker, content, infowindow));

            google.maps.event.addListener(marker, 'dblclick', (function (marker) {
                return function () {
                    $("#GCM-list").append(
                            "<tr>" +
                            "<td>" + marker.metadata.id + "</td>" +
                            "<td>" + marker.title + "</td>" +
                            '<td><em class="glyphicon glyphicon-remove-sign remover" style="cursor:pointer;"></em></td>' +
                            "</tr>");

                    $("#alert-help").addClass("hidden-xs hidden-sm hidden-md hidden-lg");
                    var seen = {};
                    $('#GCM-list tr').each(function () {
                        var txt = $(this).text();
                        if (seen[txt])
                            $(this).remove();
                        else
                            seen[txt] = true;
                    });
                    $(".remover").on("click", function () {
                        $(this).closest("tr").remove();
                        if ($('#GCM-list tr').length === 0) {
                            $("#alert-help").removeClass("hidden-xs hidden-sm hidden-md hidden-lg");
                        }
                    });
                };
            })(marker));

            markers[locations[i].name] = marker;
        }
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

function prepareBroadcast() {
    $("#GCM-list").empty();
    $("#GCM-list").append(
            "<tr>" +
            "<td>Broadcast</td>" +
            "<td> Alerta a todos los usuarios.</td>" +
            '<td><em class="glyphicon glyphicon-remove-sign remover" style="cursor:pointer;"></em></td>' +
            "</tr>");

    $("#alert-help").addClass("hidden-xs hidden-sm hidden-md hidden-lg");
    var seen = {};
    $('#GCM-list tr').each(function () {
        var txt = $(this).text();
        if (seen[txt])
            $(this).remove();
        else
            seen[txt] = true;
    });
    $(".remover").on("click", function () {
        $(this).closest("tr").remove();
        if ($('#GCM-list tr').length === 0) {
            $("#alert-help").removeClass("hidden-xs hidden-sm hidden-md hidden-lg");
        }
    });
}

$(document).ready(function () {

});