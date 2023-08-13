package com.blog.repositories;
import com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

   public interface UserRepository extends JpaRepository< User, String> {
      Optional<User> findByUserName(String userName);
   }
