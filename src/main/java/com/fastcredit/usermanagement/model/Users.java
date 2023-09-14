package com.fastcredit.usermanagement.model;


import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@Table(name = "tbl_user")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;

    private String password;


}
