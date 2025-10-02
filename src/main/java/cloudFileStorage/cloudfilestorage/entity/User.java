package cloudFileStorage.cloudfilestorage.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;


}
