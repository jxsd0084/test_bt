    var showResult = function(msg, flag){
        $('#resultInfo').text(msg);
	    $('#resultInfo').removeClass("hide");
        if(flag){
	        $('#resultInfo').addClass("bg-success");
        }else {
	        $('#resultInfo').addClass("bg-danger");
        }
    }

    var forward = function(msg, url){
        alert(msg);
        window.location.href = url;
    }


    $(".user-menu").on('click', function () {
        var obj = $("#dropdown-menu");
        if(obj.css("display")=="none"){
            obj.css("display","block");
        }else{
            obj.css("display","none");
        }
    })
