package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Note;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.NoteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class NoteMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<NoteDto , Note>() {
            @Override
            protected void configure() {
                map(source.getNoteGroupId(), destination.getNoteGroupId());
            }
        });

        modelMapper.addMappings(new PropertyMap<Note, NoteDto>() {
            @Override
            protected void configure() {
                map(source.getNoteGroupId(), destination.getNoteGroupId());
            }
        });
    }
    public Note toNote(NoteDto noteDto) {
        return modelMapper.map(noteDto, Note.class);
    }

    public NoteDto tonoteDto(Note note) {
        return modelMapper.map(note, NoteDto.class);
    }

    public Note updateEntity(NoteDto dto, Note entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
