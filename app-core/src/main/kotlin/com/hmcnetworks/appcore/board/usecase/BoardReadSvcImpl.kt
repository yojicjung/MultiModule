/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.exception.BusinessException
import com.hmcnetworks.yojic.common.exception.ExceptionCode
import com.hmcnetworks.yojic.common.model.HmcPage
import com.hmcnetworks.yojic.common.model.HmcPageable
import org.springframework.stereotype.Service

/*
 * 정의: 게시글 조회 구현 클래스
 */
@Service
class BoardReadSvcImpl(
    val boardQueryRepository: BoardQueryRepository
) : BoardReadSvc {
    // 게시글 번호로 한건 조회
    override fun readBoard(boardId: Long): BoardVo? {
        if (boardId <= 0) throw BusinessException(ExceptionCode.BUSINESS_RULE_INVALID, "조회 대상 게시글의 식별정보가 올바르지 않습니다.")
        return boardQueryRepository.readBoard(boardId)
    }

    // 검색 조건에 따른 게시글 조회
    override fun readBoardListByReadCond(boardReadParam: BoardReadParam, hmcPageable: HmcPageable): HmcPage<BoardVo> {
        return boardQueryRepository.readBoardListByReadCond(boardReadParam, hmcPageable)
    }
}
