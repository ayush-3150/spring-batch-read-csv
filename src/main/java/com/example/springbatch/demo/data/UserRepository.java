package com.example.springbatch.demo.data;

import org.springframework.data.repository.CrudRepository;

import com.example.springbatch.demo.model.User;

public interface UserRepository extends CrudRepository<User,Integer> {
    
}
