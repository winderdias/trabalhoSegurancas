package AritM_DiffH_Rsa;

import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Rsa_Bob {
	private BigInteger p,q,n,on,e,d; 
	//p,q: primos grandes; n:p*q; on:aux; e:int primo entre si do tot(n); d: inv. mult. de e;
	
	public Rsa_Bob(){
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
		Socket bobSocket = null; 
		String userInput;
		String aliceResponse = null;
		String aliceResponse1 = null;
		String aliceResponse2 = null;
		PrintWriter out = null; 
		PrintWriter out2 = null; 
		BufferedReader in = null;
		InetAddress ipExchange;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		Rsa_Bob app = new Rsa_Bob();
	    BigInteger bplaintext, bciphertext;
		int plaintext, aux=0;
			
        //caso usuário nao passar IP do servidor da troca na inicialização, pede para informar.
        if (args.length > 0){
            ipExchange = InetAddress.getByName(args[0]);
        }else{ //se usuário não informou na chamada do programa, mostra mensagem 
            System.out.print("Enter the IP that exchanged the key: ");
            ipExchange = InetAddress.getByName(stdIn.readLine());
        }
           
        System.out.println ("Connecting in " + ipExchange + " port 13131.");

        try {       
            bobSocket = new Socket(ipExchange, 13131);
            out = new PrintWriter(bobSocket.getOutputStream(), true);
            out2 = new PrintWriter(bobSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + ipExchange);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error I/O in: " + ipExchange);
            System.exit(1);
        }

        System.out.println("Enter any character : ");
			plaintext = System.in.read();

			bplaintext = BigInteger.valueOf((long)plaintext);
			bciphertext = app.encrypt(bplaintext);
			
//			System.out.println("Plaintext : " +  bplaintext.toString());
//			System.out.println("Ciphertext : " +  bciphertext.toString());

			bplaintext = app.decrypt(bciphertext);
//			System.out.println("After Decryption Plaintext : " +  bplaintext.toString());
			
			System.out.println("Key public sent to Alice = "+ app.n +"\n                         = " + app.e);
			System.out.println("Key private = "+ app.n +"\n            = " + app.d);
	        
	        out.println(app.n); //manda para Alice
	        out.println(app.e); //manda para Alice

        while(true){
	        	if(aux==0){ 
	        		aliceResponse1 = in.readLine();//lê resposta de Alice 
	        		if(aliceResponse1.contentEquals(".")){
	        			System.out.println("Alice closed connection!");
	        			break;
	        		}
	        		aux++;
	            } 
	        	if(aux==1){ 
	        		aliceResponse2 = in.readLine();//lê resposta de Alice 
	        		if(aliceResponse2.contentEquals(".")){
	        			System.out.println("Alice closed connection!");
	        			break;
	        		}
	        		aux++;
	        	}else	break;
	        }

        System.out.println("Alice's key public: "+ aliceResponse1 + "\n                  : " + aliceResponse2 );
            
        System.out.println("Enter '.' to close connection!");

        while(true){
            aliceResponse = in.readLine(); //lê resposta do Bob
            if(aliceResponse.contentEquals(".")){
                System.out.println("Alice fechou conexão");
                break;
            }else{
                System.out.println("Recebido pela Alice: " + aliceResponse);
                System.out.print("Envio: ");
                userInput = stdIn.readLine(); //lê teclado 
                out2.println(userInput); //manda para o Bob
                if(userInput.equals(".")){//finaliza conexão
                	System.out.println("Connection closed!");
                	break;
    			}
            }
        }
  
	
		out.close();
		in.close();
		stdIn.close();
		sc.close();
		bobSocket.close();
    }
}

