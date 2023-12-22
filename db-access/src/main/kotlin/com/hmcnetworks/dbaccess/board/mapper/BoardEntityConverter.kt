/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hmcnetworks.dbaccess.board.mapper

import com.hmcnetworks.dbaccess.board.entity.Board
import com.hmcnetworks.domain.board.vo.BoardVo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers


@Mapper
interface BoardEntityConverter {

    companion object {
        val INSTANCE: BoardEntityConverter = Mappers.getMapper(BoardEntityConverter::class.java)
    }

    fun fromEntityToVo(boardEntity: Board): BoardVo
    @Mapping(target = "visibleStts", ignore = true)
    @Mapping(target = "sysUpdateTime", ignore = true)
    @Mapping(target = "sysCreateTime", ignore = true)
    fun fromVoToEntity(boardVo: BoardVo): Board
}
