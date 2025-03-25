package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.User;
import com.voting.kura.repository.UserRepository;
import com.voting.kura.util.CourseCodeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new VotingException("User not found with id: " + id));
    }

    public User createUser(User user) {
        if (userRepository.existsByAdmissionNumber(user.getAdmissionNumber())) {
            throw new VotingException("User already exists with admission number: " + user.getAdmissionNumber());
        }

        // Parse course code and set fields
        Map<String, String> parsedCode = CourseCodeParser.parse(user.getAdmissionNumber());
        user.setFacultyCode(parsedCode.get("facultyCode"));
        user.setDepartmentCode(parsedCode.get("departmentCode"));
        user.setSequentialNumber(parsedCode.get("sequentialNumber"));
        user.setAdmissionYear(parsedCode.get("admissionYear"));

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setNumberTwo(userDetails.isNumberTwo());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}