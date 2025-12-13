package com.shimady.auth.service;

import com.shimady.auth.exception.ResourceNotFoundException;
import com.shimady.auth.model.Group;
import com.shimady.auth.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    protected Group getGroupById(Long id) {
        log.info("Getting group by id: {}", id);
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group with id: " + id + " not found"));
    }
}
