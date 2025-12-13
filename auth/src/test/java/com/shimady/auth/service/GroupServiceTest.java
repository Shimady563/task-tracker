package com.shimady.auth.service;

import com.shimady.auth.model.Group;
import com.shimady.auth.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void shouldGetGroupById() {
        var group = new Group();
        group.setId(1L);
        group.setName("Test Group");

        given(groupRepository.findById(group.getId())).willReturn(Optional.of(group));

        var response = groupService.getGroupById(1L);

        assertNotNull(response);
        assertEquals(group.getId(), response.getId());
        assertEquals(group.getName(), response.getName());
        then(groupRepository).should().findById(1L);
    }
}
