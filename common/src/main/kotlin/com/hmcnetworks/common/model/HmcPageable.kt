/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.model

/*
 * 정의 : 페이징 요청 정보
 * param : pageSize - 한 페이지에 노출할 컨텐츠 갯수
 *         pageNumber - 요청 페이지 번호
 */
interface HmcPageable {
    val pageSize: Long
    val pageNumber: Long
    fun getOffset(): Long
}
