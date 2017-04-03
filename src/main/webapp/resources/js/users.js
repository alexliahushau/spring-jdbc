$(document).ready(function () {
    action = function () {
        var id = event.currentTarget.firstElementChild.innerHTML;
        window.location.href = '/user/' + id;
    }
    $('table > tbody > tr').on('click', action);
})
