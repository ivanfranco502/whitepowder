//VARIABLES
var map;
var skiers = [];
var slopes = [];
var markers = {};
var counter = 0;
var iterator = 0;
var selected = [];


function getMarkers() {

    $.ajax({
        dataType: "json",
        url: BASE_URL +  "/internalApi/skier/getAll",
        success: function (data, textStatus, jqXHR) {
            data.payload.forEach(function (skier) {
                skiers.push({
                    id: skier.id,
                    name: skier.username,
                    position: new google.maps.LatLng(parseFloat(skier.position.coor_X), parseFloat(skier.position.coor_Y)),
                    alert: skier.alert,
                    role: skier.role
                });
            });
            setMarkers(map, skiers);
            skiers = [];
            window.setTimeout(getMarkers, intervalSkiers);
        }
    });
}

function getSlopes() {
    $.ajax({
        dataType: "json",
        type: "POST",
        url: BASE_URL +  "/internalApi/slope/allPath",
        data: JSON.stringify({
            '_token': "a84b055999ea2b429490e4c642f64b57958aaf20"
        }),
        contentType: "application/json",
        success: function (path, textStatus, jqXHR) {
            path.payload.forEach(function (slope) {
                var coordinates = [];
                slope.slope_coordinates.forEach(function (coordinate) {
                    coordinates.push(
                            new google.maps.LatLng(parseFloat(coordinate.x), parseFloat(coordinate.y))
                            );
                });

                slopes.push({
                    id: slope.slope_id,
                    name: slope.slope_description,
                    color: '#' + slope.slope_difficulty_color,
                    coordinates: coordinates
                });
            });
            setSlopes(map, slopes);
            slopes = [];
//            window.setTimeout(getMarkers, interval);
        }
    });
}

function initialize() {
    getMarkers();
    getSlopes();
    var centerLatlng = new google.maps.LatLng(parseFloat($('#x_coordinate').text()), parseFloat($('#y_coordinate').text()));
    var mapOptions = {
        zoom: 12,
        center: centerLatlng,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        streetViewControl: false
    };
    map = new google.maps.Map(document.getElementById('center-map'),
            mapOptions);

    if ($.isEmptyObject(markers)) {
        $("#broadcast-btn").attr('disabled', 'disabled');
    }

}

