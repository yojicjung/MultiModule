package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.exception.BusinessException
import com.hmcnetworks.yojic.common.model.HmcPageImpl
import com.hmcnetworks.yojic.common.model.HmcPageRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

/*
 * 정의: 게시글 조회 테스트 클래스
 */
@ExtendWith(MockitoExtension::class)
class BoardReadSvcImplTest {
    @Mock
    lateinit var boardQueryRepositoryImpl: BoardQueryRepository

    @InjectMocks
    lateinit var boardReadSvcImpl: BoardReadSvcImpl

    @Test
    fun `게시글 조회 - 게시글 번호로 한건 조회`() {
        // Given
        val boardId: Long = 1
        val boardVo = BoardVo(boardId = boardId, memId = 1, boardTitle = "제목", boardContents = "내용")
        // When
        Mockito.lenient().`when`(boardQueryRepositoryImpl.readBoard(boardId)).thenReturn(boardVo)
        val expectationVo = boardReadSvcImpl.readBoard(boardId)
        // Then
        assertEquals(expectationVo, boardVo)
    }

    @ParameterizedTest(name = "[{index}] boardId={0}")
    @ValueSource(longs = [0, -1])
    fun `게시글 조회 - 게시글 번호로 한건 조회 예외 케이스`(boardId: Long) {
        // Given
        val boardVo = BoardVo(boardId = boardId, memId = 1, boardTitle = "제목", boardContents = "내용")
        Mockito.lenient().`when`(boardQueryRepositoryImpl.readBoard(boardId)).thenReturn(boardVo)
        // when, then
        assertThrows(BusinessException::class.java) {
            boardReadSvcImpl.readBoard(boardId)
        }
    }

    @Test
    fun `게시글 일괄 조회 - 기본 검색 조건`() {
        // Given
        val boardReadParam = BoardReadParam()
        println("정요직")
        println(boardReadParam.readSttsCode)
        val hmcPageRequest = HmcPageRequest(0, 10)
        val contents = mutableListOf(BoardVo(1, 1), BoardVo(2, 1))
        val returnObj = HmcPageImpl(contents, hmcPageRequest, contents.size.toLong(), 1)
        Mockito.lenient().`when`(boardQueryRepositoryImpl.readBoardListByReadCond(boardReadParam, hmcPageRequest))
            .thenReturn(returnObj)
        // When
        val boardVoList = boardReadSvcImpl.readBoardListByReadCond(boardReadParam, hmcPageRequest)
        // Then
        assertTrue(0 < boardVoList.totalElements)
    }
}
