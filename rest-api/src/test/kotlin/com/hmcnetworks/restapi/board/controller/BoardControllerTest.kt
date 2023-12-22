package com.hmcnetworks.restapi.board.controller

import com.hmcnetworks.appcore.board.usecase.BoardCreateSvc
import com.hmcnetworks.appcore.board.usecase.BoardDelSvc
import com.hmcnetworks.appcore.board.usecase.BoardReadSvc
import com.hmcnetworks.appcore.board.usecase.BoardUpdtSvc
import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.restapi.board.mapper.BoardDtoConverter
import com.hmcnetworks.yojic.board.web.dto.BoardDto
import com.hmcnetworks.yojic.board.web.dto.BoardReadParamDto
import com.hmcnetworks.yojic.common.model.CustomResTmpl
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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class BoardControllerTest {
    @Mock
    lateinit var boardReadSvcImpl: BoardReadSvc

    @Mock
    lateinit var boardCreateSvcImpl: BoardCreateSvc

    @Mock
    lateinit var boardUpdtSvcImpl: BoardUpdtSvc

    @Mock
    lateinit var boardDelSvcImpl: BoardDelSvc

    @InjectMocks
    lateinit var boardController: BoardController

    @Test
    fun `게시글 생성 요청`() {
        // Given
        val requestDto = BoardDto(
            memId = 1,
            boardTitle = "게시글 제목",
            boardContents = "게시글 내용",
        )
        val svcReturnVo = BoardVo(
            boardId = 1,
            memId = 1,
            boardTitle = "게시글 제목",
            boardContents = "게시글 내용",
            sysCreateTime = LocalDateTime.now(),
            sysUpdateTime = LocalDateTime.now(),
        )

        Mockito.lenient().`when`(boardCreateSvcImpl.createBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(requestDto)))
            .thenReturn(1)

        // When
        val responseEntity: ResponseEntity<CustomResTmpl<Long>> = boardController.createBoard(requestDto)

        // Then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertEquals(1, responseEntity.body?.data)
        Mockito.verify(boardCreateSvcImpl).createBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(requestDto))
    }

    @Test
    fun `게시글 수정 요청`() {
        // Given
        val requestDto = BoardDto(
            memId = 1,
            boardTitle = "게시글 수정",
            boardContents = "게시글 수정",
        )

        Mockito.lenient().`when`(boardUpdtSvcImpl.updateBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(requestDto)))
            .thenReturn(1)

        // When
        val responseEntity: ResponseEntity<CustomResTmpl<Long>> = boardController.updateBoard(requestDto)

        // Then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertEquals(1, responseEntity.body?.data)
        Mockito.verify(boardUpdtSvcImpl).updateBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(requestDto))
    }

    @Test
    fun `게시글 삭제 요청`() {
        // Given
        val requestDto = BoardDto(
            memId = 1,
            boardTitle = "게시글 수정",
            boardContents = "게시글 수정",
        )

        Mockito.lenient().`when`(boardDelSvcImpl.hideBoard(1, 1)).thenReturn(1)

        // When
        val responseEntity: ResponseEntity<CustomResTmpl<Long>> = boardController.deleteBoard(1)

        // Then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertEquals(1, responseEntity.body?.data)
        Mockito.verify(boardDelSvcImpl).hideBoard(1, 1)
    }

    // readBoardTest 필요
    @ParameterizedTest(name = "[{index}] boardId={0}, memId={1}")
    @CsvSource(
        "-1, 0,",
        "1, 0",
        "1, -1",
        "2, 0",
        "2, -1",
    )
    fun `게시글 일괄 조회 요청 - 예외 케이스`(args: ArgumentsAccessor) {
        // Given
        val readSttsCode: Int = args.get(0).toString().toInt()
        val memId: Long = args.get(1).toString().toLong()
        val requestDto = BoardReadParamDto(readSttsCode, memId)
        // When, Then
        assertThrows(IllegalArgumentException::class.java) {
            boardController.readBoard(requestDto)
        }
    }
}
