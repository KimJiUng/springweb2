

$(function(){
    $.ajax({
        url : "getCategory",
        success : function(category){
            let html = "";
            for(let i=0; i<category.length; i++){
                html += '<button type="button" onclick="getBoardList('+category[i].cno+')">'+category[i].cname+'</button>';
            }
            $("#categorybox").html(html);
        }
    });
    getBoardList(1);

})


function getBoardList(cno){
    $.ajax({
        url : "/boardlist",
        success : function(data){
            let html = ""
            html += '<tr><th>번호</th><th>제목</th><th>작성자</th></tr>';
            for(let i = 0; i < data.length; i++){
                if(data[i].cno == cno){
                    html += '<tr onclick=location.href="view?bno='+data[i]["bno"]+'"><td>' + data[i]["bno"] + '</td><td>' + data[i]["btitle"] + '</td><td>' + data[i]["bwriter"] + '</td></tr>';
                }
            }
            $("#boardtable").html(html);
        }
    });
}
