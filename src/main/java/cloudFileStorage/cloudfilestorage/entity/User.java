package cloudFileStorage.cloudfilestorage.entity;

import jakarta.persistence.*;


@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;


}
