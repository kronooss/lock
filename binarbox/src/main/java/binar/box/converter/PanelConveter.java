package binar.box.converter;

import binar.box.domain.Panel;
import binar.box.dto.PanelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PanelConveter {

    @Autowired
    private LockSectionConvertor lockSectionConvertor;

    public PanelDTO toDTO(Panel panel){
        return PanelDTO.builder()
                .id(panel.getId())
                .lockSectionDTO(lockSectionConvertor.toDTOList(panel.getLockSection()))
                .build();
    }

    public List<PanelDTO> toDTOList(List<Panel> panels){
        return panels.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
