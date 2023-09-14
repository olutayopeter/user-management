package com.fastcredit.usermanagement.repository;


import com.fastcredit.usermanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    @Query(value = "SELECT * FROM tbl_user WHERE username = :username OR email = :email AND password = :password",nativeQuery = true)
    Users findByUsernameOrEmailAndPassword(String username,String email,String password);
    @Query(value = "SELECT * FROM tbl_user WHERE username = :username OR email = :email",nativeQuery = true)
    Users findByUsernameOrEmail(String username,String email);
    @Query(value = "SELECT * FROM tbl_user WHERE username = :username",nativeQuery = true)
    List<Users> findDetailsByUsername(String username);




}
