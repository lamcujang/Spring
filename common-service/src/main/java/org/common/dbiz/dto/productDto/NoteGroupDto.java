package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.common.dbiz.dto.productDto.JsonView.JsonViewNoteGroup;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.NoteGroup}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NoteGroupDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    Integer id;
    @Size(max = 32)
    String groupName;
    @Size(max = 255)
    String description;

    @JsonView(JsonViewNoteGroup.viewJsonNoteGroupAndNote.class)
    List<NoteDto> notes;
}