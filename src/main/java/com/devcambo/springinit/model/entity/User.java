package com.devcambo.springinit.model.entity;

import com.devcambo.springinit.model.base.BaseEntity;
import com.devcambo.springinit.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;
}
