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
public class InfoColorRequest {

    private String colorName;
    private List<SizeRequest> sizeRequestList;

}
