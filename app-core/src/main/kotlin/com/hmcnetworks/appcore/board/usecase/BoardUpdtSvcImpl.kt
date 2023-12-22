/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.exception.BusinessException
import com.hmcnetworks.yojic.common.exception.ExceptionCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*
 * 정의 : 게시글 수정 서비스 클래스
 */
@Service
class BoardUpdtSvcImpl(
    val boardQueryRepository: BoardQueryRepository,
) : BoardUpdtSvc {
    @Transactional
    override fun updateBoard(boardVo: BoardVo): Long {
        // 게시글 식별자 수정목적에 맞지 않거나 정상적이지 않은 경우
        if (boardVo.boardId.toInt() <= 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "수정 처리 대상 게시글의 식별정보가 올바르지 않습니다.",
            )
        }
        // 회원 식별자는 0 이하일 수 없음
        if (boardVo.memId <= 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "수정 처리 대상 게시글의 회원정보가 올바르지 않습니다.",
            )
        }

        // 게시글 수정
        return boardQueryRepository.updateBoard(boardVo)
    }
}
