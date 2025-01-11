package com.groom.manvsclass.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

// Mappa il Model alla Collection di MongoDB
@Document(collection = "ClassUT")
public class ClassUT {

	private String name;
	private String date;
	private String difficulty;
	private String code_Uri;
	private String description;
	private List<String> category;
	private List<Robot> robots;

	public ClassUT(String name, String date, String difficulty, String code_Uri, String description,
			List<String> category, List<Robot> robots) {
		this.name = name;
		this.date = date;
		this.difficulty = difficulty;
		this.code_Uri = code_Uri;
		this.description = description;
		this.category = category;
		this.robots = robots;
	}

	public ClassUT() {

	}

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}

	public void addRobot(Robot robot) {
		robots.add(robot);
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setcode_Uri(String code_Uri) {
		this.code_Uri = code_Uri;
	}

	public String getcode_Uri() {
		return this.code_Uri;
	}

	public void setUri(String code_Uri) {
		this.code_Uri = code_Uri;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getRobotNames() {

		List<String> robotNames = new ArrayList<>();

		for (Robot robot : robots) {
			robotNames.add(robot.getRobotName());
		}

		return robotNames;
	}

	public String getRobotPath(String robotName) {

		for (Robot robot : robots) {

			if (robot.getRobotName().equals(robotName))
				return robot.getRobotFile();
		}

		return null;
	}

	@Override
	public String toString() {
		return "ClassUT [name=" + name + ", date=" + date + ", difficulty=" + difficulty + ", code_Uri=" + code_Uri
				+ ", description=" + description + ", category=" + category + ", robots=" + robots + "]";
	}

}
