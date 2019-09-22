package gmsj.robotics.tracker.controler;
 
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
 
public class STM32CRC {
    
    int crc = 0xFFFFFFFF ;
    final int crcTable [] = { // Nibble lookup table for 0x04C11DB7 polynomial
            0x00000000,0x04C11DB7,0x09823B6E,0x0D4326D9,0x130476DC,0x17C56B6B,0x1A864DB2,0x1E475005,
            0x2608EDB8,0x22C9F00F,0x2F8AD6D6,0x2B4BCB61,0x350C9B64,0x31CD86D3,0x3C8EA00A,0x384FBDBD } ;
    void reset () {
        crc = 0xFFFFFFFF ;
    }
    void update ( ByteBuffer data ) {
        if ( data.remaining () % 4 != 0 ) {
    		System.out.println ("Bad CRC update size: " + data.remaining () );
            return ;
        }
        data.position ( 0 ) ;
        data.order ( ByteOrder.LITTLE_ENDIAN ) ;
        while ( data.hasRemaining () ) {
        	addToCrc ( data.getInt () ) ; 
        }
    }    
    void update ( byte[] data , int length ) {
        if ( length % 4 != 0 ) {
    		System.out.println ("Bad CRC update size: " + length ) ;
            return ;
        }
        for ( int i = 0 ; i < length ; i += 4 ) {
        	int word =	( data [ i + 0 ] << 24 ) & 0xff000000 |
        		    	( data [ i + 1 ] << 16 ) & 0x00ff0000 |
        		    	( data [ i + 2 ] <<  8 ) & 0x0000ff00 |
        		    	( data [ i + 3 ] <<  0 ) & 0x000000ff ;
        	addToCrc ( word ) ; 
        }
    }
    int getValue () {
        return crc ;
    }
    void addToCrc_Slow ( int data )
    {     
    	crc = crc ^ data ;
    	for ( int i = 0; i < 32; i++) {
    		if ( ( crc & 0x80000000 ) != 0 ) {
    			crc = ( crc << 1) ^ 0x04C11DB7 ; // Polynomial used in STM32
    		}
    		else {
    			crc = ( crc << 1 ) ;
    		}
    	}
    }
    void addToCrc ( int data ) {
		crc = crc ^ data ; 
		for ( int i = 0 ; i < 8 ; i ++ ) {
			crc = ( crc << 4 ) ^ crcTable [ ( crc >> 28 ) & 0x0F ] ;
		}
	}
	public int get ( ByteBuffer data ) {
		reset () ;
		update ( data ) ;
        return getValue () ;
    }
	public int get ( byte[] data , int length ) {
		reset () ;
		update ( data , length ) ;
        return getValue () ;
	}
}

