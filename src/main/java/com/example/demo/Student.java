@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String course;
}