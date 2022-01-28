package com.creditshelf.assignment.model;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Data
@TypeDefs({
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
@Entity
@Table(name = "games")
public class Game implements Serializable {
    @Id
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "first_player")
    private String player1;

    @Column(name = "second_player")
    private String player2;

    @Column(name = "game_status")
    private String gameStatus;

    @Type(type = "int-array")
    @Column(
            name = "board",
            columnDefinition = "integer[][]"
    )
    private int[][] board;

    @Transient
    private String turn;
}
