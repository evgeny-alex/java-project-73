package hexlet.code.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "task_status")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "executor")
    private User executor;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "labels_tasks",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private List<Label> labelList;
//    Если пользователь связан хотя бы с одной задачей, его нельзя удалить - пока идея сделать это на уровне сервиса
//    Если статус связан хотя бы с одной задачей, его нельзя удалить

}
