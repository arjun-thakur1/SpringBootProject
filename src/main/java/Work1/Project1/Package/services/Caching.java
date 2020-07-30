package Work1.Project1.Package.services;

import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.repository.CompanyRepository;
import Work1.Project1.Package.response.ResponseCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@CacheConfig(cacheNames={"company_cache"})
public class Caching {

    private static final long serialVersionUID = 7156526077883281623L;
     @Autowired
     CompanyRepository companyRepository;



    @Cacheable(value = "company_cache",key="#id")
       public  ResponseCompany getCompany(long id) {
       Optional<CompanyEntity> companyEntity= companyRepository.findById(id);
        CompanyEntity companyEntity1=companyEntity.get();
        return  new ResponseCompany(companyEntity1.getCompanyId(), companyEntity1.getCompanyName(), companyEntity1.getCeoName());
    }

    @CachePut(value = "company_cache",key="#companyId")
     public ResponseCompany update(long companyId,CompanyEntity companyEntity){
         companyRepository.save(companyEntity);
         return  new ResponseCompany(companyEntity.getCompanyId(), companyEntity.getCompanyName(), companyEntity.getCeoName());
     }


    @CacheEvict(value = "company_cache",key="#id", allEntries = true)
     public ResponseCompany deleteCompany(long id)
      {
          Optional<CompanyEntity> companyEntity=companyRepository.findById(id);
          CompanyEntity companyEntity1=companyEntity.get();
          return  new ResponseCompany(companyEntity1.getCompanyId(), companyEntity1.getCompanyName(), companyEntity1.getCeoName());
      }

}
