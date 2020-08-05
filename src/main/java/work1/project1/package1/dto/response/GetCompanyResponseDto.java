package work1.project1.package1.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.CompanyEntity;

import java.util.*;

@Service
public class GetCompanyResponseDto {

    @Getter @Setter private Long   id;
    @Getter @Setter private String companyName;
    @Getter @Setter private String ceoName;

    public GetCompanyResponseDto convert(CompanyEntity companyEntity)
    {
        GetCompanyResponseDto responseGetCompanyDto=new GetCompanyResponseDto();
        responseGetCompanyDto.setId(companyEntity.getId());
        responseGetCompanyDto.setCompanyName(companyEntity.getCompanyName());
        responseGetCompanyDto.setCeoName(companyEntity.getCeoName());
        return responseGetCompanyDto;
    }

    public List<GetCompanyResponseDto> convertList(List<CompanyEntity> companyEntities1) {
        List<GetCompanyResponseDto> responseGetCompanyDtoList=new ArrayList<GetCompanyResponseDto>();
        companyEntities1.forEach((c)->{
                 GetCompanyResponseDto responseGetCompanyDto=convert(c);
                responseGetCompanyDtoList.add(responseGetCompanyDto);
                }
                );
        return  responseGetCompanyDtoList;
    }
}
