package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyRestController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

    @PostMapping("/company")
    public void addCompany(@RequestBody CompanyDTO companyDTO){
        companyDTO.setId(0);
        Company company = new Company(companyDTO.getId(), companyDTO.getName());
        companyRepository.save(company);
    }

}
