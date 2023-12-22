/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
/*
 * 정의 : 커스텀 예외 코드 클래스
 */
enum class ExceptionCode(
    val errCode: String, // 커스텀 예외 코드
    val errStatus: HttpStatusCode,
    val errTitle: String,
) {
    INVALID_OBJECT_PROPERTY(errCode = "err1", errStatus = HttpStatus.BAD_REQUEST, errTitle = "객체 속성 값 valid 오류"),
    PARAM_TYPE_MISS_MATCH(errCode = "err2", errStatus = HttpStatus.BAD_REQUEST, errTitle = "요청 파라미터 타입 불일치"),
    MISSING_REQUEST_PARAM(errCode = "err3", errStatus = HttpStatus.BAD_REQUEST, errTitle = "요청 파라미터 미존재"),
    BUSINESS_RULE_INVALID(errCode = "err4", errStatus = HttpStatus.INTERNAL_SERVER_ERROR, errTitle = "도메인 규칙 위반")
}
