

$("#confirm").on('click', function () {

    $.ajax({
        url: "${CONTEXT_PATH}/cep/delete",
        contentType: "application/json",
        data:  {id: delete_id },
        dataType: "text",
        type: "GET",
        success: function (data) {
            window.location.href = '${CONTEXT_PATH}/cep/list?navigationId=${navigation_id}&topic=${topic}&navigationName=${navigation_name}';
        }
    });
})
