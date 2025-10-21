package com.example.db_setup.service;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.dto.gamification.AdminSummaryDTO;
import com.example.db_setup.model.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<AdminSummaryDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();

        List<AdminSummaryDTO> adminSummaryDTOs = new ArrayList<>();
        for (Admin admin : admins) {
            adminSummaryDTOs.add(new AdminSummaryDTO(admin.getName(), admin.getSurname(), admin.getEmail()));
        }

        return adminSummaryDTOs;
    }

    public Admin addNewAdmin(String name, String surname, String email, String password) {
        Admin admin = new Admin(name, surname, email, password);
        return adminRepository.save(admin);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.getAdminByEmail(email).orElse(null);
    }
}
