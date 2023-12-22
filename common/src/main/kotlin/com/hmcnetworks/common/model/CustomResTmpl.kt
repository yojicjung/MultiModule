/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.model

import java.time.LocalDateTime

/*
 * 정의 : responseEntity 용 API Response 템플릿
 */
data class CustomResTmpl<T>(
    val showMsgAlert: Boolean = false, // message내용 클라이언트단에서 경고창 출력 여부
    val message: String? = null, // 클라이언트에 전달할 메시지 내용
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val data: T? = null, // 전달 객체
)
