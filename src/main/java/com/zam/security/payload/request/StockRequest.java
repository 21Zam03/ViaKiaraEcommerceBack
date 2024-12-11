package com.zam.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequest {

    private Integer productId;
    private List<InfoColorRequest> infoColorRequestList;

}
