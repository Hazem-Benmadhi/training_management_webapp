package com.greenbuilding.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull()
    @Pattern(regexp = "^\\+?[0-9\\s-]{8,20}$", message = "Invalid phone number format")
    @Column(name = "tel", nullable = false)
    private String tel;

    @NotNull(message = "Formateur type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TrainerType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "idemployer", referencedColumnName = "id")
    private Employer employer;

    @AssertTrue(message = "Employer is required for EXTERNE trainers")
    private boolean isEmployerValid() {
        return type != TrainerType.EXTERNE || employer != null;
    }

    public void setType(String type) {
        try {
            this.type = TrainerType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid formateur type: " + type +
                    ". Allowed values: INTERNE, EXTERNE");
        }
    }
}

