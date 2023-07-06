package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(UserCreateRequest request) {
        userRepository.save(new User(request.getName(), request.getAge()));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponse::new).collect(Collectors.toList()); //더 깔끔한 코드
        //return users.stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getAge())).collect(Collectors.toList());
    }

    @Transactional
    //아이디를 기준으로 유저가 있는지 확인 후, 유저의 정보를 업데이트
    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

        user.updateName(request.getName());
        //userRepository.save(user); //user가 엔티티 상태이므로, user의 변경사항을 저장하거나 업데이트하는 쿼리를 날린다.
    }

    //메서드명을 기준으로 sql문을 날리게끔, Repository 인터페이스에
    public void deleteUser(String name) {
        User user = userRepository.findByName(name).get();
        if(user == null) {
            throw new IllegalArgumentException();
        }
        userRepository.delete(user);
    }
}