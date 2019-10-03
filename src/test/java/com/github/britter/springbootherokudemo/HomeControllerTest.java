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
package com.github.britter.springbootherokudemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HomeControllerTest {

    private ModelMap map;
    private HomeController ctrl;
    private RecordRepository repository;

    @BeforeEach
    void setUp() {
        map = new ModelMap();
        repository = mock(RecordRepository.class);
        ctrl = new HomeController(repository);
    }

    @Nested
    class Home {

        @Test
        void shouldAddInsertRecordToModelMap() {
            ctrl.home(map);

            assertThat(map).containsKey("insertRecord");
            assertThat(map.get("insertRecord")).isInstanceOf(Record.class);

            Record insertRecord = (Record) map.get("insertRecord");
            assertThat(insertRecord.getData()).isNull();
        }

        @Test
        public void shouldQueryRepositoryForAllRecords() {
            ctrl.home(map);

            verify(repository, only()).findAll();
        }

        @Test
        public void shouldAddRecordsFromRepositoryToModelMap() {
            when(repository.findAll()).thenReturn(Arrays.asList(new Record(), new Record(), new Record()));

            ctrl.home(map);

            assertThat(map).containsKey("records");
            assertThat(map.get("records")).isInstanceOf(List.class);

            List<Record> records = getRecords();
            assertThat(records).hasSize(3);
        }

        @SuppressWarnings("unchecked")
        private List<Record> getRecords() {
            return (List<Record>) map.get("records");
        }
    }

    @Nested
    class InsertData {

        private MapBindingResult bindingResult;

        @BeforeEach
        public void setUp() {
            bindingResult = new MapBindingResult(new HashMap<>(), "insertRecord");
        }

        @Test
        public void shouldSaveRecordWhenThereAreNoErrors() {
            Record record = new Record();
            insertData(record);

            verify(repository, times(1)).save(record);
        }

        @Test
        public void shouldNotSaveRecordWhenThereAreErrors() {
            bindingResult.addError(new ObjectError("", ""));

            insertData(new Record());

            verify(repository, never()).save(any(Record.class));
        }

        @Test
        public void shouldAddNewInsertRecordToModelMap() {
            Record record = new Record();
            insertData(record);

            assertThat(map).containsKey("insertRecord");
            assertThat(map.get("insertRecord")).isNotSameAs(record);
        }

        @Test
        public void shouldAddRecordsToModelMap() {
            insertData(new Record());

            assertThat(map).containsKey("records");
        }

        private void insertData(Record record) {
            ctrl.insertData(map, record, bindingResult);
        }
    }
}
