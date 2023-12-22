/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.exception

import org.springframework.http.HttpStatusCode
import java.util.*

/*
 * 정의 : 예외 메시지 공통 템플릿 클래스
 * 용도 : 클라이언트에 전달할 에러 메시지(에러 식별할 수 있는 최소한 정보만 제공)
 */
class ExceptionMsgTmpl(
    val errCode: String, // 커스텀 에러 코드
    val errStatus: HttpStatusCode,
    val errTitle: String,
    val errLogId: String
) {
    companion object {
        fun makeExceptionMsg(exCode: ExceptionCode): ExceptionMsgTmpl {
            return ExceptionMsgTmpl(
                errCode = exCode.errCode,
                errStatus = exCode.errStatus,
                errTitle = exCode.errTitle,
                errLogId = "${exCode.errCode}-${UUID.randomUUID()}",
            )
        }
    }

    override fun toString(): String {
        return "[errCode : $errCode, errStatus : $errStatus, errTitle : $errTitle, errLogId : $errLogId]"
    }
}
