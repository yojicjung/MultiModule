/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.hmcnetworks.appcore.board.usecase

/*
 * 정의 : 게시판 삭제 인터페이스
 */
interface BoardDelSvc {
    // 게시글 비공개 전환(boardId는 게시글 번호, memId는 게시글 주인 ID)
    fun hideBoard(boardId: Long, memId: Long): Long = 0

    // 게시글 바로 삭제(자신의 게시글만)
    fun deleteBoard(boardId: Long, memId: Long): Long = 0

    // 게시글 삭제(권한 없이 삭제)
    fun deleteBoard(boardId: Long): Long = 0
}
