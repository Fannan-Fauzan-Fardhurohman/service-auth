package com.fanxan.serviceauth.controller;

import com.fanxan.serviceauth.model.dto.response.GetUserDTO;
import com.fanxan.serviceauth.model.dto.response.UserDTO;
import com.fanxan.serviceauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a user",
            description = "Register user",
            responses = @ApiResponse(
                    description = HttpConstant.OK,
                    responseCode = HttpConstant.CODE_OK,
                    useReturnTypeSchema = true
            )
    )
    public BaseResponse<GetUserDTO> registerUser(@RequestBody UserDTO user) throws Exception {
        return new BaseResponse<>(
                HttpConstant.SUCCESS,
                HttpStatus.OK,
                userService.register(user)
        );
    }
}
