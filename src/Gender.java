
public enum Gender {

		MALE{
			public char getSymbol(){
				return 'M';
			}
		},
		
		FEMALE	{
			public char getSymbol(){
				return 'F';
			}
		};
		
		public abstract char getSymbol();
}
