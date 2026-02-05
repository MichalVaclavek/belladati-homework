package com.belladati.homework.services;

import com.belladati.sdk.BellaDatiService;
import com.belladati.sdk.dataset.data.DataRow;
import com.belladati.sdk.util.PaginatedIdList;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BellaDatiDataServiceTest {

    @Test
    public void loadDataReturnsRows() {
        BellaDatiServiceFactory factory = mock(BellaDatiServiceFactory.class);
        BellaDatiService service = mock(BellaDatiService.class);
        PaginatedIdList<DataRow> dataRows = mock(PaginatedIdList.class);
        DataRow row = mock(DataRow.class);

        when(factory.create(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(service);
        when(service.getDataSetData("datasetId")).thenReturn(dataRows);
        when(dataRows.toList()).thenReturn(Collections.singletonList(row));

        BellaDatiDataService sut = new BellaDatiDataService(
            "https://example",
            "consumerKey",
            "consumerSecret",
            "username",
            "password",
            "datasetId",
            factory
        );

        List<DataRow> result = sut.loadData();

        assertEquals(1, result.size());
        verify(factory).create(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(dataRows).load();
    }
}
