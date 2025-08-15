package com.dbiz.app.integrationservice.domain;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_interface_integration", schema = "pos")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) // Thêm annotation này
public class InterfaceIntegration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_interface_integration_sq")
    @SequenceGenerator(name = "d_interface_integration_sq", sequenceName = "d_interface_integration_sq", allocationSize = 1)
    @Column(name = "d_interface_integration_id", nullable = false, precision = 10)
    private Integer id;

    @Type(type = "jsonb")  // Chỉ định kiểu dữ liệu JSON
    @Column(name = "data", columnDefinition = "jsonb")
    private JsonNode data;

    @Size(max = 255)
    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Size(max = 255)
    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "data_text")
    private String dataText;
}
