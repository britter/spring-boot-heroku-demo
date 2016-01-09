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

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

@RunWith(HierarchicalContextRunner.class)
public class HomeControllerTest {

    private ModelMap map;
    private HomeController ctrl;
    private RecordRepository repository;

    @Before
    public void setUp() throws Exception {
        map = new ModelMap();
        repository = mock(RecordRepository.class);
        ctrl = new HomeController(repository);
    }

    public class Home {

        @Test
        public void shouldAddInsertRecordToModelMap() throws Exception {
            ctrl.home(map);

            assertThat(map, hasKey("insertRecord"));
            assertTrue(map.get("insertRecord") instanceof Record);

            Record insertRecord = (Record) map.get("insertRecord");
            assertNull(insertRecord.getData());
        }

        @Test
        public void shouldQueryRepositoryForAllRecords() throws Exception {
            ctrl.home(map);

            verify(repository, only()).findAll();
        }

        @Test
        public void shouldAddRecordsFromRepositoryToModelMap() throws Exception {
            when(repository.findAll()).thenReturn(Arrays.asList(new Record(), new Record(), new Record()));

            ctrl.home(map);

            assertThat(map, hasKey("records"));
            assertTrue(map.get("records") instanceof List);

            List<Record> records = getRecords();
            assertThat(records, hasSize(3));
        }

        @SuppressWarnings("unchecked")
        private List<Record> getRecords() {
            return (List<Record>) map.get("records");
        }
    }

    public class InsertData {

        private MapBindingResult bindingResult;

        @Before
        public void setUp() throws Exception {
            bindingResult = new MapBindingResult(new HashMap<>(), "insertRecord");
        }

        @Test
        public void shouldSaveRecordWhenThereAreNoErrors() throws Exception {
            Record record = new Record();
            insertData(record);

            verify(repository, times(1)).save(record);
        }

        @Test
        public void shouldNotSaveRecordWhenThereAreErrors() throws Exception {
            bindingResult.addError(new ObjectError("", ""));

            insertData(new Record());

            verify(repository, never()).save(any(Record.class));
        }

        @Test
        public void shouldAddNewInsertRecordToModelMap() throws Exception {
            Record record = new Record();
            insertData(record);

            assertThat(map, hasKey("insertRecord"));
            assertThat(map.get("insertRecord"), is(not(record)));
        }

        @Test
        public void shouldAddRecordsToModelMap() throws Exception {
            insertData(new Record());

            assertThat(map, hasKey("records"));
        }

        private void insertData(Record record) {
            ctrl.insertData(map, record, bindingResult);
        }
    }
}
