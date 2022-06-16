package springweb2.controller;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springweb2.dto.BoardDto;
import springweb2.service.BoardService;

import javax.servlet.http.HttpServletResponse;


@Controller
public class BoardController {

    @Autowired
    BoardService boardService;


    @GetMapping("/")
    public String index(){
        return "main";
    }

    @GetMapping("/write")
    public String writepage(){
        return "write";
    }

    // 게시물 작성
    @PostMapping("/write")
    @ResponseBody
    public boolean write(BoardDto boardDto){
        System.out.println(boardDto);
        return boardService.write(boardDto);
    }
    // 전체 게시물 출력
    @GetMapping("/boardlist")
    @ResponseBody
    public void getboardlist(HttpServletResponse response){
        JSONArray array = boardService.boardlist();
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(array);
        }catch(Exception e){e.printStackTrace();}
    }
    // 개별 게시물 출력

    // 게시물 수정

    // 게시물 삭제

}
