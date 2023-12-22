/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.restapi.board.mapper

import com.hmcnetworks.domain.board.vo.BoardVo
import com.hmcnetworks.yojic.board.web.dto.BoardDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface BoardDtoConverter {

    companion object {
        val INSTANCE: BoardDtoConverter = Mappers.getMapper(BoardDtoConverter::class.java)
    }

    fun fromDtoToVo(boardDto: BoardDto): BoardVo

    fun fromVoToDto(boardVo: BoardVo): BoardDto
}
