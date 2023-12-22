/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.yojic.common.model

import kotlin.math.ceil
/*
 * 정의 : 페이징 결과 제공 템플릿 클래스
 * param : content - 페이징 대상 컨텐츠
 *         pageable - 페이징 요청 정보(한 페이지 노출 컨텐츠 갯수, 요청 페이지 번호)
 *         total - content 갯수
 */
data class HmcPageImpl<T>(
    override val content: List<T>,
    override val pageable: HmcPageable,
    override val totalElements: Long,
    override var totalPages: Long? = null,
) : HmcPage<T> {
    init {
        this.totalPages = ceil(totalElements.toDouble() / pageable.pageSize.toDouble()).toLong()
    }

    // 다음 페이지 존재시 true
    override fun hasNext(): Boolean = pageable.pageNumber + 1 < totalPages!!

    // 다음 페이지 미존재시 true
    override fun isLast(): Boolean = !hasNext()
}
