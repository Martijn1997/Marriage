import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class involving a gender and a marital state
 * @author Martijn
 * @invar 	...
 * 			| canHaveAsSpouse() && 
 * 			|((!this.isMarried()) || (this.getSpouse().getSpouse() == this)
 * @invar 	Each person can have its gender as gender
 * 			| canHaveAsGender(this.getGender()) 
 */

public class Person {
	/**
	 * initialize this new person with given gender
	 * 
	 * @param gender
	 * 			The gender for this person
	 * @post	the gender of this new person is equal to the given gender
	 * 			|new.getGender() == gender
	 * 
	 * @throws	IllegalArgumentException
	 * 			this new person cannot have the given gender as its gender
	 * 
	 */
	public Person(Gender gender){
		
		this.gender = gender;
		this.setSpouse(null);
		
	}
	
	public Gender getGender(){
		return this.gender;
	}
	
	public boolean canHaveAsGender(Gender gender){
		return (gender != null);
	}
	
	private final Gender gender;
	
	//getSpouse, canHaveAsSpouse, setSpouse
	
	public Person getSpouse(){
		return this.spouse;
	}
	
	public boolean canHaveAsSpouse(Person person){
		if (person == null)
			return true;
		if	(this.isTerminated())
			return false;
		return this.getGender() != person.getGender();
	}
	
	/**
	 * @pre 	...
	 * 			| canHaveAsSpouse(spouse)
	 * @param person
	 */
	@Raw @Model // @model als de @effect gebruikt wordt
	private void setSpouse(@Raw Person spouse){
		assert canHaveAsSpouse(spouse);
		this.spouse = spouse;		
	}
	
	// zet de methode op private omdat enkel de methode marry personen mag koppelen
	
	/**
	 * @return	...
	 * 			|result == (this.getSpouse != null)
	 */
	public boolean isMarried(){
		return this.getSpouse() != null;
		
	}
	
	/**
	 * @param person
	 * 
	 * @post	...
	 * 			| (new this).getSpouse() == person
	 * @post	...
	 * 			| (new person).getSpouse() == this
	 * 
	 * @throws	NullPointerException
	 * 			...
	 * 			| person == null
	 * 
	 * @throws	IllegalArgumentException
	 * 			...
	 * 			| !canHaveAsSpouse(person)
	 * @throws	IllegalStateException
	 * 			...
	 * 			| this.isMarried()|| person.isMarried()
	 */
	//altijd new in de postconditie!, de NIEUWE toestand wordt bekeken met een postconditie
	public void marry(Person person) throws NullPointerException, IllegalArgumentException, IllegalStateException{
		
		if(this.isMarried()||person.isMarried())
			throw new IllegalStateException();
		if(!this.canHaveAsSpouse(person)) // niet nodig om person.canHaveAsSpouse(this) te controleren
			throw new IllegalArgumentException();
		if(person==null)
			throw new NullPointerException();
		
		this.setSpouse(person);
		person.setSpouse(this);
		
	}
	
	/**
	 * Divorce two persons
	 * 
	 * @post  if the prime object is married they are now divorced, else nothing happens
	 * 			| if(this.isMarried)
	 * 			| then	((new this.getSpouse()).getSpouse() == null)&&
	 * 			| 		((new this).getSpouse == null)
	 * 
	 * of nog mogelijk
	 * 
	 * @effect	...
	 * 			| if(this.isMarried())
	 * 			| 	then this.getSpouse().setSpouse(null) && this.setSpouse(null)
	 * 
	 */
	public void divorce(){
		if(this.isMarried())
		{
			Person ex = this.getSpouse();
			this.setSpouse(null);
			ex.setSpouse(null);
		}
			
	}
	
	
	private Person spouse;
	
	
	public void terminate(){
		this.divorce();
		this.isTerminated = true;
	}
	
	public boolean isTerminated(){
		return this.isTerminated;
		
	}
	
	private boolean isTerminated = false;
}

// System.gc roept de garbage collector het object opruimen, de gc wordt automatisch opgeroepen als
// het object geheugen bijna vol zit. Bij tijdskritische systemen ga je de gc oproepen als het
// systeem tijd heeft 'in rust' en dat de gc zonder de werking van het programma te vertragen
