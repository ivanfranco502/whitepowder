var alerts = [];
function getAlerts() {
    $.ajax({
        url: BASE_URL + MODE + "/web/" + PROFILER + "/internalApi/alert/all",
        dataType: "json",
        type: "POST",
        data: {
        },
        success: function (data, textStatus, jqXHR) {
            alerts = data.payload.last;
            $('#alertsBadge').text(data.payload.total);
            var lalal = setTimeout(getAlerts, interval);
            $('#alerts').empty();
            $('#alerts').append('<div id="alert-list" class="list-group"></div>');
            alerts.forEach(function (alert) {
                $('#alerts #alert-list').append(
                        '<a href="' + BASE_URL + MODE + "/web/" + PROFILER + '/administrator/center" class="list-group-item" data-item-id="' + alert.aler_id +'">'
                        + '<h4 class = "list-group-item-heading">' + alert.aler_user.username + '</h4>'
                        + '<p class = "list-group-item-text">Lat:' + alert.aler_x_position + '</p>'
                        + '<p class = "list-group-item-text">Long:' + alert.aler_y_position + '</p></a>');
            });
            $('#alerts #alert-list a').on('click', function () {
                markRead($(this).data('itemId'));
                getAlerts();
            });
        }
    });
}
function markRead(id) {
    $.ajax({
        url: BASE_URL + MODE + "/web/" + PROFILER + "/internalApi/alert/read/" + id,
        dataType: "json",
        type: "POST",
        data: {
        },
        success: function (data, textStatus, jqXHR) {
        }
    });
}

$(document).ready(function () {
    getAlerts();
});
