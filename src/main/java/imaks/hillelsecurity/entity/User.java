package imaks.hillelsecurity.entity;

import imaks.hillelsecurity.validation.DutchPostCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "required")
    @Email(message = "required")
    @Column(name = "email")
    private String email;

    @NotNull(message = "required")
    @Min(value = 18, message = "you must be at least 18 years old")
    @Max(value = 1000, message = "If such an old contact us by email")
    @Column(name = "age")
    private int age;

    @NotNull(message = "required")
    @DutchPostCode
//    @Pattern(regexp = "^\\d{4}\\s?[A-Z]{2}$", message = "Enter a valid postal code (e.g., 1234AB or 1234 AB).")
    @Column(name = "post_code")
    private String postCode;

    @NotBlank(message = "required")
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(min = 8, message = "min 8 characters")
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
