package springweb2.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springweb2.domain.BoardEntity;
import springweb2.domain.BoardRepository;
import springweb2.dto.BoardDto;

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

    // 수정

    // 삭제

}
