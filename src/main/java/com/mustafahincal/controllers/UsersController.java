package com.mustafahincal.controllers;

import com.mustafahincal.business.abstracts.UserService;
import com.mustafahincal.core.utilities.results.DataResult;
import com.mustafahincal.core.utilities.results.ErrorDataResult;
import com.mustafahincal.core.utilities.results.Result;
import com.mustafahincal.core.utilities.results.SuccessDataResult;
import com.mustafahincal.entities.User;
import com.mustafahincal.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getall")
    public DataResult<List<User>> getAll() {
        return this.userService.getAll();
    }

    @GetMapping("/getbyemail")
    public DataResult<User> getByEmail(@RequestParam String email) {
        return this.userService.findByEmail(email);
    }

    @GetMapping("/getbyid")
    public DataResult<UserResponse> getById(@RequestParam int id) {
        UserResponse userResponse = new UserResponse(this.userService.findById(id).getData());
        return new SuccessDataResult<UserResponse>(userResponse);
    }

    @PostMapping("/add")
    public Result add(@Valid @RequestBody User user) {
        return this.userService.add(user);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody User user) {
        return this.userService.delete(user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return this.userService.update(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<String, String>();
        for (FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "Doğrulama Hataları");
        return errors;
    }
}
