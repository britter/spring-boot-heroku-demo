/*
 * Copyright 2016 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.britter.springbootherokudemo

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.ui.ModelMap
import org.springframework.validation.MapBindingResult
import org.springframework.validation.ObjectError

@ExtendWith(MockKExtension::class)
class HomeControllerTest {

    private lateinit var map: ModelMap
    private lateinit var ctrl: HomeController
    private lateinit var repository: RecordRepository

    @BeforeEach
    fun setUp() {
        map = ModelMap()
        repository = mockk(relaxed = true)
        ctrl = HomeController(repository)
    }

    @Nested
    internal inner class Home {

        @Test
        fun shouldAddInsertRecordToModelMap() {
            ctrl.home(map)

            assertThat(map).containsKey("insertRecord")
            assertThat(map["insertRecord"]).isInstanceOf(Record::class.java)

            val insertRecord = map["insertRecord"] as Record
            assertThat(insertRecord.data).isNull()
        }

        @Test
        fun shouldQueryRepositoryForAllRecords() {
            ctrl.home(map)

            verify(exactly = 1) { repository.findAll() }
            confirmVerified(repository)
        }

        @Test
        fun shouldAddRecordsFromRepositoryToModelMap() {
            every { repository.findAll() }.returns(listOf(Record(), Record(), Record()))

            ctrl.home(map)

            assertThat(map).containsKey("records")
            assertThat(map["records"]).isInstanceOf(List::class.java)

            @Suppress("UNCHECKED_CAST")
            val records = map["records"] as List<Record>
            assertThat(records).hasSize(3)
        }
    }

    @Nested
    internal inner class InsertData {

        private lateinit var bindingResult: MapBindingResult
        private val record = Record()

        @BeforeEach
        fun setUp() {
            bindingResult = MapBindingResult(mutableMapOf<Any, Any>(), "insertRecord")
            every { repository.save(allAny<Record>()) }.returns(record)
        }

        @Test
        fun shouldSaveRecordWhenThereAreNoErrors() {
            insertData(record)

            verify(exactly = 1) { repository.save(record) }
        }

        @Test
        fun shouldNotSaveRecordWhenThereAreErrors() {
            bindingResult.addError(ObjectError("", ""))

            insertData(Record())

            verify(exactly = 0) { repository.save(allAny<Record>()) }
        }

        @Test
        fun shouldAddNewInsertRecordToModelMap() {
            insertData(record)

            assertThat(map).containsKey("insertRecord")
            assertThat(map["insertRecord"]).isNotSameAs(record)
        }

        @Test
        fun shouldAddRecordsToModelMap() {
            insertData(record)

            assertThat(map).containsKey("records")
        }

        private fun insertData(record: Record) {
            ctrl.insertData(map, record, bindingResult)
        }
    }
}
