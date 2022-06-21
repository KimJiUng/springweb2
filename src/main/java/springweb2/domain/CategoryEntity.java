package springweb2.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cno;
    private String cname;

    @OneToMany(mappedBy = "categoryEntity",cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardEntity> boardEntityList = new ArrayList<>();
}
