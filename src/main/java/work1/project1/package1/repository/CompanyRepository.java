package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import work1.project1.package1.entity.DepartmentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByCompanyName(String companyName);

    CompanyEntity findByIdAndIsActive(long companyId,boolean isActive);

    //Page<CompanyEntity> findAllByIsActive(boolean isActive, Pageable paging);

    @Query(nativeQuery = true , value = "select * from company_entity as c where id in :ids and is_active = :bool ")
    Page<CompanyEntity> findBySomething(@Param("bool") boolean isActive, @Param("ids") List<Long> ids , Pageable paging);

    CompanyEntity findByCompanyName(String companyName);

    boolean existsByIdAndIsActive(Long companyId, boolean b);

    @Query(nativeQuery = true,value = "select * from company_entity as c where c.id=?1 and c.is_active=?2 ")
    CompanyEntity findQuery(Long id, boolean b);



//    @Query(value = "SELECT * FROM ticket WHERE status=:status AND assigned_group=:groupId AND resolved_at <= :resolvedAt", nativeQuery = true)
//    Page<TicketEntity> findByGroupIdAndStatusAndCreatedBy(@Param("status") Integer status, @Param("groupId") Long groupId,
//                                                          @Param("resolvedAt") LocalDateTime resolvedAt, Pageable pageable);


//    @Query(value = "SELECT * FROM ticket_note note WHERE note.message_id in :referenceIds ORDER BY created_at DESC limit 1", nativeQuery = true)
//    TicketNoteEntity findByReferenceIds(@Param("referenceIds") List<String> referenceIds);
//
//    @Query(value = "SELECT * FROM ticket_note note WHERE note.message_id = :messageId ORDER BY id DESC limit 1", nativeQuery = true)
//    TicketNoteEntity findByMessageId(@Param("messageId") String messageId);
//
//    @Query(value = "SELECT * FROM ticket_note note WHERE note.ticket_id = :ticketId ORDER BY id ASC limit 1", nativeQuery = true)
//    TicketNoteEntity findOldestNote(@Param("ticketId") Long ticketId);
//some testing commited code
}
