package AritM_DiffH_Rsa;

import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Rsa_Alice {
	private BigInteger p,q,n,on,e,d; 
	//p,q: primos grandes; n:p*q; on:aux; e:int primo entre si do tot(n); d: inv. mult. de e;
	
	public Rsa_Alice(){
		start();
	}
	
	public void start(){
		int size = 512;
		
		p = new BigInteger(size, 15, new Random());
		q = new BigInteger(size, 15, new Random());
		
		n = p.multiply(q);
		
		on = p.subtract(BigInteger.ONE);
		on = on.multiply(q.subtract(BigInteger.ONE));
		
		do{
			e = new BigInteger(2*size, new Random());
		}while( (e.compareTo(on) != 1 || (e.gcd(on).compareTo(BigInteger.valueOf(1)) != 0)));
		
		d = e.modInverse(on);
	}

	public BigInteger encrypt(BigInteger plaintext){
		return plaintext.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger ciphertext){
		return ciphertext.modPow(d, n);
	}

	public static void main(String[] args) throws IOException {
	        ServerSocket aliceSocket = null; 
	        Socket bobSocket = null; 
	        String userInput;
	        String bobResponse = null;
	        String bobResponse1 = null;
	        String bobResponse2 = null;
	        PrintWriter out = null; 
	        PrintWriter out2 = null; 
	        BufferedReader in = null;
	        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	        Scanner sc = new Scanner(System.in);
	        Rsa_Alice app = new Rsa_Alice();
	        BigInteger bplaintext, bciphertext;
			int plaintext, aux=0;
			
	        try { 
	             aliceSocket = new ServerSocket(13131); 
	        } catch (IOException e){ 
	             System.err.println("Port 13131 unavailable!"); 
	             System.exit(1); 
	        } 

	        System.out.println ("Waiting for connection.....");

	        try { 
	            bobSocket = aliceSocket.accept();  
	        } catch (IOException e){ 
	            System.err.println("Unable to accept connection."); 
	            System.exit(1); 
	        } 

	        System.out.println ("Connected!");

	        //para enviar a Bob
	        out = new PrintWriter(bobSocket.getOutputStream(), true);
	        out2 = new PrintWriter(bobSocket.getOutputStream(), true);
	        //para ler envio do Bob
	        in = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
	
			System.out.println("Enter any character : ");
			plaintext = System.in.read();

			bplaintext = BigInteger.valueOf((long)plaintext);
			bciphertext = app.encrypt(bplaintext);
			
//			System.out.println("Plaintext : " +  bplaintext.toString());
//			System.out.println("Ciphertext : " +  bciphertext.toString());

			bplaintext = app.decrypt(bciphertext);
//			System.out.println("After Decryption Plaintext : " +  bplaintext.toString());
			
			System.out.println("Key public sent to Bob = "+ app.n +"\n                       = " + app.e);
			System.out.println("Key private = "+ app.n +"\n            = " + app.d);
	        
	        out.println(app.n); //manda para o Bob
	        out.println(app.e); //manda para o Bob

	        while(true){
	        	if(aux==0){ 
	        		bobResponse1 = in.readLine();//lê resposta de Bob
	        		if(bobResponse1.contentEquals(".")){
	        			System.out.println("Bob closed connection!");
	        			break;
	        		}
	        		aux++;
	        	}
	        	if(aux==1){ 
	        		bobResponse2 = in.readLine();//lê resposta de Bob
	        		if(bobResponse2.contentEquals(".")){
	        			System.out.println("Alice closed connection!");
	        			break;
	        		}
	        		aux++;
	            }else	break;
	        }
	            
			System.out.println("Bob's key public: "+ bobResponse1 + "\n                : " + bobResponse2 );
	            
			System.out.println("Enter '.' to close connection!");

			while(true){
	            userInput = stdIn.readLine(); //lê teclado 
	            out2.println(userInput); //manda para o Bob
	            if(userInput.equals(".")){//finaliza conexão
	            	System.out.println("Connection closed!");
	            	break;
				}
	            bobResponse = in.readLine(); //lê resposta do Bob
	            if(bobResponse.contentEquals(".")){
	                System.out.println("Bob fechou conexão");
	                break;
	            }else{
	                System.out.println("Recebido pelo Bob: " + bobResponse);
	                System.out.print("Envio: ");
	            }
	        }
	    
			out.close();
			out2.close();
			in.close();
			stdIn.close();
			sc.close();
			bobSocket.close();
			aliceSocket.close();
	    }
}
