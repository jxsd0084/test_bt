

$("#confirm").on('click', function () {

    $.ajax({
        url: "${CONTEXT_PATH}/cep/delete",
        contentType: "application/json",
        data:  {id: delete_id },
        dataType: "text",
        type: "GET",
        success: function (data) {
            alert("成功删除!")
        }
    });
})
