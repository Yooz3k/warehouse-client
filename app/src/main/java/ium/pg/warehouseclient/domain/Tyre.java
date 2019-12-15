package ium.pg.warehouseclient.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(onlyExplicitlyIncluded = true)
@Entity
public class Tyre implements Serializable {

    @ToString.Include
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ToString.Include
    private String producer;

    @ToString.Include
    private String name;

    @ToString.Include
    @ColumnInfo(name = "rim_size")
    private Integer rimSize;

    @ToString.Include
    @Builder.Default
    private int quantity = 0;

    @ToString.Include
    private Double price;

    @ColumnInfo(name = "quantity_change")
    @Builder.Default
    private int quantityChange = 0;

    @ColumnInfo(name = "last_modified")
    private LocalDateTime lastModified;

    @Builder.Default
    private Boolean deleted = false;

    /**
     * Determines if the tyre was created since the last synchronization
     * with remote database and should be added into it.
     */
    @Builder.Default
    private Boolean toBeAdded = false;

    public void setQuantity(int quantity) {
        int oldQuantity = this.quantity;
        this.quantityChange += quantity - oldQuantity;
        this.quantity = quantity;
    }
}