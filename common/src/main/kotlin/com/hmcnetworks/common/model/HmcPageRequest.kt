/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.time.times

/*
 * 정의 : 페이징 요청 정보 객체
 * 설명 : 한 페이지 크기, 요청 페이지 번호
 */
class HmcPageRequest(
    override val pageNumber: Long,
    override val pageSize: Long
) : HmcPageable {
    companion object {
        fun of(pageNumber: Long, pageSize: Long) = HmcPageRequest(pageNumber = pageNumber, pageSize = pageSize)
    }

    @JsonIgnore
    override fun getOffset(): Long = pageNumber * pageSize
}
