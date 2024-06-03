package com.sugandrey.SecurityBootApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_SOME_OTHER') or hasRole('ROLE_ONE_MORE')")
    public void doAdminStaff() {
        System.out.println("Only admin here");
    }
}
