package com.hmcnetworks.dbaccess.board.adapter

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.model.HmcPageRequest
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = ["com.hmcnetworks.dbaccess.config.jpa", "com.hmcnetworks.dbaccess.board"])
@ActiveProfiles("dbaccess-test")
class BoardQueryRepositoryImplTest() {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var boardQueryRepositoryImpl: BoardQueryRepository

    @BeforeEach
    fun beforeEach() {
        boardQueryRepositoryImpl = BoardQueryRepositoryImpl(JPAQueryFactory(em), em)
    }

    @Test
    fun `게시글 생성 쿼리 테스트`() {
        // given
        val boardVo = BoardVo(memId = 1, boardTitle = "제목 test", boardContents = "내용 test")
        // when
        val boardId = boardQueryRepositoryImpl.createBoard(boardVo)
        // then
        assertTrue(0 < boardId)
    }

    @Test
    fun `게시글 수정 쿼리 테스트`() {
        // given
        val boardVo = BoardVo(boardId = 1, memId = 1, boardTitle = "제목 update test", boardContents = "내용 update test")
        // when
        val successCnt = boardQueryRepositoryImpl.updateBoard(boardVo)
        // then
        assertTrue(0 < successCnt)
    }

    @Test
    fun `게시글 삭제 쿼리 테스트`() {
        // given
        val boardVo = BoardVo(boardId = 1, memId = 1, boardTitle = "제목 update test", boardContents = "내용 update test")
        // when
        val successCnt = boardQueryRepositoryImpl.chngBoardVisibility(1, 1, 0)
        // then
        assertTrue(0 < successCnt)
    }

    @ParameterizedTest(name = "boardId={0}")
    @CsvSource(
        "1, 1", // 공개 게시글 조회
        "2, 0", // 비공개 게시글 조회
    )
    fun `게시글 조회 쿼리 테스트 - 게시글 한건 조회`(args: ArgumentsAccessor) {
        // Given
        val boardId: Long = args.get(0).toString().toLong()
        val expectation: Long = args.get(1).toString().toLong()
        // when
        val boardVo = boardQueryRepositoryImpl.readBoard(boardId)
        // then
        assertEquals(expectation, boardVo?.boardId ?: 0)
    }

    @Test
    fun `게시글 일괄 조회 쿼리 테스트 - 기본 검색 조건`() {
        // Given
        val defaultReadParam = BoardReadParam()
        val pageRequest = HmcPageRequest(0, 10)
        // when
        var boardList = boardQueryRepositoryImpl.readBoardListByReadCond(defaultReadParam, pageRequest)
        // then
        assertThat(boardList.totalElements).isGreaterThan(0)
        val isSorted = boardList.content[0].sysCreateTime?.isAfter(boardList.content[9].sysCreateTime) ?: false
        assertTrue(isSorted) // 기본 정렬 옵션 테스트
    }

    @Test
    fun `게시글 일괄 조회 쿼리 테스트 - 특정 회원 조건`() {
        // Given
        val defaultReadParam = BoardReadParam() // 기본 전체 조회
        val specificMemOnly = BoardReadParam(1, 1) // 특정회원 포함
        val specificMemExcept = BoardReadParam(2, 1) // 특정회원 제외
        val pageRequest = HmcPageRequest(0, 10)
        // when
        var defaultList = boardQueryRepositoryImpl.readBoardListByReadCond(defaultReadParam, pageRequest)
        var specificMemOnlyList = boardQueryRepositoryImpl.readBoardListByReadCond(specificMemOnly, pageRequest)
        var specificMemExceptList = boardQueryRepositoryImpl.readBoardListByReadCond(specificMemExcept, pageRequest)
        // then
        val totalCnt = defaultList.totalElements
        val specificMenBoardCnt = specificMemOnlyList.totalElements
        val specificMenExceptBoardCnt = specificMemExceptList.totalElements
        assertThat(specificMenBoardCnt + specificMenExceptBoardCnt == totalCnt)
    }

    @Test
    fun `게시글 일괄 조회 쿼리 테스트 - 생성날짜 기간 조건`() {
        // Given
        val defaultReadParam = BoardReadParam() // 기본 전체 조회
        val strtTime = LocalDateTime.parse("2023-11-23T00:00:00")
        val endTime = LocalDateTime.parse("2023-11-24T00:00:00")
        val creationPeriodParam = BoardReadParam(createTimeStrt = strtTime, createTimeEnd = endTime) // 특정기간
        val pageRequest = HmcPageRequest(0, 10)
        // when
        var defaultList = boardQueryRepositoryImpl.readBoardListByReadCond(defaultReadParam, pageRequest)
        var creationPeriodResult = boardQueryRepositoryImpl.readBoardListByReadCond(creationPeriodParam, pageRequest)
        // then
        val totalCnt = defaultList.totalElements
        val creationPeriodResultCnt = creationPeriodResult.totalElements
        assertThat(creationPeriodResultCnt < totalCnt)
    }

    @Test
    fun `게시글 일괄 조회 쿼리 테스트 - 정렬 조건`() {
        // Given
        val latestParam = BoardReadParam(sortByLatestCreation = true) // 최신순
        val oldestParam = BoardReadParam(sortByLatestCreation = false) // 오랜된순
        val pageRequest = HmcPageRequest(0, 10)
        // when
        var boardListByLatest = boardQueryRepositoryImpl.readBoardListByReadCond(latestParam, pageRequest)
        var boardListByOldest = boardQueryRepositoryImpl.readBoardListByReadCond(oldestParam, pageRequest)
        // then
        assertTrue(
            boardListByLatest.content[0].sysCreateTime?.isAfter(boardListByLatest.content[9].sysCreateTime) ?: false,
        )
        assertTrue(
            boardListByOldest.content[0].sysCreateTime?.isBefore(boardListByOldest.content[9].sysCreateTime) ?: false,
        )
    }
}
