//CONSTANTES
var BASE_URL = "http://whitetavros.com/";
var MODE = "Sandbox";
var PROFILER = "app_dev.php";
//VARIABLES
var map;
var skiers = [];
var markers = {};
var iterator = 0;
var interval = 2000;
var selected = [];


function getMarkers() {

    $.ajax({
        dataType: "json",
        url: BASE_URL + MODE + "/web/" + PROFILER + "/internalApi/skier/getAll",
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
        if ($("#alert-message").val() == '') {

            $("#alert-danger").html('<div class="alert alert-danger"><button type="button" class="close">×</button>La alerta debe contener un mensaje.</div>');
            $('#alert-danger .close').on("click", function (e) {
                $(this).parent().fadeTo(300, 0).slideUp(300);
            });
        } else {
            var btn = $(this);
            btn.button('loading');
            $.ajax({
                url: BASE_URL + MODE + "/web/" + PROFILER + "/internalApi/GCM/sendNotification",
                dataType: "json",
                type: "POST",
                data: {
                    "_to": JSON.stringify(selected),
                    "body": $("#alert-message").val()
                },
                success: function () {
                    $("#GCM-list").empty();
                    $("#alert-message").val('');
                    btn.button('reset');
                }
            });
        }
    });
});