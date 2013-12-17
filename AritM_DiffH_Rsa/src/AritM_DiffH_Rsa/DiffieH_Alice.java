package AritM_DiffH_Rsa;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class DiffieH_Alice {

    public static void main(String[] args) throws IOException {
    	
        ServerSocket aliceSocket = null; 
        Socket bobSocket = null; 
        String userInput;
        String bobResponse;
        PrintWriter out = null; 
        BufferedReader in = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(System.in);
        BigInteger sBInt = BigInteger.valueOf(0);
        BigInteger p, b, sA, key, keyAB; 
        // primo, base, secreto de Alice, chave que Alice envia a Bob, chave compartilhada entre Bob e Alice;

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
        //para ler envio do Bob
        in = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
	
        System.out.print("Enter prime number = ");
        p=sc.nextBigInteger();
 
        System.out.print("Enter the base = ");
        b=sc.nextBigInteger();
            
        System.out.print("Enter the Alice's secret = ");
        sA=sc.nextBigInteger();

    	key = opAritmeticas.exponenciacao(b, sA, p);
    		
    	System.out.println("Key sent to Bob = "+key);
    		
    	String keyB = key+"";
        out.println(keyB); //manda para o Bob

        while(true){
            bobResponse = in.readLine(); //lê resposta do Bob
            if(bobResponse.contentEquals(".")){
                System.out.println("Bob closed connection!");
                break;
            }else{
                System.out.println("Response: " + bobResponse);
                break;
            }
        }
            
        sBInt = sBInt.add(BigInteger.valueOf(Long.parseLong(bobResponse)));
		
        keyAB = opAritmeticas.exponenciacao(sBInt, sA, p);
		System.out.println("The key shared between Alice and Bob is: "+keyAB);
            
		while(true){
            System.out.println("Enter '.' to close connection!");
            userInput = stdIn.readLine();
            if(userInput.equals(".")){//finaliza conexão
            	System.out.println("Connection closed!");
            	out.println(".");
            	break;
			}
        }
    
		out.close();
		in.close();
		stdIn.close();
		sc.close();
		bobSocket.close();
		aliceSocket.close();
    }
}
