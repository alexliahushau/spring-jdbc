var token = $("meta[name='_csrf']").attr("content"),
    header = $("meta[name='_csrf_header']").attr("content"),
    eventsList = [],
    selected = {},
    tickets = [],
    messageFactory = function(type, message) {
        var alert = $('#msgBox');

        if (message) {
            alert.addClass('alert alert-' + type);
            alert.append($('<span>' + message + '</span>'));
            setTimeout(function() {
                alert.children().remove();
                alert.removeClass('alert alert-' + type);        
            }, 2500);
        } else {
            alert.removeClass('alert');
        }
    },
    getEvents = function () {
        $.ajax({
            method: "Get",
            dataType: "json",
            url: "api/v1/events",
        }).done(function (data) {
            eventsList = data;
            $.each(data, function (i, item) {
                $("select#events").append($('<option>', {
                    value: i,
                    text: item.name
                }));
            });
            $.each(data, function (i, item) {
                $("table#events > tbody").append($("<tr style='display: table-row;'>" + "<td>" + item.id + "</td>" + "<td>" + item.name + "</td>" + "<td>" + "<select id='airDates' name='airDates' class='span5'>" + "<#list " + item.airDates + " as date>" + "<option value='data'>${date}</option>" + "</select>" + "</td>" + "<td><a class='btn btn-success' href='booking/event?eventId=" + item.id + "&airDate=" + encodeURIComponent(item.airDates[0]) + "'>Get booked tickets</a></td>" + "</tr>"));
            });
            getEventAirDates(data[0]);
            getSeats();
        });
    },
    getEventAirDates = function (data) {
        $("select#airDates option").remove();
        selected.event = data.target ? eventsList[data.target.selectedIndex] : data;
        $.each(selected.event.airDates, function (i, date) {
            $("select#airDates").append(
                $('<option>', {
                    value: date,
                    text: date
                }));
        });
    },
    getSeats = function (refresh) {
        selected.airDate = $("select#airDates option:selected").val();
        $.ajax({
            method: "Get",
            dataType: "json",
            url: "api/v1/seats",
            data: {
                id: selected.event.id,
                airDate: selected.airDate
            }
        }).done(function (data) {
            var select = $("select#seats");
            select.children().remove();
            $.each(data, function (i, item) {
                select.append($('<option>', {
                    value: item,
                    text: item
                }));
            });

            if (refresh) select.multiselect('destroy');
            select.multiselect();
            getTicketsPrice();
        });
    },
    refreshSelectedSeats = function () {
        selected.seats = [];
        $.each($(".ui-multiselect-menu input[aria-selected='true']"), function (i, seat) {
            selected.seats.push(seat.value);
        });
    },
    getTicketsPrice = function () {
        refreshSelectedSeats();
        $.ajax({
            method: "Get",
            dataType: "json",
            accept: "application/json",
            url: "api/v1/seats/price",
            data: $.param({
                eventId: selected.event.id,
                airDate: selected.airDate,
                seats: selected.seats
            }, true)
        }).done(function (data) {
            $('#totalPrice').html("Total price: $".concat(data ? data : 0));
        });
    },
    bookTickets = function () {
        refreshSelectedSeats();
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            method: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            url: "api/v1/tickets",
            data: JSON.stringify({
                eventId: selected.event.id,
                airDate: selected.airDate,
                seats: selected.seats
            })
        }).done(function (data) {
            messageFactory('success', 'Successfully booked');
            getSeats(true);
        }).error(function (data) {
            messageFactory('error', data.responseJSON.message);
        });
    },
    fetchMyBookedTickets = function () {
        $.ajax({
            method: "GET",
            dataType: "json",
            accept: "application/json",
            url: "api/v1/tickets"
        }).done(function (data) {
            tickets = data;
            var table = $("table#tickets tbody");
            table.children().remove();
            $.each(data, function (i, item) {
                var dateTime = item.dateTime.substring(17, 20) !== "00" ? item.dateTime : item.dateTime.slice(0, -3);
                table.append(
                    $("<tr style='display: table-row;'>" + "<td>" + item.id + "</td>" + "<td>" + item.event.name + "</td>" + "<td>" + dateTime + "</td>" + "<td>" + item.dateTime + "</td>" + "<td>" + item.seat + "</td>" + "<td>" + item.event.rating + "</td>" + "<td>" + item.purchased + "</td>" + "</tr>")
                );
            });
            $('#tickets > tbody > tr').on('click', getTicketPDF);
        });
    },
    buyTickets = function () {
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            method: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "api/v1/buyTickets",
            data: JSON.stringify(getAllUnpurchased(tickets))
        }).done(function (data) {
            messageFactory('success', 'Successfully purchased');
            var table = $("table#tickets tbody");
            table.children().remove();
            $.each(data, function (i, item) {
                var dateTime = item.dateTime.substring(17, 20) !== "00" ? item.dateTime : item.dateTime.slice(0, -3);
                table.append(
                    $("<tr style='display: table-row;'>" + "<td>" + item.id + "</td>" + "<td>" + item.event.name + "</td>" + "<td>" + dateTime + "</td>" + "<td>" + item.dateTime + "</td>" + "<td>" + item.seat + "</td>" + "<td>" + item.event.rating + "</td>" + "<td>" + item.purchased + "</td>" + "</tr>")
                );
            });
        }).error(function (data) {
            messageFactory('error', data.responseJSON.message);
        });
    },
    getAllUnpurchased = function(tickets) {
        var _tickets = [];
        $.each(tickets, function(i, ticket) {
            if (!ticket.purchased) _tickets.push(ticket);
        });
        return _tickets;
    },
    getTicketPDF = function () {
        var id = event.currentTarget.firstElementChild.innerHTML;
        window.location.href = '/api/v1/tickets/' + id + ".pdf";
    };


$(document).ready(function () {
    getEvents();
    $('select#events').on('change', getEventAirDates);
    $('select#airDates').on('change', getSeats);
    $('select#seats').on('change', getTicketsPrice);
    $('#bookTickets').on('click', bookTickets);
    $('#getBookedTickets').on('click', fetchMyBookedTickets);
    $('#buyTickets').on('click', buyTickets);
});