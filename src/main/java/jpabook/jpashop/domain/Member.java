package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;
    @JsonIgnore // 회원정보 조회 시 orders의 내용은 안나오게 함
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
