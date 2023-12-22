/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.model
/*
 * 정의 : 페이징 결과 제공 템플릿
 * param : content - 페이징 대상 컨텐츠
 *         pageable - 페이징 요청 정보(한 페이지 노출 컨텐츠 갯수, 요청 페이지 번호)
 *         total - content 갯수
 */
interface HmcPage<T> {
    val content: List<T>
    val pageable: HmcPageable
    val totalElements: Long
    val totalPages: Long?
    fun hasNext(): Boolean
    fun isLast(): Boolean
}
