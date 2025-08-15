package com.dbiz.app.integrationservice.domain.view;

import com.dbiz.app.integrationservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Embeddable
public class UserV  implements Serializable {


    @Size(max = 128)
    @Column(name = "full_name", length = 128)
    private String fullName;


    @Column(name = "d_user_id")
    private Integer id;

}