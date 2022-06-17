

let password = "";
$(function(){
    $.ajax({
        url : "view",
        type : "POST",
        success : function(data){
            $("#bno").html(data.bno);
            $("#bwriter").html(data.bwriter);
            $("#btitle").val(data.btitle);
            $("#bcontent").val(data.bcontent);
            password = data.bpassword;
        }
    });
})

function bupdate(){
    let bpassword = $("#bpassword").val();
    alert($("#btitle").val());
    if(bpassword == password){
        $.ajax({
            url : "bupdate",
            data : {"btitle":$("#btitle").val(), "bcontent":$("#bcontent").val() },
            type : "POST",
            success : function(re){
                if(re){
                    alert("Success!");
                    location.reload();
                }else{
                    alert("Error!");
                }
            }
        })
    }else{
        alert("Please check a password");
    }
}

function bdelete(){
    let bpassword = $("#bpassword").val();
    if(bpassword == password){
        $.ajax({
            url : "bdelete",
            type : "DELETE",
            success : function(re){
                if(re){
                    alert("Success!");
                    location.href = "/";
                }else{
                    alert("Error!");
                }
            }
        })
    }else{
        alert("Please check a password");
    }
}