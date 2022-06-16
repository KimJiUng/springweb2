function bwrite(){

    let form = $("#saveform")[0];
    let formdata = new FormData(form);

     $.ajax({
        url : "/write",
        data : formdata,
        type : "POST",
        contentType : false,
        processData : false,
        success : function(re){
            if(re){
                alert("작성완료");
                location.href = "/";
            }else{
                alert("작성실패");
            }
        }
    });
}

