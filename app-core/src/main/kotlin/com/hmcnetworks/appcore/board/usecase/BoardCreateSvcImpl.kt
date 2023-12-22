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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardCreateSvcImpl(
    @Autowired
    val boardQueryRepositoryImpl: BoardQueryRepository,
) : BoardCreateSvc {
    @Transactional
    override fun createBoard(boardVo: BoardVo): Long {
        // 게시글 식별자가 들어온 경우 예외 처리(오직 생성 목적 처리만 가능)
        if (boardVo.boardId.toInt() > 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "게시글 생성 목적 처리에서 게시글 수정 처리는 불가합니다.",
            )
        } else if (boardVo.boardId.toInt() < 0) {
            throw BusinessException(
                ExceptionCode.BUSINESS_RULE_INVALID,
                "생성 처리 대상 게시글의 식별정보가 올바르지 않습니다.",
            )
        }

        // 회원 식별자는 0 이하일 수 없음
        if (boardVo.memId <= 0) {
            throw BusinessException(ExceptionCode.BUSINESS_RULE_INVALID, "생성 처리 대상 게시글의 회원정보가 올바르지 않습니다.")
        }

        return boardQueryRepositoryImpl.createBoard(boardVo)
    }
}
