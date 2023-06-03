package com.bs.booking.services;

import com.bs.booking.models.Test;
import com.bs.booking.repositories.TestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TestService {

    private final TestRepository testRepository;

    public String helloWorld() {return "Hello World!";}

    public List<Test> getTests() {
        return testRepository.findAll();
    }

    public List<Test> getTestByAgeAndName(Integer age, String name) {
        return testRepository.findByAgeAndName(age,name);
    }

    @Transactional
    public Test createTest(Test createData) {
        return testRepository.save(createData);
    }

    @Transactional
    public Test updateTest(Test updateData) {
        Test originTest = testRepository.findByTestId(updateData.getTestId());
        if(originTest != null) {
            if(updateData.getAge()!=null) originTest.setAge(updateData.getAge());
            if(updateData.getName() != null) originTest.setName(updateData.getName());
            if(updateData.getDescription() != null)
                originTest.setDescription(updateData.getDescription());
            return originTest;

        }else return null;
    }

    @Transactional
    public void deleteTest(Long testId) {
        Test originTest = testRepository.findByTestId(testId);
        if(originTest != null) testRepository.delete(originTest);
    }
}
