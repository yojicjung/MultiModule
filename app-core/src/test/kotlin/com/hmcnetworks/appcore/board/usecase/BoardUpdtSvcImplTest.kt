package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.common.exception.BusinessException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BoardUpdtSvcImplTest {
    @Mock
    lateinit var boardQueryRepositoryImpl: BoardQueryRepository

    @InjectMocks
    lateinit var boardUpdtSvcImpl: BoardUpdtSvcImpl

    @Test
    fun `게시글 수정 정상 케이스`() {
        // Given
        val boardVo = BoardVo(memId = 1, boardId = 1, boardTitle = "updt 제목", boardContents = "updt 게시글")
        // When
        Mockito.lenient().`when`(boardQueryRepositoryImpl.updateBoard(boardVo)).thenReturn(1)
        val targetBoard = boardUpdtSvcImpl.updateBoard(boardVo)
        // Then
        assertEquals(1, targetBoard)
    }

    @ParameterizedTest(name = "[{index}] boardId={0}, memId={1}")
    @CsvSource(
        "0, 1",
        "1, 0",
        "-1, 1",
        "1, -1",
    )
    fun `게시글 수정 예외 케이스`(args: ArgumentsAccessor) {
        // Given
        val boardId: Long = args.get(0).toString().toLong()
        val memId: Long = args.get(1).toString().toLong()
        val boardVo = BoardVo(boardId = boardId, memId = memId, boardTitle = "updt 제목", boardContents = "updt 게시글")
        Mockito.lenient().`when`(boardQueryRepositoryImpl.updateBoard(boardVo)).thenReturn(1)
        // When, Then
        assertThrows(BusinessException::class.java) {
            boardUpdtSvcImpl.updateBoard(boardVo)
        }
    }
}
