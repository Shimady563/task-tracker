package com.shimady.auth.repository;

import com.shimady.auth.TestcontainersConfiguration;
import com.shimady.auth.model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
public class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    private final Group group = new Group();

    @BeforeEach
    void setUp() {
        group.setName("Test Group");

        entityManager.persist(group);
        entityManager.flush();
    }

    @Test
    void testFindById() {
        var foundGroup = groupRepository.findById(group.getId());

        assertTrue(foundGroup.isPresent());
        assertEquals(group.getId(), foundGroup.get().getId());
        assertEquals(group.getName(), foundGroup.get().getName());
    }
}
