package com.greenbuilding.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



import java.util.HashSet;
import java.util.Set;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @NotBlank(message = "Required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255)
    @NotNull
    @NotBlank(message = "Required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "structure", nullable = false)
    @NotNull
    private Structure structure;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idprofil", nullable = false, referencedColumnName = "id")
    private Profil profil;

    @Email(message = "Invalid email Format")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{8,20}$", message = "Invalid phone number format")
    @NotBlank(message = "Phone number is required")
    @Column(name = "tel", length = 8, nullable = false)
    private String tel;


    @ManyToMany
    @JoinTable(
            name = "formation_participant",
            joinColumns = @JoinColumn(name = "idparticipant"),
            inverseJoinColumns = @JoinColumn(name = "idformation")
    )

    private Set<Formation> formations = new HashSet<>();

    public void setEmail(String email) {
        if (!this.email.equals(email) && !email.isEmpty()) {
            this.email = email;
        }else{
            throw new RuntimeException("New email already exists");
        }
    }

}