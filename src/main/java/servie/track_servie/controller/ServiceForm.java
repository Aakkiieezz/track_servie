package servie.track_servie.controller;

import java.util.List;
import lombok.Data;

@Data
public class ServiceForm
{
	private String sortDir = "asc";
	private List<Integer> genreIds;
	private List<String> languages;
	private List<String> statuses;
	private Boolean watched;
	private String type = "";
	private Integer startYear;
	private Integer endYear;
	private int pageNumber = 0;
	private String sortBy = "title";
}