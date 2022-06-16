$(function(){
    $.ajax({
        url : "/boardlist",
        success : function(data){
            console.log(data);
            let html = ""
            html += '<tr><th>번호</th><th>제목</th><th>작성자</th></tr>';
            for(let i = 0; i < data.length; i++){
                html += '<tr><td>' + data[i]["bno"] + '</td><td>' + data[i]["btitle"] + '</td><td>' + data[i]["bwriter"] + '</td></tr>';
            }
            $("#boardtable").html(html);
        }
    });
})