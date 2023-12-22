package com.hmcnetworks.appcore.board.usecase

import com.hmcnetworks.appcore.board.out.db.BoardQueryRepository
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
class BoardDelSvcImplTest {
    @Mock
    lateinit var boardQueryRepositoryImpl: BoardQueryRepository

    @InjectMocks
    lateinit var boardDelSvcImpl: BoardDelSvcImpl

    @Test
    fun `게시글 삭제 정상 케이스`() {
        // Given
        val boardId: Long = 1
        val memId: Long = 1
        Mockito.lenient().`when`(boardQueryRepositoryImpl.chngBoardVisibility(boardId, memId, 1)).thenReturn(1)
        // When
        val isSuccess = boardDelSvcImpl.hideBoard(boardId = boardId, memId = memId)
        // Then
        assertEquals(1, isSuccess)
    }

    @ParameterizedTest(name = "[{index}] boardId={0}, memId={1}")
    @CsvSource(
        "0, 1",
        "1, 0",
        "-1, 1",
        "1, -1",
    )
    fun `게시글 삭제 예외 케이스`(args: ArgumentsAccessor) {
        // Given
        val boardId: Long = args.get(0).toString().toLong()
        val memId: Long = args.get(1).toString().toLong()
        Mockito.lenient().`when`(boardQueryRepositoryImpl.chngBoardVisibility(boardId, memId, 1)).thenReturn(1)
        // When, Then
        assertThrows(BusinessException::class.java) {
            boardDelSvcImpl.hideBoard(boardId = boardId, memId = memId)
        }
    }
}
