// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "dsc"
                    ]
                ]
            })
        }
    );
});
$('#filterButton').click(function () {
    sendAjax();
});

$('#cancelButton').click(function () {
    $('#filterForm')[0].reset();
    sendAjax();
});

function sendAjax(){
    var form = $('#filterForm');
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: form.serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
};

