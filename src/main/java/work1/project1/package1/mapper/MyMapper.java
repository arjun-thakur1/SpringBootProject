package work1.project1.package1.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.response.EmployeeAddResponseDto;
import work1.project1.package1.entity.EmployeeEntity;

@Service
public class MyMapper extends ModelMapper {


    PropertyMap<EmployeeAddRequest,EmployeeEntity > clientPropertyMap = new PropertyMap< EmployeeAddRequest,EmployeeEntity >() {
        @Override
        protected void configure() {
            skip(source.getCompanyId());
            skip(source.getManagerId());
            skip(source.getDepartmentId());
            skip(destination.getId());
        }
    };


/*
    public CompanyResponse convert(CompanyEntity companyEntity, String message)
        {
            CompanyResponse responseCompanyDto=new CompanyResponse();

            responseCompanyDto.setId(companyEntity.getId());
            responseCompanyDto.setCompanyName(companyEntity.getCompanyName());
            responseCompanyDto.setCeoName(companyEntity.getCeoName());
        //    responseCompanyDto.setStatus((long) 200);
            responseCompanyDto.setMessage(message);
            return responseCompanyDto;
        }

    public DepartmentCompanyResponse convert(DepartmentEntity departmentEntity, String message)
    {
        DepartmentCompanyResponse responseDepartmentDto=new DepartmentCompanyResponse();

        responseDepartmentDto.setId(departmentEntity.getId());
      //  responseDepartmentDto.setCompanyId(departmentEntity.getCompanyId());
        responseDepartmentDto.setDepartmentName(departmentEntity.getDepartmentName());
        responseDepartmentDto.setStatus((long) 200);
        responseDepartmentDto.setMessage(message);
        return responseDepartmentDto;
    }

    public List<DepartmentCompanyResponse> convert(List<DepartmentEntity> departmentEntityList, String message) {

        List<DepartmentCompanyResponse> responseDepartmentDtoList=new ArrayList<>();
        departmentEntityList.forEach((d)->
                responseDepartmentDtoList.add(convert(d,message))
        );
        return responseDepartmentDtoList;
    }



        public EmployeeResponseDto convert(EmployeeEntity personEntity, String message)
    {
        EmployeeResponseDto responseEmployeeDto=new EmployeeResponseDto();

        responseEmployeeDto.setId(personEntity.getId());
        responseEmployeeDto.setName(personEntity.getName());
        responseEmployeeDto.setPhone(personEntity.getPhone());
        responseEmployeeDto.setStatus((long) 200);
        responseEmployeeDto.setMessage(message);

        return responseEmployeeDto;
    }

    public EmployeeCompleteResponse convert(EmployeeEntity employeeEntity1, EmployeeMappingEntity d) {
        EmployeeCompleteResponse completeDto=new EmployeeCompleteResponse();

        completeDto.setId(employeeEntity1.getId());
        completeDto.setName(employeeEntity1.getName());
        completeDto.setPhone(employeeEntity1.getPhone());
        completeDto.setSalary(employeeEntity1.getSalary());
        completeDto.setManagerId(d.getManagerId());
        completeDto.setDesignation(d.getDesignation());
        completeDto.setStatus((long) 200);
        completeDto.setMessage(SUCCESS);

        return completeDto;
    }
*/
    /* created for delete emp response but not need
    public EmployeeResponseDto convert(EmployeeEntity employeeEntity,long status,String message)
    {
        EmployeeResponseDto responseEmployeeDto=new EmployeeResponseDto();

        responseEmployeeDto.setId(employeeEntity.getId());
        responseEmployeeDto.setStatus(status);
        responseEmployeeDto.setMessage(message);

        return responseEmployeeDto;
    }
  */
}

