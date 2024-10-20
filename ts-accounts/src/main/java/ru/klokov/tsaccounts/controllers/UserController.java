package ru.klokov.tsaccounts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.klokov.tsaccounts.dtos.CreateOrUpdateUserDto;
import ru.klokov.tscommon.dtos.UserDto;
import ru.klokov.tsaccounts.mappers.UserEntityMapper;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tsaccounts.services.UserService;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.specifications.search_models.UserSearchModel;

import java.util.List;

@Slf4j
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

    @Operation(
            summary = "Get user by id",
            method = "get")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        UserModel user = userService.findById(id);
        return userEntityMapper.convertModelToDTO(user);
    }

    @Operation(
            summary = "Find users by filter",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/filter")
    public PagedResult<UserDto> findByFilter(@RequestBody UserSearchModel model) {
        Page<UserModel> modelsPage = userService.findByFilter(model);
        return new PagedResult<>(modelsPage.map(userEntityMapper::convertModelToDTO));
    }

    @Operation(
            summary = "Create new user",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public UserDto create(@RequestBody CreateOrUpdateUserDto userDto) {
        log.debug("Try to create user from controller");
        UserModel model = userService.create(userDto);
        return userEntityMapper.convertModelToDTO(model);
    }

    @Operation(
            summary = "Update user",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/update")
    public UserDto updateUser(@RequestParam Long id, @RequestBody UserDto userDto) {
        log.debug("Try to update user from controller");
        UserModel model = userService.updateUserById(id, userDto);
        return userEntityMapper.convertModelToDTO(model);
    }
}
