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
