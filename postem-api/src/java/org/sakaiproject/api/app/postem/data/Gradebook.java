/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.api.app.postem.data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public interface Gradebook {
	
	final String SORT_BY_TITLE = "title";
	final String SORT_BY_CREATOR = "creator";
	final String SORT_BY_MOD_BY = "modBy";
	final String SORT_BY_MOD_DATE = "modDate";
	final String SORT_BY_RELEASED = "released";
	
	public String getTitle();

	public void setTitle(String title);

	public String getCreator();

	public void setCreator(String creator);
	
	public String getCreatorEid();
	
	public void setCreatorEid(String creatorUserId);

	public Timestamp getCreated();

	public void setCreated(Timestamp created);

	public String getLastUpdater();

	public void setLastUpdater(String lastUpdater);
	
	public String getLastUpdaterEid();
	
	public void setLastUpdaterEid(String lastUpdaterUserId);

	public String getUpdatedDateTime();

	public Timestamp getLastUpdated();

	public void setLastUpdated(Timestamp lastUpdated);

	public String getContext();

	public void setContext(String context);

	public Set<StudentGrades> getStudents();

	public void setStudents(Set<StudentGrades> students);

	public Template getTemplate();

	public void setTemplate(Template template);

	public List<Heading> getHeadings();

	public void setHeadings(List<Heading> headings);

	public Long getId();

	public void setId(Long id);

	public Boolean getReleased();

	public void setReleased(Boolean released);

	public String getHeadingsRow();

	public boolean hasStudent(String username);

	public boolean getRelease();

	public void setRelease(boolean release);

	public Boolean getReleaseStatistics();

	public void setReleaseStatistics(Boolean releaseStatistics);

	public boolean getReleaseStats();

	public void setReleaseStats(boolean releaseStats);

	public String getProperWidth(int column);

	public List getRawData(int column);

	public List getAggregateData(int column) throws Exception;

	// public Map getTotals(int column);

	public StudentGrades studentGrades(String username);
	
	public TreeMap getStudentMap();
	
	public String getFirstUploadedUsername();
	
	public void setFirstUploadedUsername(String username);
	
	/**
	 * 
	 * @return a list of the heading titles (String) associated with this
	 * gradebook
	 */
	public List getHeadingsTitleList();
	
	public List<String> getTempHeadingsTitleList();
	public void setTempHeadingsTitleList(List<String> headingTitles);
	
	public Map<String, List> getTempStudentToGradesMap();
	public void setTempStudentToGradesMap(Map<String, List> tempStudentToGradesMap);

}
