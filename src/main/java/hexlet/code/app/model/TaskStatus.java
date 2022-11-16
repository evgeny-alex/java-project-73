package hexlet.code.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@Table(name = "taskstatus")
public class TaskStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    @Column(name = "name", unique = true)
    @Size(min = 3, max = 1_000)
    private String name;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
