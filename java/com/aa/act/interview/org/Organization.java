package com.aa.act.interview.org;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Organization
{
	private Position position;

	private ArrayList<Employee> numberOfEmps = new ArrayList<>();

	public Organization()
	{
		position = createOrganization();
	}

	protected abstract Position createOrganization();

	/**
	 * hire the given person as an employee in the position that has that title
	 *
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title)
	{
		Employee employee;

		//if no employees have been added to the system, we'll start with a identifier of 0 for first employee and
		//add one to that number for each employee added, so they have unique identifiers
		if(numberOfEmps.isEmpty())
		{
			employee = new Employee(0, person);
		}
		else
		{
			employee = new Employee(numberOfEmps.size(), person);
		}
		numberOfEmps.add(employee);

		//if the position title is CEO than we will just set the employee for that position
		if(position.getTitle().equalsIgnoreCase(title))
		{
			position.setEmployee(Optional.of(employee));
			return Optional.of(position);
		}
		//otherwise we need to check if the title of the position exists by checking all employee titles
		// going down the list from ceo
		else
		{
			//to do this, we'll pass the original position to a method that will cycle through all positions
			// and their direct reports to try and find a match
			Position positionOptional = checkDirectReports(position, title, employee);

			//if no position is returned then the job title was never found
			if(positionOptional == null)
				return null;
			else
				return Optional.of(positionOptional);
		}
	}

	@Override
	public String toString() {
		return printOrganization(position, "");
	}

	private String printOrganization(Position pos, String prefix)
	{
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}


	//Used to check all job titles and their direct report job titles to try and find a match to the passed in "title" param
	private Position checkDirectReports(Position position, String title, Employee employee)
	{
		for(Position p : position.getDirectReports())
		{
			//if the parameter title matches the parameter position.title
			if(title.equalsIgnoreCase(p.getTitle()))
			{
				//We'll then set the employee to that position and I changed the setEmployee
				// method to return a position so we could than just return that value to our Hire method
				return p.setEmployee(Optional.of(employee));
			}
			//otherwise we will have to check to passed in Position's direct reporters to try and find a
			// match and continue to rerun it all the way down the line
			else
			{
				if(p.getDirectReports().size() > 0)
				{
					Position nextPosition = checkDirectReports(p, title, employee);
					if(nextPosition != null)
					{
						return nextPosition;
					}
				}
			}
		}
		//if no matches were found, then no such job title exists
		return null;
	}
}
