package edu.digitalnao.researchesapi.service;

import edu.digitalnao.researchesapi.web.model.ResearcherDto;

public interface ResearcherService {
    ResearcherDto getResearcherDetail(String id);
}
