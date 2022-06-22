package springweb2.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import springweb2.domain.*;
import springweb2.dto.BoardDto;
import springweb2.dto.ReplyDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public JSONObject boardlist(String key,String keyword,int cno,int page){
        JSONObject object2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Pageable pageable = PageRequest.of(page,3, Sort.Direction.DESC,"bno");
        Page<BoardEntity> boardEntities = null;
        if(key.equals("btitle")){
            boardEntities = boardRepository.findByBtitle(cno,keyword,pageable);
        }else if(key.equals("bcontent")){
            boardEntities = boardRepository.findByBcontent(cno,keyword,pageable);
        }else{
            boardEntities = boardRepository.findByBtitle(cno,keyword,pageable);
        }

        // 페이지에 표시할 총 페이징 버튼 개수
        int btncount = 5;
        // 페이징버튼의 시작 번호 [ (현재페이지/표시할버튼수)*표시할버튼수 + 1 ]
        int startbtn = (page/btncount)*btncount+1;
        // 페이징버튼의 끝 번호  [ 시작버튼 + 표시할버튼수 - 1
        int endbtn = startbtn + btncount-1;
        // 만약 끝번호가 마지막페이지보다 크면 끝번호는 마지막 페이지 번호로 사용
        if(endbtn>boardEntities.getTotalPages()) endbtn = boardEntities.getTotalPages();
        if(endbtn==0) endbtn=1;
        object2.put("totalpage",boardEntities.getTotalPages());
        object2.put("startbtn",startbtn);
        object2.put("endbtn",endbtn);

        for(BoardEntity boardEntity : boardEntities){
            JSONObject object = new JSONObject();
            object.put("bno",boardEntity.getBno());
            object.put("btitle",boardEntity.getBtitle());
            object.put("bwriter",boardEntity.getBwriter());
            object.put("cno",boardEntity.getCategoryEntity().getCno());
            object.put("cname",boardEntity.getCategoryEntity().getCname());
            jsonArray.put(object);
        }
        object2.put("array",jsonArray);
        return object2;
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

    // 게시물 수정
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

    @Transactional
    public boolean reupdate(String rcontent, String rpassword, int rno){
        Optional<ReplyEntity> optionalReply = replyRepository.findById(rno);
        if(optionalReply.isPresent()){
            ReplyEntity replyEntity = optionalReply.get();
            if(replyEntity.getRpassword().equals(rpassword)){
                replyEntity.setRcontent(rcontent);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    // 게시물 삭제
    public boolean bdelete(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        if(boardEntity==null){
            return false;
        }else{
            List<ReplyEntity> replyEntityList = replyRepository.findAll();
            for(ReplyEntity entity : replyEntityList){
                if(entity.getBoardEntity().getBno()==bno){
                    replyRepository.delete(entity);
                }
            }
            boardRepository.delete(boardEntity);
            return true;
        }
    }

    // 댓글 삭제
    public boolean redelete(String rpassword, int rno){
        Optional<ReplyEntity> optionalReply = replyRepository.findById(rno);
        if(optionalReply.isPresent()){
            ReplyEntity replyEntity = optionalReply.get();
            if(replyEntity.getRpassword().equals(rpassword)){
                List<ReplyEntity> replyEntityList = replyRepository.findAll();
                for(ReplyEntity entity : replyEntityList){
                    if(entity.getRindex()==replyEntity.getRno()){
                        replyRepository.delete(entity);
                    }
                }
                replyRepository.delete(replyEntity);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
