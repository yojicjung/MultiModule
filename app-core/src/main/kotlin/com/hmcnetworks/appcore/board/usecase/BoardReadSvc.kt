/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.model.HmcPage
import com.hmcnetworks.yojic.common.model.HmcPageable


/*
 * 정의 : 게시글 조회 인터페이스
 */
interface BoardReadSvc {
    // 게시글 상세 조회
    fun readBoard(boardId: Long): BoardVo?

    // 검색 조건에 따른 게시글 조회
    fun readBoardListByReadCond(boardReadParam: BoardReadParam, hmcPageable: HmcPageable): HmcPage<BoardVo>
}
