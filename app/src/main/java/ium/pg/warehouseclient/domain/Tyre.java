package ium.pg.warehouseclient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Tyre {

    private Long id;
    private String producer;
    private String name;
    private Integer rimSize;
    @Builder.Default
    private Integer quantity = 0;
    private Double price;

}