/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.board.web.dto

import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

data class BoardDto(
    var boardId: Long = 0,
    var memId: Long = 0,
    @field:Length(min = 1, max = 30, message = "제목은 1글자 이상 30글자 이하로 작성해주세요.")
    val boardTitle: String,
    @field:Length(min = 1, max = 1000, message = "게시글은 1글자 이상 1000글자 이하로 작성해주세요.")
    val boardContents: String,
    var sysUpdateTime: LocalDateTime? = null,
    var sysCreateTime: LocalDateTime? = null,
)
