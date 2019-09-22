package gmsj.robotics.tracker.model;

public enum DataType {
	BOOLEAN , 
	UINT_8 ,
	INT_8 ,
	UINT_10 ,
	UINT_16 ,
	INT_16 ,
	UINT_32 ,
	INT_32 ,
	FLOAT_32 ,
	ARRAY_8 ,
	INVALID ; 
	
	public float getMinimumValue () {
		float min = 0 ;
		switch( this ) {
		case BOOLEAN : 	
		case UINT_8  : 
		case UINT_10 : 	
		case UINT_16 : 	
		case UINT_32 : 	
		case ARRAY_8 : 	min = 0 ; break ;
		case INT_8 : 	min = -128 ; break ;
		case INT_16 :	min = -32768 ; break ;
		case INT_32 :	min = Integer.MIN_VALUE ; break ;
		case FLOAT_32 : min = Float.NEGATIVE_INFINITY ; break ;
		default : 		min = 0 ; break ;
		}
		return min ;
	}		
	public float getMaximumValue () {
		float max ;

		switch( this ){
			case BOOLEAN : 	max = 1 ; break ;
			case UINT_8 : 	max = 0xFF ; break ;
			case INT_8 : 	max = 0x7F ; break ;
			case UINT_10 : 	max = 0x3FF ; break ;
			case UINT_16 : 	max = 0xFFFF ; break ;
			case INT_16 : 	max = 0x7FFF ; break ;
			case UINT_32 : 	max = 4294967295L ; break ;
			case INT_32 : 	max = 0x7FFFFFFF ; break ;
			case FLOAT_32 : max = Float.POSITIVE_INFINITY ; break ;
			default : 		max = 0 ; break ;
		}
		return max ;
	}
	public boolean isSigned () 
	{
		return ( this.equals ( DataType.INT_8 )		|| 
				 this.equals ( DataType.INT_16 ) 	|| 
				 this.equals ( DataType.INT_32 ) 	|| 
				 this.equals ( DataType.FLOAT_32 ) ) ;
	}
}
