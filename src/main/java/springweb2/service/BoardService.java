package springweb2.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springweb2.domain.BoardEntity;
import springweb2.domain.BoardRepository;
import springweb2.dto.BoardDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BoardService {



    @Autowired
    BoardRepository boardRepository;

    // 쓰기
    public boolean write(BoardDto boardDto){
        BoardEntity boardEntity = boardDto.toentity();
        boardRepository.save(boardEntity);
        if(boardEntity.getBno()<1){
            return false;
        }
        return true;
    }
    // 모든 게시물 출력
    public JSONArray boardlist(){
        JSONArray jsonArray = new JSONArray();
        List<BoardEntity> boardlist = boardRepository.findAll();
        for(BoardEntity boardEntity : boardlist){
            JSONObject object = new JSONObject();
            object.put("bno",boardEntity.getBno());
            object.put("btitle",boardEntity.getBtitle());
            object.put("bwriter",boardEntity.getBwriter());
            jsonArray.put(object);
        }
        return jsonArray;
    }
    // 개별 게시물 출력
    public JSONObject getboard(int bno){

        JSONObject object = new JSONObject();
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        object.put("bno",boardEntity.getBno());
        object.put("bwriter",boardEntity.getBwriter());
        object.put("btitle",boardEntity.getBtitle());
        object.put("bcontent",boardEntity.getBcontent());
        object.put("bpassword",boardEntity.getBpassword());
        return object;
    }

    // 수정
    @Transactional
    public boolean bupdate(String btitle, String bcontent,int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        if(boardEntity==null){
            return false;
        }else{
            boardEntity.setBtitle(btitle);
            boardEntity.setBcontent(bcontent);
            return true;
        }

    }

    // 삭제
    public boolean bdelete(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        if(boardEntity==null){
            return false;
        }else{
            boardRepository.delete(boardEntity);
            return true;
        }

    }

}
