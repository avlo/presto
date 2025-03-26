package com.prosilion.presto.nostr.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NostrUserServiceIT {
    @Autowired
    NostrUserService nostrUserService;
    
    @Test
    void createUser() {}
}
