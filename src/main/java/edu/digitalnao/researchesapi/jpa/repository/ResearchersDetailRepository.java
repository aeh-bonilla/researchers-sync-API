package edu.digitalnao.researchesapi.jpa.repository;

import edu.digitalnao.researchesapi.jpa.vo.ResearchersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchersDetailRepository extends JpaRepository<ResearchersDetail, String> {}
