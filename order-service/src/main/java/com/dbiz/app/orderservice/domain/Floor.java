package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_floor", schema = "pos")
public class Floor extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_floor_sq")
    @SequenceGenerator(name = "d_floor_sq", sequenceName = "d_floor_sq", allocationSize = 1)
    @Column(name = "d_floor_id", nullable = false, precision = 10)
    private Integer id;


    @Column(name = "floor_no", nullable = false, length = 5)
    private String floorNo;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_floor_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String floorUu;

    @Column(name = "display_index")
    private Integer displayIndex;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "erp_floor_id")
    private Integer erpFloorId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Floor floor = (Floor) o;
        return getId() != null && Objects.equals(getId(), floor.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}