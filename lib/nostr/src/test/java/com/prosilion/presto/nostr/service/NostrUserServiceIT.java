package com.prosilion.presto.nostr.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NostrUserServiceIT {
    @Test
    void createUser() {
        assertEquals((2-1), 1);
    }

//    TODO: recall, below test intentionally failing so-as-to-prove "gradle check" is executing properly    
    @Test
    void createUser2() {
        assertEquals((1-1), 1);
    }
}
