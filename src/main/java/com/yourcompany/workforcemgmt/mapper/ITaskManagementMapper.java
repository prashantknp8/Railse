package com.yourcompany.workforcemgmt.mapper;


import com.yourcompany.workforcemgmt.dto.CreateTaskRequest;
import com.yourcompany.workforcemgmt.dto.TaskManagementDto;
import com.yourcompany.workforcemgmt.model.TaskManagement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {

    TaskManagementDto modelToDto(TaskManagement model);

    TaskManagement requestItemToModel(CreateTaskRequest.RequestItem item);

    TaskManagement dtoToModel(TaskManagementDto dto);

    List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);
}
