
let cno = 1;
let key = "";
let keyword = "";
let page = 0;
$(function(){
    $.ajax({
        url : "getCategory",
        success : function(category){
            let html = "";
            for(let i=0; i<category.length; i++){
                html += '<button type="button" onclick="selectCategory('+category[i].cno+')">'+category[i].cname+'</button>';
            }
            $("#categorybox").html(html);
        }
    });
    getBoardList(key,keyword,cno,page);

})

function selectCategory(selectcno){
    key = "";
    keyword = "";
    page = 0;
    $("#keyword").val("");
    cno = selectcno;
    getBoardList(key,keyword,cno,page);
}


function getBoardList(key,keyword,cno,page){
    $.ajax({
        url : "/boardlist",
        data : {"key":key,"keyword":keyword,"cno":cno,"page":page},
        success : function(data){
            console.log(data);
            let html = ""
            html += '<tr><th>번호</th><th>제목</th><th>작성자</th></tr>';
            if(data.array.length==0){
                html+='<tr><th colspan="3">검색 결과가 없습니다.</th></tr>'
            }
            for(let i = 0; i < data.array.length; i++){
                html += '<tr onclick=location.href="view?bno='+data.array[i]["bno"]+'"><td>' + data.array[i]["bno"] + '</td><td>' + data.array[i]["btitle"] + '</td><td>' + data.array[i]["bwriter"] + '</td></tr>';
            }
            //////////////////////////// 페이징 버튼 생성 코드 ////////////////////////////////////
            let pagehtml = "";

            ////////////////////////// 이전 버튼 /////////////////////////////////////////////
            if(page==0){    // 현재 페이지가 첫페이지이면
                pagehtml += '<li class="page-item disabled"><button class="page-link" type="button" onclick="getBoardList(key,keyword,cno,'+(page)+')">이전</button> </li>';
            }else{  // 현재페이지가 첫페이지가 아니면
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getBoardList(key,keyword,cno,'+(page-1)+')">이전</button> </li>';
            }
            ////////////////////////////////////////////////////////////////////////////////
            for(let i = data.startbtn; i <=data.endbtn; i++) {
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getBoardList(key,keyword,cno,'+(i-1)+')">'+i+'</button> </li>';
            }
            ////////////////////////// 다음 버튼 ///////////////////////////////////////////////////
            if(page==data.totalpage-1 || data.totalpage == 0){
                pagehtml += '<li class="page-item disabled"><button class="page-link" type="button" onclick="getBoardList(key,keyword,cno,'+(page)+')">다음</button> </li>';
            }else{
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getBoardList(key,keyword,cno,'+(page+1)+')">다음</button> </li>';
            }
            /////////////////////////////////////////////////////////////////////////////////////////
            $("#boardtable").html(html);
            $("#pagebtnbox").html(pagehtml);
        }
    });
}

function search(){
    key = $("#key").val();
    keyword = $("#keyword").val();
    getBoardList(key,keyword,cno,page);
}
