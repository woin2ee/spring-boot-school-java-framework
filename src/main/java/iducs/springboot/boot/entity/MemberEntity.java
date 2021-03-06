package iducs.springboot.boot.entity;

import lombok.*;

import javax.persistence.*;

// Spring Data 관련 Annotations
@Entity
@Table(name = "tbl_member")

// Lombok 관련 Annotations
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(length = 30, nullable = false)
    private String id;
    @Column(length = 30, nullable = false)
    private String pw;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 30, nullable = true)
    private String phone;
    @Column(length = 100)
    private String address;
}
