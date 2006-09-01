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

package org.sakaiproject.tool.postem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.faces.application.FacesMessage;

public class CSV {

	private List contents;

	private List headers;

	private List students;

	private String csv;
	
	public static final char COMMA_DELIM = ',';
	public static final char TAB_DELIM = '\t';

	// private boolean withHeaders = true;

	public CSV(String csv, boolean withHeader) throws DataFormatException {
		contents = retrieveContents(csv, COMMA_DELIM);
		headers = retrieveHeaders(contents);
		students = retrieveStudents(contents, withHeader);
		this.csv = csv;
	}
	
	public CSV(String csv, boolean withHeader, char delimiter) throws DataFormatException {
		contents = retrieveContents(csv, delimiter);
		headers = retrieveHeaders(contents);
		students = retrieveStudents(contents, withHeader);
		this.csv = csv;
	}

	public CSV(List contents, boolean withHeader) {
		headers = retrieveHeaders(contents);
		students = retrieveStudents(contents, withHeader);
		csv = createFromContents(contents);
		this.contents = contents;
	}

	public List getHeaders() {
		return headers;
	}

	public List getStudents() {
		return students;
	}
	
	public List getStudentUsernames() {
		List usernames = new ArrayList();
		
		Iterator studentIter = students.iterator();
		while (studentIter.hasNext()) {
			List s1 = (List) studentIter.next();
			String usr = ((String) s1.get(0)).trim();
			usernames.add(usr);
		}
		
		return usernames;
	}

	public String getCsv() {
		return csv;
	}

	public static String createFromContents(List rows) {
		StringBuffer csv = new StringBuffer();

		Iterator riter = rows.iterator();
		while (riter.hasNext()) {
			Iterator citer = ((List) riter.next()).iterator();
			while (citer.hasNext()) {
				String current = (String) citer.next();
				current = current.replaceAll("\"", "\"\"");

				csv.append("\"");
				csv.append(current);
				csv.append("\"");

				if (citer.hasNext()) {
					csv.append(",");
				}
			}
			if (riter.hasNext()) {
				csv.append("\r\n");
			}
		}
		return csv.toString();
	}

	public static List retrieveContents(String csv, char delimiter) throws DataFormatException {

		List all = new ArrayList();
		List current = new ArrayList();
		StringBuffer it = new StringBuffer();

		boolean inQuotes = false;

		int length = csv.length();
		for (int ii = 0; ii < length; ii++) {

			if (inQuotes) {
				if (ii == length - 1) {
					current.add((it.length() == 0) ? " " : it.toString());
					all.add(current);
					break;
				}
				if (csv.charAt(ii) == '"') {
					if (ii < length - 1 && csv.charAt(ii + 1) == '"') {
						it.append("\"");
						ii++;
					} else {
						inQuotes = false;
					}
				} else {
					it.append(csv.charAt(ii));
				}
			} else {
				if (ii == length - 1 && csv.charAt(ii) != '\n'
						&& csv.charAt(ii) != '\r') {
					if (csv.charAt(ii) == delimiter) {
						current.add((it.length() == 0) ? " " : it.toString());
						current.add("");
					} else {
						it.append(csv.charAt(ii));
						current.add((it.length() == 0) ? " " : it.toString());
					}
					all.add(current);
					break;
				}
				if (csv.charAt(ii) == delimiter) {
					current.add((it.length() == 0) ? " " : it.toString());
					it = new StringBuffer();
					// this line would trim leading spaces per the spec, but not trailing
					// ones.
					// we don't care about it so much anyways, since the info
					// will be output in a web context
					// csv = csv.trim();
				} else if (csv.charAt(ii) == '\r' || csv.charAt(ii) == '\n') {
					if (ii < length - 1 && csv.charAt(ii + 1) == '\n') {
						ii++;
					}		
					current.add((it.length() == 0) ? " " : it.toString());
					it = new StringBuffer();
		            all.add(current);
					current = new ArrayList();
					
				} else if (csv.charAt(ii) == '"') {
					inQuotes = true;
				} else {
					it.append(csv.charAt(ii));
				}
			}	
		}
        
		// we want to identify the row with the most columns and then append blank columns until all the rows
		// are the same length
		int finalNumCols = 0;
		for (int i=0; i < all.size(); i++) {
			if(((List)all.get(i)).size() > finalNumCols) {
				finalNumCols = ((List)all.get(i)).size();
			}
		}
		
		// make sure all of the rows have the same number of cols as the longest row
		for (int j=0; j < all.size(); j++) {
				
			while(((List)all.get(j)).size() < finalNumCols) {					
				((List)all.get(j)).add(" ");
			}											
		}
	
		return all;
	}

	public static List retrieveHeaders(List rows) {
		if (rows == null || rows.size() == 0) {
			return null;
		}

		return (List) rows.get(0);
	}

	public static List retrieveStudents(List rows, boolean withHeader) {
		List headers = retrieveHeaders(rows);
		List results = new ArrayList(rows);
		if (withHeader == true) {
			results.remove(0);
		}
		return results;
	}

	public static int determineColumns(String csv, char delimiter) {
		int total = 0;
		boolean inQuotes = false;

		int length = csv.length();
		for (int ii = 0; ii < length; ii++) {
			if (inQuotes) {
				if (csv.charAt(ii) == '"') {
					if (ii < length - 1 && csv.charAt(ii + 1) == '"') {
						ii++;
					} else {
						inQuotes = false;
					}
				}
			} else {
				if (csv.charAt(ii) == delimiter) {
					total++;
				} else if (csv.charAt(ii) == '\r' || csv.charAt(ii) == '\n') {
					break;
				} else if (csv.charAt(ii) == '"') {
					inQuotes = true;
				}
			}
		}
		total++;
		return total;
	}
}