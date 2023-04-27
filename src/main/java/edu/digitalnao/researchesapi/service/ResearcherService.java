package edu.digitalnao.researchesapi.service;

import java.util.ArrayList;
import edu.digitalnao.researchesapi.web.model.ResearcherDto;

public interface ResearcherService {
    ResearcherDto getResearcherDetail(String id);
    ArrayList<ResearcherDto> getResearcherDetailList();
}
