package org.userservice.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.userservice.userservice.dto.adminclient.AnnounceResponse;
import org.userservice.userservice.dto.codebankclient.UserScoreRequest;
import org.userservice.userservice.dto.user.UserInfoRequest;
import org.userservice.userservice.dto.user.UserInfoResponse;
import org.userservice.userservice.dto.user.UserListResponse;
import org.userservice.userservice.dto.user.UserStatisticResponse;
import org.userservice.userservice.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping("/statistics")
    public ResponseEntity<?> findUserStatistics(@RequestHeader("UserId") String userId) {
        return ResponseEntity.ok(userService.findUserStatisticsAndUpdateRankByUserId(userId));
    }

    @Override
    @GetMapping("/userInfo")
    public ResponseEntity<?> findUserInfos(@RequestHeader("UserId") String userId) {
        ;
        return ResponseEntity.ok(userService.findUserInfoByUserId(userId));
    }

    @Override
    @PutMapping("/userInfo")
    public ResponseEntity<?> updateUserInfos(
            @Validated @RequestBody UserInfoRequest userInfoRequest,
            @RequestHeader("UserId") String userId) {
        return ResponseEntity.ok(userService.updateUserInfoByUserId(userInfoRequest, userId));
    }

    //blog-service -> user-service
    @Override
    @GetMapping("/info")
    public ResponseEntity<?> findUserInfoForBlogService(@RequestHeader("UserId") String userId) {
        return ResponseEntity.ok(userService.findUserInfoForBlogService(userId));
    }

    //user-service -> admin-service
    @Override
    @GetMapping("/announce")
    public ResponseEntity<Page<AnnounceResponse>> getAnnouncementsFromAdminService(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAnnouncementsFromAdminService(page, size));
    }

    @Override
    @GetMapping("/announce/{announceId}")
    public ResponseEntity<?> getAnnouncementDetailsFromAdminService(@PathVariable long announceId) {
        return ResponseEntity.ok(userService.getAnnouncementDetailsFromAdminService(announceId));
    }

    //code-service -> user-service
    @Override
    @PutMapping("/score")
    public ResponseEntity<?> updateScore(@RequestBody UserScoreRequest userScoreRequest) {
        userService.calculateTierAndUpdateScore(userScoreRequest);
        return ResponseEntity.ok("점수 및 티어 업데이트 완료");
    }

    @Override
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("UserId") String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserListResponse>> getUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String input) {

        Page<UserListResponse> userList = userService.getUserList(page, size, input);
        return ResponseEntity.ok(userList);
    }

    //초기화
    @GetMapping("/init/{userId}")
    public ResponseEntity<?> getUserInfosForAdminService(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.findUserInfosForAdminService(userId));
    }

    @PostMapping("/init/nickname")
    public ResponseEntity<?> initNickName(@RequestParam("UserId") String userId) {
        return ResponseEntity.ok(userService.initNickName(userId));
    }

    @PostMapping("/init/profile")
    public ResponseEntity<?> initProfileImage(@RequestParam("UserId") String userId) {
        return ResponseEntity.ok(userService.initProfileImage(userId));
    }

    @PostMapping("/init/message")
    public ResponseEntity<?> initStatusMessage(@RequestParam("UserId") String userId) {
        return ResponseEntity.ok(userService.initStatusMessage(userId));
    }
}
