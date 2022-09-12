package antifraud.app.controller;

import antifraud.app.DTO.*;
import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.User;
import antifraud.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.controller
 */
@RestController
@RequestMapping("/api/auth")
public class UserRegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper mapper;

    @PutMapping(path = "/role", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<UserDTO> changeUserRole(@RequestBody UserUpdateRoleDTO userUpdateRoleDTO) throws IllegalAccessException {
        userService.updateUserRole(userUpdateRoleDTO);
        User userByUsername = userService.getUserByUsername(userUpdateRoleDTO.getUsername());
        return new ResponseEntity<UserDTO>(mapper.map(userByUsername, UserDTO.class), HttpStatus.OK);
    }

    @PutMapping(path = "/access", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<UserStatusDTO> enableOrDisableUser(@RequestBody UserEnableDTO userEnableDTO) {
        String userStatus = userService.enableDisableUser(userEnableDTO);
        UserStatusDTO userStatusDTO = new UserStatusDTO();
        userStatusDTO.setStatus(userStatus);
        return new ResponseEntity<UserStatusDTO>(userStatusDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        userService.registerNewUser(user);
        UserDTO mapped = mapper.map(userService.getUserByUsername(user.getUsername()), UserDTO.class);
        return new ResponseEntity<UserDTO>(mapped, HttpStatus.CREATED);
    }

    @GetMapping(path = "/list", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers.stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList()));
    }

    @DeleteMapping(path = "/user/{username}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<UserDeleteDTO> deleteUser(@PathVariable String username) {
        User userByUsername = userService.getUserByUsername(username);
        UserDeleteDTO deleteDTO = mapper.map(userByUsername, UserDeleteDTO.class);
        userService.removeUser(username);
        return ResponseEntity.ok(deleteDTO);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleBadRequest(ConstraintViolationException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicationEntityException.class})
    public ResponseEntity handleBadRequest(DuplicationEntityException e, WebRequest request) {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}