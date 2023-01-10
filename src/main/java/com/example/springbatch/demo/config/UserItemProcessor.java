package com.example.springbatch.demo.config;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.example.springbatch.demo.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    @Nullable
    public User process(@NonNull User user) throws Exception {
        user.setCountry("country: "+user.getCountry());
        return user;
    }

}

