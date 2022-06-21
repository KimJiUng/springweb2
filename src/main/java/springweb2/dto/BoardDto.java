package springweb2.dto;

import lombok.*;
import springweb2.domain.BoardEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private int bno;
    private String btitle;
    private String bcontent;
    private String bwriter;
    private String bpassword;
    private String category;


    public BoardEntity toentity(){
        return BoardEntity.builder()
                .bno(this.bno)
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .bwriter(this.bwriter)
                .bpassword(this.bpassword)
                .build();
    }
}
