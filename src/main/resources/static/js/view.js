
let bbno = 0;
let password = "";
$(function(){
    $.ajax({
        url : "view",
        type : "POST",
        success : function(data){
            $("#bno").html(data.bno);
            $("#rbno").val(data.bno);
            bbno = data.bno;
            $("#bwriter").html(data.bwriter);
            $("#btitle").val(data.btitle);
            $("#bcontent").val(data.bcontent);
            password = data.bpassword;
            rview();
        }
    });

})

function bupdate(){
    let bpassword = $("#bpassword").val();
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

function rview(){

    let bno = $("#rbno").val();
    $.ajax({
        url : "/rview",
        data : {"bno":bno},
        success : function(reply){
            let html = "";
            let maxdepth =0;
            for(let i=0; i<reply.length; i++){
                if(maxdepth<reply[i].rdepth){
                    maxdepth = reply[i].rdepth;
                }
            }
            let rereply = [];
            for(let i=0; i<reply.length; i++){
                let rdepth = 0;
                let indexno = -1;
                let indexno2 = -1;
                if(reply[i].rdepth==0){
                    html += '<tr><td>번호 : '+reply[i].rno+'</td><td>작성자 : '+reply[i].rwriter+'</td><td>내용 : '+reply[i].rcontent+'</td><td><button onclick="rrwrite('+reply[i].rno+')">답글</button><button onclick="reupdatebtn('+reply[i].rno+')">수정</button><button onclick="redeletebtn('+reply[i].rno+')">삭제</button></td></tr>';
                    html += '<tr id="re'+reply[i].rno+'" style="display:none;"><td colspan="4"><form id="rsaveform'+reply[i].rno+'">'+
                               '<div class="row">'+
                                   '<div class="col-md-6">'+
                                       '<input class="form-control" type="text" name="rcontent" id="rcontent" placeholder="댓글">'+
                                   '</div>'+
                                   '<div class="col-md-2">'+
                                       '<input class="form-control" type="text" name="rwriter" id="rwriter" placeholder="작성자">'+
                                   '</div>'+
                                   '<div class="col-md-2">'+
                                       '<input class="form-control" type="text" name="rpassword" id="rpassword" placeholder="비밀번호">'+
                                   '</div>'+
                                   '<div class="col-md-2">'+
                                       '<button class="form-control" type="button" onclick="rsave('+reply[i].rno+')">댓글작성</button>'+
                                   '</div>'+
                                   '<input type="hidden" id="rbno" name="bno" value="'+bbno+'">'+
                                   '<input type="hidden" id="rindex" name="rindex" value="'+reply[i].rno+'">'+
                                   '<input type="hidden" id="rdepth" name="rdepth" value="'+(rdepth+1)+'">'+
                               '</div>'+
                           '</form></td></tr>';
                    html += '<tr id="reu'+reply[i].rno+'" style="display:none;"><td colspan="4"><input type="text" id="reupdatecontent'+reply[i].rno+'" value="'+reply[i].rcontent+'"><input type="text" id="reupdatepassword'+reply[i].rno+'" placeholder="비밀번호"><button onclick="reupdate('+reply[i].rno+')">수정</button> </td></tr>';
                    html += '<tr id="red'+reply[i].rno+'" style="display:none;"><td colspan="4"><input type="text" id="redeletepassword'+reply[i].rno+'" placeholder="비밀번호"><button onclick="redelete('+reply[i].rno+')">삭제</button> </td></tr>';
                    indexno = reply[i].rno;
                    indexno2 = reply[i].rno;
                    rereply.push(reply[i].rno);
                }
                let rdepth2 = 0;
                let maxindexcount = 0;

                for(let f=0; f<reply.length; f++){
                    if(reply[f].rindex==indexno){
                        maxindexcount++;
                    }
                }

                let indexcount = 1;
                while(rdepth<=maxdepth && indexcount<=maxindexcount){
                    for(let j=0; j<reply.length; j++){
                        let rereplycheck = true;
                        for(let aa=0; aa<rereply.length; aa++){
                            if(rereply[aa]==reply[j].rno){
                                rereplycheck = false;
                            }
                        }
                        if((reply[j].rindex == indexno || (reply[j].rindex == indexno2 && indexcount>1) )  && rdepth2<reply[j].rdepth && rereplycheck){
                            html += '<tr>';
                            for(let a=0; a<reply[j].rdepth; a++){
                                html += '<td></td>';
                            }
                            html += '<td>번호 : '+reply[j].rno+'</td><td>작성자 : '+reply[j].rwriter+'</td><td>내용 : '+reply[j].rcontent+'</td><td><button onclick="reupdatebtn('+reply[j].rno+')">수정</button><button onclick="redeletebtn('+reply[j].rno+')">삭제</button></td></tr>';
                            html += '<tr id="reu'+reply[j].rno+'" style="display:none;"><td></td><td colspan="4"><input type="text" id="reupdatecontent'+reply[j].rno+'" value="'+reply[j].rcontent+'"><input type="text" id="reupdatepassword'+reply[j].rno+'" placeholder="비밀번호"><button onclick="reupdate('+reply[j].rno+')">수정</button> </td></tr>';
                            html += '<tr id="red'+reply[j].rno+'" style="display:none;"><td></td><td colspan="4"><input type="text" id="redeletepassword'+reply[j].rno+'" placeholder="비밀번호"><button onclick="redelete('+reply[j].rno+')">삭제</button> </td></tr>';
                            indexno = reply[j].rno;
                            rereply.push(reply[j].rno);
                        }
                    }
                    rdepth++;
                    rdepth2++;
                    if(rdepth==maxdepth){
                        indexcount++;
                        rdepth=0;
                        rdepth2=0;
                    }
                }


            }

            $("#replytable").html(html);
        }
    });
}





let test;
function rsave(rindex){
    let form = $("#rsaveform"+rindex)[0];
    let formdata = new FormData(form);
    test = formdata;
    console.log(form);
     $.ajax({
        url : "/rwrite",
        data : formdata,
        type : "POST",
        contentType : false,
        processData : false,
        success : function(re){
            if(re){
                alert("작성완료");
                let bno = $("#rbno").val();
                location.href = "/view?bno="+bno;
            }else{
                alert("작성실패");
            }
        }
    });
}

function rrwrite(rno){
    $("#re"+rno).show();
    $("#reu"+rno).hide();
    $("#red"+rno).hide();
}

function reupdatebtn(rno){
    $("#re"+rno).hide();
    $("#reu"+rno).show();
    $("#red"+rno).hide();
}

function redeletebtn(rno){
    $("#re"+rno).hide();
    $("#reu"+rno).hide();
    $("#red"+rno).show();
}

function reupdate(rno){
    let rcontent = $("#reupdatecontent"+rno).val();
    let rpassword = $("#reupdatepassword"+rno).val();
    $.ajax({
        url : "/reupdate",
        data : {"rcontent" : rcontent, "rpassword" : rpassword, "rno":rno},
        type : "PUT",
        success : function(re){
            if(re){
                alert("수정완료");
                let bno = $("#rbno").val();
                location.href = "/view?bno="+bno;
            }else{
                 alert("비밀번호가 일치하지 않습니다.");
            }
        }

    });

}

function redelete(rno){
    let rpassword = $("#redeletepassword"+rno).val();
    $.ajax({
        url : "/redelete",
        data : {"rpassword" : rpassword,"rno":rno},
        type : "DELETE",
        success : function(re){
            if(re){
                alert("삭제완료");
                let bno = $("#rbno").val();
                location.href = "/view?bno="+bno;
            }else{
                alert("비밀번호가 일치하지 않습니다.");
            }
        }

    });
}