/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.yojic.common.exception.BusinessException
import com.hmcnetworks.yojic.common.exception.ExceptionCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*
 * 정의 : 게시판 삭제 서비스
 */
@Service
class BoardDelSvcImpl(
    private val boardQueryRepository: BoardQueryRepository,
) : BoardDelSvc {
    // 게시글 비공개로 전환(클라이언트단 사용자가 삭제 요청한 경우)
    @Transactional
    override fun hideBoard(boardId: Long, memId: Long): Long {
        // 게시글 번호는 0 이하일 수 없음
        if (boardId.toInt() <= 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "삭제 처리 대상 게시글의 식별정보가 올바르지 않습니다.",
            )
        }
        // 회원 식별자는 0 이하일 수 없음
        if (memId.toInt() <= 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "삭제 처리 대상 게시글의 회원정보가 올바르지 않습니다.",
            )
        }
        // 사용자가 삭제 요청한 게시글 비공개로 전환
        return boardQueryRepository.chngBoardVisibility(boardId, memId, 1)
    }
}