function setSlopes(map, slopes) {

    var startSymbol = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 4,
        strokeColor: '#393'
    };

    var endSymbol = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 4,
        strokeColor: '#393'
    };

    slopes.forEach(function (slope) {
        var slopePath = new google.maps.Polyline({
            path: slope.coordinates,
            icons: [
                {
                    icon: startSymbol,
                    offset: '0%'
                }, {
                    icon: endSymbol,
                    offset: '100%'
                }
            ],
            geodesic: true,
            strokeColor: slope.color,
            strokeOpacity: 1.0,
            strokeWeight: 2
        });

        slopePath.setMap(map);
    });

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
            counter--;
            $("#counter span").text(counter);
            delete markers[index];
            if ($.isEmptyObject(markers)) {
                $("#broadcast-btn").attr('disabled', 'disabled');
            }
        }
    }

    for (i = 0; i < locations.length; i++) {

        if (markers.hasOwnProperty(locations[i].name)) {
            markers[locations[i].name].setPosition(locations[i].position);
            markers[locations[i].name].setIcon(locations[i].role == 'ROLE_RESCU' ? 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png' :
                    locations[i].alert !== 0 ? 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png' : 'http://maps.google.com/mapfiles/ms/icons/red-dot.png');
        } else {
            var marker = new google.maps.Marker({
                position: locations[i].position,
                map: map,
                draggable: false,
                title: locations[i].name,
                animation: google.maps.Animation.DROP,
                icon: locations[i].role == 'ROLE_RESCU' ? 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png' :
                        locations[i].alert !== 0 ? 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png' : 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
            });

            marker.metadata = {
                "id": locations[i].id
            };

            var content = "<div style='padding: 5%; width: 80px;'>" + locations[i].id + ". " + locations[i].name + "</div>";
            var infowindow = new google.maps.InfoWindow();

            google.maps.event.addListener(marker, 'dblclick', (function (marker, content, infowindow) {
                return function () {
                    infowindow.setContent(content);
                    infowindow.open(map, marker);

                };
            })(marker, content, infowindow));

            google.maps.event.addListener(marker, 'click', (function (marker) {
                return function () {
                    if (selected[0] != "Broadcast") {

                        $("#GCM-list").append(
                                "<tr>" +
                                "<td data-userID=" + marker.metadata.id + ">" + marker.metadata.id + "</td>" +
                                "<td>" + marker.title + "</td>" +
                                '<td><em class="glyphicon glyphicon-remove-sign remover" style="cursor:pointer;"></em></td>' +
                                "</tr>");

                        checkAndAdd(selected, marker.metadata.id);
                        $("#btn-send-alert").removeAttr("disabled");

                        $("#alert-help").addClass("hidden-xs hidden-sm hidden-md hidden-lg");
                        var seen = {};
                        $('#GCM-list tr').each(function () {
                            var txt = $(this).text();
                            if (seen[txt])
                                $(this).remove();
                            else
                                seen[txt] = true;
                        });
                    } else {
                        $("#alert-danger").html('<div class="alert alert-danger"><button type="button" class="close">×</button>Mensaje Broadcast se encuentra seleccionado, este se enviará a todos los usuarios.</div>');
                        $('#alert-danger .close').on("click", function (e) {
                            $(this).parent().fadeTo(300, 0).slideUp(300);
                        });
                    }
                };
            })(marker));

            markers[locations[i].name] = marker;
            counter++;
            $("#broadcast-btn").removeAttr('disabled');
            $("#counter span").text(counter);
        }
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

function checkAndAdd(arr, id) {
    var found = arr.some(function (el) {
        return el === id;
    });
    if (!found) {
        arr.push(id);
    }

}

function prepareBroadcast() {
    $("#GCM-list").empty();
    $("#GCM-list").append(
            "<tr>" +
            "<td>Broadcast</td>" +
            "<td> Alerta a todos los usuarios.</td>" +
            '<td><em class="glyphicon glyphicon-remove-sign remover" style="cursor:pointer;"></em></td>' +
            "</tr>");

    selected = ["Broadcast"];
    $("#alert-help").addClass("hidden-xs hidden-sm hidden-md hidden-lg");
    var seen = {};
    $('#GCM-list tr').each(function () {
        var txt = $(this).text();
        if (seen[txt])
            $(this).remove();
        else
            seen[txt] = true;
    });
    $("#btn-send-alert").removeAttr("disabled");
    $(".remover").on("click", function () {
        var toDelete;
        toDelete = selected.indexOf($(this).closest("tr td").prev().prev().text());

        selected = [];
        $("#btn-send-alert").attr("disabled", "disabled");
        $(this).closest("tr").remove();
        if ($('#GCM-list tr').length === 0) {
            $("#alert-help").removeClass("hidden-xs hidden-sm hidden-md hidden-lg");
        }
    });
}

$(document).ready(function () {

    $.ajax({
        dataType: "json",
        type: "POST",
        url: BASE_URL +  "/internalApi/notifications/allPreset",
//        data: JSON.stringify({
//            "_token": "a84b055999ea2b429490e4c642f64b57958aaf20"
//        }),
        contentType: "application/json",
        success: function (data, textStatus, jqXHR) {
            $.each(data.payload, function (index, item) { // Iterates through a collection
                $("#notification").append(
                        "<option value=" + item.noty_id + ">" + item.noty_description + "</option>");
            });
        }
    });

    $("#notification").change(function () {
        if ($(this).val() !== 'X') {
            $("#alert-message").attr('disabled', 'disabled');
            $("#alert-message").val($("#notification option[value='" + $(this).val() +"']").text());
        } else {
            $("#alert-message").removeAttr('disabled', 'disabled');
            $("#alert-message").val('');
        }
    });

    $("#GCM-list").on("click", ".remover", function () {
        var toDelete;
        toDelete = selected.indexOf($(this).closest("tr td").prev().prev().text());

        selected.splice(toDelete, 1);
        $(this).closest("tr").remove();

        if ($('#GCM-list tr').length === 0) {
            $("#alert-help").removeClass("hidden-xs hidden-sm hidden-md hidden-lg");
        }

        if (selected.length == 0) {
            $("#btn-send-alert").attr("disabled", "disabled");
        }
    });

    $("#btn-send-alert").on("click", function () {
        if ($("#alert-message").val() == '' || selected.length == 0) {

            $("#alert-danger").html('<div class="alert alert-danger"><button type="button" class="close">×</button>La notificación debe contener un mensaje y al menos un destinatario.</div>');
            $('#alert-danger .close').on("click", function (e) {
                $(this).parent().fadeTo(300, 0).slideUp(300);
            });
        } else {
            var btn = $(this);
            btn.button('loading');
            $.ajax({
                url: BASE_URL +  "/internalApi/GCM/sendNotification",
                dataType: "json",
                type: "POST",
                data: {
                    "_to": JSON.stringify(selected),
                    "_notification_id": JSON.stringify($("#notification").val()), 
                    "_body": JSON.stringify($("#alert-message").val())
                },
                success: function () {
                    $("#GCM-list").empty();
                    selected = [];
                    $("#alert-message").val('');
                    btn.button('reset');
                    $("#alert-danger .close").parent().fadeTo(300, 0).slideUp(300);
                    $("#alert-success").html('<div class="alert alert-success"><button type="button" class="close">×</button>La notificación se envió con éxito a todos los destinatarios.</div>');
                    $('#alert-success .close').on("click", function (e) {
                        $(this).parent().fadeTo(300, 0).slideUp(300);
                    });
                    //Solo para asegurar que se ejecute despues del reset del boton                    
                    setTimeout(function () {
                        $("#btn-send-alert").attr('disabled', 'disabled');
                    }, 0);

                }
            });
        }
    });
});