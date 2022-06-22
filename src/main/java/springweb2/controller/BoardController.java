package springweb2.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springweb2.dto.BoardDto;
import springweb2.dto.ReplyDto;
import springweb2.service.BoardService;

import javax.servlet.http.HttpServletRequest;
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

    // 댓글 작성
    @PostMapping("/rwrite")
    @ResponseBody
    public boolean rwrite(ReplyDto replyDto){
        System.out.println(replyDto);
        return boardService.rwrite(replyDto);
    }


    // 전체 게시물 출력
    @GetMapping("/boardlist")
    @ResponseBody
    public void getboardlist(HttpServletResponse response,@RequestParam("key") String key,@RequestParam("keyword") String keyword, @RequestParam("cno") int cno,@RequestParam("page") int page){
        JSONObject object = boardService.boardlist(key,keyword,cno,page);
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch(Exception e){e.printStackTrace();}
    }
    // 개별 게시물 출력
    @Autowired
    private HttpServletRequest request;
    @GetMapping("/view")
    public String viewpage(@RequestParam("bno") int bno){
        request.getSession().setAttribute("bno",bno);
        return "view";
    }
    // 댓글 출력
    @GetMapping("/rview")
    @ResponseBody
    public void getreplylist(HttpServletResponse response,@RequestParam("bno") int bno){
        JSONArray jsonArray = boardService.getreplylist(bno);
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        }catch(Exception e){e.printStackTrace();}
    }


    @PostMapping("/view")
    @ResponseBody
    public void view(HttpServletResponse response, HttpServletRequest request){
        int bno = (Integer)(request.getSession().getAttribute("bno"));
        JSONObject object = boardService.getboard(bno);
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch(Exception e){e.printStackTrace();}

    }

    @GetMapping("/getCategory")
    public void getCategory(HttpServletResponse response){
        JSONArray jsonArray = boardService.getcategory();
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        }catch(Exception e){e.printStackTrace();}
    }


    // 게시물 수정
    @PostMapping("/bupdate")
    @ResponseBody
    public boolean bupdate(@RequestParam("btitle") String btitle, @RequestParam("bcontent") String bcontent){
        int bno = (Integer)(request.getSession().getAttribute("bno"));
        return boardService.bupdate(btitle, bcontent,bno);
    }

    // 댓글 수정
    @PutMapping("/reupdate")
    @ResponseBody
    public boolean reupdate(@RequestParam("rcontent") String rcontent, @RequestParam("rpassword") String rpassword, @RequestParam("rno") int rno){
        return boardService.reupdate(rcontent,rpassword,rno);
    }


    // 게시물 삭제
    @DeleteMapping("/bdelete")
    @ResponseBody
    public boolean bdelete(){
        int bno = (Integer)(request.getSession().getAttribute("bno"));
        return boardService.bdelete(bno);
    }

    // 댓글 삭제
    @DeleteMapping("/redelete")
    @ResponseBody
    public boolean redelete(@RequestParam("rpassword") String rpassword, @RequestParam("rno") int rno){
        return boardService.redelete(rpassword,rno);
    }

}
