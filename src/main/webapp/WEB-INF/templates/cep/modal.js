

$("#confirm").on('click', function () {

    $.ajax({
        url: "${CONTEXT_PATH}/cep/delete",
        contentType: "application/json",
        data:  {id: delete_id },
        dataType: "text",
        type: "GET",
        success: function (data) {
            window.location.href = '${CONTEXT_PATH}/cep/list?parent_id=${parent_id}&child_id=${child_id}&parent_name=${parent_name}&topic=${topic}';
        }
    });
})
