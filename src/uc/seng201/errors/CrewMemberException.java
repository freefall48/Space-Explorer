package uc.seng201.errors;

import uc.seng201.crew.CrewMember;

/**
 * Exception handling for the CrewMember errors.
 */
public class CrewMemberException extends IllegalArgumentException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final CrewMember crewMember;

    public CrewMemberException(CrewMember crewMember) {
        super();
        this.crewMember = crewMember;
    }

    public CrewMemberException() {
        super();
        crewMember = null;
    }
}
