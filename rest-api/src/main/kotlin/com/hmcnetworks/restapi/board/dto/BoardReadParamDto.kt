/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.board.web.dto

/*
 * 정의 : 클라이언트 파라미터 받는 DTO
 */
data class BoardReadParamDto(
    val readSttsCode: Int? = 0,
    val memId: Long? = 0,
    val createTimeStrt: String? = null,
    val createTimeEnd: String? = null,
    val sortByLatestCreation: Int? = 1,
    val pageSize: Long? = 10,
    val pageNumber: Long? = 0
)
