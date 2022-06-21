package springweb2.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springweb2.domain.*;
import springweb2.dto.BoardDto;
import springweb2.dto.ReplyDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BoardService {



    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ReplyRepository replyRepository;

    // 쓰기
    @Transactional
    public boolean write(BoardDto boardDto){
        BoardEntity boardEntity = boardDto.toentity();
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        boolean check = false;
        CategoryEntity categoryEntity = null;
        for(CategoryEntity entity : categoryEntityList){
            if(entity.getCname().equals(boardDto.getCategory())){
                categoryEntity = entity;
                check = true;
            }
        }
        if(check==false){
            categoryEntity = CategoryEntity.builder()
                    .cname(boardDto.getCategory())
                    .build();
            categoryRepository.save(categoryEntity);
        }
        boardRepository.save(boardEntity);
        boardEntity.setCategoryEntity(categoryEntity);
        categoryEntity.getBoardEntityList().add(boardEntity);
        if(boardEntity.getBno()<1){
            return false;
        }
        return true;
    }

    // 댓글 작성
    @Transactional
    public boolean rwrite(ReplyDto replyDto){
        ReplyEntity replyEntity = replyDto.toentity();
        if(replyEntity==null){
            return false;
        }else{
            replyRepository.save(replyEntity);
            BoardEntity boardEntity = boardRepository.findById(replyDto.getBno()).get();
            replyEntity.setBoardEntity(boardEntity);
            boardEntity.getReplyEntityList().add(replyEntity);
            return true;
        }

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
            object.put("cno",boardEntity.getCategoryEntity().getCno());
            object.put("cname",boardEntity.getCategoryEntity().getCname());
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
        object.put("cno",boardEntity.getCategoryEntity().getCno());
        object.put("cname",boardEntity.getCategoryEntity().getCname());
        return object;
    }
    // 카테고리 목록 출력
    public JSONArray getcategory(){
        JSONArray jsonArray = new JSONArray();
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        for(CategoryEntity entity : categoryEntityList){
            JSONObject object = new JSONObject();
            object.put("cno",entity.getCno());
            object.put("cname",entity.getCname());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    // 댓글 출력
    public JSONArray getreplylist(int bno){
        JSONArray jsonArray = new JSONArray();
        List<ReplyEntity> replyEntityList = replyRepository.findAll();
        for(ReplyEntity entity : replyEntityList){
            if(entity.getBoardEntity().getBno()==bno){
                JSONObject object = new JSONObject();
                object.put("rno",entity.getRno());
                object.put("bno",entity.getBoardEntity().getBno());
                object.put("rcontent",entity.getRcontent());
                object.put("rdepth", entity.getRdepth());
                object.put("rindex",entity.getRindex());
                object.put("rwriter",entity.getRwriter());
                object.put("rpassword",entity.getRpassword());
                jsonArray.put(object);
            }

        }
        return jsonArray;
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
