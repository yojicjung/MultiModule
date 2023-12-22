package com.hmcnetworks.restapi.board.controller

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dbaccess-test")
class BoardControllerGetEndPointTest(
    @Autowired val mockMvc: MockMvc,
) {
    @ParameterizedTest(name = "{index}-{displayName} {0}={1}")
    @CsvSource(
        ", , $.data.errCode, err3",
        "boardId, 0, $.data.errCode, err4",
        "boardId, 1, $.data.boardId, 1",
        "boardId, a, $.data.errCode, err2",
    )
    fun `(통합)게시글 번호로 조회 - 파라미터 테스트`(args: ArgumentsAccessor) {
        // given
        val paramKey = args.get(0)?.toString() // 파라미터
        val paramValue = args.get(1)?.toString() // 파라미터 값
        val expression = args.get(2).toString() // 테스트 표현식
        val expectation = args.get(3) // 기대값
        val mockReq = MockMvcRequestBuilders.get("/readBoardDetail")
        if (paramKey != null) mockReq.param(paramKey, paramValue)

        // when, then
        mockMvc.perform(mockReq)
            .andExpect(jsonPath(expression).value(expectation))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `(통합)게시글 조회 - 조회 조건 파라마터 없는 경우`() {
        // given
        val mockReq = MockMvcRequestBuilders.get("/readBoard")

        // when, then
        mockMvc.perform(mockReq)
            .andExpect(status().isOk).andExpect(
                jsonPath("$.data.content.size()")
                    .value(10),
            )
            .andExpect(
                jsonPath("$.data.pageable.pageNumber")
                    .value(0),
            )
    }

    @Test
    fun `(통합)게시글 조회 - 정렬 조건 파라미터 테스트`() {
        // given
        val mockReq = MockMvcRequestBuilders.get("/readBoard").param("sortByLatestCreation", "0")

        // when, then
        val result =
            mockMvc.perform(mockReq).andExpect(status().isOk).andExpect(jsonPath("$.data.content.size()").value(10))
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0)).andReturn()

        // jsonStr to json(추후 직렬화 수정 예정)
        val resBodyStr = result.response.contentAsString
        val jsonObject: JsonObject = Json.decodeFromString(resBodyStr)
        val content = jsonObject["data"]?.jsonObject?.get("content")?.jsonArray

        // 날짜 정렬 비교
        val firstIdxVal =
            LocalDateTime.parse(content?.get(0)?.jsonObject?.get("sysCreateTime").toString().replace("\"", ""))
        val secondIdxVal =
            LocalDateTime.parse(content?.get(1)?.jsonObject?.get("sysCreateTime").toString().replace("\"", ""))
        assertTrue(firstIdxVal.isBefore(secondIdxVal))
    }

    @Test
    fun `(통합)게시글 조회 - 정렬 조건 파라미터 타입 미스 테스트`() {
        // given
        val mockReq = MockMvcRequestBuilders.get("/readBoard").param("sortByLatestCreation", "a")

        // when, then
        mockMvc.perform(mockReq).andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(jsonPath("$.data.errCode").value("err1"))
    }

    @Test
    fun `(통합)게시글 조회 - 특정 회원 게시글 조회`() {
        // given
        val mockReq = MockMvcRequestBuilders.get("/readBoard").param("readSttsCode", "2").param("memId", "1")
        // when, then
        mockMvc.perform(mockReq).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.data.content.size()").value(10))
    }
}
