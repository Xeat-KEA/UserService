package org.userservice.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.userservice.userservice.dto.user.UserInfoRequest;
import org.userservice.userservice.dto.user.UserInfoResponse;
import org.userservice.userservice.dto.user.UserStatisticResponse;
import org.userservice.userservice.error.ErrorResponse;

@Tag(name = "User", description = "회원관리 API")
public interface UserApi {
    @Operation(summary = "사용자 문제풀이 통계 정보 조회",
            description = "사용자가 해결한 문제, 정식 등록된 문제, 점수, 등수에 대한 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserStatisticResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "사용자를 찾을 수 없는 경우", description = "해당하는 ID를 가진 사용자를 찾지 못하였을 경우", value = """
                                    {
                                        "status":"404",
                                        "code": "U001",
                                        "message": "사용자를 찾을 수 없습니다."
                                    }
                                    """),
                    }
                    )),
            @ApiResponse(responseCode = "500", description = "기타 바인딩 에러로 인한 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "binding error", description = "기타 바인딩 에러가 발생하였을 경우", value = """
                                    {
                                      "status": 500,
                                      "code": "BINDING_ERROR",
                                      "message": "Binding error occurred",
                                      "errors": [
                                        {
                                          "field": "nickName",
                                          "value": "nickname with spaces",
                                          "reason": "닉네임에는 공백이 포함될 수 없습니다."
                                        },
                                        {
                                          "field": "profileMessage",
                                          "value": "This status message is too long!",
                                          "reason": "상태 메세지는 최대 30자여야 합니다."
                                        }
                                      ]
                                    }
                                                                   
                                    """),
                    }
                    ))
    })
    ResponseEntity<?> findUserStatistics(String userId);

    @Operation(summary = "사용자 정보 조회",
            description = "내 정보 수정 탭에서 확인할 수 있는 사용자 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "사용자를 찾을 수 없는 경우", description = "해당하는 ID를 가진 사용자를 찾지 못하였을 경우", value = """
                                    {
                                        "status":"404",
                                        "code": "U001",
                                        "message": "사용자를 찾을 수 없습니다."
                                    }
                                    """),
                    }
                    ))
    })
    ResponseEntity<?> findUserInfos(String userId);

    @Operation(summary = "사용자 정보 수정",
            description = "내 정보 수정 탭에서 수정한 사용자 정보를 업데이트 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "사용자를 찾을 수 없는 경우", description = "해당하는 ID를 가진 사용자를 찾지 못하였을 경우", value = """
                                    {
                                        "status":"404",
                                        "code": "U001",
                                        "message": "사용자를 찾을 수 없습니다."
                                    }
                                    """),
                    }
                    )),
            @ApiResponse(responseCode = "500", description = "수정 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "파일 업로드에 실패한 경우", description = "파일 업로드 실패하였을 경우", value = """
                                    {
                                        "status":"500",
                                        "code": "C006",
                                        "message": "파일 업로드에 실패했습니다."
                                    }
                                    """),
                            @ExampleObject(name = "binding error", description = "기타 바인딩 에러가 발생하였을 경우", value = """
                                    {
                                      "status": 500,
                                      "code": "BINDING_ERROR",
                                      "message": "Binding error occurred",
                                      "errors": [
                                        {
                                          "field": "nickName",
                                          "value": "nickname with spaces",
                                          "reason": "닉네임에는 공백이 포함될 수 없습니다."
                                        },
                                        {
                                          "field": "profileMessage",
                                          "value": "This status message is too long!",
                                          "reason": "상태 메세지는 최대 30자여야 합니다."
                                        }
                                      ]
                                    }
                                                                   
                                    """),
                    }
                    ))
    })
    ResponseEntity<?> updateUserInfos(
            @Parameter(description =
                    "1. 닉네임 \n" +
                            "2. 상태 메세지 \n" +
                            "3. 기본 프로그래밍 언어", required = true) UserInfoRequest userInfoRequest,
            @Parameter(description = "4. 프로필 사진")
            MultipartFile file, String userId);
}
