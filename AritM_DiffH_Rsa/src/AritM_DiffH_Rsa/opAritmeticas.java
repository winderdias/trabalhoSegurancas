package AritM_DiffH_Rsa;

import java.math.BigInteger;

public class opAritmeticas {

	public static BigInteger soma(BigInteger a, BigInteger b, BigInteger N){
		return a.mod(N).add(b.mod(N));
	}
	
	public static BigInteger multiplicacao(BigInteger a, BigInteger b, BigInteger N){
		return a.mod(N).multiply(b.mod(N));
	}
	
	public static BigInteger exponenciacao(BigInteger a, BigInteger b, BigInteger N){
		BigInteger two = new BigInteger("2");
		if (b.equals(BigInteger.ZERO)) return BigInteger.ONE;
		else if(b.mod(two).equals(BigInteger.ZERO)){
			BigInteger bsobdois = b.divide(two);
			BigInteger x = exponenciacao(a,bsobdois,N);
			return (x.multiply(x)).mod(N); 
		}else{
			BigInteger bmenos = b.subtract(BigInteger.ONE);
			return (a.mod(N).multiply(exponenciacao(a,bmenos,N)).mod(N));
		}
	}
	
	public static BigInteger inversoM(BigInteger a, BigInteger b){
		BigInteger b0 = b, t, q,aux;
		BigInteger x0 = BigInteger.ZERO, x1 = BigInteger.ONE;
		
		if(b.compareTo(BigInteger.ONE)==0){
			return BigInteger.ONE;
		}
		
		while (a.compareTo(BigInteger.ONE)==1){
			q=a.divide(b);
			t=b;
			b=a.remainder(b);
			a=t;
			t=x0;
			aux = q.multiply(x0);
			x0 = x1.subtract(aux);
			x1=t;
		}
		
		if(BigInteger.ZERO.compareTo(x1)==1){
			x1=x1.add(b0);
		}
		return x1;
	}	
	
		
	/*public static void main(String args[]) {  
		BigInteger a = new BigInteger("17");  
        BigInteger b = new BigInteger("9");  
        BigInteger N = new BigInteger("5");  
        BigInteger Res = inversoM(a,N);
        
        System.out.println("\nRes:"+Res); 
		
	}  */
}
