/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.domain.board.vo.BoardVo

/*
 * 정의 : 게시글 수정 목적 인터페이스
 */
interface BoardUpdtSvc {
    // 게시글 제목, 내용 수정 메서드
    fun updateBoard(boardVo: BoardVo): Long
}
