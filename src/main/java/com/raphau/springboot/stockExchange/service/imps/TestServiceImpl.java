package com.raphau.springboot.stockExchange.service.imps;

import com.raphau.springboot.stockExchange.dao.TestRepository;
import com.raphau.springboot.stockExchange.entity.Test;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;

    @Override
    public List<Test> getTest() {
        return testRepository.findAll();
    }
}
