var map;
var skiers = [];
var markers = [];
var iterator = 0;
$.ajax({
    dataType: "json",
    url: "http://whitetavros.com/Sandbox/web/app_dev.php/internalApi/skier/getAll",
    success: function (data, textStatus, jqXHR) {
        data.payload.forEach(function (skier) {
            skiers.push({
                name: skier.username,
                position: new google.maps.LatLng(parseFloat(skier.position.coor_X), parseFloat(skier.position.coor_Y))
            });
        });
//        drop();
        setMarkers(map, skiers);
    }
});

function initialize() {
    var centerLatlng = new google.maps.LatLng(parseFloat($('#x_coordinate').text()), parseFloat($('#y_coordinate').text()));
    var mapOptions = {
        zoom: 12,
        center: centerLatlng,
        mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    map = new google.maps.Map(document.getElementById('center-map'),
            mapOptions);
}

function setMarkers(map, locations) {

    var marker, i;

    for (i = 0; i < locations.length; i++) {
        var marker = new google.maps.Marker({
            position: skiers[i].position,
            map: map,
            draggable: false,
            title: skiers[i].name,
            animation: google.maps.Animation.DROP
        });
        marker.metadata = {"id": i};
        var content = "<div style='padding: 5%; width: 80px;'>" + i + ". " + skiers[i].name + "</div>";

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
                        '<td><em class="glyphicon glyphicon-remove-sign remover"></em></td>' +
                        "</tr>");
                
                $(this).find("td remover").on("click", function () {
                    $(this).closest("tr").remove();
                });
            };
        })(marker));




    }
}

//
//

//
//function drop() {
//    for (var i = 0; i < skiers.length; i++) {
//        setTimeout(function () {
//            addMarker();
//        }, i * 200);
//    }
//}
//
//function addMarker() {
//    var infowindow = new google.maps.InfoWindow({
//        content: skiers[iterator].name
//    });
//
//    markers.push(new google.maps.Marker({
//        position: skiers[iterator].position,
//        map: map,
//        draggable: false,
//        title: skiers[iterator].name,
//        animation: google.maps.Animation.DROP
//    }));
//
//
//    google.maps.event.addListener(markers[iterator], 'click', (function (markers, content, infowindow) {
//        return function () {
//            infowindow.setContent(content);
//            infowindow.open(map, markers);
//        };
//    })(markers, content, infowindow));
//    iterator++;
//}
//
//
//
google.maps.event.addDomListener(window, 'load', initialize);

$(document).ready(function () {

});



//function () {
//        infowindow.open(map, markers[iterator]);
//    }