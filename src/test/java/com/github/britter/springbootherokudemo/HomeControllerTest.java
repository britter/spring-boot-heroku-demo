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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

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

    @Test
    public void shouldAddInsertRecordToModelMap_whenCallingHome() throws Exception {
        ctrl.home(map);

        assertThat(map, hasKey("insertRecord"));
        assertTrue(map.get("insertRecord") instanceof Record);
    }

    @Test
    public void shouldQueryRepositoryForAllRecords_whenCallingHome() throws Exception {
        ctrl.home(map);

        verify(repository, only()).findAll();
    }

    @Test
    public void shouldAddRecordsFromRepositoryToModelMap_whenCallingHome() throws Exception {
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
