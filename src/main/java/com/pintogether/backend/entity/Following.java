package com.pintogether.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Following {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member follower;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member followee;
}
