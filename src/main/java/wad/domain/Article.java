package wad.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Article extends AbstractPersistable<Long> {
    
    private String header;
    private String ingress;
    private String content;
    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> subject;
    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> writerName;
    private LocalDateTime pdate;
    private int reloads;
    
}