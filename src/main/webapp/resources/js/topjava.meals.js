const formatDate = 'Y-m-d';
const formatTime = 'H:i';

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: "ajax/profile/meals/",
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": "ajax/profile/meals/",
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories",
                },
                {
                    "data": "excess",
                    createdCell: function (td, cellData, rowData, row, col) {
                        $(td).css('display', 'none');
                        if (cellData.toString() === 'true') {
                            $(td).closest('tr').attr("data-mealExcess", "true");
                        } else $(td).closest('tr').attr("data-mealExcess", "false");
                    }
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        }),
        updateTable: updateFilteredTable
    });

    $('#startDate').datetimepicker({
        timepicker: false,
        datepicker: true,
        format: formatDate,
    });

    $('#endDate').datetimepicker({
        timepicker: false,
        datepicker: true,
        format: formatDate,
    });
    $('#startTime').datetimepicker({
        timepicker: true,
        datepicker: false,
         format: formatTime,
    });

    $('#endTime').datetimepicker({
        timepicker: true,
        datepicker: false,
        hours12: true,
        format: formatTime,
    });

});