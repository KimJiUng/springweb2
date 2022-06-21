package springweb2.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "reply")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String rcontent;
    private int rdepth;
    private int rindex;
    private String rwriter;
    private String rpassword;

    @ManyToOne
    @JoinColumn(name = "bno")
    private BoardEntity boardEntity;

}
