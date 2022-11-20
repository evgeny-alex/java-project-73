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
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    @Column(name = "name", unique = true)
    @Size(min = 3, max = 1_000)
    private String name;

    @Column(name = "description")
    @Size(min = 3, max = 1_000)
    private String description;

    @Column(name = "task_status")
    private TaskStatus taskStatus;

    @Column(name = "author")
    private User author;

    @Column(name = "executor")
    private User executor;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

//    Если пользователь связан хотя бы с одной задачей, его нельзя удалить - пока идея сделать это на уровне сервиса
//    Если статус связан хотя бы с одной задачей, его нельзя удалить

}
