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
class BoardCreateSvcImplTest {
    @Mock
    lateinit var boardQueryRepositoryImpl: BoardQueryRepository

    @InjectMocks
    lateinit var boardCreateSvcImpl: BoardCreateSvcImpl

    @Test
    fun `게시글 생성 정상 케이스`() {
        // Given
        val boardVo = BoardVo(memId = 1, boardTitle = "게시글 제목", boardContents = "게시글 내용")
        Mockito.lenient().`when`(boardQueryRepositoryImpl.createBoard(boardVo)).thenReturn(1)
        // When
        val isSuccess = boardCreateSvcImpl.createBoard(boardVo)
        // Then
        assertEquals(isSuccess, 1)
    }

    @ParameterizedTest(name = "[{index}] boardId={0}, memId={1}")
    @CsvSource(
        "-1, 1",
        "1, 0",
        "0, 0",
        "0, -1",
    )
    fun `게시글 생성 예외 케이스`(args: ArgumentsAccessor) {
        // Given
        val boardId: Long = args.get(0).toString().toLong()
        val memId: Long = args.get(1).toString().toLong()
        val boardVo = BoardVo(boardId = boardId, memId = memId, boardTitle = "게시글 제목", boardContents = "게시글 내용")
        Mockito.lenient().`when`(boardQueryRepositoryImpl.createBoard(boardVo)).thenReturn(1)
        // When, Then
        assertThrows(BusinessException::class.java) {
            boardCreateSvcImpl.createBoard(boardVo)
        }
    }
}
