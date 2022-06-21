package springweb2.dto;

import lombok.*;
import springweb2.domain.CategoryEntity;
import springweb2.domain.ReplyEntity;

import javax.persistence.Table;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "reply")
public class ReplyDto {

    private int rno;
    private String rcontent;
    private int rdepth;
    private int rindex;
    private String rwriter;
    private String rpassword;
    private int bno;

    public ReplyEntity toentity(){
        return ReplyEntity.builder()
                .rno(this.rno)
                .rcontent(this.rcontent)
                .rdepth(this.rdepth)
                .rindex(this.rindex)
                .rwriter(this.rwriter)
                .rpassword(this.rpassword)
                .build();
    }

}
