/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.dbaccess.board.adapter

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.dbaccess.board.entity.QBoard
import com.hmcnetworks.dbaccess.board.mapper.BoardEntityConverter
import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.model.HmcPage
import com.hmcnetworks.yojic.common.model.HmcPageImpl
import com.hmcnetworks.yojic.common.model.HmcPageable
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


/*
 * 정의 : 게시판 쿼리 리포지토리
 */
@Repository
class BoardQueryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val em: EntityManager
) : BoardQueryRepository {
    // 게시글 생성 메서드
    override fun createBoard(boardVo: BoardVo): Long {
        var board = BoardEntityConverter.INSTANCE.fromVoToEntity(boardVo)
        em.persist(board)
        return board.boardId
    }

    // 게시글 수정 메서드
    override fun updateBoard(boardVo: BoardVo): Long {
        val board = QBoard.board
        return jpaQueryFactory
            .update(board)
            .set(board.boardTitle, boardVo.boardTitle)
            .set(board.boardContents, boardVo.boardContents)
            .set(board.sysUpdateTime, LocalDateTime.now())
            .where(
                board.boardId.eq(boardVo.boardId),
                board.memId.eq(boardVo.memId),
            )
            .execute()
    }

    // 게시글 공개/비공개 여부 변경 메서드
    override fun chngBoardVisibility(boardId: Long, memId: Long, visibleStts: Int): Long {
        val board = QBoard.board
        return jpaQueryFactory
            .update(board)
            .set(board.visibleStts, visibleStts)
            .where(
                board.boardId.eq(boardId),
                board.memId.eq(memId),
            )
            .execute()
    }

    // 게시글 boardId로 조회
    override fun readBoard(boardId: Long): BoardVo? {
        val board = QBoard.board
        return jpaQueryFactory
            .select(
                Projections.fields(
                    BoardVo::class.java,
                    board.boardId,
                    board.boardTitle,
                    board.boardContents,
                    board.sysCreateTime,
                    board.sysUpdateTime,
                ),
            ).from(board)
            .where(
                board.boardId.eq(boardId),
                board.visibleStts.eq(0),
            )
            .fetchOne()
    }

    // 게시글 조회 조건에 의해 조회
    override fun readBoardListByReadCond(boardReadParam: BoardReadParam, hmcPageable: HmcPageable): HmcPage<BoardVo> {
        val board = QBoard.board
        val contents: List<BoardVo> = jpaQueryFactory
            .select(
                Projections.fields(
                    BoardVo::class.java,
                    board.boardId,
                    board.boardTitle,
                    board.boardContents,
                    board.sysCreateTime,
                    board.sysUpdateTime,
                ),
            ).from(board)
            .where(
                memIdEqOrNotEq(boardReadParam.readSttsCode, boardReadParam.memId),
                boardCreateTimeBetween(boardReadParam.createTimeStrt, boardReadParam.createTimeEnd),
                board.visibleStts.eq(0),
            ).orderBy(
                sortCreateTime(boardReadParam.sortByLatestCreation),
                sortCreateTimeEqual(boardReadParam.sortByLatestCreation),
            )
            .offset(hmcPageable.getOffset())
            .limit(hmcPageable.pageSize)
            .fetch()

        // 첫 페이지 조회시 조회된 게시글의 갯수가 페이지 사이즈 보다 작은 경우, 전체 게시글 갯수는 조회된 게시글의 갯수
        return if (hmcPageable.pageNumber.toInt() == 0 && contents.size < hmcPageable.pageSize) {
            HmcPageImpl(contents, hmcPageable, contents.size.toLong())
        } else {
            val contentsCnt: Long = jpaQueryFactory
                .select(
                    board.count(),
                ).from(board)
                .where(
                    memIdEqOrNotEq(boardReadParam.readSttsCode, boardReadParam.memId),
                    boardCreateTimeBetween(boardReadParam.createTimeStrt, boardReadParam.createTimeEnd),
                    board.visibleStts.eq(0),
                ).orderBy(sortCreateTime(boardReadParam.sortByLatestCreation))
                .fetchFirst()
            HmcPageImpl(contents, hmcPageable, contentsCnt)
        }
    }

    fun memIdEqOrNotEq(readSttsCode: Int, memId: Long): BooleanExpression? {
        return when (readSttsCode) {
            1 -> QBoard.board.memId.ne(memId) // readSttsCode=1인 경우 해당 회원 글 제외
            2 -> QBoard.board.memId.eq(memId) // readSttsCode=2인 경우 해당 회원 글만
            else -> null
        }
    }

    fun boardCreateTimeBetween(createTimeStrt: LocalDateTime?, createTimeEnd: LocalDateTime?): BooleanExpression? {
        return if (createTimeStrt != null && createTimeEnd != null) {
            QBoard.board.sysCreateTime.between(createTimeStrt, createTimeEnd)
        } else {
            null
        }
    }

    fun sortCreateTime(sortByLatestCreation: Boolean): OrderSpecifier<LocalDateTime>? {
        return if (sortByLatestCreation) {
            QBoard.board.sysCreateTime.desc()
        } else {
            QBoard.board.sysCreateTime.asc()
        }
    }

    // 생성날짜가 같은 경우 pk가 두번째 정렬 조건
    fun sortCreateTimeEqual(sortByLatestCreation: Boolean): OrderSpecifier<Long>? {
        return if (sortByLatestCreation) {
            QBoard.board.boardId.desc() // 최신순 생성날짜 같은 경우 pk 내림차순
        } else {
            QBoard.board.boardId.asc() // 오래된순 생성날짜 같은 경우 pk 오름차순
        }
    }
}
