/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.restapi.board.controller

import com.hmcnetworks.appcore.board.usecase.BoardCreateSvc
import com.hmcnetworks.appcore.board.usecase.BoardDelSvc
import com.hmcnetworks.appcore.board.usecase.BoardReadSvc
import com.hmcnetworks.appcore.board.usecase.BoardUpdtSvc
import com.hmcnetworks.domain.board.vo.BoardReadParam
import com.hmcnetworks.restapi.board.mapper.BoardDtoConverter
import com.hmcnetworks.yojic.board.web.dto.BoardDto
import com.hmcnetworks.yojic.board.web.dto.BoardReadParamDto
import com.hmcnetworks.yojic.common.model.CustomResTmpl
import com.hmcnetworks.yojic.common.model.HmcPage
import com.hmcnetworks.yojic.common.model.HmcPageImpl
import com.hmcnetworks.yojic.common.model.HmcPageRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class BoardController(
    val boardReadSvcImpl: BoardReadSvc,
    val boardCreateSvcImpl: BoardCreateSvc,
    val boardUpdtSvcImpl: BoardUpdtSvc,
    val boardDelSvcImpl: BoardDelSvc,
) {
    @GetMapping("/readBoardDetail")
    fun readBoardById(
        @RequestParam boardId: Int,
    ): ResponseEntity<CustomResTmpl<BoardDto>> {
        // boardId null test
        val boardVo = boardReadSvcImpl.readBoard(boardId.toLong())

        val boardDto = if (boardVo != null) BoardDtoConverter.INSTANCE.fromVoToDto(boardVo) else null
        // boardVo가 null인 경우
        return ResponseEntity.ok(CustomResTmpl<BoardDto>(data = boardDto))
    }

    @GetMapping("/readBoard")
    fun readBoard(
        @ModelAttribute readParamDto: BoardReadParamDto,
    ): ResponseEntity<CustomResTmpl<HmcPage<BoardDto>>> {
        // 조회 조건 셋팅
        val readParam = BoardReadParam(
            readSttsCode = readParamDto.readSttsCode ?: 0,
            memId = readParamDto.memId ?: 0,
            createTimeStrt = readParamDto.createTimeStrt?.let { LocalDateTime.parse(it) },
            createTimeEnd = readParamDto.createTimeEnd?.let { LocalDateTime.parse(it) },
            sortByLatestCreation = readParamDto.sortByLatestCreation == 1,
        )
        // 페이징 요청 정보
        val hmcPageable = HmcPageRequest.of(readParamDto.pageNumber ?: 0, readParamDto.pageSize ?: 10)
        // 조회
        val boardVoPageList = boardReadSvcImpl.readBoardListByReadCond(readParam, hmcPageable)
        // vo to dto 변환
        val dtoList = mutableListOf<BoardDto>()
        for (boardVo in boardVoPageList.content) {
            dtoList.add(BoardDtoConverter.INSTANCE.fromVoToDto(boardVo))
        }
        val resDtoPageList = HmcPageImpl(dtoList, boardVoPageList.pageable, boardVoPageList.totalElements)
        return ResponseEntity.ok(CustomResTmpl<HmcPage<BoardDto>>(data = resDtoPageList))
    }

    @PostMapping("/createBoard")
    fun createBoard(
        @Valid @RequestBody
        boardDto: BoardDto,
    ): ResponseEntity<CustomResTmpl<Long>> {
        // boardId 있는지 값 체크
        boardDto.memId = 1 // 하드코딩 추후 회원관리 개발되면 수정 예정
        val boardId = boardCreateSvcImpl.createBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(boardDto))
        return ResponseEntity.ok(CustomResTmpl<Long>(data = boardId))
    }

    @RequestMapping("/updateBoard", method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun updateBoard(
        @Valid @RequestBody
        boardDto: BoardDto,
    ): ResponseEntity<CustomResTmpl<Long>> {
        // boardId가 반드시 존재해야함(예외처리 구현해야함)
        // memId는 시큐리티 컨텍스트에서 가져올 예정
        // boardTitle, boardContents는 @Valid로 적용
        boardDto.memId = 1 // 하드코딩 추후 회원관리 개발되면 수정 예정
        val isSuccess = boardUpdtSvcImpl.updateBoard(BoardDtoConverter.INSTANCE.fromDtoToVo(boardDto))
        // 만약 결과가 1이 아닌 경우 예외처리??
        return ResponseEntity.ok(CustomResTmpl<Long>(data = isSuccess))
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    fun deleteBoard(
        @PathVariable boardId: Long,
    ): ResponseEntity<CustomResTmpl<Long>> {
        // boardId가 반드시 존재해야함(예외처리 구현해야함)
        val memId: Long = 1 // 하드코딩 추후 회원관리 개발되면 수정 예정
        val isSuccess = boardDelSvcImpl.hideBoard(boardId, memId)
        // 만약 결과가 1이 아닌 경우 예외처리??
        return ResponseEntity.ok(CustomResTmpl<Long>(data = isSuccess))
    }
}
