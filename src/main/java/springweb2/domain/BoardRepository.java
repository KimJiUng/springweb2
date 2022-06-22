package springweb2.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {

    @Query(value = "select * from board where cno=:cno and btitle like %:keyword%", nativeQuery = true)
    Page<BoardEntity> findByBtitle(@Param("cno") int cno, @Param("keyword") String keyword, Pageable pageable);

    @Query( value = "select * from board where btitle like %:keyword%" , nativeQuery = true )
    Page<BoardEntity> findBybtitle(@Param("keyword")  String keyword,Pageable pageable  );


    // 2. 내용 검색
    @Query(value = "select * from board where cno=:cno and bcontent like %:keyword%", nativeQuery =true)
    Page<BoardEntity> findByBcontent(@Param("cno") int cno, @Param("keyword") String keyword,Pageable pageable);


}
