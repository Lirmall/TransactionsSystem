package ru.klokov.tsaccounts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.mappers.UserEntityMapper;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tsaccounts.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/common/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserEntityMapper userEntityMapper;

    @Operation(
            summary = "Get all users",
            method = "get")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public List<UserDto> findAll() {
        List<UserModel> userModels = userService.findAll();
        return userModels.stream().map(userEntityMapper::convertModelToDTO).toList();
    }
}
