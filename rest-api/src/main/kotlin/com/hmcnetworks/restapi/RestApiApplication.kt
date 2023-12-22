package com.hmcnetworks.restapi

import com.hmcnetworks.dbaccess.config.jpa.QueryDslConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
    value = [QueryDslConfig::class],
)
@EntityScan(basePackages = ["com.hmcnetworks.dbaccess.board"])
@ComponentScan(basePackages = ["com.hmcnetworks.dbaccess.board", "com.hmcnetworks.appcore", "com.hmcnetworks.restapi.board"])
class RestApiApplication

fun main(args: Array<String>) {
    runApplication<RestApiApplication>(*args)
}
